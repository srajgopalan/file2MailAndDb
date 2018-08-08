package com.srajgopalan.camel.springboot.file2MailAndDb.route;

import com.srajgopalan.camel.springboot.file2MailAndDb.domain.Item;
import com.srajgopalan.camel.springboot.file2MailAndDb.exception.DataException;
import com.srajgopalan.camel.springboot.file2MailAndDb.process.MailProcessor;
import com.srajgopalan.camel.springboot.file2MailAndDb.process.SqlProcessor;
import com.srajgopalan.camel.springboot.file2MailAndDb.process.SuccessProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Slf4j
public class SimpleCamelRoute extends RouteBuilder {

    @Autowired
    private Environment environment;

    private DataFormat dataFormat = new BindyCsvDataFormat(Item.class);

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Autowired
    private SqlProcessor sqlProcessor;

    @Autowired
    private SuccessProcessor successProcessor;

    @Autowired
    private MailProcessor mailProcessor;

    @Override
    public void configure() throws Exception {

        log.info("Starting route..");

        //define a custom error handler
        //errorHandler(deadLetterChannel("log:errorInRoute?level=ERROR&showProperties=true").maximumRedeliveries(3).redeliveryDelay(3000).useExponentialBackOff().backOffMultiplier(2));

        onException(PSQLException.class).log(LoggingLevel.ERROR, "PSQL exception caught in route..").maximumRedeliveries(3).redeliveryDelay(3000);

        onException(DataException.class).log(LoggingLevel.ERROR, "Data Exception caught in route..").process(mailProcessor);

        from("{{startRoute}}").routeId("mainRoute").routeDescription("Route from File to DB")
                .log("Triggered the timer in environment: "+environment.getProperty("message") + "..")

                .choice()
                    .when(header("env").isEqualTo("mock"))
                        .log("This is a mock test, hence not running pollEnrich..")
                    .otherwise()
                        .pollEnrich("{{fromRoute}}")
                .end()

                .to("{{toRoute1}}")
                .log("File has been moved to output directory now going to unmarshal..")
                .unmarshal(dataFormat)
                .to("log:Unmarshalled object ?level=INFO&showBody=true")
        .split(body())
                .log("the split line is: ${body}")
                .process(sqlProcessor)
                .to("{{toRoute2}}")
                .to("log:insertLog?showHeaders=true")
        .end()
        .process(successProcessor)
        .to("{{toRoute3}}");

        log.info("Ending route..");
    }
}
