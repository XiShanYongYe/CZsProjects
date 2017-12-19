

import java.sql.Timestamp;

/**
 * @description 变迁
 * @author xxx
 *
 */

public class PetriTransition extends PetriVertex {
	
	/*
	 * 	用以标识此顶点为迁移
	 * */
	private String flag = "transition";
	private String lifecycle;
	private Timestamp timestamp;
	private long SEFT;
	private long SLFT;
	
    public PetriTransition(String name) {
        super(name);
    }

    public String getFlag(){
    	return flag;
    }
    @Override
    public String toString() {
    	return name;
    }

	public void setLifecycle(String lifecycle) {
		this.lifecycle = lifecycle;
	}

	public String getLifecycle() {
		return lifecycle;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setSEFT(long sEFT) {
		SEFT = sEFT;
	}

	public long getSEFT() {
		return SEFT;
	}

	public void setSLFT(long sLFT) {
		SLFT = sLFT;
	}

	public long getSLFT() {
		return SLFT;
	}
}
