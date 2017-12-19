

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MineDirectPrecedenceTest {
		
	private DirectPrecedences directPrecedence;//暂时存放值，用于添加到list中
	private List<EventFlow> flow;	//存放一条trace,里面包含多个event
	private List<String> start;	//存放ts
//	private Set<DirectPrecedences> diSet;
	private List<DirectPrecedences>diSet;
	private int count=0;
	public MineDirectPrecedenceTest(List<EventFlow> flow){
		this.flow=flow;
		start=new ArrayList<String>();
//		diSet=new HashSet<>();
		diSet = new ArrayList<>();
	}
	public List<DirectPrecedences> doMineDirectPrecedence(){
		long log = System.currentTimeMillis();
		for(int i=0;i<flow.size();i++){
			count++;
		/*	if(count==42){
				break;
			}*/
			getDirectPrecedence(flow.get(i));
		}
//		System.out.println("count:"+count);
		return diSet;
	}
	// procedure DIRECTPRECEDENCE
	private void getDirectPrecedence(EventFlow trace){
		for(int i=0;i<trace.size()-1;i++){
			DirectPrecedences directPrecedences=new DirectPrecedences(trace.get(i).getValue(),trace.get(i+1).getValue());
//			System.out.println(directPrecedences.toString()+"\t"+directPrecedences.hashCode());
			long log = System.nanoTime();
//			if(!diSet.contains(directPrecedences))
				diSet.add(directPrecedences);
//			System.out.println("1.1.1:--------"+(System.nanoTime()-log)/1000);
		}
	}
	
	
	

}
