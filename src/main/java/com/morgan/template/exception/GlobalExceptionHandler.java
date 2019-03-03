package com.morgan.template.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.morgan.template.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorMessage> handleAllExceptions(Throwable ex, WebRequest request) {
        ErrorMessage errorDetails = new ErrorMessage();

        while (ex.getCause() != null && !ex.getCause().equals(ex)) {
            ex = ex.getCause();
        }

        if (ex instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) ex;
            errorDetails.message(String.format("%s is not a %s", ife.getValue(), ife.getTargetType()));
            return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }

        if (ex instanceof HttpMessageNotReadableException) {
            HttpMessageNotReadableException hmnre = (HttpMessageNotReadableException) ex;
            errorDetails.message(hmnre.getMessage());
            return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }

        if (ex instanceof ValidationException) {
            ValidationException cve = (ValidationException) ex;
            errorDetails.message(cve.getMessage());
            return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }

        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) ex;
            List<String> errors = new ArrayList<>();
            for (FieldError error : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
                errors.add(error.getField() + ": " + error.getDefaultMessage());
            }
            for (ObjectError error : methodArgumentNotValidException.getBindingResult().getGlobalErrors()) {
                errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
            }
            errorDetails.message(errors.stream().collect(Collectors.joining("\n")));
            return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }

        errorDetails.message(ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
