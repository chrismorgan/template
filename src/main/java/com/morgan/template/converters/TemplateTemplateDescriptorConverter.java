package com.morgan.template.converters;

import com.morgan.template.model.Template;
import com.morgan.template.model.TemplateDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TemplateTemplateDescriptorConverter implements Converter<Template, TemplateDescriptor> {

    @Override
    public TemplateDescriptor convert(Template template) {
        TemplateDescriptor templateDescriptor = new TemplateDescriptor();
        templateDescriptor.id(template.getId());
        templateDescriptor.body(template.getBody());
        templateDescriptor.channels(template.getChannels());
        return templateDescriptor;
    }
}
