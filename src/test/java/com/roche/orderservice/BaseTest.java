package com.roche.orderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.io.IOException;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
abstract class BaseTest {

    static final String banana = "1111111";
    static final String apple = "2222222";
    static final String kiwi = "3333333";

    @Autowired
    protected MockMvc mvc;

    @Autowired
    private ObjectMapper jackson;

    protected String toJson(Object object) throws IOException {
        return jackson.writeValueAsString(object);
    }

}
