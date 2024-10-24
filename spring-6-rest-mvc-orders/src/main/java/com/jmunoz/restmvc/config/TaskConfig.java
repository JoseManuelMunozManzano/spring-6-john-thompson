package com.jmunoz.restmvc.config;

import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executors;

// Con @EnableAsync lo hacemos asíncrono.
@EnableAsync
@Configuration
public class TaskConfig {

    // Configuramos un TaskExecutor donde creamos Virtual Threads (Java 21) para manejar tareas asíncronas.
    // El nombre del Bean lo cogemos de la documentación de Spring Framework.
    @Bean(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME)
    public AsyncTaskExecutor asyncTaskExecutor() {
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }
}
