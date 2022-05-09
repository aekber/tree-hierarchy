package org.dumbo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.dumbo.model.Tree;
import org.dumbo.model.TreeDTO;
import org.dumbo.repository.TreeRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TreeService {
    private final TreeRepository treeRepository;

    public TreeService(TreeRepository treeRepository) {
        this.treeRepository = treeRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Tree> getNodeById(Long id) {
        Optional<Tree> node = treeRepository.findById(id);
        return node;
    }

    @Transactional(readOnly = true)
    public List<TreeDTO> getNodeDescendants(Long id) {
        return treeRepository.getDescendantsByNodeId(id);
    }

    @Transactional
    public void addNode(String name, Integer parentId) {
        treeRepository.save(new Tree(name, parentId));
    }

    @Transactional
    public void changeNodeParent(Long id, Integer newParentId) {
        Optional<Tree> node = treeRepository.findById(id);
        node.ifPresent(tree -> {
            tree.setParent(newParentId);
            treeRepository.save(tree);
        });

    }

}
