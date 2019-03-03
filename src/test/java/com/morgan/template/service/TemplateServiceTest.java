package com.morgan.template.service;

import com.morgan.template.exception.TemplateNotFoundException;
import com.morgan.template.model.Template;
import com.morgan.template.model.TemplateDescriptor;
import com.morgan.template.repository.TemplateRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TemplateServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private TemplateRepository templateRepository;
    @Mock
    private TemplateFactory templateFactory;
    @Mock
    private ConversionService conversionService;
    private TemplateService templateService;

    @Before
    public void setUp() throws Exception {
        templateService = new TemplateService(templateRepository, templateFactory, conversionService);
    }

    @Test
    public void createTemplate_called_createsAndReturnsTemplate() {
        //Given
        Template template = new Template();
        template.setBody("");
        template.setChannels(Collections.emptyList());
        given(templateFactory.createTemplate()).willReturn(template);
        template.setId(1l);
        given(templateRepository.addTemplate(any(Template.class))).willReturn(template);
        TemplateDescriptor templateDescriptor = new TemplateDescriptor();
        templateDescriptor.id(1l);
        templateDescriptor.channels(Collections.emptyList());
        templateDescriptor.body("");
        given(conversionService.convert(any(Template.class), any(Class.class))).willReturn(templateDescriptor);

        //When
        ResponseEntity<TemplateDescriptor> responseEntity = templateService.createTemplate();

        //Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
        TemplateDescriptor templateDescriptor1 = responseEntity.getBody();
        assertThat(templateDescriptor1.getBody()).isEmpty();
        assertThat(templateDescriptor1.getId()).isEqualTo(1);
        assertThat(templateDescriptor1.getChannels()).hasSize(0);
        verify(templateFactory).createTemplate();
        verify(templateRepository).addTemplate(eq(template));
        verify(conversionService).convert(eq(template), any(Class.class));
    }

    @Test
    public void deleteTemplate_noneInRepo_throwsException() {
        //Given
        willThrow(new TemplateNotFoundException(1l)).given(templateRepository).deleteTemplate(anyLong());

        //Then
        expectedException.expect(TemplateNotFoundException.class);

        //When
        ResponseEntity<Void> responseEntity = templateService.deleteTemplate(1l);

    }

    @Test
    public void deleteTemplate_oneInRepo_deletes() {
        //Given
        willDoNothing().given(templateRepository).deleteTemplate(anyLong());

        //When
        ResponseEntity<Void> responseEntity = templateService.deleteTemplate(1l);

        //Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        verify(templateRepository).deleteTemplate(eq(1l));
    }


    @Test
    public void getAllTemplates_twoCreated_returnsBoth() {
        //Given
        Template template1 = new Template();
        template1.setBody("");
        template1.setChannels(Collections.emptyList());
        template1.setId(1l);
        Template template2 = new Template();
        template2.setBody("");
        template2.setChannels(Collections.emptyList());
        template2.setId(2l);
        given(templateRepository.getAllTemplates()).willReturn(Arrays.asList(template1, template2));

        TemplateDescriptor templateDescriptor1 = new TemplateDescriptor();
        templateDescriptor1.id(1l);
        templateDescriptor1.channels(Collections.emptyList());
        templateDescriptor1.body("");

        TemplateDescriptor templateDescriptor2 = new TemplateDescriptor();
        templateDescriptor2.id(2l);
        templateDescriptor2.channels(Collections.emptyList());
        templateDescriptor2.body("");
        given(conversionService.convert(any(Template.class), any(Class.class)))
                .willReturn(templateDescriptor1, templateDescriptor2);

        //When
        ResponseEntity<List<TemplateDescriptor>> responseEntity = templateService.getAllTemplates();

        //Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);

        assertThat(responseEntity.getBody()).hasSize(2);
        assertThat(responseEntity.getBody()).containsExactlyInAnyOrder(templateDescriptor1, templateDescriptor2);
        verify(templateRepository).getAllTemplates();
        verify(conversionService).convert(eq(template1), any(Class.class));
        verify(conversionService).convert(eq(template2), any(Class.class));
    }

    @Test
    public void getTemplate_oneCreated_isReturned() {
        //Given
        Template template1 = new Template();
        template1.setBody("");
        template1.setChannels(Collections.emptyList());
        template1.setId(1l);
        given(templateRepository.getTemplate(anyLong())).willReturn(Optional.of(template1));

        TemplateDescriptor templateDescriptor1 = new TemplateDescriptor();
        templateDescriptor1.id(1l);
        templateDescriptor1.channels(Collections.emptyList());
        templateDescriptor1.body("");

        given(conversionService.convert(any(Template.class), any(Class.class)))
                .willReturn(templateDescriptor1);

        //When
        ResponseEntity<TemplateDescriptor> responseEntity = templateService.getTemplate(1l);

        //Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(templateDescriptor1);
        verify(templateRepository).getTemplate(eq(1l));
        verify(conversionService).convert(eq(template1), any(Class.class));
    }

    @Test
    public void updateTemplate_oneExists_isUpdated() {

        //Given
        Template template1 = new Template();
        template1.setBody("Hello World");
        template1.setChannels(Collections.emptyList());
        template1.setId(1l);
        willDoNothing().given(templateRepository).updateTemplate(anyLong(), any(Template.class));

        TemplateDescriptor templateDescriptor1 = new TemplateDescriptor();
        templateDescriptor1.id(1l);
        templateDescriptor1.channels(Collections.emptyList());
        templateDescriptor1.body("Hello World");

        given(conversionService.convert(any(TemplateDescriptor.class), any(Class.class)))
                .willReturn(template1);
        given(conversionService.convert(any(Template.class), any(Class.class)))
                .willReturn(templateDescriptor1);

        //When
        ResponseEntity<TemplateDescriptor> responseEntity = templateService.updateTemplate(1l, templateDescriptor1);

        //Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(templateDescriptor1);
        verify(templateRepository).updateTemplate(eq(1l), eq(template1));
        verify(conversionService).convert(eq(templateDescriptor1), any(Class.class));

    }
}