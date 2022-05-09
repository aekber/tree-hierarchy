package org.dumbo.service;

import org.dumbo.exception.InvalidNodeParamException;
import org.dumbo.exception.NodeNotFoundException;
import org.dumbo.exception.OnlyOneRootNodeAllowedException;
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

        return treeRepository.findById(id).orElseThrow(() -> new NodeNotFoundException("Node with id: " + id + " not found!"));
    }

    @Transactional(readOnly = true)
    public List<NodeDTO> getNodeDescendants(Long id) throws InvalidNodeParamException {
        if(id == null){
            throw new InvalidNodeParamException("Invalid node id: " + id);
        }
        return treeRepository.getDescendantsByNodeId(id);
    }

    @Transactional
    public Node addNode(Node node) throws InvalidNodeParamException, NodeNotFoundException, OnlyOneRootNodeAllowedException {
        if(node == null){
            throw new InvalidNodeParamException("Invalid node parameter! node is null ");
        }

        if(node.getParent() != null) { // if parent is null, then that means it is root. No need to check if node exists or not.
            treeRepository.findById(node.getParent().longValue()).orElseThrow(() -> new NodeNotFoundException("Node with parent id: " + node.getParent() + " not found!"));
        } else { // A root node is being tried to add. Then check if there is already one.
            List<Node> list = treeRepository.findByParentIsNull();
            if(list.size() == 1){
                throw new OnlyOneRootNodeAllowedException("Only one root node is allowed. Please check parent node id!");
            }
        }

        return treeRepository.save(node);
    }

    @Transactional
    public Node changeNodeParent(Long id, Integer newParentId) throws InvalidNodeParamException, NodeNotFoundException {
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
        return treeRepository.save(updatedNode);
    }

}
