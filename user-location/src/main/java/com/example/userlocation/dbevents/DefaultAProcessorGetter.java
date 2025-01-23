package com.example.userlocation.dbevents;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Map;

@AllArgsConstructor
public abstract class DefaultAProcessorGetter<TAnnotation, TTarget>
        implements EventAnnotationProcessorGetter<TAnnotation, TTarget>,
        BeanPostProcessor {

    protected Map<TAnnotation, TTarget> targets;

    @Override
    public Map<TAnnotation, TTarget> getTargets() {
        return targets;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        Class<?> beanClass = bean.getClass();

        Map<TAnnotation, TTarget> newTargets = callback(beanClass);
        if (newTargets != null) {
            targets.putAll(newTargets);
        }

        return bean;
    }

    protected abstract Map<TAnnotation, TTarget> callback(Class<?> clazz);
}
