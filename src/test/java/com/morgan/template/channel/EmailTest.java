package com.morgan.template.channel;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class EmailTest {

    private Email email;

    @Before
    public void setUp() throws Exception {
        email = new Email();
    }

    @Test
    public void canSend_shouldSend_returnTrue() {
        //Given
        String channelToSend = "EMAIL";

        //When
        boolean canSend = email.canSend(channelToSend);

        //Then
        assertThat(canSend).isTrue();
    }

    @Test
    public void canSend_shouldntSend_returnFalse() {

        //Given
        String channelToSend = "SMS";

        //When
        boolean canSend = email.canSend(channelToSend);

        //Then
        assertThat(canSend).isFalse();

    }
}