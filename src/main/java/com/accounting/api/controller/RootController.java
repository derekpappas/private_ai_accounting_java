package com.accounting.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getApiInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("status", "UP");
        info.put("version", "1.0");
        info.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(info);
    }
} 