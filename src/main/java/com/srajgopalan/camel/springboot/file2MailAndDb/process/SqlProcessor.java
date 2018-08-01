package com.srajgopalan.camel.springboot.file2MailAndDb.process;

import com.srajgopalan.camel.springboot.file2MailAndDb.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SqlProcessor implements org.apache.camel.Processor{

    @Override
    public void process(Exchange exchange) throws Exception {

    Item item = (Item) exchange.getIn().getBody();

    log.info("Inside processor, composing SQL query..");

    log.info("Item: "+item.toString());

    StringBuilder query = new StringBuilder();
    String table = "items";

    if (item.getOperation().equals("INSERT")){
        query.append("INSERT INTO ");
        query.append(table);
        query.append("(sku,description,price) values ('");
        query.append(item.getSku());
        query.append("','");
        query.append(item.getDescription());
        query.append("',");
        query.append(item.getPrice());
        query.append(");");

    }

    log.info("Query to be run: "+ query);

    exchange.getIn().setBody(query);

    }
}
