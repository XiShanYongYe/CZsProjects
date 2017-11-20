import java.util.ArrayList;

import org.nust.heroine.basicstruct.PetriNet;


public class PnmlManager {
	
	private ArrayList<PetriNet> petriNets = new ArrayList<PetriNet>();
	


	private  PnmlManager() {
		// TODO Auto-generated constructor stub
	}
	static PnmlManager  pm = new PnmlManager();
	
	public static PnmlManager getPM(){
		return pm;
	}
	
	public void add(PetriNet petriNet){
		petriNets.add(petriNet);
	}
	
	public ArrayList<PetriNet> getPetriNets() {
		return petriNets;
	}
	
	
}
