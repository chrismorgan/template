package com.morgan.template.channel;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SmsTest {

    private Sms sms;

    @Before
    public void setUp() throws Exception {

        sms = new Sms();
    }

    @Test
    public void canSend_shouldSend_returnTrue() {
        //Given
        String channelToSend = "SMS";

        //When
        boolean canSend = sms.canSend(channelToSend);

        //Then
        assertThat(canSend).isTrue();
    }

    @Test
    public void canSend_shouldntSend_returnFalse() {

        //Given
        String channelToSend = "EMAIL";

        //When
        boolean canSend = sms.canSend(channelToSend);

        //Then
        assertThat(canSend).isFalse();

    }
}