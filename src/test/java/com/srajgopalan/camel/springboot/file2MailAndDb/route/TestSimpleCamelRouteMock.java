package com.srajgopalan.camel.springboot.file2MailAndDb.route;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@ActiveProfiles("mock")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@MockEndpoints
public class TestSimpleCamelRouteMock {

    @Autowired
    private CamelContext context;

    @Autowired
    private Environment environment;

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    protected CamelContext createCamelContext() {
        return context;
    }

    @EndpointInject(uri = "{{toRoute1}}")
    private MockEndpoint mockEndpoint;

    @Test
    public void testFileMoveMock() throws InterruptedException {

        String message = "this is a test message";
        String filename = "test.txt";

        //MockEndpoint mockEndpoint = getMockEndpoint("{{toRoute1}}");
        mockEndpoint.expectedBodiesReceived(message);
        mockEndpoint.expectedMessageCount(1);

        //inject the file
        producerTemplate.sendBodyAndHeader(environment.getProperty("startRoute"), message,
                "env",environment.getProperty("spring.profiles.active") );

        mockEndpoint.assertIsSatisfied();
    }
}


