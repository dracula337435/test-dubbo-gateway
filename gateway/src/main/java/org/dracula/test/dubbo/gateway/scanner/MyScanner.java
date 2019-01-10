package org.dracula.test.dubbo.gateway.scanner;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 参考org.mybatis.spring.mapper.ClassPathMapperScanner
 * @author dk
 */
public class MyScanner extends ClassPathBeanDefinitionScanner{

    private Consumer<GenericBeanDefinition> definitionConsumer;

    public MyScanner(BeanDefinitionRegistry registry, Consumer<GenericBeanDefinition> definitionConsumer) {
        super(registry, false);
        addIncludeFilter((metadataReader, metadataReaderFactory)->true);
        this.definitionConsumer = definitionConsumer;
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty()) {
            logger.warn("在包'" + Arrays.toString(basePackages) + "'中未找到接口，请检查配置");
        } else {
            processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        for (BeanDefinitionHolder holder : beanDefinitions) {
            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
            definitionConsumer.accept(definition);
        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface();
    }

}
