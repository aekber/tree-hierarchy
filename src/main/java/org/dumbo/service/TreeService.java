package org.dumbo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.dumbo.model.Tree;
import org.dumbo.model.TreeDTO;
import org.dumbo.repository.TreeRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Service
public class TreeService {
    private final TreeRepository treeRepository;

    public TreeService(TreeRepository treeRepository) {
        this.treeRepository = treeRepository;
    }


    @Transactional(readOnly = true)
    public List<TreeDTO> getDescendants(int id) {
        List<Tree> nodes = treeRepository.getDescendantsByNodeId(id);

        List<TreeDTO> nodeDTOList = new ArrayList<>();
        nodes.iterator().forEachRemaining(currentNode ->  nodeDTOList.add(new TreeDTO(currentNode.getId(), currentNode.getParent(), currentNode.getRoot(), currentNode.getLevel())));
        return nodeDTOList;
    }

}
