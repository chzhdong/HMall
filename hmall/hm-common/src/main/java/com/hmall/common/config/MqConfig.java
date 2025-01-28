package com.hmall.common.config;

import com.hmall.common.utils.UserContext;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter() {
            @Override
            public Object fromMessage(Message message) {
                // 从消息头中提取用户信息
                Object payload = super.fromMessage(message);
                Long userId = extractUserInfoFromMessage(message);
                if (userId != null) {
                    UserContext.setUser(userId);
                }
                return payload;
            }

            private Long extractUserInfoFromMessage(Message message) {
                try {
                    return (Long) message.getMessageProperties()
                            .getHeaders()
                            .get("user-info");
                } catch (Exception e) {
                    return null;
                }
            }
        };
    }

    @Bean
    public MessagePostProcessor userIdPostProcessor() {
        return message -> {
            Long currentUser = UserContext.getUser();
            if (currentUser != null) {
                message.getMessageProperties().getHeaders()
                        .put("user-info", currentUser);
            }
            return message;
        };
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, RabbitTemplateConfigurer configurer,
            MessageConverter userAwareMessageConverter, MessagePostProcessor userInfoMessagePostProcessor) {

        RabbitTemplate template = new RabbitTemplate();
        configurer.configure(template, connectionFactory);
        template.setMessageConverter(userAwareMessageConverter);
        template.setBeforePublishPostProcessors(userInfoMessagePostProcessor);
        template.setMandatory(true);
        return template;
    }
}
