package com.morgan.template.service;

import com.morgan.template.api.SendApiDelegate;
import com.morgan.template.channel.Channel;
import com.morgan.template.exception.ChannelNotFoundException;
import com.morgan.template.exception.TemplateNotFoundException;
import com.morgan.template.model.MetaData;
import com.morgan.template.model.Template;
import com.morgan.template.repository.TemplateRepository;
import com.samskivert.mustache.Mustache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService implements SendApiDelegate {

    private final TemplateRepository templateRepository;
    private final List<Channel> channelsHandlers;

    @Autowired
    public MessageService(TemplateRepository templateRepository, List<Channel> channels) {
        this.templateRepository = templateRepository;
        this.channelsHandlers = channels;
    }

    @Override
    public ResponseEntity<Void> sendMessage(Long id, String channel, MetaData metaData) {
        Optional<Template> template = templateRepository.getTemplate(id);
        if (!template.isPresent()) {
            throw new TemplateNotFoundException(id);
        }

        Template concreteTemplate = template.get();
        if (channelsHandlers.stream().noneMatch(handler -> handler.canSend(channel))) {
            throw new ChannelNotFoundException(channel);
        }

        List<Channel> validHandlers = channelsHandlers.stream()
                .filter(handler -> handler.canSend(channel))
                .filter(handler -> concreteTemplate.getChannels().stream().anyMatch(templateChannel -> templateChannel.equals(channel)))
                .collect(Collectors.toList());

        String destination = metaData.getDestination();
        validHandlers.forEach(handler -> handler.send(destination, buildMessage(concreteTemplate, metaData)));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String buildMessage(Template template, MetaData metaData) {
        com.samskivert.mustache.Template tmpl = Mustache.compiler().compile(template.getBody());
        return tmpl.execute(metaData.getData());
    }
}
