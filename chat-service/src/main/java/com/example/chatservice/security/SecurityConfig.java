package com.example.chatservice.security;

import com.example.chatservice.helpers.ResponseHelper;
import com.example.chatservice.models.ApiErrorResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    // ADMIN token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IkFETUlOIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgyL2FwaS9sZXNzb3JzIiwiZXhwIjoxNjk5MjE2NDk3fQ.YJxdKh8th1uFcTAH11oCDaaeJRSElbSYgwWZS75VP54
    // CLIENT token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IkNMSUVOVCIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4Mi9hcGkvbGVzc29ycyIsImV4cCI6MTY5OTIxNjYwN30.MHQCXJmzbImOPVD_bUHzbW3s962Q93ls4Lb-8SNwDcg
    // LESSOR token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IkxFU1NPUiIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4Mi9hcGkvbGVzc29ycyIsImV4cCI6MTY5OTIxNjY3NH0.0R2lCePcEEpaWSuioMqLtKypLLy3vaHtuxvAEO1XHU8

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.exceptionHandling().accessDeniedHandler(((request, response, accessDeniedException) -> {
            ResponseHelper.setResponse(response, FORBIDDEN.value(),  new ApiErrorResponse(accessDeniedException.getMessage()));
        }));
        http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }



}
