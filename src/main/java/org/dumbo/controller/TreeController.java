package org.dumbo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/getDescendants")
    public List<TreeDTO> getDescendants(@RequestParam int id) {
        return treeService.getDescendants(id);
    }
}
