package org.dracula.test.dubbo.gateway.scanner;

import com.alibaba.dubbo.config.spring.ReferenceBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.util.function.Consumer;

/**
 * 参考org.mybatis.spring.mapper.MapperScannerConfigurer
 * @author dk
 */
public class MyScannerConfigurer implements BeanDefinitionRegistryPostProcessor {

    private String basePackage;

    private Consumer<GenericBeanDefinition> scan1st = definition -> {
        String previousClassName = definition.getBeanClassName();
        definition.setBeanClass(ReferenceBean.class);
        definition.getPropertyValues().addPropertyValue("interface", previousClassName);
        definition.getPropertyValues().addPropertyValue("url", "rest://localhost:8080");
        definition.getPropertyValues().addPropertyValue("id", previousClassName + "-remote-by-gateway");
    };

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        MyScanner scanner = new MyScanner(registry, scan1st);
        scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 参考org.mybatis.spring.mapper.MapperScannerConfigurer
        // left intentionally blank
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

}
