package com.srajgopalan.camel.springboot.file2MailAndDb.route;

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

//    @Autowired
//    private CamelContext context;

    @Autowired
    private Environment environment;

    @Autowired
    private ProducerTemplate producerTemplate;

//    @Autowired
//    protected CamelContext createCamelContext() {
//        return context;
//    }

    @EndpointInject(uri = "{{toRoute1}}")
    private MockEndpoint mockEndpoint1;

    @EndpointInject(uri = "{{toRoute3}}")
    private MockEndpoint mockEndpoint3;


    @Test
    public void testFileMoveMock() throws InterruptedException {

        String message = "Operation,SKU,Item,Price\n" +
                "ADD,100,Samsung TV,100\n" +
                "ADD,101,LG TV,200";

        String filename = "test.txt";

        //MockEndpoint mockEndpoint = getMockEndpoint("{{toRoute1}}");
        mockEndpoint1.expectedBodiesReceived(message);
        mockEndpoint1.expectedMessageCount(1);

        //inject the file
        producerTemplate.sendBodyAndHeader(environment.getProperty("startRoute"), message,
                "env",environment.getProperty("spring.profiles.active") );

        mockEndpoint1.assertIsSatisfied();
    }

    @Test
    public void testFileMoveandDbMock() throws InterruptedException {

        // In this test we are mocking the components including the DB so we
        // will only test that the output file (success file) is created
        // after the DB insert step

        String message = "Operation,SKU,Item,Price\n" +
                "ADD,100,Samsung TV,100\n" +
                "ADD,101,LG TV,200";

        String filename = "test.txt";

        mockEndpoint1.expectedBodiesReceived(message);
        mockEndpoint1.expectedMessageCount(1);

        String outputMessage = "The data has been updated successfully!";

        mockEndpoint3.expectedBodiesReceived(outputMessage);
        mockEndpoint3.expectedMessageCount(1);

        //inject the file
        producerTemplate.sendBodyAndHeader(environment.getProperty("startRoute"), message,
                "env",environment.getProperty("spring.profiles.active") );

        mockEndpoint1.assertIsSatisfied();
        mockEndpoint3.assertIsSatisfied();


    }
}


