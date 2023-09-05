package com.estating.goldensheet.listener;

import com.estating.goldensheet.service.GoldensheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoldensheetListeners {

    private final GoldensheetService service;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "parse.goldensheet")
    public void planGoldensheet(Message message) {
        var req = service.parseGoldensheet(message.getBody());

        rabbitTemplate.convertAndSend("opportunity", "opportunity.cmd.plan", req, (msg) -> {
            msg.getMessageProperties().setCorrelationId(message.getMessageProperties().getCorrelationId());
            return msg;
        });
    }

}
