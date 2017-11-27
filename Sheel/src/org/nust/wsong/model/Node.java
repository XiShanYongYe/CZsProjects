package org.nust.wsong.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * 
 * @author xxx
 * 
 */
public class Node implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	private UUID id;

	private String label;

	public Node(String label) {
		this.label = label;
		this.id = UUID.randomUUID();
	}

	public UUID getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Object clone() {
		Node o = null;
		try {
			o = (Node) super.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println(e.toString());
		}
		o.setLabel(this.getLabel());
		return o;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Node))
			return false;
		Node n = (Node) obj;
		return this.getId().equals(n.getId());
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return "Node [id=" + id + ", label=" + label + "]";
	}

}
