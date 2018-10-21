package com.boo.auth0;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.json.mgmt.users.User;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.UUID.randomUUID;

@Component
@Log4j2
@Getter
public class UserManager {

    @Autowired
    private ApplicationContext appCtx;

    private static final String CONNECTION = "Username-Password-Authentication";

    private String password = randomUUID().toString();

    private String gatewayId = randomUUID().toString();

    private Optional<User> user = empty();


    public void createUser() throws Auth0Exception {
        log.info("UserManager.createUser");
        ManagementAPI managementAPI = appCtx.getBean(ManagementAPI.class);
        user = Optional.of(managementAPI.users().create(createUser(gatewayId, password)).execute());
    }

    public void loginUser() throws Auth0Exception {
        log.info("UserManager.loginUser");
        AuthAPI authAPI = appCtx.getBean(AuthAPI.class);
        authAPI.login(user.get().getEmail(), password).execute();
    }

    public void deleteUser() {
        user.ifPresent(u ->
        {
            try {
                appCtx.getBean(ManagementAPI.class).users().delete(u.getId()).execute();
            } catch (Auth0Exception e) {
                log.error("could not delete auth0 user", e);
            }
        });
    }


    private User createUser(String gatewayId, String password) {
        User user = new User();
        user.setId(gatewayId);
        user.setEmail(convert(gatewayId));
        user.setName(gatewayId);
        user.setConnection(CONNECTION);
        user.setPassword(password);
        user.setEmailVerified(true);
        return user;
    }

    private static String convert(String gatewayId) {
        return "no-reply-" + gatewayId + "@boo.com";
    }
}
