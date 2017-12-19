

import java.util.Set;

public class AlphaX {
	private Set<String> a;
	private Set<String> b;
	public AlphaX() {}
	public AlphaX(Set<String> a,Set<String> b){
		this.a=a;
		this.b=b;
	}
	public void setA(Set<String> a) {
		this.a = a;
	}
	public Set<String> getA() {
		return a;
	}
	public void setB(Set<String> b) {
		this.b = b;
	}
	public Set<String> getB() {
		return b;
	}
	
	
	public boolean equals(Object obj) {
		if (obj instanceof AlphaX) {
			AlphaX alphaX = (AlphaX) obj;
			if (alphaX.a.equals(this.a) && alphaX.b.equals(this.b))
				return true;
		}
		return false;
	}
	public int hashCode() {
		return a.hashCode()+b.hashCode();
	}
	public String toString(){
		return a.toString()+b.toString();
	}

}
