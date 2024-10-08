package com.jmunoz.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.jmunoz.lifecycle.controllers.MyController;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class LifeCycleDemoBean implements InitializingBean, DisposableBean, BeanNameAware,
    BeanFactoryAware, ApplicationContextAware, BeanPostProcessor {

  public LifeCycleDemoBean() {
    System.out.println("## I'm in the LifeCycleBean Constructor ##");
  }

  private String javaVer;
  
  // Spring, ya de forma automática, contiene propiedades ya con valor en su contexto.
  // Esta propiedad viene del environment.
  @Value("${java.specification.version}")
  public void setJavaVer(String javaVer) {
    this.javaVer = javaVer;
    System.out.println("## 1 Properties Set. Java Ver: " + this.javaVer);
  }

  @Override
  public void setBeanName(String name) {
    System.out.println("## 2 BeanNameAware My Bean Name is: " + name);
  }
  
  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    System.out.println("## 3 Bean Factory Aware - Bean Factory has been set");
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    System.out.println("## 4 ApplicationContextAware - Application context has been set");
  }

  @PostConstruct
  public void postConstruct() {
    System.out.println("## 5 postConstruct The Post Construct annotated method has been called");
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    System.out.println("## 6 afterPropertiesSet Populate Properties The LifeCycleBean has its properties");
  }

  @PreDestroy
  public void preDestroy() {
    System.out.println("## 7 The @PreDestroy annotated method has been called");
  }

  @Override
  public void destroy() throws Exception {
    System.out.println("## 8 DisposableBean.destroy The Lifecycle bean has been terminated");
  }

  // Estos dos métodos siguientes están fuera de secuencia.
  // Van a ser llamados por cada objeto en el contexto, antes del método destroy()
  // Es parte del startup.
  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    System.out.println("## postProcessBeforeInitialization: " + beanName);

    if (bean instanceof MyController) {
      MyController myController = (MyController) bean;
      System.out.println("Calling before init");
      myController.beforeInit();
    }

    return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    System.out.println("## postProcessAfterInitialization: " + beanName);

    if (bean instanceof MyController) {
      MyController myController = (MyController) bean;
      System.out.println("Calling after init");
      myController.afterInit();
    }

    return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
  }
}
