package com.lgniewkowski.em.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driver"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(env.getProperty("entitymanager.packagesToScan"));

        Properties hibernateProperties = new Properties();
        for(String hibernatePropertyKey : env.getProperty("hibernate.properties", String[].class)) {
            hibernateProperties.put(hibernatePropertyKey, env.getProperty(hibernatePropertyKey));
        }
        sessionFactory.setHibernateProperties(hibernateProperties);

        return sessionFactory;
    }

    /*@Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        LocalSessionFactoryBean sessionFactorys = sessionFactory();
        transactionManager.setSessionFactory(sessionFactorys.getObject());
        return transactionManager;
    }*/
}
