package com.dzhatdoev.vk.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ApplicationContextHolder {

    // Контекст Spring
    private static ApplicationContext ctx;

    @Autowired
    public ApplicationContextHolder(ApplicationContext applicationContext) {
        ApplicationContextHolder.ctx = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return ctx;
    }
}
