package org.processmining.plugins.dcfe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetFactory;

public class DCFEPlugin {
	@Plugin(name = "DCFE Plug-in", parameterLabels = { "Input a Log" }, returnLabels = { "return deadlock",
			"return lack of synchronize" }, returnTypes = { Petrinet.class,
					Petrinet.class }, userAccessible = true, help = "Discovering control flow errors from business process event logs.")
	@UITopiaVariant(affiliation = "NJUST", author = "Zhen Chang", email = "wsong@njust.edu.cn")
	public static Petrinet[] dcfePlugin(PluginContext context, XLog log) {
		Random random = new Random();
		int progress = 0;
		context.getProgress().setMinimum(0);
		context.getProgress().setMaximum(100);
		context.getProgress().setCaption("Discovering control flow errors from business process event logs ");
		context.getProgress().setIndeterminate(false);
		
		List<EventFlow> L = ReadLog.getTraces(log);
		progress+=random.nextInt(50);
		context.getProgress().setValue(progress);
		PreprocessEventLog preprocessEventLog = new PreprocessEventLog(L, "te");
		progress+=random.nextInt(10);
		context.getProgress().setValue(progress);
		List<EventFlow> L1 = preprocessEventLog.getL1();
		List<EventFlow> L2 = preprocessEventLog.getL2();
		DiscoverEventRelations dis1 = new DiscoverEventRelations(L1);
		DiscoverEventRelations dis2 = new DiscoverEventRelations(L2);
		progress+=random.nextInt(10);
		context.getProgress().setValue(progress);
		DiscoverDeadlocks deadlocks = new DiscoverDeadlocks(dis1);
		deadlocks.mainBody();
		List<PetriNetMy> deadlockPetriNets = deadlocks.getPetriNets();
		progress+=random.nextInt(10);
		context.getProgress().setValue(progress);
		DiscoverSynchronization synchronization = new DiscoverSynchronization(dis2);
		synchronization.mainBody();
		List<PetriNetMy> synchronizationPetriNets = synchronization.getPetriNets();
		progress+=random.nextInt(10);
		context.getProgress().setValue(progress);

		Map<String, Place> placeMap = new HashMap<>();
		Map<String, Transition> transitionMap = new HashMap<>();
		Petrinet deadlock = PetrinetFactory.newPetrinet("Deadlock");
		for (PetriNetMy petriNetMy : deadlockPetriNets) {
			for (PetriPlace place : petriNetMy.getPlaces()) {
				Place p = deadlock.addPlace(place.getName());
				placeMap.put(place.getName(), p);
			}
			for (PetriTransition transition : petriNetMy.getTransitions()) {
				Transition t = deadlock.addTransition(transition.getName());
				transitionMap.put(transition.getName(), t);
			}
			for (PetriArc arc : petriNetMy.getEdges()) {
				if (arc.getSource().getName().startsWith("P")) {
					Place p = placeMap.get(arc.getSource().getName());
					Transition t = transitionMap.get(arc.getTarget().getName());
					deadlock.addArc(p, t);
				} else {
					Transition t = transitionMap.get(arc.getSource().getName());
					Place p = placeMap.get(arc.getTarget().getName());
					deadlock.addArc(t, p);
				}
			}
		}

		Petrinet synchronize = PetrinetFactory.newPetrinet("Lack of synchronize");
		for (PetriNetMy petriNetMy : synchronizationPetriNets) {
			for (PetriPlace place : petriNetMy.getPlaces()) {
				Place p = synchronize.addPlace(place.getName());
				placeMap.put(place.getName(), p);
			}
			for (PetriTransition transition : petriNetMy.getTransitions()) {
				Transition t = synchronize.addTransition(transition.getName());
				transitionMap.put(transition.getName(), t);
			}
			for (PetriArc arc : petriNetMy.getEdges()) {
				if (arc.getSource().getName().startsWith("P")) {
					Place p = placeMap.get(arc.getSource().getName());
					Transition t = transitionMap.get(arc.getTarget().getName());
					synchronize.addArc(p, t);
				} else {
					Transition t = transitionMap.get(arc.getSource().getName());
					Place p = placeMap.get(arc.getTarget().getName());
					synchronize.addArc(t, p);
				}
			}
		}
		context.getProgress().setValue(100);
		return new Petrinet[] { deadlock, synchronize };
	}
}
