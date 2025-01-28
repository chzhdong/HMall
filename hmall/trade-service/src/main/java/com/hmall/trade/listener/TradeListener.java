package com.hmall.trade.listener;

import com.hmall.common.utils.UserContext;
import com.hmall.trade.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TradeListener {

    private final IOrderService orderService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "trade.success.queue"),
            exchange = @Exchange(name = "order.direct"),
            key = {"pay.success"}
    ))
    public void listenSuccess(Long orderId) {
        orderService.markOrderPaySuccess(orderId);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "user.identity.queue"),
            exchange = @Exchange(name = "user.direct"),
            key = {"user.identity"}
    ))
    public void userLogin(Long userId) {
        UserContext.setUser(userId);
    }}
