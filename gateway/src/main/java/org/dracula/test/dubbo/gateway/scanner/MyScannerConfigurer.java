package org.dracula.test.dubbo.gateway.scanner;

import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.alibaba.dubbo.config.spring.ServiceBean;
import org.dracula.test.dubbo.gateway.nowhere.FakeImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 参考org.mybatis.spring.mapper.MapperScannerConfigurer
 * @author dk
 */
public class MyScannerConfigurer implements BeanDefinitionRegistryPostProcessor {

    private String basePackage;

    private Map<String, String> class2remote = new HashMap<>();

    private Map<String, String> class2nowhere = new HashMap<>();

    private Consumer<BeanDefinitionHolder> scan1st = holder -> {
        GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
        String previousClassName = definition.getBeanClassName();
        definition.setBeanClass(ReferenceBean.class);
        definition.getPropertyValues().addPropertyValue("interface", previousClassName);
        definition.getPropertyValues().addPropertyValue("url", "rest://localhost:8080");
    };

    private Consumer<BeanDefinitionHolder> scan2nd = holder -> {
        GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
        String previousClassName = definition.getBeanClassName();
        definition.setBeanClass(FakeImpl.class);
        definition.setFactoryMethodName("getObject");
        definition.getConstructorArgumentValues().addIndexedArgumentValue(0, previousClassName);
        definition.getConstructorArgumentValues().addIndexedArgumentValue(1, new RuntimeBeanReference(class2remote.get(previousClassName)));
    };

    private Consumer<BeanDefinitionHolder> scan3rd = holder -> {
        GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
        String previousClassName = definition.getBeanClassName();
        definition.setBeanClass(ServiceBean.class);
        definition.getPropertyValues().add("interface", previousClassName);
        definition.getPropertyValues().add("ref", new RuntimeBeanReference(class2nowhere.get(previousClassName)));
    };

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        MyScanner scanner = new MyScanner(registry, scan1st);
        //在扫描过程中改BeanName，因为扫描中有个机制，如果用Generator得到同名bean，判断两bean是否兼容。。。
        scanner.setBeanNameGenerator(new AnnotationBeanNameGenerator(){
            @Override
            public String generateBeanName(BeanDefinition var1, BeanDefinitionRegistry var2){
                String temp = super.generateBeanName(var1, var2)+"-remote-by-gateway";
                String interfaceName = var1.getBeanClassName();
                class2remote.put(interfaceName, temp);
                return temp;
            }
        });
        scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
        scanner = new MyScanner(registry, scan2nd);
        scanner.setBeanNameGenerator(new AnnotationBeanNameGenerator(){
            @Override
            public String generateBeanName(BeanDefinition var1, BeanDefinitionRegistry var2){
                String temp = super.generateBeanName(var1, var2)+"-nowhere-by-gateway";
                String interfaceName = var1.getBeanClassName();
                class2nowhere.put(interfaceName, temp);
                return temp;
            }
        });
        scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
        scanner = new MyScanner(registry, scan3rd);
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
