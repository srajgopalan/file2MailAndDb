package com.srajgopalan.camel.springboot.file2MailAndDb.route;

import com.srajgopalan.camel.springboot.file2MailAndDb.process.HealthCheckProcessor;
import com.srajgopalan.camel.springboot.file2MailAndDb.process.MailProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HealthCheckRoute extends RouteBuilder {

    @Autowired
    private Environment environment;

    @Autowired
    private HealthCheckProcessor healthCheckProcessor;

    @Autowired
    private MailProcessor mailProcessor;

    @Override
    public void configure() throws Exception {

        from("{{healthRoute}}").routeId("healthCheckRoute")
                .log("Querying endpoint: "+ environment.getProperty("healthUrl"))

                .choice()
                    .when(header("env").isNotEqualTo("mock"))
                    .pollEnrich("{{healthUrl}}")
                .end()

                .process(healthCheckProcessor)
                
                .choice()
                    .when(header("error").isEqualTo("true"))
                        .choice()
                            .when(header("env").isNotEqualTo("mock"))
                                .process(mailProcessor)
                            .end()
                .end();




    }
}
