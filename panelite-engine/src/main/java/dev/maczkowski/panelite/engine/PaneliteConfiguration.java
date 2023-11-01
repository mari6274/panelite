package dev.maczkowski.panelite.engine;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class PaneliteConfiguration implements BeanPostProcessor {
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof InternalResourceViewResolver) {
            InternalResourceViewResolver internalResourceViewResolver = (InternalResourceViewResolver) bean;
            internalResourceViewResolver.setPrefix("/panelite/views/");
            internalResourceViewResolver.setSuffix(".jsp");
        }
        return bean;
    }
}
