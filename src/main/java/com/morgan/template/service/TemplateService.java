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

/**
 * Handles the CRUD operations for managing templates
 */
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

    /**
     * Creates blank template, assigns it an Id, stores it, and returns the template object to the endpoint
     *
     * @return TemplateDescriptor representing the template
     */
    @Override
    public ResponseEntity<TemplateDescriptor> createTemplate() {
        Template template = templateRepository.addTemplate(templateFactory.createTemplate());
        TemplateDescriptor templateDescriptor = conversionService.convert(template, TemplateDescriptor.class);
        log.debug("Created new template");
        return new ResponseEntity<>(templateDescriptor, HttpStatus.CREATED);
    }

    /**
     * Deletes the template specified by the Id*
     * @param id Template Id
     * @throws TemplateNotFoundException if the Id does not exist in the store
     * @return
     */
    @Override
    public ResponseEntity<Void> deleteTemplate(@Min(1L) Long id) {
        templateRepository.deleteTemplate(id);
        log.debug("Deleted template " + id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Returns all the templates in the store
     * @return List of TemplateDescriptors
     */
    @Override
    public ResponseEntity<List<TemplateDescriptor>> getAllTemplates() {
        List<Template> templates = templateRepository.getAllTemplates();
        List<TemplateDescriptor> templateDescriptors = templates.stream()
                .map(template -> conversionService.convert(template, TemplateDescriptor.class))
                .collect(Collectors.toList());
        log.debug("Retrieved all templates");
        return new ResponseEntity<>(templateDescriptors, HttpStatus.OK);
    }

    /**
     * Retrieves a TemplateDescriptor by the template Id
     * @param id Template Id
     * @return TemplateDescriptor
     */
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

    /**
     * Updates the template given by the template Id to the representation given by the templateDescriptor
     * The supplied template descriptor is updated to use the parameter template Id to avoid inconsistency. In this
     * implementation the template is only updated is it already exists in the store. It is not intended to be
     * idempotent.
     * @param id template Id
     * @param templateDescriptor
     * @return the updated TemplateDescriptor
     */
    @Override
    public ResponseEntity<TemplateDescriptor> updateTemplate(@Min(1L) Long id, TemplateDescriptor templateDescriptor) {
        Template template = conversionService.convert(templateDescriptor, Template.class);
        templateRepository.updateTemplate(id, template);

        log.debug("Updated template " + id);
        TemplateDescriptor updatedDescriptor = conversionService.convert(template, TemplateDescriptor.class);
        return new ResponseEntity<>(updatedDescriptor, HttpStatus.OK);
    }
}
