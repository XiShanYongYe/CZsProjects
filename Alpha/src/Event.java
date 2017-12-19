

import java.sql.Timestamp;

public class Event {
	private String value;
	private String lifecycle;
	private Timestamp timestamp;
	private long SEFT=Long.MAX_VALUE;
	private long SLFT=0;
	private int countTrace=0;//统计这个event出现在多少条trace中
	public Event(String value,String lifecycle,Timestamp timestamp) {
		this.value = value;
		this.lifecycle=lifecycle;
		this.timestamp=timestamp;
	}
	public Event(){
	
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
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
	@Override
	public boolean equals(Object obj) {
		Event d1 = null;
		if (obj instanceof Event) {// 如果传进来的对象是D1的子类那么就对属性做比较
			d1 = (Event) obj;
			if (d1.value.equals(this.value))
				return true;
		}
		return false;
	}
	public int hashCode() {
		return value.hashCode();
	}

	public String toString() {
//		return value +" "+Math.round((double)SEFT/600000)+" "+Math.round((double)SLFT/600000)+" "+countTrace;
		return value +" "+(double)SEFT/600000+" "+(double)SLFT/600000+" "+countTrace;
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
	public void setCountTrace(int countTrace) {
		this.countTrace = countTrace;
	}
	public int getCountTrace() {
		return countTrace;
	}

}
