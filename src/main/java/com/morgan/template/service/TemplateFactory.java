package com.morgan.template.service;

import com.morgan.template.model.Template;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Creates Templates in a basic state
 */
@Service
public class TemplateFactory {

    /**
     * Create and return a template with empty channels list and a blank body
     * The template Id is null
     *
     * @return
     */
    public Template createTemplate() {
        Template template = new Template();
        template.setChannels(Collections.emptyList());
        template.setBody("");
        return template;
    }
}
