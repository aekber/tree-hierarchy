package org.dumbo.controller;

import org.dumbo.repository.TreeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TreeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TreeRepository treeRepository;

    @Test
    public void getDescendants_whenGetAllNodes_thenListOfDescendants() throws Exception {
        // when - action
        ResultActions response = mockMvc.perform(get("/tree/getDescendants/3"));

        // then - verify the output
        response.andExpect(status().isOk());
        response.andDo(print());
        response.andExpect(jsonPath("$.size()", equalTo(11)));
        response.andExpect(jsonPath("$[0].id").value(3));
        response.andExpect(jsonPath("$[0].parent").value(1));
        response.andExpect(jsonPath("$[0].root").value(1));
        response.andExpect(jsonPath("$[0].height").value(0));
        response.andExpect(jsonPath("$[10].id").value(25));
        response.andExpect(jsonPath("$[10].parent").value(22));
        response.andExpect(jsonPath("$[10].root").value(1));
        response.andExpect(jsonPath("$[10].height").value(4));
    }

    @Test
    public void addNode_whenANewNode_thenReturnNewNode() throws Exception{
        // when - action
        ResultActions response = mockMvc.perform(post("/tree/add").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Test\", \"parent\":12}"));

        // then - verify the output
        response.andExpect(status().isOk());
        response.andDo(print());
        response.andExpect(jsonPath("$.id").isNumber());
        response.andExpect(jsonPath("$.name").value("Test"));
        response.andExpect(jsonPath("$.parent").value(12));
    }

    @Test
    public void getNodeById_whenAnIdGiven_thenReturnNode() throws Exception{
        // when - action
        ResultActions response = mockMvc.perform(get("/tree/get/3"));

        // then - verify the output
        response.andExpect(status().isOk());
        response.andDo(print());
        response.andExpect(jsonPath("$.id").value(3));
        response.andExpect(jsonPath("$.name").value("R. Sharma"));
        response.andExpect(jsonPath("$.parent").value(1));
    }

    @Test
    public void changeParent_whenAnIdGiven_thenReturnParentChangedNode() throws Exception{
        // when - action
        ResultActions response = mockMvc.perform(get("/tree/change/19/3")); //existing node:19, existing parent: 8, new parent:3

        // then - verify the output
        response.andExpect(status().isOk());
        response.andDo(print());
        response.andExpect(jsonPath("$.id").value(19));
        response.andExpect(jsonPath("$.name").value("Rishabh Pant"));
        response.andExpect(jsonPath("$.parent").value(3));
    }
}
