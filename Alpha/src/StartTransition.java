

public class StartTransition {
	private String name;
	private int count=0;
	
	public StartTransition(){}
	
	public StartTransition(String name,int count){
		this.name=name;
		this.count=count;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	

	@Override
	public boolean equals(Object obj) {
		StartTransition d1 = null;
		if (obj instanceof StartTransition) {// 如果传进来的对象是D1的子类那么就对属性做比较
			d1 = (StartTransition) obj;
			if (d1.name.equals(this.name))
				return true;
		}
		return false;
	}
	public int hashCode() {
		return name.hashCode();
	}
	
	public String toString(){
		return name+":"+count;
	}

}
