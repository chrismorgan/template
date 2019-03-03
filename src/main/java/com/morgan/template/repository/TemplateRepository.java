package com.morgan.template.repository;

import com.morgan.template.exception.TemplateNotFoundException;
import com.morgan.template.model.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Repository
public class TemplateRepository {

    private final Map<Long, Template> templateRepository;
    private final AtomicLong templateCounter = new AtomicLong();

    public TemplateRepository() {
        templateRepository = new ConcurrentHashMap<>();
    }

    /**
     * Creates a new template id and stores the template against it
     *
     * @param template
     * @return
     */
    public Template addTemplate(Template template) {
        Long templateId = getNewTemplateId();
        template.setId(templateId);
        templateRepository.put(templateId, template);
        log.debug("Added template " + templateId);
        return template;
    }

    /**
     * Retrieves the template given by the Id
     *
     * @param templateId
     * @return the template referenced by the id
     */
    public Optional<Template> getTemplate(Long templateId) {
        log.debug("Trying to retreve template " + templateId);
        return Optional.ofNullable(templateRepository.get(templateId));
    }

    /**
     * Removes the template given by the id
     *
     * @param templateId the template to remove
     * @throws TemplateNotFoundException if the id is not found
     */
    public void deleteTemplate(Long templateId) {
        if (templateRepository.get(templateId) == null) {
            log.error("Could not find template " + templateId);
            throw new TemplateNotFoundException(templateId);
        } else {
            templateRepository.remove(templateId);
            log.debug("Deleted template " + templateId);
        }
    }

    /**
     * Updates the entry (if any) at the id with the supplied template
     *
     * @param id
     * @param template
     */
    public void updateTemplate(Long id, Template template) {
        log.debug("Updating template " + id);
        if (templateRepository.get(id) == null) {
            log.error("Could not find template " + id);
            throw new TemplateNotFoundException(id);
        } else {
            template.setId(id);
            templateRepository.put(id, template);
        }
    }

    /**
     * Returns all the templates
     *
     * @return A list of the templates in the repository
     */
    public List<Template> getAllTemplates() {
        log.debug("Getting all templates");
        List<Template> templateList = new ArrayList<>();
        templateList.addAll(templateRepository.values());
        return templateList;
    }

    private Long getNewTemplateId() {
        return templateCounter.incrementAndGet();
    }

}
