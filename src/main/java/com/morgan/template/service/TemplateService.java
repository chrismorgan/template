package com.morgan.template.service;

import com.morgan.template.api.TemplateApiDelegate;
import com.morgan.template.exception.TemplateNotFoundException;
import com.morgan.template.model.Template;
import com.morgan.template.model.TemplateDescriptor;
import com.morgan.template.repository.TemplateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TemplateService implements TemplateApiDelegate {

    private final TemplateRepository templateRepository;
    private final TemplateFactory templateFactory;
    private final ConversionService conversionService;

    @Autowired
    public TemplateService(TemplateRepository templateRepository,
                           TemplateFactory templateFactory,
                           ConversionService conversionService) {
        this.templateRepository = templateRepository;
        this.templateFactory = templateFactory;
        this.conversionService = conversionService;
    }

    @Override
    public ResponseEntity<TemplateDescriptor> createTemplate() {
        Template template = templateRepository.addTemplate(templateFactory.createTemplate());
        TemplateDescriptor templateDescriptor = conversionService.convert(template, TemplateDescriptor.class);
        log.debug("Created new template");
        return new ResponseEntity<>(templateDescriptor, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteTemplate(@Min(1L) Long id) {
        templateRepository.deleteTemplate(id);
        log.debug("Deleted template " + id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<TemplateDescriptor>> getAllTemplates() {
        List<Template> templates = templateRepository.getAllTemplates();
        List<TemplateDescriptor> templateDescriptors = templates.stream()
                .map(template -> conversionService.convert(template, TemplateDescriptor.class))
                .collect(Collectors.toList());
        log.debug("Retrieved all templates");
        return new ResponseEntity<>(templateDescriptors, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TemplateDescriptor> getTemplate(@Min(1L) Long id) {
        Optional<Template> template = templateRepository.getTemplate(id);
        if (!template.isPresent()) {
            log.debug("Template not found " + id);
            throw new TemplateNotFoundException(id);
        } else {
            TemplateDescriptor templateDescriptor = conversionService.convert(template.get(), TemplateDescriptor.class);
            log.debug("Retrieved template " + id);
            return new ResponseEntity<>(templateDescriptor, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<TemplateDescriptor> updateTemplate(@Min(1L) Long id, TemplateDescriptor templateDescriptor) {
        Template template = conversionService.convert(templateDescriptor, Template.class);
        templateRepository.updateTemplate(id, template);

        log.debug("Updated template " + id);
        TemplateDescriptor updatedDescriptor = conversionService.convert(template, TemplateDescriptor.class);
        return new ResponseEntity<>(updatedDescriptor, HttpStatus.OK);
    }
}
