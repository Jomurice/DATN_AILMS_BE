package com.datn.ailms.services;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.model.dto.request.AuthenRequest;
import com.datn.ailms.model.dto.request.IntrospectRequest;
import com.datn.ailms.model.dto.request.LogoutRequest;
import com.datn.ailms.model.dto.response.AuthenResponse;
import com.datn.ailms.model.dto.response.IntrospectResponse;
import com.datn.ailms.model.entities.account_entities.InvalidatedToken;
import com.datn.ailms.model.entities.account_entities.User;
import com.datn.ailms.repositories.userRepo.InvalidatedTokenRepository;
import com.datn.ailms.repositories.userRepo.UserRepository;
import com.datn.ailms.interfaces.IAuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements IAuthenticationService {

    private final UserRepository _userRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;


    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected  long REFRESHABLE_DURATION;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken(), true);

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiry = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jit)
                    .expiryTime(expiry)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException e){
            log.info("Token already expired");
        }
    }



    @Override
    public AuthenResponse authenticate(AuthenRequest authenRequest) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = _userRepository.findByUsername(authenRequest.getUsername()).orElseThrow(() ->
            new AppException(ErrorCode.USER_NOT_EXISTED));
        boolean authenticated = passwordEncoder.matches(authenRequest.getPassword(), user.getPassword());

        if(!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(user);
        return AuthenResponse.builder()
                .token(token.token)
                .authenticated(true)
                .build();
    }

    private TokenInfo generateToken(User user){

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        Date issueTime = new Date();
        Date expiryTime = new Date(Instant.ofEpochMilli(issueTime.getTime())
                .plus(VALID_DURATION, ChronoUnit.SECONDS)
                .toEpochMilli());
        //các data trong body gọi là claimset
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername()) // đại diện cho user đăng nhập
                .issuer("tien") // thường  là domain service
                .issueTime(issueTime) //Lấy thời điểm hịiện tại
                .expirationTime(expiryTime) //Thời  hạn token
                .jwtID(java.util.UUID.randomUUID().toString())
                .claim("name",user.getName())
                .claim("scope",buildScope(user))
                .claim("id",user.getId())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes())); //thuật toán ký và giải mã trùng nhau https://generate-random.org/
            return new TokenInfo(jwsObject.serialize(), expiryTime);

        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> { stringJoiner.add( "ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }

    private record TokenInfo(String token, Date expiryDate) {}

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        // mã  hoá đọc token
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh) ? new Date(
                signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        // gán biến đã verify
        var verified = signedJWT.verify(verifier);

        if(!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        // Kiểm tra xem token đó có id nằm trong invalidToken hay không, nu đã có thì throw còn không thì thôi
        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) throw new AppException(ErrorCode.UNAUTHENTICATED);
        return signedJWT;

    }
}
