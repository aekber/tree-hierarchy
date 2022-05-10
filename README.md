# Hierarchical Data Representation

This project aims to show how hierarchical data is stored in db and how crud operations are done on this data through the rest api.

For this implementation tree based relational data structure has been chosen. In this regard, each node of the tree
will have following information;

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

```
sample
```



Developed with Java 8 under Ubuntu 20.04 LTS.

# Build

Run this commands for building project and running the tests:
```
docker-compose up --build
```
