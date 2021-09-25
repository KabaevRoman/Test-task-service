package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class DataHandlerTests {
    @Test
    void testOfSet() throws InterruptedException {
        DataHandlerService dataHandlerService = new DataHandlerService();
        dataHandlerService.setData("1", "content1", 3);
        dataHandlerService.setData("2", "content2", 15);
        dataHandlerService.setData("3", "content3");
        Assertions.assertEquals(dataHandlerService.dataMap.get("1").getContent(), "content1");//check if set works correctly
        Thread.sleep(5000);
        Assertions.assertNull(dataHandlerService.dataMap.get("1"));//check that ttl works correctly
        Assertions.assertNotNull(dataHandlerService.dataMap.get("2"));
    }

    @Test
    void testOfGet() throws InterruptedException {
        DataHandlerService dataHandlerService = new DataHandlerService();
        dataHandlerService.setData("1", "content1", 3);
        dataHandlerService.setData("2", "content2", 15);
        dataHandlerService.setData("3", "content3");
        Assertions.assertEquals(dataHandlerService.getData("1"), dataHandlerService.dataMap.get("1"));//check if getData methods gets correct object comparing to picking object directly from map
        Thread.sleep(2000);
        dataHandlerService.getData("1");
        Thread.sleep(2000);
        //check if getData resets ttl of object
        Assertions.assertNotNull(dataHandlerService.dataMap.get("1"));
        Thread.sleep(3000);
        Assertions.assertNull(dataHandlerService.dataMap.get("1"));
        //check if getData resets ttl of object
    }

    @Test
    void testOfRemove() {
        DataHandlerService dataHandlerService = new DataHandlerService();
        dataHandlerService.setData("2", "content2", 15);
        Data temp = dataHandlerService.dataMap.get("2");
        Assertions.assertEquals(temp, dataHandlerService.remove("2"));//check if remove returns deleted object
        Assertions.assertNull(dataHandlerService.dataMap.get("2"));//check if object really was removed
    }

    @Test
    void testOfDumpLoad() throws IOException, InterruptedException, ClassNotFoundException {
        DataHandlerService dataHandlerService = new DataHandlerService();
        dataHandlerService.setData("1", "content1", 3);
        dataHandlerService.setData("2", "content2", 2);
        dataHandlerService.setData("3", "content3");
        dataHandlerService.dump();//creating file dump with data about dataMap named dump.txt
        Data[] data = new Data[3];//remembering objects, that will be deleted
        data[0] = dataHandlerService.dataMap.get("1");
        data[1] = dataHandlerService.dataMap.get("2");
        data[2] = dataHandlerService.dataMap.get("3");
        Thread.sleep(5000);//waiting till all the objects will be deleted
        Assertions.assertNull(dataHandlerService.dataMap.get("1"));
        Assertions.assertNull(dataHandlerService.dataMap.get("2"));
        Assertions.assertNull(dataHandlerService.dataMap.get("3"));//check if all objects were deleted
        dataHandlerService.load();//loading data dump
        Assertions.assertEquals(data[0].getContent(), dataHandlerService.dataMap.get("1").getContent());//not possible to compare Data class objects because timer is different
        Assertions.assertEquals(data[1].getContent(), dataHandlerService.dataMap.get("2").getContent());//if these equals everything is fine
        Assertions.assertEquals(data[2].getContent(), dataHandlerService.dataMap.get("3").getContent());
        Thread.sleep(3000);//checking if ttl works fine after load
        Assertions.assertNull(dataHandlerService.dataMap.get("1"));
        Assertions.assertNull(dataHandlerService.dataMap.get("2"));
        Assertions.assertNotNull(dataHandlerService.dataMap.get("3"));
    }
}
