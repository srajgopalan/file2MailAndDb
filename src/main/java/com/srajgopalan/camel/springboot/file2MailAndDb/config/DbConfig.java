package com.srajgopalan.camel.springboot.file2MailAndDb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DbConfig {

    @Bean(name="dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource(){
        DataSource ds = DataSourceBuilder.create().build();    // this will automatically parse all the
                                                               // properties in application.yml and populate it
                                                               // in the datasource builder
        return ds;
    }

}
