package com.nvminh162.projectxbackend.controller.errors;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nvminh162.projectxbackend.entity.ApiResponse;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNotFound(NoSuchElementException ex) {
        var result = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Handle Not Found", null, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorList = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        String errors = String.join("; ", errorList);

        ApiResponse<Object> response = new ApiResponse<>(HttpStatus.BAD_REQUEST,
                errors, null, "VALIDATION_ERROR");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /*
     * Handle the rest exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllException(Exception ex) {
        var result = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null, "INTERNAL_SERVER_ERROR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}
