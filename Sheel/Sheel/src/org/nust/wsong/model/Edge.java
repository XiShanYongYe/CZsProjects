package org.nust.wsong.model;

import java.io.Serializable;
/**
 * @description
 * @author xxx
 * @since 2016-10-1
 */

public class Edge implements Serializable,Cloneable{

	private static final long serialVersionUID = 3486802786745182175L;

	private String ID;

	private Node Source;

	private Node Target;

	private String label;

	public Edge() {

	}

	public Edge(Node Source, Node Target) {
		this.Source = Source;
		this.Target = Target;
	}

	public Edge(Node Source, Node Target, String label) {
		this.Source = Source;
		this.Target = Target;
		this.label = label;
	}

	public Node getSource() {
		return Source;
	}

	public void setSource(Node Source) {
		this.Source = Source;
	}

	public Node getTarget() {
		return Target;
	}

	public void setTarget(Node Target) {
		this.Target = Target;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Edge))
			return false;
		Edge a = (Edge) obj;
		return this.getSource().equals(a.getSource())
				&& this.getTarget().equals(a.getTarget());
	}

	
	@Override
	public int hashCode() {
		return Source.getName().hashCode()+37*Target.getName().hashCode();
	}
	
	public Edge clone(){
		Edge o = null;
		try{
			o=(Edge)super.clone();
			o.Source=(Node)Source.clone();
			o.Target=(Node)Target.clone();
			
		}catch(CloneNotSupportedException e){
			e.printStackTrace();
			
		}
		return o;
	}

	@Override
	public String toString() {
		return Source.toString()+"-"+Target.toString();
	}
	
}
