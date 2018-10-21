package com.boo.auth0;


import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.net.AuthRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Log4j2
@Configuration
@EnableWebSecurity
public class Auth0Config extends WebSecurityConfigurerAdapter {

    @Value("${HORIZON_IAM_DOMAIN}")
    private String auth0domain;

    @Value("${HORIZON_IAM_CLIENT_ID}")
    private String auth0ClientId;

    @Value("${HORIZON_IAM_CLIENT_SECRET}")
    private String auth0ClientSecret;

    @Value("${HORIZON_IAM_API_AUDIENCE}")
    private String auth0ApiAudience;
    

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public ManagementAPI getManagementApi() {
        return new ManagementAPI(auth0domain, getToken(getAuthAPI()));
    }

    @Bean
    public AuthAPI getAuthAPI() {
        return new AuthAPI(auth0domain, auth0ClientId, auth0ClientSecret);
    }

    public String getToken(AuthAPI authAPI) {
        AuthRequest authRequest = authAPI.requestToken(auth0ApiAudience);
        try {
            return authRequest.execute().getAccessToken();
        } catch (Auth0Exception e) {
            String message = "Could not get token for management API.";
            log.error(message + "Exception: {} - Hint: Check Auth0Confi environment settings.", String.valueOf(e));
            throw new RuntimeException(message, e);
        }
    }
}
