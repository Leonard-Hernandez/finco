package com.finco.finco.infrastructure.config.error;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.finco.finco.entity.account.exception.AccountNotFoundException;
import com.finco.finco.entity.exception.EbusinessException;
import com.finco.finco.entity.goal.exception.GoalNotFoundException;
import com.finco.finco.entity.role.exception.RoleNotFoundException;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.user.exception.UserNotFoundException;

@RestControllerAdvice
public class ExceptionHandlerController {

    private final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler({ EbusinessException.class })
    private ResponseEntity<ErrorResponse> ebussinessException(EbusinessException ex) {
        logger.error("Business Exception: {}", ex.getMessage());
        return buildResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ UserNotFoundException.class, AccountNotFoundException.class, GoalNotFoundException.class,
            RoleNotFoundException.class })
    private ResponseEntity<ErrorResponse> notFoundException(EbusinessException ex) {
        logger.error("Business Exception: {}", ex.getMessage());
        return buildResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedBusinessException.class)
    private ResponseEntity<ErrorResponse> accessDeniedBusinessException(AccessDeniedBusinessException ex) {
        logger.error("Business Exception: {}", ex.getMessage());
        return buildResponse(ex, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> argumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> erros = e.getFieldErrors();

        Map<String, String> response = new HashMap<>();

        for (FieldError field : erros) {
            response.put(field.getField(), field.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ErrorResponse> propertyReferenceException(PropertyReferenceException e) {
        logger.error("PropertyReferenceException: {}", e.getMessage());
        String propertyName = e.getPropertyName();
        String message = "Property " + propertyName + " not found";
        ErrorResponse errorResponse = new ErrorResponse("PropertyReferenceException", message, LocalDateTime.now());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, BadCredentialsException.class})
    public ResponseEntity<ErrorResponse> badRequestHandler(Exception e) {
        return buildResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception e) {
        logger.error("Exception: {}", e.getMessage());
        return buildResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<ErrorResponse> buildResponse(Exception e, HttpStatus status) {
        ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(status.value()).body(response);
    }

}
