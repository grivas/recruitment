package riskanalizer.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import riskanalizer.service.controller.RiskAssessment;
import riskanalizer.service.repository.BalanceRepository;
import riskanalizer.service.repository.DefaultBalanceRepository;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableAutoConfiguration
@EnableWebMvc
public class RiskanalizerServiceApplication implements WebApplicationInitializer {

    public static void main(String[] args) {
        SpringApplication.run(RiskanalizerServiceApplication.class, args);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        WebApplicationContext context = getContext();
        servletContext.addListener(new ContextLoaderListener(context));
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DispatcherServlet", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/*");
    }

    private AnnotationConfigWebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(RiskanalizerServiceApplication.class);
        return context;
    }

    @Bean
    public ConcurrentHashMap<String, Integer> dataStore() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public BalanceRepository balanceRepository(ConcurrentHashMap<String, Integer> dataStore) {
        return new DefaultBalanceRepository(dataStore);
    }

    @Bean
    public RiskAssessment decisionController(BalanceRepository balanceRepository) {
        return new RiskAssessment(balanceRepository);
    }
}
