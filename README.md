# Hierarchical Data Representation

This project aims to show how hierarchical data is stored in db and how crud operations are done on this data through the rest api.

For this implementation tree based relational data structure has been chosen. In this regard, each node of the tree
will have the following information;

- Node ID
- Parent Node ID
- Root Node ID
- Height of the node

The rest api which is provided here is supposed to do following operations;

- Get node information by its id
- Get descendants of a given node
- Change parent info of a given node
- Add a new node

# Design decisions
## The Approach

There are many options for storing hierarchical data in a relational database. Each of them has different pros and cons. After some researches
and considerations, I have finalized my decision to use one of these methods. Let's take a look at their pros and cons.

### Adjacency List
- Columns in db: ID, ParentID
- Easy to implement.
- Cheap node moves, inserts, and deletes.
- Expensive to find the level, ancestry & descendants, path

### Nested Sets
- Columnsin db: Left, Right
- Cheap ancestry, descendants
- Very expensive O(n/2) moves, inserts, deletes due to volatile encoding

Due to Common Table Expression support of PostgreSQL 14 and relatively cheaper CRUD operations of Adjacency List method, I decided to move
forward with this approach. I assume that there will not be millions of line data in database. 

So that, this approach is fast enough for small-medium sized applications. On the other hand, it was easier to implement the solution.

The table layout looks like this:

```
CREATE TABLE IF NOT EXISTS nodes(id SERIAL PRIMARY KEY, name text not null, parent INTEGER);
```

CTE(Common Table Expression) allows to run recursive queries in the hierarchy. This helps a lot to utilize methods like
finding heights and relations between nodes.

```
WITH RECURSIVE CTE AS (SELECT
         id,
         name,
         parent,
         1 as root,
         0 as level     
     FROM nodes     
     WHERE id = ?     
     UNION
     SELECT
         e.id,
         e.name,
         e.parent,
         (SELECT n1.id FROM nodes n1 WHERE n1.parent is null),
         s.level + 1     
     FROM
         nodes e             
     INNER JOIN subordinates s ON s.id = e.parent ) 
     SELECT * FROM CTE c
```

The query above returns descendant nodes and height of the given node id. 
Most of the business logic has been implemented here.

# Build

Run this command for building project and running the tests:
```
docker-compose up --build
```

# Test Data

Under src/main/resources directory, there are some dummy data(data.sql) to be able to use in the beginning and for the tests. 
Integration and unit tests are using this data set.

# Rest Endpoints

Tools like curl and postman can be used to test the api. These are the endpoint for required functionality.

- Get specific node information

```
http://localhost:8085/tree/get/3/
```

- Add a new node

```
http://localhost:8085/tree/add
```

- Get descendants of given node

```
http://localhost:8085/tree/getDescendants/3/
```

- Change parent node of a given node

```
http://localhost:8085/tree/change/3/4
```

# The Tech Stack

The project is developed under Ubuntu 20.04 with Java 11

- Spring Boot with Spring WebMVC, Rest API, Embedded Tomcat
- Spring Boot Data JPA
- PostgreSQL 14
- Maven
- Docker container with docker-compose


# Troubleshooting

In case of issue( like this ERROR: duplicate key value violates unique constraint "nodes_pkey"
Detail: Key (id)=(1) already exists.) during adding a new node to the tree, the sequence
info needs to be updated with this command:

```
SELECT setval(pg_get_serial_sequence('nodes', 'id'), coalesce(max(id)+1, 1), false) FROM nodes;
```
