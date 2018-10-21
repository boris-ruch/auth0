package com.boo.auth0;

import com.auth0.exception.Auth0Exception;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Auth0ApplicationTests {
	
	
	@Autowired
	private UserManager userManager;

	@Test
	public void auth0ConfigurationTest() throws Auth0Exception {
		
		userManager.createUser();		
		assertThat(userManager.getUser().isPresent()).isTrue();		
		userManager.loginUser();
		userManager.deleteUser();
		
	}

}
