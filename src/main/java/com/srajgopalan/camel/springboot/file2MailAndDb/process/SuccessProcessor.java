package com.srajgopalan.camel.springboot.file2MailAndDb.process;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SuccessProcessor implements org.apache.camel.Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String msg = "The data has been updated successfully!";
        exchange.getIn().setBody(msg);
    }
}
