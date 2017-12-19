import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MineThreeRelations {
	private Set<Causal> cSet;
	private Set<Independence> indeSet;
	private Set<InterLeaving> interSet;
	private List<DirectPrecedences> directPrecedences;
	private List<String> eventList;
	public MineThreeRelations(List<DirectPrecedences> directPrecedences,List<String> eventList){
		this.directPrecedences=directPrecedences;
		this.eventList=eventList;
		cSet=new HashSet<>();
		indeSet=new HashSet<>();
		interSet=new HashSet<>();
		doMine();
	}
	public void doMine(){
		for(int i=0;i<eventList.size();i++){
			for(int j=0;j<eventList.size();j++){
				DirectPrecedences di1=new DirectPrecedences(eventList.get(i),eventList.get(j));
				DirectPrecedences di2=new DirectPrecedences(eventList.get(j),eventList.get(i));
				if(directPrecedences.contains(di1) && !directPrecedences.contains(di2)){
					Causal causal=new Causal(eventList.get(i),eventList.get(j));
					cSet.add(causal);
				}
				if(directPrecedences.contains(di1) && directPrecedences.contains(di2)){
					InterLeaving interLeaving1=new InterLeaving(eventList.get(i),eventList.get(j));
					InterLeaving interLeaving2=new InterLeaving(eventList.get(j),eventList.get(i));
					interSet.add(interLeaving1);
					interSet.add(interLeaving2);
				}
			}
		}
		for(int i=0;i<eventList.size();i++){
			for(int j=0;j<eventList.size();j++){
				Causal causal1=new Causal(eventList.get(i),eventList.get(j));
				Causal causal2=new Causal(eventList.get(j),eventList.get(i));
				InterLeaving interLeaving3=new InterLeaving(eventList.get(i),eventList.get(j));
				InterLeaving interLeaving4=new InterLeaving(eventList.get(j),eventList.get(i));
				if(!cSet.contains(causal1) && !cSet.contains(causal2) && !interSet.contains(interLeaving3) && !interSet.contains(interLeaving4)){
					Independence independence1=new Independence(eventList.get(i),eventList.get(j));
					Independence independence2=new Independence(eventList.get(j),eventList.get(i));
					indeSet.add(independence1);
					indeSet.add(independence2);
				}
			}
		}
		
	}

	public List<Causal> getcList() {
		List<Causal> cList=new ArrayList<>();
		cList.addAll(cSet);
		return cList;
	}
	public List<Independence> getIndeList() {
		List<Independence> indeList=new ArrayList<>();
		indeList.addAll(indeSet);
		return indeList;
	}
	public List<InterLeaving> getInterList() {
		List<InterLeaving> interList=new ArrayList<>();
		interList.addAll(interSet);
		return interList;
	}

}
