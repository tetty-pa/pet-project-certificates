package com.epam.esm.config.customLogger

import kotlin.reflect.KClass
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy

@Component
class LoggingBeanPostProcessor : BeanPostProcessor {
    private val loggingBeans = mutableMapOf<String, KClass<*>>()

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(LoggingBeanPostProcessor::class.java)
    }

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
        val beanClass: KClass<*> = bean::class
        if (beanClass.java.isAnnotationPresent(Logging::class.java)) {
            loggingBeans[beanName] = beanClass
        }
        return bean
    }

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        val beanClass = loggingBeans[beanName]
        beanClass?.apply {
            return Proxy.newProxyInstance(
                beanClass.java.classLoader,
                beanClass.java.interfaces,
                InvocationHandler { _, method, args ->
                    val annotation = beanClass.java.getAnnotation(Logging::class.java)
                    if (annotation.isRequest) {
                        LOGGER.info("{}#{}: {}", bean::class.java, method.name, args?.toList())
                    }
                    val result = method.invoke(bean, *(args ?: emptyArray()))

                    if (annotation.isResponse) {
                        LOGGER.info(
                            "{}#{}: {}", bean::class.java, method.name, result?.toString() ?: "void"
                        )
                    }
                    return@InvocationHandler result
                }
            )
        }
        return bean
    }
}

