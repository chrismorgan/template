package com.morgan.template.channel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Sms implements Channel {

    private static final String SMS = "SMS";

    @Override
    public boolean canSend(String name) {
        return SMS.equals(name);
    }

    @Override
    public void send(String destination, String message) {
        log.debug("Sending " + message + " to " + destination);
    }
}
