package org.dumbo.controller;

import org.dumbo.exception.InvalidNodeParamException;
import org.dumbo.exception.NodeNotFoundException;
import org.dumbo.model.Node;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.dumbo.model.NodeDTO;
import org.dumbo.service.TreeService;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/tree/")
public class TreeController {

    private final TreeService treeService;

    public TreeController(TreeService treeService) {
        this.treeService = treeService;
    }

    @GetMapping("/get/{nodeId}")
    public ResponseEntity<Node> getNodeById(@PathVariable Long nodeId) {
        try {
            return ResponseEntity.ok(treeService.getNodeById(nodeId));
        } catch (InvalidNodeParamException | NodeNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/getDescendants/{nodeId}")
    public ResponseEntity<List<NodeDTO>> getDescendants(@PathVariable Long nodeId) {
        try {
            return ResponseEntity.ok(treeService.getNodeDescendants(nodeId));
        } catch (InvalidNodeParamException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/add/{name}/{parentId}")
    public ResponseEntity<Void> add(@PathVariable String name, @PathVariable Integer parentId) {
        try {
            treeService.addNode(name, parentId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InvalidNodeParamException | NodeNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/change/{id}/{newParentId}")
    public ResponseEntity<Void> changeParent(@PathVariable Long id, @PathVariable Integer newParentId) {
        try {
            treeService.changeNodeParent(id, newParentId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InvalidNodeParamException | NodeNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
