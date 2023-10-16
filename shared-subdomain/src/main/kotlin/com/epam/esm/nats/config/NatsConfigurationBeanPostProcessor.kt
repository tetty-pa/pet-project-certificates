package com.epam.esm.nats.config

import com.epam.esm.nats.NatsController
import com.epam.esm.nats.ReactiveMessageHandler
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component
import reactor.core.scheduler.Scheduler

@Component
class NatsConfigurationBeanPostProcessor(
    private val messageHandlerScheduler: Scheduler
) : BeanPostProcessor {
    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        if (bean is NatsController<*, *>) {
            val reactiveHandler = ReactiveMessageHandler(bean, messageHandlerScheduler)
            bean.connection
                .createDispatcher(reactiveHandler)
                .subscribe(bean.subject)
        }
        return bean
    }
}
