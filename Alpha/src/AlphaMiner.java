
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AlphaMiner {
	private List<String> eventList;
	private List<Causal> causalList;
	private List<Independence> independenceList;
	private static List<Set<String>> traceeventList;

	private static ArrayList<Dot> dots;
	private static ArrayList<Dot> noiseDots;
	private static String filename;

	private Set<String> A1;
	private Set<String> B1;
	private Set<String> A2;
	private Set<String> B2;
	private List<StartTransition> leftT;// 找出最左最右变迁，用于添加最左最右库所
	private List<StartTransition> rightT;// 通过比较字符串大小获得

	private List<AlphaX> yList;
	private List<AlphaX> noiseyList;
	private Set<AlphaX> ySet;
	private Set<AlphaX> noiseYSet;
	private Map<PetriPlace, AlphaX> placeMap;
	private Map<PetriPlace, AlphaX> noiseplaceMap;
	private List<PetriTransition> transitionList;
	private List<PetriEdge> arcList;

	public AlphaMiner() {
	}

	// 不含噪声
	public AlphaMiner(List<String> eventList, List<Causal> causalList, List<Independence> independenceList,
			List<Set<String>> traceeventList, String dirFile) {
		this.eventList = eventList;
		this.causalList = causalList;
		this.independenceList = independenceList;
		this.traceeventList = traceeventList;
		this.filename = filename;
		dots = new ArrayList<Dot>();
		noiseDots = new ArrayList<Dot>();
		leftT = new ArrayList<StartTransition>();
		rightT = new ArrayList<StartTransition>();
		this.getLeftRight();
		yList = new ArrayList<AlphaX>();
		ySet = new HashSet<AlphaX>();
		this.getYList();
		this.initPlaceList();
		this.initTransitonList();
		this.initArcList();

		new WriteToPnml().write(dots, dirFile);
	}

	private void writeY(String dirY) {
		// TODO Auto-generated method stub
		File file = new File(dirY);
		FileWriter fw = null;
		BufferedWriter writer = null;
		try {
			fw = new FileWriter(file);
			writer = new BufferedWriter(fw);
			for (int i = 0; i < yList.size() - 1; i++) {
				writer.write(yList.get(i).toString());
				writer.write(":");// 换行
			}
			writer.write(yList.get(yList.size() - 1).toString());
			writer.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void writeNoiseY(String dirY) {
		// TODO Auto-generated method stub
		File file = new File(dirY);
		FileWriter fw = null;
		BufferedWriter writer = null;
		try {
			fw = new FileWriter(file);
			writer = new BufferedWriter(fw);
			for (int i = 0; i < noiseyList.size() - 1; i++) {
				writer.write(noiseyList.get(i).toString());
				writer.write(":");// 换行
			}
			writer.write(noiseyList.get(noiseyList.size() - 1).toString());
			writer.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void getLeftRight() {
		for (int i = 0; i < traceeventList.size(); i++) {
			List<String> list = new ArrayList<String>();
			list.addAll(traceeventList.get(i));
			StartTransition startTransition = new StartTransition(list.get(0), 0);
			StartTransition startTransition1 = new StartTransition(list.get(list.size() - 1), 0);
			if (i == 0) {
				leftT.add(startTransition);
				rightT.add(startTransition1);
			} else {
				int count = 0;
				for (int j = 0; j < leftT.size(); j++) {
					if (leftT.get(j).equals(startTransition)) {
						leftT.get(j).setCount(leftT.get(j).getCount() + 1);
						break;
					} else {
						count++;
						if (count == leftT.size())
							leftT.add(startTransition);
					}
				}

				count = 0;
				for (int j = 0; j < rightT.size(); j++) {
					if (rightT.get(j).equals(startTransition1)) {
						rightT.get(j).setCount(rightT.get(j).getCount() + 1);
						break;
					} else {
						count++;
						if (count == rightT.size())
							rightT.add(startTransition1);
					}
				}
			}
		}
	}

	private void getYList() {
		// 求X集合
		for (String event : eventList) {
			// or-split
			List<String> rightList = new ArrayList<String>();
			for (Causal causal : causalList) {
				if (causal.getLeftEvent().equals(event)) { // 左侧值相等，则进一步判断右侧关系
					// 左侧值相同，右侧值都添加到一个集合中，把具有#关系的添加到一个新集合中
					rightList.add(causal.getRightEvent());
				}
			}
			// 对右侧所有值进行处理，判断是不是具有#关系，是就作为一个集合
			if (rightList.size() == 1) {
				A1 = new HashSet<String>();
				B1 = new HashSet<String>();
				A1.add(event);
				B1.add(rightList.get(0));
				AlphaX X1 = new AlphaX(A1, B1);
				ySet.add(X1);
			} else {
				for (int i = 0; i < rightList.size() - 1; i++) {
					A1 = new HashSet<String>();
					Set<String> tempSet = new HashSet<String>();
					tempSet.add(rightList.get(i));
					for (int j = i + 1; j < rightList.size(); j++) {
						Independence independence1 = new Independence(rightList.get(i), rightList.get(j));
						Independence independence2 = new Independence(rightList.get(j), rightList.get(i));
						if (independenceList.contains(independence1) || independenceList.contains(independence2)) {
							tempSet.add(rightList.get(j));
						}
					}
					A1.add(event);
					if (!A1.isEmpty()) {
						AlphaX X1 = new AlphaX(A1, tempSet);
						ySet.add(X1);
					}
				}
			}

			// or-join
			List<String> leftList = new ArrayList<String>();
			for (Causal causal : causalList) {
				if (causal.getRightEvent().equals(event)) { // 右侧值相等，则进一步判断左侧关系
					// 左侧值相同，右侧值都添加到一个集合中，把具有#关系的添加到一个新集合中
					leftList.add(causal.getLeftEvent());
				}
			}
			// 对右侧所有值进行处理，判断是不是具有#关系，是就作为一个集合
			if (leftList.size() == 1) {
				A2 = new HashSet<String>();
				B2 = new HashSet<String>();
				A2.add(leftList.get(0));
				B2.add(event);
				AlphaX X2 = new AlphaX(A2, B2);
				ySet.add(X2);
			} else {
				for (int i = 0; i < leftList.size() - 1; i++) {
					A2 = new HashSet<String>();
					B2 = new HashSet<String>();
					Set<String> tempSet = new HashSet<String>();
					tempSet.add(leftList.get(i));
					for (int j = i + 1; j < leftList.size(); j++) {
						Independence independence1 = new Independence(leftList.get(i), leftList.get(j));
						Independence independence2 = new Independence(leftList.get(j), leftList.get(i));
						if (independenceList.contains(independence1) || independenceList.contains(independence2)) {
							tempSet.add(leftList.get(j));
						}
					}
					A2.add(event);
					if (!A2.isEmpty()) {
						AlphaX X2 = new AlphaX(tempSet, A2);
						ySet.add(X2);
					}
				}
			}

		}
		// 合并，如[3][5，6]，[4][5，6]，[3，4][5]，[3，4][6]合并为[3，4][5，6]
		yList.addAll(ySet);
		for (int i = 0; i < yList.size() - 1; i++) {
			for (int j = i + 1; j < yList.size(); j++) {
				if (yList.get(i).getA().equals(yList.get(j).getA())) {
					List<String> bList1 = new ArrayList<String>();
					List<String> bList2 = new ArrayList<String>();
					bList1.addAll(yList.get(i).getB());
					bList2.addAll(yList.get(j).getB());
					int flag = 0;
					for (int m = 0; m < bList1.size(); m++) {
						for (int n = 0; n < bList2.size(); n++) {
							Independence independence = new Independence(bList1.get(m), bList2.get(n));
							Independence independence2 = new Independence(bList2.get(n), bList1.get(m));
							if (!independenceList.contains(independence) && !independenceList.contains(independence2)) {
								flag = 1;
								break;
							}
						}
						if (flag == 1)
							break;
					}
					if (flag == 0) {
						Set<String> set = new HashSet<String>();
						set.addAll(yList.get(i).getB());
						set.addAll(yList.get(j).getB());
						yList.get(i).setB(set);
						yList.get(j).setB(set);
					}

				}
			}
		}
		// 求Y集合
		yList.addAll(ySet);
		Set<Integer> numberSet = new HashSet<Integer>();
		List<Integer> numberList = new ArrayList<Integer>();
		for (int i = 0; i < yList.size() - 1; i++) {
			for (int j = i + 1; j < yList.size(); j++) {
				if (yList.get(i).getA().equals(yList.get(j).getA())
						&& yList.get(i).getB().containsAll(yList.get(j).getB())) {
					numberSet.add(j);
				} else if (yList.get(i).getA().equals(yList.get(j).getA())
						&& yList.get(j).getB().containsAll(yList.get(i).getB())) {
					numberSet.add(i);
				} else if (yList.get(j).getB().equals(yList.get(i).getB())
						&& yList.get(i).getA().containsAll(yList.get(j).getA())) {
					numberSet.add(j);
				} else if (yList.get(j).getB().equals(yList.get(i).getB())
						&& yList.get(j).getA().containsAll(yList.get(i).getA())) {
					numberSet.add(i);
				}
			}
		}
		numberList.addAll(numberSet);
		Collections.sort(numberList);
		for (int i = numberList.size() - 1; i >= 0; i--) {
			yList.remove(((Integer) numberList.get(i)).intValue());
		}
	}

	/**
	 * 初始化库所集合
	 */
	private void initPlaceList() {

		Set<String> placeStringSet = new HashSet<String>();
		placeMap = new HashMap<PetriPlace, AlphaX>();
		int count = 1;// 从1开开始添加，0表示最左侧的值
		for (AlphaX collection : yList) {

			Dot dot = new Dot(1);
			dot.setId("p" + count);
			dot.setName("p" + count);
			dots.add(dot);
			placeMap.put(new PetriPlace("p" + count++), collection);
		}
		// 给开始和结束库所两侧添加空白
		Set aSet = new HashSet<String>();
		aSet.add("");
		for (int i = 0; i < leftT.size(); i++) {
			Set b1Set = new HashSet<String>();
			b1Set.add(leftT.get(i).getName());
			AlphaX alphaX = new AlphaX(aSet, b1Set);
			if (!placeStringSet.contains("p" + count)) {
				Dot dot = new Dot(1);
				dot.setId("p" + count);
				dots.add(dot);
				placeStringSet.add("p" + count);
			}
			placeMap.put(new PetriPlace("p" + count++), alphaX);
		}
		for (int i = 0; i < rightT.size(); i++) {
			Set b2Set = new HashSet<String>();
			b2Set.add(rightT.get(i).getName());
			AlphaX alphaX = new AlphaX(b2Set, aSet);
			if (!placeStringSet.contains("p" + count)) {
				Dot dot2 = new Dot(1);
				dot2.setId("p" + count);
				dots.add(dot2);
				placeStringSet.add("p" + count);
			}
			// placeMap.put(new PetriPlace("p" + count++), alphaX);
			placeMap.put(new PetriPlace("p" + count), alphaX);
		}
	}

	/**
	 * 初始化变迁集合
	 */
	private void initTransitonList() {
		transitionList = new ArrayList<PetriTransition>();
		for (String transition : eventList) {
			transitionList.add(new PetriTransition(transition));
			Dot dot = new Dot(2);
			dot.setId(transition);
			dots.add(dot);
		}
	}

	/**
	 * 初始化边集
	 */
	private void initArcList() {
		arcList = new ArrayList<PetriEdge>();
		Set<PetriPlace> placeSet = placeMap.keySet();
		int count = 0;
		for (PetriPlace place : placeSet) {
			AlphaX alpha = placeMap.get(place);
			Set<String> firstSet = alpha.getA();
			Set<String> secondSet = alpha.getB();
			for (String string : firstSet) {
				if (!string.equals("")) {
					arcList.add(new PetriEdge(new PetriTransition(string), place));
					Dot dot = new Dot(3);
					dot.setId("arc" + count++);
					dot.setSource(string);
					dot.setTarget(place.getName());
					dots.add(dot);
				}
			}
			for (String s2 : secondSet) {
				if (!s2.equals("")) {
					arcList.add(new PetriEdge(place, new PetriTransition(s2)));
					Dot dot = new Dot(3);
					dot.setId("arc" + count++);
					dot.setSource(place.getName());
					dot.setTarget(s2);
					dots.add(dot);
				}
			}

		}
	}

	public List<AlphaX> getyList() {
		return yList;
	}

	public Map<PetriPlace, AlphaX> getPlaceMap() {
		return placeMap;
	}

	public List<PetriTransition> getTransitionList() {
		return transitionList;
	}

	public List<PetriEdge> getArc() {
		return arcList;
	}

	public List<AlphaX> getnoiseyList() {
		return noiseyList;
	}

	public Map<PetriPlace, AlphaX> getnoisePlaceMap() {
		return noiseplaceMap;
	}

	public Map<PetriPlace, AlphaX> getNoiseplaceMap() {
		return noiseplaceMap;
	}

	public void setNoiseplaceMap(Map<PetriPlace, AlphaX> noiseplaceMap) {
		this.noiseplaceMap = noiseplaceMap;
	}

}
