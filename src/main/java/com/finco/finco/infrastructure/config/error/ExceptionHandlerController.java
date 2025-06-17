package com.finco.finco.infrastructure.config.error;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.finco.finco.entity.EbusinessException;
import com.finco.finco.entity.role.exception.RoleNotFoundException;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.user.exception.UserNotFoundException;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler({UserNotFoundException.class, RoleNotFoundException.class})
    private ResponseEntity<Map<String, String>> notFoundException(EbusinessException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", ex.getClass().getSimpleName());
        body.put("message", ex.getMessage());
        body.put("datetime", LocalDateTime.now().toString());

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(body);

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

    @ExceptionHandler(AccessDeniedBusinessException.class)
    public ResponseEntity<Map<String, String>> accessDeniedException(AccessDeniedBusinessException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", ex.getClass().getSimpleName());
        body.put("message", ex.getMessage());
        body.put("datetime", LocalDateTime.now().toString());

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(body);
    }

}
