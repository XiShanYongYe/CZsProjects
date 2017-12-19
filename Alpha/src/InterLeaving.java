

public class InterLeaving {
	// ��ϵ����
	private String leftEvent;

	// ��ϵ���Ҳ�
	private String rightEvent;

	// ͳ�Ƴ��ִ��������ڼ����㷨5
	private int count=0;
	
	public InterLeaving(){		
	}
	public InterLeaving(String leftEvent,String rightEvent){		
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
		InterLeaving d1 = null;
		if (obj instanceof InterLeaving) {// ����������Ķ�����D1��������ô�Ͷ��������Ƚ�
			d1 = (InterLeaving) obj;
			if (d1.leftEvent.equals(this.leftEvent) && d1.rightEvent.equals(this.rightEvent))
				return true;
		}
		return false;
	}

	public int hashCode() {
		return (7 * getLeftEvent().hashCode() + 3 * getRightEvent().hashCode());
	}

	public String toString() {
		return leftEvent + "||" + rightEvent ;
	}

}
