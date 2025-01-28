package com.hmall.pay.listener;

import com.hmall.common.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PayListener {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "user.identity.queue"),
            exchange = @Exchange(name = "user.direct"),
            key = {"user.identity"}
    ))
    public void userLogin(Long userId) {
        UserContext.setUser(userId);
    }}
