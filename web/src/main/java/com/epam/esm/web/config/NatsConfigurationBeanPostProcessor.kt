package com.epam.esm.web.config

import com.epam.esm.web.natsController.NatsController
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component

@Component
class NatsConfigurationBeanPostProcessor : BeanPostProcessor {
    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        if (bean is NatsController<*, *>) {
            val dispatcher = bean.connection.createDispatcher { message ->
                bean.handle(message)
                    .doOnNext {
                        bean.connection.publish(message.replyTo, it.toByteArray())
                    }
                    .subscribe()
            }
            dispatcher.subscribe(bean.subject)
        }
        return bean
    }
}
