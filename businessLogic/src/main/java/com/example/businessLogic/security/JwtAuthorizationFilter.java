package com.example.businessLogic.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.businessLogic.helpers.ResponseHelper;
import com.example.businessLogic.models.ApiErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {


    @Autowired
    private Environment environment;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null) {
            try {
                String secret = "secret";// todo: environment is null for some reason
                Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getSubject();
                Collection<GrantedAuthority> roles = List.of(new SimpleGrantedAuthority(decodedJWT.getClaim("role").asString()));
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, roles);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
            } catch (Exception exception) {
                ResponseHelper.setResponse(response, FORBIDDEN.value(), new ApiErrorResponse("Token validation failed"));
            }
        } else {
            ResponseHelper.setResponse(response, FORBIDDEN.value(), new ApiErrorResponse("Missing token"));
        }
    }
}
