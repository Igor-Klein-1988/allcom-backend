package de.allcom;

import lombok.Data;
import org.springframework.messaging.simp.stomp.StompHeaders;

@Data
public class StompMessage {
    private final StompHeaders stompHeaders;
    private final String payload;
}
