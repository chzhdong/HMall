package com.hmall.trade.listener;

import com.hmall.api.client.PayClient;
import com.hmall.api.dto.PayOrderDTO;
import com.hmall.trade.domain.po.Order;
import com.hmall.trade.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TradeListener {

    private final IOrderService orderService;
    private final PayClient payClient;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "trade.success.queue"),
            exchange = @Exchange(name = "order.direct"),
            key = {"pay.success"}
    ))
    public void listenSuccess(Long orderId) {
        orderService.markOrderPaySuccess(orderId);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "delay.queue", durable = "true"),
            exchange = @Exchange(name = "delay.direct", delayed = "true"),
            key = "delay"
    ))
    public void listenDelay(Long orderId){
        Order order = orderService.getById(orderId);
        if(order == null || order.getStatus() != 1){
            // 订单不存在或者已经支付
            return;
        }
        PayOrderDTO payOrder = payClient.queryPayOrderByBizOrderNo(orderId);
        if(payOrder != null && payOrder.getStatus() == 3){
            orderService.markOrderPaySuccess(orderId);
        } else {
            orderService.cancelOrder(orderId);
        }
    }
}