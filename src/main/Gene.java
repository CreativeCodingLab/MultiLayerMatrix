package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import processing.core.PApplet;


import static main.SimilarityMatrix_1_4.data;
import static main.SimilarityMatrix_1_4.ggg;
import static main.SimilarityMatrix_1_4.leaderSortedMap;
import static main.SimilarityMatrix_1_4.locals;


public class Gene {
	// For a gene, we have the count number of each type of relations
	public String name = "????";
	public Integrator iX, iY, iH,iW;
	public int order;
	
	public boolean isSelected;
	
	public Gene(String name_, int order_){
		name = name_;
		iX = new Integrator(main.SimilarityMatrix_1_4.marginX,.5f,.2f);
		iY = new Integrator(main.SimilarityMatrix_1_4.marginY,.5f,.2f);
		iW = new Integrator(0,.5f,.2f);
		iH = new Integrator(0,.5f,.2f);
		order = order_;
		isSelected = false;
	}
	
	//Reverse genes for drawing
	public static void reveseGenesForDrawing(){	
		for (int i=0;i<ggg.size();i++){
			ggg.get(i).order = ggg.size()-ggg.get(i).order-1;
		}
	}
	//Swap genes for drawing
	public static void swapGenesForDrawing(){	
		for (int i=0;i<ggg.size();i++){
			ggg.get(i).order =(ggg.get(i).order+ ggg.size()/2)% ggg.size();
		}
	}
	
	// Order genes by name
	public static void orderByName(){	
		Map<String, Integer> unsortMap = new HashMap<String, Integer>();
		for (int i=0;i<ggg.size();i++){
			unsortMap.put(ggg.get(i).name, i);
		}
		Map<String, Integer> treeMap = new TreeMap<String, Integer>(unsortMap);
		int count=0;
		for (Map.Entry<String, Integer> entry : treeMap.entrySet()) {
			int inputOrder = entry.getValue();
			ggg.get(inputOrder).order = count;
			count++;
		}
	}
	
	// Order genes by reading order
	public static void orderByReadingOrder(){	
		for (int i=0;i<ggg.size();i++){
			ggg.get(i).order = i;
		}
	}
	// Order genes by random
	public static void orderBySumSimilarity(PApplet p){	
		
		/*
		ArrayList<Integer> a = new ArrayList<Integer>();
		for (int i=0;i<ggg.size();i++){
			a.add(i);
		}
		while (a.size()>0){
			int num = (int) p.random(a.size());
			ggg.get(a.size()-1).order = a.get(num);
			a.remove(num);
		}*/
		ArrayList<Integer> a = new ArrayList<Integer>();
		for (int i=0;i<ggg.size();i++){
			a.add(i);
		}
		Map<Integer, Float> unsortMap = new HashMap<Integer, Float>();
		for (int i=0;i<ggg.size();i++){
			unsortMap.put(i, getSumDissimilarity(i, a));
		}
		Map<Integer, Float> sorted = sortByComparator3(unsortMap);
		int count=0;
		for (Map.Entry<Integer, Float> entry : sorted.entrySet()) {
			ggg.get(entry.getKey()).order=count;
			count++;
		}
		
			
	}
	
	
	
	
	public static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap) {
		// Convert Map to List
		List<Map.Entry<String, Integer>> list = 
			new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());
 
		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1,
                                           Map.Entry<String, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});
 
		// Convert sorted map back to a Map
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	private static Map<Integer, Integer> sortByComparator2(Map<Integer, Integer> unsortMap) {
		// Convert Map to List
		List<Map.Entry<Integer, Integer>> list = 
			new LinkedList<Map.Entry<Integer, Integer>>(unsortMap.entrySet());
 
		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
			public int compare(Map.Entry<Integer, Integer> o1,
                                           Map.Entry<Integer, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});
 
		// Convert sorted map back to a Map
		Map<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();
		for (Iterator<Map.Entry<Integer, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<Integer, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	private static Map<Integer, Float> sortByComparator3(Map<Integer, Float> unsortMap) {
		// Convert Map to List
		List<Map.Entry<Integer, Float>> list = 
			new LinkedList<Map.Entry<Integer, Float>>(unsortMap.entrySet());
 
		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<Integer, Float>>() {
			public int compare(Map.Entry<Integer, Float> o1,
                                           Map.Entry<Integer, Float> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});
 
		// Convert sorted map back to a Map
		Map<Integer, Float> sortedMap = new LinkedHashMap<Integer, Float>();
		for (Iterator<Map.Entry<Integer, Float>> it = list.iterator(); it.hasNext();) {
			Map.Entry<Integer, Float> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	public static Map<Integer, String> sortByComparator4(Map<Integer, String> unsortMap) {
		// Convert Map to List
		List<Map.Entry<Integer, String>> list = 
			new LinkedList<Map.Entry<Integer, String>>(unsortMap.entrySet());
 
		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<Integer, String>>() {
			public int compare(Map.Entry<Integer, String> o1,
                                           Map.Entry<Integer, String> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});
 
		// Convert sorted map back to a Map
		Map<Integer, String> sortedMap = new LinkedHashMap<Integer, String>();
		for (Iterator<Map.Entry<Integer, String>> it = list.iterator(); it.hasNext();) {
			Map.Entry<Integer, String> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	
	
	// Order by similarity *********** SMALL MOLECULES are first
	public static void orderBySimilarity(){	
		ArrayList<Integer> processedProteins =  new ArrayList<Integer>();
		int beginIndex = 0;
		int order = 0;
		ggg.get(beginIndex).order=order;
		order++;
		processedProteins.add(beginIndex);
		
		for (int i=0;i<ggg.size();i++){
			int similarIndex =  getSimilarGene(beginIndex, processedProteins);
			if (similarIndex<0) break; 
			ggg.get(similarIndex).order=order;
			order++;
			beginIndex = similarIndex;
			processedProteins.add(similarIndex);
		}
		
		
	}
		
	
	public static int getSimilarGene(int orderReading1, ArrayList<Integer> a){
		float minDis = Float.POSITIVE_INFINITY;
		int minIndex = -1;
		for (int i=0;i<ggg.size();i++){
			if (orderReading1==i) continue;
			if (a.contains(i)) continue;
			float dis = computeDis(orderReading1,i);
			if (dis<minDis){
				minDis = dis;
				minIndex = i;
			}
		}
		return minIndex;
	}
	
	public static float computeDis(int orderReading1, int orderReading2){
		float dis = 0;
		for (int i=0;i<ggg.size();i++){
			/*
			int index = i%2048;
			int index1 = orderReading1%2048;
			int index2 = orderReading2%2048;
			float data1 = data[index1][index];
			float data2 = data[index2][index];
			dis += PApplet.abs(data1-data2);
			*/
			dis += PApplet.abs(data[orderReading1][i]-data[orderReading2][i]);
			
			
			if (data[orderReading1][i]==0 && data[orderReading2][i]==0){
			}
			else if (data[orderReading1][i]==0 || data[orderReading2][i]==0){
				dis += 0.1f;
			}
		}
		return dis;
	}
	

	
	
	
	
// ************************ grouping
	// Group by similarity
	@SuppressWarnings("unchecked")
	public static void groupBySimilarity(float r){
		//r=r*8.7f;
		long t1 = System.currentTimeMillis();
		SliderDissimilarity.percent.set(0);
		locals = new ArrayList[ggg.size()];
		for (int i=0;i<ggg.size();i++){
			locals[i] = new ArrayList<Integer>();
		}
			
		System.out.println("1111");
		ArrayList<Integer> leaderList = new ArrayList<Integer>();
		for (int i=0;i<ggg.size();i++){
			boolean foundLeader = false;
			int indexLeader =-1;
			for (int l=0;l<leaderList.size();l++){
				int leader = leaderList.get(l);
				if (Gene.computeDis(leader, i)<r){
					//if (i%1000==0)
					//	System.out.println(i+" 	leaderList = "+leaderList.size()+"	r="+r);
					
					if (indexLeader<0){
						indexLeader = leader;
						foundLeader=true;
					}
					else {
						if (Gene.computeDis(leader,i)<Gene.computeDis(indexLeader, i)){
							indexLeader = leader;
						}
					}
				}
			}
			if (!foundLeader){
				indexLeader=i;
				leaderList.add(indexLeader);
				locals[indexLeader].add(indexLeader);
			}
			else {
				ggg.get(i).order = ggg.get(indexLeader).order;
				locals[indexLeader].add(i);
			}
			SliderDissimilarity.percent.target((float)i*60f/ggg.size());
		}
		long t2 = System.currentTimeMillis();
		main.SimilarityMatrix_1_4.runningtime = (t2-t1)/1000f;
		System.out.println("total time = " +((t2-t1)/1000));
		
		//Second round
		if (leaderList.size()<ggg.size()/10){
			locals = new ArrayList[ggg.size()];
			for (int i=0;i<ggg.size();i++){
				locals[i] = new ArrayList<Integer>();
			}
			for (int i=0;i<ggg.size();i++){
				boolean foundLeader = false;
				int indexLeader =-1;
				for (int l=0;l<leaderList.size();l++){
					int leader = leaderList.get(l);
					if (Gene.computeDis(leader, i)<r){
						if (indexLeader<0){
							indexLeader = leader;
							foundLeader=true;
						}
						else {
							if (Gene.computeDis(leader, i)<Gene.computeDis(indexLeader, i)){
								indexLeader = leader;
							}
						}
					}
				}
				if (!foundLeader){
					System.out.println("*********************Something wrong in the second round");
				}
				else {
					ggg.get(i).order = ggg.get(indexLeader).order;
					locals[indexLeader].add(i);
				}
				SliderDissimilarity.percent.target(60f+(float)i*30f/ggg.size());
			}
		}
		System.out.println("3333");
		
		// order leader list by leader order
		SliderDissimilarity.percent.target(90);
		Map<Integer, Integer> unsortMap = new HashMap<Integer, Integer>();
		for (int i=0;i<leaderList.size();i++){
			int leader = leaderList.get(i);
			unsortMap.put(leader, ggg.get(leader).order);
		}
		leaderSortedMap = sortByComparator2(unsortMap);
		SliderDissimilarity.percent.target(100);
		
		// Order character in each group by reading order
		for (int i=0;i<ggg.size();i++){
			locals[i] = orderLocals(locals[i]);
		}
	}
	public static ArrayList<Integer> orderLocals(ArrayList<Integer> a){
		ArrayList<Integer> b = new ArrayList<Integer>();
		Map<Integer, Float> unsortMap = new HashMap<Integer, Float>();
		for (int i=0;i<a.size();i++){
			int index = a.get(i);
			unsortMap.put(index, getSumDissimilarity(index, a));
		}
		Map<Integer, Float> sorted = sortByComparator3(unsortMap);
		for (Map.Entry<Integer, Float> entry : sorted.entrySet()) {
			b.add(entry.getKey());
		}
			
		
		return b;
	}
		
	public static float getSumDissimilarity(int index1, ArrayList<Integer> a){
		float sum=0;
		for (int i2=0;i2<a.size();i2++){
			int index2 = a.get(i2);
			sum+=data[index1][index2];
		}
		sum = sum*10*ggg.size()+index1;
		return sum;
	}
}
