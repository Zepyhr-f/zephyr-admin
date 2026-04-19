package com.zephyr.generator;

import com.zephyr.generator.pojo.dto.GenInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * 代码生成测试
 *
 * @author Zephyr
 * @since 2025-09-20
 */
@SpringBootTest
@AutoConfigureMockMvc
public class GeneratorTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void genTest() throws Exception {
        String[] tables = {"AML_TRANSACTION", "AML_PARTY", "AML_CORPORATION", "AML_LOAN_AGREEMENT", "AML_PARTY_AFFLT"};

        for (String table : tables) {
            GenInfo genInfo = new GenInfo();
            genInfo.setTableName(table);
            genInfo.setModuleName("rule");

            MvcResult result = mockMvc.perform(post("/generator/run")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(genInfo)))
                    .andExpect(status().isOk())
                    .andReturn();

            System.out.println("返回结果: " + result.getResponse().getContentAsString(StandardCharsets.UTF_8));
        }

    }
}