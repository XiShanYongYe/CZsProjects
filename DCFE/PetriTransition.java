package org.processmining.plugins.dcfe;

/**
 * Petri transition
 * 
 * @author zhen
 * 
 */
public class PetriTransition extends PetriVertex {

	private String flag = "transition";

	public PetriTransition(String name) {
		super(name);
	}

	public String getFlag() {

		return flag;

	}

	public String toString() {

		return name;

	}

}
