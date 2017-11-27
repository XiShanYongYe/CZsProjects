package org.nust.wsong.model;

import java.io.Serializable;
/**
 * 
 * @author xxx
 * @since 2016-10-1
 */
public class Node  implements Serializable,Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int ID;
	
	private String PID;

	private int inDegree = 0;

	private int outDegree = 0;
	
	public Node() {
	}
	
	public Node(String name){
		this.name = name;
	}
	
	public int getInDegree() {
		return inDegree;
	}
	public void setInDegree(int inDegree) {
		this.inDegree = inDegree;
	}
	public int getOutDegree() {
		return outDegree;
	}
	public void setOutDegree(int outDegree) {
		this.outDegree = outDegree;
	}
	public void inDegreeSubOne(){
		inDegree--;
	}
	
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}

	public Object clone()    
    {    
        Node o=null;    
       try    
        {    
            o=(Node)super.clone();    
        }    
       catch(CloneNotSupportedException e)    
        {    
            System.out.println(e.toString());    
        } 
       o.setName(this.getName());
       return o;    
    }  


	public String getPID() {
		return PID;
	}

	public void setPID(String pID) {
		PID = pID;
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
		return this.getName().equals(n.getName());
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public String toString() {
		return name;
	}
}
