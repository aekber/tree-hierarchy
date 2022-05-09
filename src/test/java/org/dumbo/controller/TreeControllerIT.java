package org.dumbo.controller;

import org.dumbo.model.Node;
import org.dumbo.repository.TreeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TreeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TreeRepository treeRepository;

    @Before
    public void clear(){
        treeRepository.deleteAll();
    }

    @Test
    public void getDescendants_whenGetAllNodes_thenListOfDescendants() throws Exception {
        // given - setup or precondition
        List<Node> nodes = Arrays.asList(new Node(1L, "Test1", null),
                                         new Node(2L, "Test2", 1),
                                         new Node(3L, "Test3", 1),
                                         new Node(4L, "Test4", 2),
                                         new Node(5L, "Test5", 2),
                                         new Node(6L, "Test6", 5),
                                         new Node(7L, "Test7", 6));
        treeRepository.saveAll(nodes);

        // when - action
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/tree/getDescendants/2"));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.size()", hasSize(5))); // 2-4-5-6-7
        response.andExpect(jsonPath("$[0].responseObject.id").value(2));
        response.andExpect(jsonPath("$[0].responseObject.parent").value(1));
        response.andExpect(jsonPath("$[0].responseObject.root").value(1));
        response.andExpect(jsonPath("$[0].responseObject.height").value(1));
        response.andExpect(jsonPath("$[4].responseObject.id").value(7));
        response.andExpect(jsonPath("$[4].responseObject.parent").value(6));
        response.andExpect(jsonPath("$[4].responseObject.root").value(1));
        response.andExpect(jsonPath("$[4].responseObject.height").value(4));
    }

    @Test
    public void addNode_whenANewNode_thenReturnNewNode() throws Exception{
        // given - setup or precondition


        // when - action
        ResultActions response = mockMvc.perform(put("/tree/add/0").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"A\"}"));

        // then - verify the output
        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.responseObject.id").isNumber());
        response.andExpect(jsonPath("$.responseObject.name").value("A"));
        response.andExpect(jsonPath("$.responseObject.path").isEmpty());
        response.andExpect(jsonPath("$.responseObject.parentId").isEmpty());
    }

}
