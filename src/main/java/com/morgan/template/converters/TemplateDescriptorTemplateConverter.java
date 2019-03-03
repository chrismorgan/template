package com.morgan.template.converters;

import com.morgan.template.model.Template;
import com.morgan.template.model.TemplateDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TemplateDescriptorTemplateConverter implements Converter<TemplateDescriptor, Template> {

    @Override
    public Template convert(TemplateDescriptor templateDescriptor) {
        Template template = new Template();
        template.setId(templateDescriptor.getId());
        template.setBody(templateDescriptor.getBody());
        template.setChannels(templateDescriptor.getChannels());

        return template;
    }
}
