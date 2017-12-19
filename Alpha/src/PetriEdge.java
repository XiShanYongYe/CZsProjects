

/**
 * @description Petri网中的边
 * @author xxx
 *
 */
public class PetriEdge {
	/**
	 * 边的源点
	 */
	private PetriVertex source;
	/**
	 * 边的终点
	 */
	private PetriVertex target;
	/**
	 * 边名
	 */
	private String name;

	/**
	 * 
	 */
	private int tokensCount;

	public PetriEdge(PetriVertex source, PetriVertex target) {
		this.source = source;
		this.target = target;
		this.tokensCount = 1;
	}

	public PetriEdge(PetriVertex source, PetriVertex target, int tokensCount) {
		this.source = source;
		this.target = target;
		this.tokensCount = tokensCount;
	}

	public PetriEdge(PetriVertex source, PetriVertex target, String name) {
		this.source = source;
		this.target = target;
		this.name = name;
		this.tokensCount = 1;
	}

	public PetriEdge(PetriVertex source, PetriVertex target, String name, int tokensCount) {
		this.source = source;
		this.target = target;
		this.name = name;
		this.tokensCount = tokensCount;
	}

	public PetriVertex getSource() {
		return source;
	}

	public void setSource(PetriVertex source) {
		this.source = source;
	}

	public PetriVertex getTarget() {
		return target;
	}

	public void settarget(PetriVertex target) {
		this.target = target;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTokensCount() {
		return this.tokensCount;
	}

	public void setTokensCount(int tokensCount) {
		this.tokensCount = tokensCount;
	}

	public String toString() {
		// return tokensCount == 1 ? "" : new Integer(tokensCount).toString();
		return source.toString() + "-->" + target.toString();
	}
}
