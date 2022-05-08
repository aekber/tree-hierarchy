package org.dumbo.controller;

import org.dumbo.model.Tree;
import org.springframework.web.bind.annotation.*;
import org.dumbo.model.TreeDTO;
import org.dumbo.service.TreeService;

import java.util.List;


@RestController
@RequestMapping("/tree/")
public class TreeController {

    private final TreeService treeService;

    public TreeController(TreeService treeService) {
        this.treeService = treeService;
    }

    @GetMapping("/get/{nodeId}")
    public Tree getNodeById(@PathVariable Long nodeId) {
        return treeService.getNodeById(nodeId).get();
    }

    @GetMapping("/getDescendants/{nodeId}")
    public List<TreeDTO> getDescendants(@PathVariable Long nodeId) {
        return treeService.getNodeDescendants(nodeId);
    }

    @GetMapping("/add/{name}/{parentId}")
    public void add(@PathVariable String name, @PathVariable Integer parentId) {
        treeService.addNode(name, parentId);
    }

    @GetMapping("/change/{id}/{newParentId}")
    public void changeParent(@PathVariable Long id, @PathVariable Integer newParentId) {
        treeService.changeNodeParent(id, newParentId);
    }
}
