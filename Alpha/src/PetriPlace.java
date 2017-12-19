

/**
 * @description 库所
 * @author xxx
 *
 */

public class PetriPlace extends PetriVertex {

	/**
	 * 标记此定点为PetriPlace
	 * */
	private String flag = "place";
	
    /**
     * 托肯
     */
    private int tokens;

    /**
     * 初始托肯
     */

    private transient int startupTokensCount;
    
    /**
     * 
     * 边界
     */
    private int boundaries;
    
    /**
     * 初始边界
     */
    private transient int startupBoundaries;
    
    public PetriPlace(String name) {
        super(name);
        tokens = startupTokensCount = 0;
        boundaries = startupBoundaries = Integer.MAX_VALUE;
    }

    /**
     * Constructor
     * @param id                    ID of Place
     * @param name                  Name of Place
     * @param startupMarkersCount   Startup markers count
     */
    public PetriPlace(String name, int startupTokensCount) {
        super(name);
        this.tokens = this.startupTokensCount = startupTokensCount;
    }

    /**
     * Get current count of markers
     * @return
     */
    public int getTokensCount() {
        return this.tokens;
    }

    public void setTokens(int tokens) {
		this.tokens = tokens;
	}
    /**
     * Add number of markers
     * @param i     Markers count
     * @return
     */
    public int changeTokensCount(int i) {
        return tokens += i;
    }

    /**
     * Set startup markers count as current
     */
    public void resetTokensCount() {
        tokens = startupTokensCount;
    }

    /**
     * Set new startup markers count
     * @param i     markers count
     */
    public void setStartupTokensCount(int i) {
        this.startupTokensCount = i;
        resetTokensCount();
    }
    
    public int getBoundaries() {
		return boundaries;
	}
    
    public void setBoundaries(int boundaries) {
		this.boundaries = boundaries;
	}
    

    public void resetBoundaries() {
    	boundaries = startupBoundaries;
    }
    
    public String getFlag (){
    	return flag;
    }/*
    @Override
	public boolean equals(Object obj) {
		PetriPlace d1 = null;
		if (obj instanceof PetriPlace) {// ����������Ķ�����D1��������ô�Ͷ��������Ƚ�
			d1 = (PetriPlace) obj;
			if (d1.name.equals(this.name))
				return true;
		}
		return false;
	}

	public int hashCode() {
		return (7 * getName().hashCode());
	}*/

    @Override
    public String toString() { 
//        return "[Place" + id + "   " +"name"+name+" "+"tokens:"+ tokens+"]";
    	return name;
    }
}
