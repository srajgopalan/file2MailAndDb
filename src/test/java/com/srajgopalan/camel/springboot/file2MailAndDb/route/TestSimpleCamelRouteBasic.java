package com.srajgopalan.camel.springboot.file2MailAndDb.route;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

public class TestSimpleCamelRouteBasic {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private Environment environment;

    //ensures that the input and output directories are cleaned up before we start our testing
    @BeforeClass
    public static void startupClean() throws IOException {
        FileUtils.cleanDirectory(new File("/tmp/camel/input"));
        FileUtils.deleteDirectory(new File("/tmp/camel/output"));
    }

    @Test
    public void testFileMoveBasic() throws InterruptedException {
        String message = "this is a test message";
        String filename = "test.txt";

        //inject the file
        producerTemplate.sendBodyAndHeader(environment.getProperty("fromRoute"), message,
                Exchange.FILE_NAME, filename);

        Thread.sleep(5000);

        //now check if the output file exists

        File outputDir = new File("/tmp/camel/output");

        File outputFile = new File( "/tmp/camel/output/" + filename);

        assertTrue(outputDir.exists());
        assertNotEquals(0, outputDir.listFiles());
        assertTrue(outputFile.exists());

    }
}
