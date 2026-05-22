package com.vahak.pc.policy.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfig {

    @Bean
    fun jsonMessageConverter(): MessageConverter {
        return JacksonJsonMessageConverter()
    }

    @Bean
    fun childEventsExchange(): TopicExchange {
        return TopicExchange("child.events")
    }

    // --- Creation Queue ---
    @Bean
    fun childCreatedQueue(): Queue {
        return Queue("policy.child.created.queue", true)
    }

    @Bean
    fun childCreatedBinding(childCreatedQueue: Queue, childEventsExchange: TopicExchange): Binding {
        return BindingBuilder.bind(childCreatedQueue).to(childEventsExchange).with("child.created")
    }

    // --- Deletion Queue ---
    @Bean
    fun childDeletedQueue(): Queue {
        return Queue("policy.child.deleted.queue", true)
    }

    @Bean
    fun childDeletedBinding(childDeletedQueue: Queue, childEventsExchange: TopicExchange): Binding {
        return BindingBuilder.bind(childDeletedQueue).to(childEventsExchange).with("child.deleted")
    }
}