package org.dumbo.model;

import java.util.Objects;

public class NodeDTO {

	private Long id;
	private Integer parent;
	private Long root;
	private Integer height;

	public NodeDTO(Long id, Integer parent, Long root, Integer height) {
		this.id = id;
		this.parent = parent;
		this.root = root;
		this.height = height;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public Long getRoot() {
		return root;
	}

	public void setRoot(Long root) {
		this.root = root;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		NodeDTO nodeDTO = (NodeDTO) o;
		return Objects.equals(id, nodeDTO.id) && Objects.equals(parent, nodeDTO.parent) && Objects.equals(root, nodeDTO.root) && Objects.equals(height, nodeDTO.height);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, parent, root, height);
	}

	@Override
	public String toString() {
		return "TreeDTO{" +
				"id=" + id +
				", parent=" + parent +
				", root=" + root +
				", height=" + height +
				'}';
	}
}
