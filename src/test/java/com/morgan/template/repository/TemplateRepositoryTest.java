package com.morgan.template.repository;

import com.morgan.template.model.Template;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class TemplateRepositoryTest {

    private TemplateRepository templateRepository;

    @Before
    public void setUp() throws Exception {
        templateRepository = new TemplateRepository();
    }

    @Test
    public void addTemplate_cleanRepo_addsTemplateAndAssignsId1() {
        //Given
        Template template = new Template();
        template.setChannels(Collections.emptyList());
        template.setBody(null);

        //When
        templateRepository.addTemplate(template);

        //Then
        assertThat(template.getId()).isEqualTo(1);
        assertThat(template.getChannels()).hasSize(0);
        assertThat(template.getBody()).isBlank();
    }

    @Test
    public void getTemplate_oneEntry_isRetrieved() {

        //Given
        Template template = new Template();
        template.setChannels(Collections.emptyList());
        template.setBody(null);
        templateRepository.addTemplate(template);

        //When
        Optional<Template> returnedTemplate = templateRepository.getTemplate(1l);

        //Then
        assertThat(returnedTemplate).isPresent();
        assertThat(returnedTemplate.get()).isEqualTo(template);

    }

    @Test
    public void deleteTemplate_oneEntry_isDeleted() {

        //Given
        Template template = new Template();
        template.setChannels(Collections.emptyList());
        template.setBody(null);
        templateRepository.addTemplate(template);

        //When
        templateRepository.deleteTemplate(1l);

        //Then
        assertThat(templateRepository.getTemplate(1l)).isNotPresent();

    }

    @Test
    public void updateTemplate_oneEntry_isUpdated() {

        //Given
        Template template = new Template();
        template.setChannels(Collections.emptyList());
        template.setBody(null);
        templateRepository.addTemplate(template);

        //When
        Template updatedTemplate = new Template();
        updatedTemplate.setBody("Hello world");
        templateRepository.updateTemplate(1l, updatedTemplate);

        //Then
        assertThat(templateRepository.getTemplate(1l)).isPresent();
        Template test = templateRepository.getTemplate(1l).get();
        assertThat(test.getBody()).isEqualTo("Hello world");
        assertThat(test.getId()).isEqualTo(1);
        assertThat(test.getChannels()).isNull();
    }

    @Test
    public void getAllTemplates_twoEntries_bothReturned() {
        //Given
        Template oneTemplate = new Template();
        oneTemplate.setChannels(Collections.emptyList());
        oneTemplate.setBody(null);
        templateRepository.addTemplate(oneTemplate);

        Template twoTemplate = new Template();
        twoTemplate.setBody("Hello world");
        twoTemplate.setChannels(Arrays.asList("EMAIL"));
        templateRepository.addTemplate(twoTemplate);

        //When
        List<Template> all = templateRepository.getAllTemplates();

        //Then
        assertThat(all).hasSize(2);
        assertThat(all).containsExactlyInAnyOrder(oneTemplate, twoTemplate);

    }
}