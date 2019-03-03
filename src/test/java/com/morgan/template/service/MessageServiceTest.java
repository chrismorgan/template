package com.morgan.template.service;

import com.morgan.template.channel.Channel;
import com.morgan.template.channel.Email;
import com.morgan.template.exception.ChannelNotFoundException;
import com.morgan.template.exception.TemplateNotFoundException;
import com.morgan.template.model.MetaData;
import com.morgan.template.model.Template;
import com.morgan.template.repository.TemplateRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private Email email;
    private List<Channel> channels = new ArrayList<>();
    @Mock
    private TemplateRepository templateRepository;
    private MessageService messageService;

    @Before
    public void setUp() throws Exception {
        channels.add(email);
        messageService = new MessageService(templateRepository, channels);

    }

    @Test
    public void sendMessage_templateExistsChannelHandled_sendsMessage() {

        //Given
        Template template = new Template();
        template.setId(1l);
        List<String> channels = new ArrayList<String>();
        channels.add("EMAIL");
        template.setChannels(channels);
        template.setBody("Hello {{name}}");
        given(templateRepository.getTemplate(anyLong())).willReturn(Optional.of(template));
        given(email.canSend(anyString())).willReturn(true);

        //When
        MetaData metaData = new MetaData();
        metaData.destination("dest");
        Map<String, String> data = new HashMap<>();
        data.put("name", "Chris");
        metaData.setData(data);
        messageService.sendMessage(1l, "EMAIL", metaData);

        //Then
        verify(email, times(2)).canSend("EMAIL");
        verify(email).send("dest", "Hello Chris");
        verify(templateRepository).getTemplate(1l);
    }

    @Test
    public void sendMessage_templateNotExistsChannelHandled_sendsMessage() {

        //Given
        given(templateRepository.getTemplate(anyLong())).willReturn(Optional.empty());

        //Then
        expectedException.expect(TemplateNotFoundException.class);

        //When
        MetaData metaData = new MetaData();
        metaData.destination("dest");
        Map<String, String> data = new HashMap<>();
        data.put("name", "Chris");
        metaData.setData(data);
        messageService.sendMessage(1l, "EMAIL", metaData);

    }

    @Test
    public void sendMessage_templateExistsChannelNotHandled_sendsMessage() {

        //Given
        Template template = new Template();
        template.setId(1l);
        List<String> channels = new ArrayList<String>();
        channels.add("PUSH");
        template.setChannels(channels);
        template.setBody("Hello {{name}}");
        given(templateRepository.getTemplate(anyLong())).willReturn(Optional.of(template));
        given(email.canSend(eq("PIGEON"))).willReturn(false);

        //Then
        expectedException.expect(ChannelNotFoundException.class);

        //When
        MetaData metaData = new MetaData();
        metaData.destination("dest");
        Map<String, String> data = new HashMap<>();
        data.put("name", "Chris");
        metaData.setData(data);
        messageService.sendMessage(1l, "PIGEON", metaData);

    }
}