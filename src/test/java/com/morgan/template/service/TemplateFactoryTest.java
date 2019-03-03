package com.morgan.template.service;

import com.morgan.template.model.Template;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class TemplateFactoryTest {

    private TemplateFactory templateFactory;

    @Before
    public void setUp() throws Exception {
        templateFactory = new TemplateFactory();
    }

    @Test
    public void createTemplate_createsEmptyTemplate() {

        //Given

        //When
        Template template = templateFactory.createTemplate();

        //Then
        assertThat(template).isNotNull();
        assertThat(template.getId()).isNull();
        assertThat(template.getChannels()).hasSize(0);
        assertThat(template.getBody()).isBlank();
    }
}