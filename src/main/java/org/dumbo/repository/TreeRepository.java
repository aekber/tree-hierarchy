package org.dumbo.repository;

import org.dumbo.model.Node;
import org.dumbo.model.NodeDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TreeRepository extends CrudRepository<Node, Long> {

    @Query(nativeQuery = true, name = "TreeDTO.getDescendantsByNodeId")
    List<NodeDTO> getDescendantsByNodeId(@Param("parentPath") Long id);

    Optional<Node> findByParentIsNull();
}
