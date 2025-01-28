package com.hmall.user.listener;

import com.hmall.common.utils.UserContext;
import com.hmall.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserListener {
    private final IUserService userService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("user.money.queue"),
            exchange = @Exchange("user.direct"),
            key = {"deduct.money"}
    ))
    public void moneyDeduct(Map<String, Object> msg) {
        userService.deductMoney((String) msg.get("pw"), (Integer) msg.get("amount"));
    }
}
