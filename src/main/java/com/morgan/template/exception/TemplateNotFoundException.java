package com.morgan.template.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TemplateNotFoundException extends RuntimeException {

    public TemplateNotFoundException(Long templateId) {
        super("Template " + templateId + " does not exist");
    }

}
