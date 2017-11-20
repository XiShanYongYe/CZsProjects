package org.nust.heroine.basicstruct;

import java.util.ArrayList;

/**
 * 
 * 类名：Data 用途：保存Petri Net中数据信息
 * 
 */
public class Data {

	/**
	 * 数据的ID
	 */
	protected String ID;
	/**
	 * 数据的名称
	 */
	protected String Name;
	/**
	 * 数据的类型
	 */
	protected String type;
	/**
	 * 数据读
	 */
	protected ArrayList<Node> readby = new ArrayList<Node>();
	/**
	 * 数据写
	 */
	protected ArrayList<Node> writtenby = new ArrayList<Node>();
	/**
	 * 判断数据是否是PetriNet格式
	 */
	protected boolean petriNet = false;

	public Data() {

	}

	public Data(String Name) {
		this.Name = Name;
	}

	public Data(String Name, String ID) {
		this.Name = Name;
		this.ID = ID;
	}

	public Data(ArrayList<Node> writtenby, ArrayList<Node> readby) {
		this.writtenby = writtenby;
		this.readby = readby;
	}

	public Data(String Name, String ID, ArrayList<Node> writtenby,
			ArrayList<Node> readby) {
		this.Name = Name;
		this.ID = ID;
		this.writtenby = writtenby;
		this.readby = readby;
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<Node> getWrittenby() {
		return writtenby;
	}

	public void setWrittenby(ArrayList<Node> writtenby) {
		this.writtenby = writtenby;
	}

	public void addWrittenby(Node n) {
		this.writtenby.add(n);
	}

	public ArrayList<Node> getReadby() {
		return readby;
	}

	public void setReadby(ArrayList<Node> readby) {
		this.readby = readby;
	}

	public void addReadby(Node n) {
		this.readby.add(n);
	}

	public boolean isPetriNet() {
		return petriNet;
	}

	public void setPetriNet(boolean petriNet) {
		this.petriNet = petriNet;
	}

}
