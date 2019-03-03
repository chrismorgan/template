package com.morgan.template.service;

import com.morgan.template.model.Template;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class TemplateFactory {

    public TemplateFactory() {

    }

    public Template createTemplate() {
        Template template = new Template();
        template.setChannels(Collections.emptyList());
        template.setBody("");
        return template;
    }
}
