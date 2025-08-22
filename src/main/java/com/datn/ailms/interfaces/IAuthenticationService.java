package com.datn.ailms.interfaces;

import com.datn.ailms.model.dto.request.AuthenRequest;
import com.datn.ailms.model.dto.request.IntrospectRequest;
import com.datn.ailms.model.dto.response.AuthenResponse;
import com.datn.ailms.model.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

public interface IAuthenticationService {
    AuthenResponse authenticate(AuthenRequest authenRequest);
    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException;
}
