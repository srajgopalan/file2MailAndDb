package com.srajgopalan.camel.springboot.file2MailAndDb.process;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MailProcessor implements org.apache.camel.Processor {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment environment;

    @Override
    public void process(Exchange exchange) throws Exception {

        Exception e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom(environment.getProperty("mailFrom"));
        simpleMailMessage.setTo(environment.getProperty("mailTo"));
        simpleMailMessage.setSubject("Exception in camel route for file2MailAndDb");

        log.info("Exception Raised in Route: "+ e.getMessage());

        if (e.getMessage() != null) {

            simpleMailMessage.setText("Exception raised in camel route: " + e.getMessage() + "\n");
        }

        log.info("about to send mail to: "+ environment.getProperty("mailTo") + " with exception details");

        javaMailSender.send(simpleMailMessage);
    }
}
