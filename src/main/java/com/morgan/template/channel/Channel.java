package com.morgan.template.channel;

public interface Channel {

    boolean canSend(String name);

    void send(String destination, String message);
}
