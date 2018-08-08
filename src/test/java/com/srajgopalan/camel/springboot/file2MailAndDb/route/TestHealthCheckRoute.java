package com.srajgopalan.camel.springboot.file2MailAndDb.route;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertEquals;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@ActiveProfiles("mock")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
public class TestHealthCheckRoute {

    @Autowired
    private ProducerTemplate producerTemplate;

    public RouteBuilder createRouteBuilder(){
        return new HealthCheckRoute();
    }

    @Test
    public void checkHealthMock(){

        String testInput = "{\"status\":\"DOWN\",\"details\":{\"camel\":{\"status\":\"UP\",\"details\":{\"name\":\"camel-1\",\"version\":\"2.22.0\",\"uptime\":\"1.925 seconds\",\"uptimeMillis\":1925,\"status\":\"Started\"}},\"diskSpace\":{\"status\":\"UP\",\"details\":{\"total\":499055067136,\"free\":30911418368,\"threshold\":10485760}},\"db\":{\"status\":\"DOWN\",\"details\":{\"error\":\"org.springframework.jdbc.CannotGetJdbcConnectionException: Failed to obtain JDBC Connection; nested exception is org.postgresql.util.PSQLException: Connection to localhost:54321 refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.\"}},\"mail\":{\"status\":\"UP\",\"details\":{\"location\":\"smtp.gmail.com:587\"}}}}";

        String response = (String) producerTemplate.requestBodyAndHeader("{{healthRoute}}",testInput, "env","mock");

        String expectedResponse =  "Status Down reported for application, details : \n" +
                "\n" +
                " {camel={status=UP, details={name=camel-1, version=2.22.0, uptime=1.925 seconds, uptimeMillis=1925, status=Started}}, diskSpace={status=UP, details={total=499055067136, free=30911418368, threshold=10485760}}, db={status=DOWN, details={error=org.springframework.jdbc.CannotGetJdbcConnectionException: Failed to obtain JDBC Connection; nested exception is org.postgresql.util.PSQLException: Connection to localhost:54321 refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.}}, mail={status=UP, details={location=smtp.gmail.com:587}}}";

        assertEquals(expectedResponse,response);


    }
}
