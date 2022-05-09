package org.dumbo.service;

import org.dumbo.exception.InvalidNodeParamException;
import org.dumbo.exception.NodeNotFoundException;
import org.dumbo.model.Node;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.dumbo.model.NodeDTO;
import org.dumbo.repository.TreeRepository;

import java.util.List;
import java.util.Optional;


@Service
public class TreeService {
    private final TreeRepository treeRepository;

    public TreeService(TreeRepository treeRepository) {
        this.treeRepository = treeRepository;
    }

    @Transactional(readOnly = true)
    public Node getNodeById(Long id) throws InvalidNodeParamException, NodeNotFoundException {
        if(id == null){
            throw new InvalidNodeParamException("Invalid node id: " + id);
        }

        return treeRepository.findById(id)
                .orElseThrow(() -> new NodeNotFoundException("Node with id: " + id + " not found!"));
    }

    @Transactional(readOnly = true)
    public List<NodeDTO> getNodeDescendants(Long id) throws InvalidNodeParamException {
        if(id == null){
            throw new InvalidNodeParamException("Invalid node id: " + id);
        }
        return treeRepository.getDescendantsByNodeId(id);
    }

    @Transactional
    public void addNode(String name, Integer parentId) throws InvalidNodeParamException, NodeNotFoundException {
        if(name == null || name.isEmpty() || parentId == null){ // There can be only one root node, parentId == null
            throw new InvalidNodeParamException("Invalid node parameter! name: " + name + ", parent id: " + parentId);
        }

        Optional<Node> node = treeRepository.findById(parentId.longValue());
        if(!node.isPresent()){
            throw new NodeNotFoundException("Node with parent id: " + parentId + " not found!");
        }

        treeRepository.save(new Node(name, parentId));
    }

    @Transactional
    public void changeNodeParent(Long id, Integer newParentId) throws InvalidNodeParamException, NodeNotFoundException {
        if(id == null || newParentId == null){
            throw new InvalidNodeParamException("Invalid node parameter! id: " + id + ", new parent id: " + newParentId);
        }

        Optional<Node> node = treeRepository.findById(id);
        if(!node.isPresent()){
            throw new NodeNotFoundException("Node with id: " + id + " not found!");
        }

        Optional<Node> parent = treeRepository.findById(newParentId.longValue());
        if(!parent.isPresent()){
            throw new NodeNotFoundException("Node with parent id: " + newParentId + " not found!");
        }

        Node updatedNode = node.get();
        updatedNode.setParent(newParentId);
        treeRepository.save(updatedNode);
    }

}
