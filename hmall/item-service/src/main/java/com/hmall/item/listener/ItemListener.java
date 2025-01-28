package com.hmall.item.listener;

import com.hmall.api.dto.ItemDTO;
import com.hmall.api.dto.OrderDetailDTO;
import com.hmall.common.utils.UserContext;
import com.hmall.item.service.IItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Correlation;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemListener {

    private final IItemService itemService;
    private final RabbitTemplate rabbitTemplate;
    private static final HashMap<String, List<ItemDTO>> container = new HashMap<>();

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "deduct.stock.queue"),
            exchange = @Exchange(name = "item.direct"),
            key = {"deduct.stock"}
    ))
    public void deductStock(List<OrderDetailDTO> items) {
        itemService.deductStock(items);
    }
}
