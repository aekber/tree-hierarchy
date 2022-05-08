package org.dumbo.repository;

import org.dumbo.model.Tree;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TreeRepository extends CrudRepository<Tree, Long> {

    @Query(value =  "WITH RECURSIVE CTE AS (\n" +
                    "    SELECT\n" +
                    "        id,\n" +
                    "        name,\n" +
                    "        parent,\n" +
                    "        1 as root,\n" +
                    "        0 as level\n" +
                    "    FROM\n" +
                    "        nodes\n" +
                    "    WHERE\n" +
                    "            id = :parentPath\n" +
                    "    UNION\n" +
                    "    SELECT\n" +
                    "        e.id,\n" +
                    "        e.name,\n" +
                    "        e.parent,\n" +
                    "        (SELECT n1.id FROM nodes n1 WHERE n1.parent is null),\n" +
                    "        s.level + 1\n" +
                    "    FROM\n" +
                    "        nodes e\n" +
                    "            INNER JOIN CTE s ON s.id = e.parent\n" +
                    ") SELECT\n" +
                    "    *\n" +
                    "FROM\n" +
                    "    CTE c", nativeQuery = true)
    List<Tree> getDescendantsByNodeId(@Param("parentPath") Long id);
}
