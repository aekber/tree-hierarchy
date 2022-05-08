package org.dumbo.repository;

import org.springframework.stereotype.Repository;
import org.dumbo.model.Tree;
import org.dumbo.model.TreeDTO;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class TreeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Tree> getDescendantsByNodeId(int id){
        return entityManager.createNativeQuery("WITH RECURSIVE subordinates AS (\n" +
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
                        "            INNER JOIN subordinates s ON s.id = e.parent\n" +
                        ") SELECT\n" +
                        "    *\n" +
                        "FROM\n" +
                        "    subordinates c", Tree.class)
                .setParameter("parentPath", id)
                .getResultList();
    }
}

