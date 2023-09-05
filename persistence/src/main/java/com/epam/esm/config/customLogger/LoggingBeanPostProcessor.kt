package com.epam.esm.config.customLogger

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.cglib.proxy.Enhancer
import org.springframework.cglib.proxy.MethodInterceptor
import org.springframework.stereotype.Component

@Component
class LoggingBeanPostProcessor : BeanPostProcessor {
    private val loggingBeans = mutableMapOf<String, Class<*>>()

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(LoggingBeanPostProcessor::class.java)
    }

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
        val beanClass: Class<*> = bean.javaClass
        if (beanClass.methods.any { method -> method.isAnnotationPresent(Logging::class.java) }) {
            loggingBeans[beanName] = beanClass
        }
        return bean
    }

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        val originalBean = loggingBeans[beanName]
        if (originalBean != null) {
            val enhancer = Enhancer()
            enhancer.setSuperclass(originalBean.superclass)
            enhancer.setInterfaces(originalBean.interfaces)
            enhancer.setCallback(MethodInterceptor { _, method, args, _ ->
                val originalMethod = originalBean.methods.find { it == method }
                if (originalMethod != null) {
                    val annotation = originalMethod.getAnnotation(Logging::class.java)
                    if (annotation != null) {
                        if (annotation.isRequest) {
                            LOGGER.info("{}#{}: {}", originalBean.simpleName, method.name, args?.toList())
                        }
                    }
                }
                method.invoke(bean, *args)
            })
            return enhancer.create()
        }
        return bean
    }
}

