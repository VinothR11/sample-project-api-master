package com.example.carbooking.controller;

import com.example.carbooking.dto.LoginDto;

import com.example.carbooking.entities.RegisterEntity;
import com.example.carbooking.service.RegisterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/register")
public class RegisterController {
    @Autowired
    private RegisterServiceImpl registerServiceImpl;
    @PostMapping("/create")
    public ResponseEntity<RegisterEntity> createRegister(@RequestBody RegisterEntity registerEntity) {
        RegisterEntity savedEntity = registerServiceImpl.create(registerEntity);
        return ResponseEntity.ok(savedEntity);
    }
 @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDto loginDto) {
        try {
                RegisterEntity registerEntity = registerServiceImpl.login(loginDto);

            String username = registerEntity.getUsername();
            String userType = registerEntity.getUsertype();
            String token = userType.equalsIgnoreCase("driver")
                    ? "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwidXNlcm5hbWUiOiJnb2t1bCIsInVzZXJ0eXBlIjoiRFJJVkVSIiwiaWF0IjoxNTE2MjM5MDIyfQ.gvCCHa30B628-j3cUFjTBrpX2yXGX75t5QS6TNRURuCYVSg-ygh74tpHBeZ9cl-hfP2iv7l_Y8Gql0WqRCXy0g"
                    : "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwidXNlcm5hbWUiOiJwdWdhbCIsInVzZXJ0eXBlIjoiVVNFUiIsImlhdCI6MTUxNjIzOTAyMn0.4D6lEN-mwsNG_-NP4ZoUxcL_Sh8-w-e-hmf-145H2SASRlqd1vbGUAwI8HJEdDbRQLXApWCjVJvaqUjioxM_BQ";
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("username", username);
            response.put("userType", userType);
            response.put("token", token);
            response.put("id",String.valueOf(registerEntity.getId()));
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}