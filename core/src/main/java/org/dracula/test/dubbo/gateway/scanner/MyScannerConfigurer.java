package org.dracula.test.dubbo.gateway.scanner;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.ReflectUtils;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
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
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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

    private ServiceConfig serviceConfigTemplate;

    private ReferenceConfig referenceConfigTemplate;

    private Consumer<BeanDefinitionHolder> scan1st = holder -> {
        GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
        String previousClassName = definition.getBeanClassName();
        definition.setBeanClass(ReferenceBean.class);
        MutablePropertyValues propertyValues = definition.getPropertyValues();
        propertyValues.addPropertyValue("interface", previousClassName);
        appendAnnotation(Reference.class, referenceConfigTemplate, propertyValues);
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
        MutablePropertyValues propertyValues = definition.getPropertyValues();
        propertyValues.add("interface", previousClassName);
        propertyValues.add("ref", new RuntimeBeanReference(class2nowhere.get(previousClassName)));
        //抄注解写法的话，注解中正好没有ref，想来也合理
        appendAnnotation(Service.class, serviceConfigTemplate, propertyValues);
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

    /**
     * 参考com.alibaba.dubbo.config.AbstractConfig#appendAnnotation(java.lang.Class, java.lang.Object)
     * @param annotationClass
     * @param annotation
     */
//    protected void appendAnnotation(Class<?> annotationClass, Object annotation) {
    private void appendAnnotation(Class<?> annotationClass, Object annotation, MutablePropertyValues propertyValues) {
        Method[] methods = annotationClass.getMethods();
        for (Method method : methods) {
//            if (method.getDeclaringClass() != Object.class
            if (method.getDeclaringClass() != Annotation.class
                    && method.getReturnType() != void.class
                    && method.getParameterTypes().length == 0
                    && Modifier.isPublic(method.getModifiers())
                    && !Modifier.isStatic(method.getModifiers())) {
                try {
                    String property = method.getName();
                    if ("interfaceClass".equals(property) || "interfaceName".equals(property)) {
//                        property = "interface";
                        continue;
                    }
//                    String setter = "set" + property.substring(0, 1).toUpperCase() + property.substring(1);
                    //发现如果返回值是boolean，getter方法名用的是“is...”
                    String getterPrefix = isBoolean(method.getReturnType()) && !isException(property) ? "is":"get";
                    String getter = getterPrefix + property.substring(0, 1).toUpperCase() + property.substring(1);
                    Method getterMethod = annotation.getClass().getMethod(getter);
//                    Object value = method.invoke(annotation, new Object[0]);
                    Object value = getterMethod.invoke(annotation);
                    if (value != null && !value.equals(method.getDefaultValue())) {
                        Class<?> parameterType = ReflectUtils.getBoxedClass(method.getReturnType());
                        if ("filter".equals(property) || "listener".equals(property)) {
                            parameterType = String.class;
                            value = com.alibaba.dubbo.common.utils.StringUtils.join((String[]) value, ",");
                        } else if ("parameters".equals(property)) {
                            parameterType = Map.class;
                            value = CollectionUtils.toStringMap((String[]) value);
                        }
//                        try {
//                            Method setterMethod = getClass().getMethod(setter, new Class<?>[]{parameterType});
//                            setterMethod.invoke(this, new Object[]{value});
//                        } catch (NoSuchMethodException e) {
//                            // ignore
//                        }
                        propertyValues.addPropertyValue(property, value);
                    }
                } catch (Throwable e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    private boolean isBoolean(Class clazz){
        return Boolean.class==clazz || boolean.class==clazz;
    }

    private boolean isException(String prop){
        return "lazy".equals(prop)
                || "sticky".equals(prop)
                || "sent".equals(prop)
                || "stubevent".equals(prop)
                || "export".equals(prop);
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public ServiceConfig getServiceConfigTemplate() {
        return serviceConfigTemplate;
    }

    public void setServiceConfigTemplate(ServiceConfig serviceConfigTemplate) {
        this.serviceConfigTemplate = serviceConfigTemplate;
    }

    public ReferenceConfig getReferenceConfigTemplate() {
        return referenceConfigTemplate;
    }

    public void setReferenceConfigTemplate(ReferenceConfig referenceConfigTemplate) {
        this.referenceConfigTemplate = referenceConfigTemplate;
    }
}
