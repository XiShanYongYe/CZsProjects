package org.nust.heroine.basicstruct;

import java.util.ArrayList;

/**
 * 
 * ������Data ��;������Petri Net��������Ϣ
 * 
 */
public class Data {

	/**
	 * ���ݵ�ID
	 */
	protected String ID;
	/**
	 * ���ݵ�����
	 */
	protected String Name;
	/**
	 * ���ݵ�����
	 */
	protected String type;
	/**
	 * ���ݶ�
	 */
	protected ArrayList<Node> readby = new ArrayList<Node>();
	/**
	 * ����д
	 */
	protected ArrayList<Node> writtenby = new ArrayList<Node>();
	/**
	 * �ж������Ƿ���PetriNet��ʽ
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
