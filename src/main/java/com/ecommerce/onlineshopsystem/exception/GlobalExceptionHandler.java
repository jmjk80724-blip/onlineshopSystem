package com.ecommerce.onlineshopsystem.exception;

import com.ecommerce.onlineshopsystem.cart.CartNotFoundException;
import com.ecommerce.onlineshopsystem.category.CategoryNotFoundException;
import com.ecommerce.onlineshopsystem.product.ProductNotFoundException;
import com.ecommerce.onlineshopsystem.user.InvalidCredentialsException;
import com.ecommerce.onlineshopsystem.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({
            UserNotFoundException.class,
            CartNotFoundException.class,
            ProductNotFoundException.class,
            CategoryNotFoundException.class
    })


    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException ex) {
        return error(HttpStatus.NOT_FOUND, ex.getMessage());
    }
    //404 - URL matches no controller and no static resource
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotResourceFound(NoResourceFoundException ex) {
        return error(HttpStatus.NOT_FOUND, "Endpoint not found");
    }

    //401 - authentication / credentials
    @ExceptionHandler({AuthenticationException.class, InvalidCredentialsException.class})
    public ResponseEntity<Map<String, Object>> handleUnauthorized(Exception ex) {
        return error(HttpStatus.UNAUTHORIZED, "Invalid username or password");
    }

    //403 - authenticated but not permitted
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleForbidden(AccessDeniedException ex){
        return error(HttpStatus.FORBIDDEN, "Access denied - insufficient permissions");
    }

    // 400 - Business rule violations
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,Object>> handleIllegalArgument(IllegalArgumentException ex){
        return error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // 400 - request binding / validation failures
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex){
        Map<String, String> fields = new LinkedHashMap<>();
        collect(ex.getBindingResult().getFieldErrors(),
                ex.getBindingResult().getGlobalErrors(),
                fields);
        return fieldError(HttpStatus.BAD_REQUEST, fields);

    }








private ResponseEntity<Map<String, Object>> error(HttpStatus status, String message) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("error", message != null ? message : status.getReasonPhrase());
    return ResponseEntity.status(status).body(body);
}

private ResponseEntity<Map<String, Object>> fieldError(HttpStatus status, Map<String, String> fields) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("error" , fields);
    return ResponseEntity.status(status).body(body);
}

private void collect(List<FieldError> fieldErrors, List<ObjectError> globalErrors, Map<String, String> field ) {
    for(FieldError fieldError : fieldErrors) {
        String message = fieldError.getDefaultMessage() != null
                ? fieldError.getDefaultMessage()
                : "invalid field";
        field.merge(fieldError.getField(), message, (existing, added) -> existing + "; "+ added);
    }
    for(ObjectError globalError : globalErrors) {
        String message = globalError.getDefaultMessage() != null
                ? globalError.getDefaultMessage()
                : "invalid field";
        field.merge(globalError.getObjectName(), message, (existing, added) -> existing + "; "+ added);
    }
}

}
