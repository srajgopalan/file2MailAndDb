package com.srajgopalan.camel.springboot.file2MailAndDb.route;

import lombok.extern.slf4j.Slf4j;
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
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j

public class TestSimpleCamelRouteBasic {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private Environment environment;

    //ensures that the input and output directories are cleaned up before we start our testing
    @BeforeClass
    public static void startupClean() throws IOException {
        log.info("In cleanup function..");
        FileUtils.cleanDirectory(new File("/tmp/camel/input"));
        FileUtils.deleteDirectory(new File("/tmp/camel/output"));
        FileUtils.deleteDirectory(new File("/tmp/camel/input/error"));
    }

    @Test
    public void testFileMoveBasic() throws InterruptedException {
        String message = "Operation,SKU,Item,Price\n" +
                "INSERT,100,Samsung TV,500\n" +
                "INSERT,101,LG TV,700";

        String filename = "test.txt";

        //inject the file
        producerTemplate.sendBodyAndHeader(environment.getProperty("fromRoute"), message,
                Exchange.FILE_NAME, filename);

        Thread.sleep(5000);

        //now check if the output file exists

        File outputDir = new File("/tmp/camel/output");

        File outputFile = new File("/tmp/camel/output/" + filename);

        assertTrue(outputDir.exists());
        assertNotEquals(0, outputDir.listFiles());
        assertTrue(outputFile.exists());

    }

    @Test
    public void testFileMoveDbInsert() throws InterruptedException, IOException {
        String message = "Operation,SKU,Item,Price\n" +
                "INSERT,100,Samsung TV,500\n" +
                "INSERT,101,LG TV,700";
        String filename = "test.txt";

        //inject the file
        producerTemplate.sendBodyAndHeader(environment.getProperty("fromRoute"), message,
                Exchange.FILE_NAME, filename);

        Thread.sleep(5000);

        //now check if the success file exists

        File outputDir = new File("/tmp/camel/output");

        String outFileName = "success.txt";

        File outputFile = new File("/tmp/camel/output/" + outFileName);

        String outputMessage = "The data has been updated successfully!";
        String output = new String(Files.readAllBytes(Paths.get("/tmp/camel/output/success.txt")));

        assertTrue(outputFile.exists());

        assertEquals(outputMessage, output);
    }

    @Test
    public void testFileMoveDbModify() throws InterruptedException, IOException {
        String message = "Operation,SKU,Item,Price\n" +
                "UPDATE,100,Samsung TV,600\n" +
                "UPDATE,101,LG TV,800";
        String filename = "test.txt";

        //inject the file
        producerTemplate.sendBodyAndHeader(environment.getProperty("fromRoute"), message,
                Exchange.FILE_NAME, filename);

        Thread.sleep(5000);

        //now check if the success file exists

        File outputDir = new File("/tmp/camel/output");

        String outFileName = "success.txt";

        File outputFile = new File("/tmp/camel/output/" + outFileName);

        String outputMessage = "The data has been updated successfully!";
        String output = new String(Files.readAllBytes(Paths.get("/tmp/camel/output/success.txt")));

        assertTrue(outputFile.exists());

        assertEquals(outputMessage, output);
    }


    @Test
    public void testFileMoveDbRemove() throws InterruptedException, IOException {
        String message = "Operation,SKU,Item,Price\n" +
                "DELETE,100,Samsung TV,600\n" +
                "DELETE,101,LG TV,800";
        String filename = "test.txt";

        //inject the file
        producerTemplate.sendBodyAndHeader(environment.getProperty("fromRoute"), message,
                Exchange.FILE_NAME, filename);

        Thread.sleep(5000);

        //now check if the success file exists

        File outputDir = new File("/tmp/camel/output");

        String outFileName = "success.txt";

        File outputFile = new File("/tmp/camel/output/" + outFileName);

        String outputMessage = "The data has been updated successfully!";
        String output = new String(Files.readAllBytes(Paths.get("/tmp/camel/output/success.txt")));

        assertTrue(outputFile.exists());

        assertEquals(outputMessage, output);
    }

    @Test
    public void testFileMoveDbInsert_Exception() throws InterruptedException, IOException {
        String message = "Operation,SKU,Item,Price\n" +
                "INSERT,,Samsung TV,500\n"
                +
                "INSERT,101,LG TV,700";
        String filename = "exception.txt";

        //inject the file
        producerTemplate.sendBodyAndHeader(environment.getProperty("fromRoute"), message,
                Exchange.FILE_NAME, filename);

        Thread.sleep(5000);

        // now check if the output file exists (as the exception is thrown, subsequent insert wont success
        // and the success file will not be created

        File outputDir = new File("/tmp/camel/output");

        File outputFile = new File("/tmp/camel/output/" + filename);

        assertTrue(outputFile.exists());

        File errorDir = new File("/tmp/camel/input/error");

        File errorFile = new File("/tmp/camel/input/error" + filename);

        assertTrue(outputDir.exists());
        assertTrue(outputFile.exists());
    }


}
