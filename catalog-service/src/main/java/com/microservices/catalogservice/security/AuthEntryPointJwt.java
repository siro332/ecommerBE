package com.microservices.catalogservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {
        Map<String, Object> errorObject = new HashMap<>();
        int errorCode = 401;
        errorObject.put("message", "Unauthorized access of protected resource, invalid credentials");
        errorObject.put("error", HttpStatus.UNAUTHORIZED);
        errorObject.put("code", errorCode);
        errorObject.put("timestamp", new Timestamp(new Date().getTime()));
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setStatus(errorCode);
        ObjectMapper objectMapper = new ObjectMapper();
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(errorObject));
    }
}
