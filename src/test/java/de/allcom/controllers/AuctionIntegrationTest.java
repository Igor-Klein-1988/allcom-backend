package de.allcom.controllers;

import de.allcom.StompMessage;
import java.lang.reflect.Type;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuctionIntegrationTest {

    @LocalServerPort
    private int port;

    private WebSocketStompClient stompClient;
    private final BlockingQueue<StompMessage> blockingQueue = new ArrayBlockingQueue<>(1);

    @BeforeEach
    public void setup() {
        this.stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Test
    @Sql(scripts = "/sql/data.sql")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testProcessBet() throws Exception {
        URI uri = URI.create("ws://localhost:" + port + "/auction-websocket");
        System.out.println("port = " + port);
        StompSession stompSession = stompClient.connect(uri.toString(), new StompSessionHandlerAdapter() {
                                               })
                                               .get(1, TimeUnit.SECONDS);

        stompSession.subscribe("/topic/auction/1", new DefaultStompFrameHandler());

        String sampleBet = "{\"auctionId\":1,\"betAmount\":100}";
        stompSession.send("/app/makeBet", sampleBet.getBytes());

        StompMessage message = blockingQueue.poll(1, TimeUnit.SECONDS);

        // Проверьте, что контроллер корректно обработал сообщение
        assertEquals("Ожидаемый ответ", message.getPayload());
    }

    private class DefaultStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return String.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            blockingQueue.add(new StompMessage(headers, (String) payload));
        }
    }
}
