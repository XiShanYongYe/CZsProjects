package org.nust.heroine.basicstruct;

import java.util.ArrayList;

public class Circuit {
	protected ArrayList<Node> circuitNodes;
	
	public ArrayList<Node> getCircuitNodes() {
		return circuitNodes;
	}

	public void setCircuitNodes(ArrayList<Node> circuitNodes) {
		this.circuitNodes = circuitNodes;
	}

	public Circuit(){
		circuitNodes = new ArrayList<Node>();
	}
	//主要目的 去掉并发的环
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj == this)
			return true;
		if(obj == null)
			return false;
		if(!(obj instanceof Circuit))
			return false;
		 Circuit  temp = (Circuit)obj;
		 return (circuitNodes.get(0).equals(temp.getCircuitNodes().get(0)))&&(circuitNodes.get(circuitNodes.size()-1).equals(temp.getCircuitNodes().get(temp.getCircuitNodes().size()-1)));
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getCircuitNodes().toString();
	}
	
}
