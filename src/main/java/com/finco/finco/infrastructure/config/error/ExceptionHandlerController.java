package com.finco.finco.infrastructure.config.error;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.finco.finco.entity.EbusinessException;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler({EbusinessException.class})
    private ResponseEntity<Map<String, String>> ebussinessException(EbusinessException ex) {
        return buildResponse(ex, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> argumentNotValidException(MethodArgumentNotValidException e){
        List<FieldError> erros = e.getFieldErrors();

        Map<String, String> response = new HashMap<>();

        for(FieldError field : erros) {
            response.put(field.getField(), field.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        return buildResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> exceptionHandler(Exception e) {
        return buildResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Map<String, String>> buildResponse(Exception e, HttpStatus status) {
        Map<String, String> response = new HashMap<>();
        response.put("error", e.getClass().getSimpleName());
        response.put("message", e.getMessage());
        response.put("datetime", LocalDateTime.now().toString());
        return ResponseEntity.status(status.value()).body(response);
    }

}
