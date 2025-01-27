package com.hmall.cart.listener;

import com.hmall.cart.service.ICartService;
import com.rabbitmq.client.AMQP;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CartClearListener {
    private final ICartService cartService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "cart.clear.queue"),
            exchange = @Exchange(name = "trade.topic", type = ExchangeTypes.TOPIC),
            key = {"order.create"}
    ))
    public void cartClear(Set<Long> itemIds){
        cartService.removeByItemIds(itemIds);
    }
}
