package com.epam.esm.web

import com.epam.esm.web.natsController.NatsController
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component

@Component
class NatsConfigurationBeanPostProcessor : BeanPostProcessor {
    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
        if (bean is NatsController<*, *>) {
            val dispatcher = bean.connection.createDispatcher { message ->
                val response = bean.handle(message)
                bean.connection.publish(message.replyTo, response.toByteArray())
            }
            dispatcher.subscribe(bean.subject)
        }
        return bean
    }
}
