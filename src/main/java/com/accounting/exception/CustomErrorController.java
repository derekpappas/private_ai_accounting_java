package com.accounting.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request, WebRequest webRequest) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", request.getAttribute("javax.servlet.error.exception"));
        errorDetails.put("message", request.getAttribute("javax.servlet.error.message"));
        errorDetails.put("status", request.getAttribute("javax.servlet.error.status_code"));
        errorDetails.put("path", request.getAttribute("javax.servlet.error.request_uri"));
        
        return ResponseEntity.status(500).body(errorDetails);
    }
} 