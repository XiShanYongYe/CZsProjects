

import java.util.ArrayList;


public class EventFlow extends ArrayList<Event> {

	private String traceId;
	private int count = 1;
	//////////////////////////////////
	private Event event;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	/**
	 * 向eventflow 添加 event
	 * 
	 * @param event
	 */
	public void addEvent(Event event) {
		this.add(event);
	}

	/**
	 * 判断当前eventflow是否包含event
	 * 
	 * @param event
	 * @return
	 */

	// public boolean isContainEvent(XEvent event) {
	// for (XEvent e : this) {
	// if (e.getAttributes().get("concept:name").toString()
	// .equals(event.getAttributes().get("concept:name").toString()))
	// return true;
	// }
	// return false;
	// }

/*	public boolean isContainEvent(String event) {
		for (String e : this) {
			if (e.equals(event))
				return true;
		}
		return false;
	}*/

	// public boolean equals(Object obj) {
	// if (obj instanceof EventFlow) {
	// EventFlow flow = (EventFlow) obj;
	// if (this.getTraceId() == flow.getTraceId())
	// return true;
	// }
	// return false;
	// }

	// public String toString() {
	// String trace = "";
	// String[] strArray = this.toArray(new String[this.size()]);
	// for (int i = 0; i < strArray.length - 1; i++) {
	// trace += strArray[i] + "--->";
	// }
	// trace += strArray[strArray.length - 1];
	// return trace;
	// }

	/*public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String str : this) {
			sb.append(str);
		}
		return sb.toString();
	}*/
	
	public String toString(){
		return traceId+count+event.toString();
	}

	/*public boolean equals(Object obj) {
		if (obj instanceof EventFlow) {
			EventFlow eventFlow = (EventFlow) obj;
			if (eventFlow.toString().equals(this.toString()))
				return true;
		}
		return false;
	}*/
	public boolean equals(Object obj) {
		if (obj instanceof EventFlow) {
			EventFlow eventFlow = (EventFlow) obj;
			if (eventFlow.toString().equals(this.toString()))
				return true;
		}
		return false;
	}


	public int hashCode() {
		return this.toString().hashCode();
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Event getEvent() {
		return event;
	}
}
