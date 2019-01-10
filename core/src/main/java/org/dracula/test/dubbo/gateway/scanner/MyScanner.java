package org.dracula.test.dubbo.gateway.scanner;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 参考org.mybatis.spring.mapper.ClassPathMapperScanner
 * @author dk
 */
public class MyScanner extends ClassPathBeanDefinitionScanner{

    private Consumer<BeanDefinitionHolder> holderConsumer;

    public MyScanner(BeanDefinitionRegistry registry, Consumer<BeanDefinitionHolder> holderConsumer) {
        super(registry, false);
        addIncludeFilter((metadataReader, metadataReaderFactory)->true);
        this.holderConsumer = holderConsumer;
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
            holderConsumer.accept(holder);
        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface();
    }

}
