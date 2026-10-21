
package com.chandan.employeemanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.chandan.employeemanagement.exception.EmployeeNotFoundException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatusCode;



import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(EmployeeNotFoundException.class)
public ResponseEntity<Map<String, String>> handleEmployeeNotFound(
        EmployeeNotFoundException ex) {

    Map<String, String> error = new HashMap<>();
    error.put("error", ex.getMessage());

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
}
@ExceptionHandler(ResponseStatusException.class)
public ResponseEntity<Map<String, String>> handleResponseStatusException(
        ResponseStatusException ex) {

    Map<String, String> error = new HashMap<>();
    error.put("error", ex.getReason());

    HttpStatusCode status = ex.getStatusCode();

    return new ResponseEntity<>(error, status);
}
}
