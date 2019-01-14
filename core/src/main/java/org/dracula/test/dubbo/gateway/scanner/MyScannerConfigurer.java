package org.dracula.test.dubbo.gateway.scanner;

import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.alibaba.dubbo.config.spring.ServiceBean;
import org.dracula.test.dubbo.gateway.nowhere.FakeImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
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

    private static Logger logger = LoggerFactory.getLogger(MyScannerConfigurer.class);

    private String basePackage;

    private Map<String, String> class2remote = new HashMap<>();

    private Map<String, String> class2nowhere = new HashMap<>();

    private String serviceConfigTemplate;

    private String referenceConfigTemplate;

    private Consumer<BeanDefinitionHolder> scan1st = holder -> {
        GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
        String previousClassName = definition.getBeanClassName();
        definition.setBeanClass(ReferenceBean.class);
        definition.setParentName(referenceConfigTemplate);
        MutablePropertyValues propertyValues = definition.getPropertyValues();
        propertyValues.addPropertyValue("interface", previousClassName);
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
        definition.setParentName(serviceConfigTemplate);
        MutablePropertyValues propertyValues = definition.getPropertyValues();
        propertyValues.add("interface", previousClassName);
        propertyValues.add("ref", new RuntimeBeanReference(class2nowhere.get(previousClassName)));
    };

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        //
        RootBeanDefinition beanDefinition = (RootBeanDefinition)registry.getBeanDefinition(serviceConfigTemplate);
        beanDefinition.setAbstract(true);
        beanDefinition.getPropertyValues().removePropertyValue("interface");
        beanDefinition = (RootBeanDefinition)registry.getBeanDefinition(referenceConfigTemplate);
        beanDefinition.setAbstract(true);
        beanDefinition.getPropertyValues().removePropertyValue("interface");
        //
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

    public String getServiceConfigTemplate() {
        return serviceConfigTemplate;
    }

    public void setServiceConfigTemplate(String serviceConfigTemplate) {
        this.serviceConfigTemplate = serviceConfigTemplate;
    }

    public String getReferenceConfigTemplate() {
        return referenceConfigTemplate;
    }

    public void setReferenceConfigTemplate(String referenceConfigTemplate) {
        this.referenceConfigTemplate = referenceConfigTemplate;
    }
}
