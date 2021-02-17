package ru.radish.crawler.application;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AppContextGetter{

    @Autowired
    private ApplicationContext context;

    public ApplicationContext getAppContext(){
        return context;
    }

}
