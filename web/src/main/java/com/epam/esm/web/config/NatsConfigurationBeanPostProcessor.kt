package com.epam.esm.web.config

import com.epam.esm.web.natsController.NatsController
import com.epam.esm.web.natsController.ReactiveMessageHandler
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component
import reactor.core.scheduler.Scheduler

@Component
class NatsConfigurationBeanPostProcessor(
    private val scheduler: Scheduler
) : BeanPostProcessor {
    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        if (bean is NatsController<*, *>) {
            val reactiveHandler = ReactiveMessageHandler(bean, scheduler)
            bean.connection
                .createDispatcher(reactiveHandler)
                .subscribe(bean.subject)
        }
        return bean
    }
}
