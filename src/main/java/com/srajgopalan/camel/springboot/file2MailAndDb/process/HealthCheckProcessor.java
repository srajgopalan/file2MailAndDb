package com.srajgopalan.camel.springboot.file2MailAndDb.process;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class HealthCheckProcessor implements org.apache.camel.Processor {
    @Override
    public void process(Exchange exchange) throws Exception {

        String body = exchange.getIn().getBody(String.class);

        //log.info("The healthCheck result is: "+ body);

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> map = objectMapper.readValue(body, new TypeReference<Map<String,Object>>() {});

        String status = (String) map.get("status");

        log.info("status:" + status);

        if (!status.equals("UP") ) {
            String details = map.get("details").toString();

            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("Status Down reported for application, details : \n\n ");
            stringBuilder.append(details);

            exchange.getIn().setHeader("error", true);
            exchange.getIn().setBody(stringBuilder.toString());
            exchange.setProperty(Exchange.EXCEPTION_CAUGHT, stringBuilder.toString());

        }

    }
}
