package com.hmall.user.listener;

import cn.hutool.core.math.Money;
import com.hmall.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MoneyDeductListener {
    private final IUserService userService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("user.money.deduct.queue"),
            exchange = @Exchange("user.direct"),
            key = {"money.deduct"}
    ))
    public void moneyDeduct(Map<String, Object> msg) {
        userService.deductMoney((String) msg.get("pw"), (Integer) msg.get("amount"));
    }
}
