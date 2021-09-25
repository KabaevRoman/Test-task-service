package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class ControllerTest {

    protected MockMvc mvc;
    private final String host = "http://localhost:8080/";
    @Autowired
    WebApplicationContext webApplicationContext;

    @Test
    public void setTest() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mvc.perform(MockMvcRequestBuilders.get(host + "set/1/1234/5"));
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(host + "remove/1")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("{\"content\":\"1234\",\"key\":\"1\",\"ttl\":5}", content);
        Thread.sleep(5000);
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(host + "remove/1")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("{\"message\":\"data with key: 1 not found\",\"status_code\":404}", content);
    }

    @Test
    public void getTest() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mvc.perform(MockMvcRequestBuilders.get(host + "set/1/1234/2"));
        mvc.perform(MockMvcRequestBuilders.get(host + "set/2/12345/3"));
        mvc.perform(MockMvcRequestBuilders.get(host + "set/3/12346/10"));
        Thread.sleep(1000);
        mvc.perform(MockMvcRequestBuilders.get(host + "get/1"));//check if get request resets ttl
        Thread.sleep(1000);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(host + "get/1")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("{\"content\":\"1234\",\"key\":\"1\",\"ttl\":2}", content);
        Thread.sleep(5000);
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(host + "get/1")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("{\"message\":\"data with key: 1 not found\",\"status_code\":404}", content);
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(host + "get/3")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("{\"content\":\"12346\",\"key\":\"3\",\"ttl\":10}", content);
    }

    @Test
    public void removeTest() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mvc.perform(MockMvcRequestBuilders.get(host + "set/1/1234/5"));
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(host + "remove/1")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("{\"content\":\"1234\",\"key\":\"1\",\"ttl\":5}", content);//check if remove returns content
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(host + "get/1")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("{\"message\":\"data with key: 1 not found\",\"status_code\":404}", content);//check if element was removed
    }

    @Test
    public void dumpLoadTest() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mvc.perform(MockMvcRequestBuilders.get(host + "set/1/1234/2"));
        mvc.perform(MockMvcRequestBuilders.get(host + "set/2/12345/3"));
        mvc.perform(MockMvcRequestBuilders.get(host + "set/3/12346/5"));
        mvc.perform(MockMvcRequestBuilders.get(host + "dump"));
        Thread.sleep(5000);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(host + "get/1")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        Assertions.assertEquals("{\"message\":\"data with key: 1 not found\",\"status_code\":404}",
                mvcResult.getResponse().getContentAsString());
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(host + "get/2")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        Assertions.assertEquals("{\"message\":\"data with key: 2 not found\",\"status_code\":404}",
                mvcResult.getResponse().getContentAsString());
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(host + "get/3")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        Assertions.assertEquals("{\"message\":\"data with key: 3 not found\",\"status_code\":404}",
                mvcResult.getResponse().getContentAsString());
        mvc.perform(MockMvcRequestBuilders.get(host + "load"));
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(host + "get/3")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        Assertions.assertEquals("{\"content\":\"12346\",\"key\":\"3\",\"ttl\":5}",
                mvcResult.getResponse().getContentAsString());
        Thread.sleep(5000);//including check for ttl
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(host + "get/1")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        Assertions.assertEquals("{\"message\":\"data with key: 1 not found\",\"status_code\":404}",
                mvcResult.getResponse().getContentAsString());
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(host + "get/2")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        Assertions.assertEquals("{\"message\":\"data with key: 2 not found\",\"status_code\":404}",
                mvcResult.getResponse().getContentAsString());
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(host + "get/3")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        Assertions.assertEquals("{\"message\":\"data with key: 3 not found\",\"status_code\":404}",
                mvcResult.getResponse().getContentAsString());
    }
}
