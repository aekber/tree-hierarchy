package org.dumbo.repository;

import org.dumbo.model.Tree;
import org.dumbo.model.TreeDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TreeRepository extends CrudRepository<Tree, Long> {

    @Query(nativeQuery = true, name = "TreeDTO.getDescendantsByNodeId")
    List<TreeDTO> getDescendantsByNodeId(@Param("parentPath") Long id);
}
