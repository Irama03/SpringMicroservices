package com.example.authservice;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtGenerationTest {

    @Autowired
    private MockMvc mvc;

    private static final String SECRET = "secret";

    private boolean jwtIsCorrect(String jwt, String expectedName, String expectedEmail, String expectedRole) {
        if(jwt == null)
            return false;
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(jwt);
            String name = decodedJWT.getSubject();
            String email = decodedJWT.getClaim("email").asString();
            String role = decodedJWT.getClaim("role").asString();
            if(!name.equals(expectedName) || !email.equals(expectedEmail) || !role.equals(expectedRole))
                return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Test
    public void givenCorrectCredentials_whenLogin_thenJWTAndStatus200Returned() throws Exception {
        String email = "example1@gmail.com";
        String password = "12345";
        MvcResult mvcres = this.mvc
                .perform(
                    post("/api/login")
                        .param("email", email)
                        .param("password", password)
                )
                .andExpect(status().isOk())
                .andReturn();
        String res = mvcres.getResponse().getContentAsString();
        Assertions.assertTrue(jwtIsCorrect(res, "hlib", email, "ADMIN"));
    }

    @Test
    public void givenIncorrectCredentials_whenLogin_thenStatus403Returned() throws Exception {
        String email = "example1@gmail.com";
        String password = "12342225";
        this.mvc
            .perform(
                post("/api/login")
                    .param("email", email)
                    .param("password", password)
            )
            .andExpect(status().isForbidden());
    }

}
