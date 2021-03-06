package org.dumbo.model;

import javax.persistence.*;
import java.util.Objects;

@NamedNativeQuery(query =   "WITH RECURSIVE CTE AS (\n" +
                            "    SELECT\n" +
                            "        id,\n" +
                            "        name,\n" +
                            "        parent,\n" +
                            "        1 as root,\n" +
                            "        0 as height\n" +
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
                            "        s.height + 1\n" +
                            "    FROM\n" +
                            "        nodes e\n" +
                            "            INNER JOIN CTE s ON s.id = e.parent\n" +
                            ") SELECT\n" +
                            "    *\n" +
                            "FROM\n" +
                            "    CTE c", resultSetMapping = "mappingNativeQuery", name = "TreeDTO.getDescendantsByNodeId")
@SqlResultSetMapping(
        name = "mappingNativeQuery",
        classes = {
                @ConstructorResult(
                        targetClass = NodeDTO.class,
                        columns = {
                                @ColumnResult( name = "id", type = Long.class),
                                @ColumnResult( name = "parent", type = Integer.class),
                                @ColumnResult( name = "root", type = Long.class),
                                @ColumnResult( name = "height", type = Integer.class)
                        }
                )
        }
)
@Entity
@Table(name="nodes")
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "parent")
    private Integer parent;

    public Node() {
    }

    public Node(String name, Integer parent) {
        this.name = name;
        this.parent = parent;
    }

    public Node(Long id, String name, Integer parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id.equals(node.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Tree{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parent=" + parent +
                '}';
    }
}
