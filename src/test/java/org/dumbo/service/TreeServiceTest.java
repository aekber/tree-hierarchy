package org.dumbo.service;

import org.dumbo.exception.InvalidNodeParamException;
import org.dumbo.exception.NodeNotFoundException;
import org.dumbo.model.Node;
import org.dumbo.model.NodeDTO;
import org.dumbo.repository.TreeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class TreeServiceTest {

    @Mock
    TreeRepository mockTreeRepository;

    @InjectMocks
    TreeService treeService;

    @Test(expected = InvalidNodeParamException.class)
    public void testGetNodeById_invalidParam() throws Exception {
        treeService.getNodeById(null);
    }

    @Test(expected = NodeNotFoundException.class)
    public void testGetNodeById_notFound() throws Exception {
        when(mockTreeRepository.findById(1234L)).thenReturn(null);
        treeService.getNodeById(1234L);
    }

    @Test
    public void testNodeGetById() throws Exception {
        when(mockTreeRepository.findById(3L)).thenReturn(Optional.of(new Node("Test node", null)));
        Node node = treeService.getNodeById(3L);
        assertEquals("Test node", node.getName());
        assertNull(node.getParent());
    }

    @Test(expected = InvalidNodeParamException.class)
    public void testGetNodeDescendants_invalidParam()  throws Exception {
        treeService.getNodeDescendants(null);
    }

    public void testGetNodeDescendants()  throws Exception {
        when(mockTreeRepository.getDescendantsByNodeId(3L)).thenReturn(Arrays.asList(new NodeDTO(3L, 1, 1L, 0),
                                                                                     new NodeDTO(8L, 3, 1L, 1),
                                                                                     new NodeDTO(9L, 3, 1L, 1),
                                                                                     new NodeDTO(10L, 9, 1L, 2),
                                                                                     new NodeDTO(11L, 10, 1L, 3)));
        List<NodeDTO> nodeDescendants = treeService.getNodeDescendants(3L);

        assertEquals(nodeDescendants.size(), 5);
        assertEquals(Optional.ofNullable(nodeDescendants.get(0).getHeight()), 0);
        assertEquals(Optional.ofNullable(nodeDescendants.get(4).getHeight()), 3);
    }

    @Test(expected = InvalidNodeParamException.class)
    public void testAddNode_invalidParam() throws Exception {
        treeService.addNode(null, null);
    }

    @Test(expected = NodeNotFoundException.class)
    public void testAddNode_notFound() throws Exception {
        when(mockTreeRepository.findById(1234L)).thenReturn(null);
        treeService.addNode("ali", 1234);
    }

    @Test
    public void testAddNode() throws Exception {
        Node node = new Node("ali", 3);
        when(mockTreeRepository.save(node)).thenReturn(node);
        Node node1 = treeService.addNode(node.getName(), node.getParent());
        assertEquals(node1.getName(), node.getName());
        assertEquals(node1.getParent(), node.getParent());
    }

    @Test(expected = InvalidNodeParamException.class)
    public void testChangeNodeParent_invalidParam() throws Exception {
        treeService.changeNodeParent(null, null);
    }

    @Test(expected = NodeNotFoundException.class)
    public void testChangeNodeParent_notFound() throws Exception {
        when(mockTreeRepository.findById(1234L)).thenReturn(null);
        when(mockTreeRepository.findById(3L)).thenReturn(null);
        treeService.changeNodeParent(3L, 1234);
    }

    @Test
    public void testChangeNodeParent() throws Exception {
        Node node = new Node("ali", 3);
        Node node1 = new Node(4L,"veli", 1);

        when(mockTreeRepository.findById(3L)).thenReturn(Optional.of(node));
        when(mockTreeRepository.findById(4L)).thenReturn(Optional.of(node1));

        Node node2 = treeService.changeNodeParent(3L, 4);
        assertEquals(node2.getName(), "ali");
        assertEquals(Optional.ofNullable(node2.getParent()), 4L);
    }
}
