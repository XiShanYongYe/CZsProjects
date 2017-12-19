

public class Independence {
	// 关系的左部
	private String leftEvent;

	// 关系的右部
	private String rightEvent;

	// 统计出现次数，便于计算算法5
	private int count=0;
	
	public Independence(){		
	}
	public Independence(String leftEvent,String rightEvent){		
		this.leftEvent=leftEvent;
		this.rightEvent=rightEvent;
	}

	public String getLeftEvent() {
		return leftEvent;
	}

	public void setLeftEvent(String leftEvent) {
		this.leftEvent = leftEvent;
	}

	public String getRightEvent() {
		return rightEvent;
	}

	public void setRightEvent(String rightEvent) {
		this.rightEvent = rightEvent;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	@Override
	public boolean equals(Object obj) {
		Independence d1 = null;
		if (obj instanceof Independence) {// 如果传进来的对象是D1的子类那么就对属性做比较
			d1 = (Independence) obj;
			if (d1.leftEvent.equals(this.leftEvent) && d1.rightEvent.equals(this.rightEvent) ||
					(d1.leftEvent.equals(this.rightEvent) && d1.rightEvent.equals(this.leftEvent)))
				return true;
		}
		return false;
	}

	public int hashCode() {
		return (7 * getLeftEvent().hashCode() + 3 * getRightEvent().hashCode());
	}

	public String toString() {
		return leftEvent + "#" + rightEvent;
	}

}
