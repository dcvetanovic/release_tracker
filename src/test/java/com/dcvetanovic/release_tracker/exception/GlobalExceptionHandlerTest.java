package com.dcvetanovic.release_tracker.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleReleaseNotFoundException_ReturnsNotFoundResponse() {
        ReleaseNotFoundException ex = new ReleaseNotFoundException(99999L);
        ResponseEntity<Object> response = handler.handleReleaseNotFoundException(ex);

        assertEquals(404, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        Assertions.assertNotNull(body);
        assertEquals("Release not found", body.get("error"));
    }
}
