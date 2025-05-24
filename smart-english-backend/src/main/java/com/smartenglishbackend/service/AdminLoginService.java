package com.smartenglishbackend.service;

import com.smartenglishbackend.dto.request.DTOAdmin;
import com.smartenglishbackend.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AdminLoginService {
    @Value("${admin.username}")
    private String adminUsername;
    @Value("${admin.password}")
    private String adminPassword;
    @Autowired
    private JWTUtils jwtUtils;
    public boolean adminLogin(DTOAdmin admin) {
        if(admin == null || admin.getUsername() == null || admin.getPassword() == null){
            return false;
        }
        return admin.getUsername().equals(adminUsername) && admin.getPassword().equals(adminPassword);
    }
    public String getToken() {
        return jwtUtils.generateToken(-1);
    }
    public boolean isAdmin(String token){
        return jwtUtils.verifyToken(token) && jwtUtils.getUserIdFromToken(token).equals(-1);
    }
}
