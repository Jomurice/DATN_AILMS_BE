package com.datn.ailms.config;

import com.datn.ailms.constants.PredefineRole;
import com.datn.ailms.model.entity.Role;
import com.datn.ailms.model.entity.User;
import com.datn.ailms.repository.RoleRepository;
import com.datn.ailms.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.HashSet;

// tu dong tao 1 user admin khi start
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
     PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @NonFinal
    static final String WO_USERNAME = "wo";
    @NonFinal
    static final String WO_PASSWORD = "wo123";

    @NonFinal
    static final String LM_USERNAME = "lm";
    @NonFinal
    static final String LM_PASSWORD = "lm123";

    @Bean
//    @ConditionalOnProperty(
//            prefix = "spring",
//            value = "datasource.driverClassName",
//            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
//        log.info("Initializing application.....");
        return args -> {
            if (userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(PredefineRole.USER_ROLE)
                        .description("User role")
                        .build());

                Role adminRole = roleRepository.save(Role.builder()
                        .name(PredefineRole.ADMIN_ROLE)
                        .description("Admin role")
                        .build());

                var roles = new HashSet<Role>();
                roles.add(adminRole);

                User admin = User.builder()
                        .username(ADMIN_USER_NAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(roles)
                        .build();


                userRepository.save(admin);
                log.warn("admin user has been created with default password: admin, please change it");
            }

//            if(userRepository.findByUsername(WO_USERNAME).isEmpty()) {
//                roleRepository.save(Role.builder()
//                        .name(PredefineRole.USER_ROLE)
//                        .description("User role")
//                        .build());
//
//                Role woRole = roleRepository.save(Role.builder()
//                        .name(PredefineRole.WAREHOUSE_OPERATOR_ROLE)
//                        .description("Warehouse Operator")
//                        .build());
//            }
            log.info("Application initialization completed .....");
        };
    }
}
