package com.morgan.template.channel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Email implements Channel {

    private static final String EMAIL = "EMAIL";

    @Override
    public boolean canSend(String name) {
        return EMAIL.equals(name);
    }

    @Override
    public void send(String destination, String message) {

        log.debug("Sending " + message + " to " + destination);
    }
}
