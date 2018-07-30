package com.srajgopalan.camel.springboot.file2MailAndDb.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SimpleCamelRoute extends RouteBuilder {

    @Autowired
    private Environment environment;

    @Override
    public void configure() throws Exception {
        from("{{startRoute}}")
                .log("Triggered the timer in environment: "+environment.getProperty("message") + "..")

                .choice()
                    .when(header("env").isEqualTo("mock"))
                        .log("This is a mock test, hence not running pollEnrich..")
                    .otherwise()
                        .pollEnrich("{{fromRoute}}")
                .end()

                .to("{{toRoute1}}");
    }
}
