package org.nust.wsong.model;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 存储一个循环结构
 * N1为循环中至少执行一次的部分
 * N2位循环中比N1少执行一次的部分
 * @author xxx
 * 
 */
public class Loop {
	private Set<String> N1;
	private Set<String> N2;
	public Loop() {
		N1 = new LinkedHashSet<>();
		N2 = new LinkedHashSet<>();
	}
	public Set<String> getN1() {
		return N1;
	}
	public void setN1(Set<String> n1) {
		N1 = n1;
	}
	public Set<String> getN2() {
		return N2;
	}
	public void setN2(Set<String> n2) {
		N2 = n2;
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Loop))
			return false;
		Loop newLoop = (Loop) obj;
		return getN1().equals(newLoop.getN1())&&getN2().equals(newLoop.getN2());
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash+=getN1().hashCode();
		hash+=getN2().hashCode();
		return hash;
	}
	
	@Override
	public String toString() {
		return "Loop [N1=" + N1 + ", N2=" + N2 + "]";
	}
	
}
