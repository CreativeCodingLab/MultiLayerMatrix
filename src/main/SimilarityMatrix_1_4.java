package main;
/*
 * DARPA project
 *
 * Copyright 2015 by Tuan Dang.
 *
 * The contents of this file are subject to the Mozilla Public License Version 2.0 (the "License")
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 */

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;


import processing.core.*;

public class SimilarityMatrix_1_4 extends PApplet {
	private static final long serialVersionUID = 1L;
	public int count = 0;
	public String currentFile = "./HongData/CharSim2048.csv";

	
	public static ButtonBrowse buttonBrowse;
	
	// Store the genes results
	public static float[][] data;
	
	// Global data
	public static ArrayList<Gene> ggg = new ArrayList<Gene>();
	
	public static Map<Integer, Integer> leaderSortedMap;
	public static ArrayList<Integer>[] locals = null;
	
	public static String message="";
	
	public ThreadLoader1 loader1=new ThreadLoader1(this);
	public Thread thread1=new Thread(loader1);
	
	
	public ThreadLoader4 loader4=new ThreadLoader4(this);
	public Thread thread4=new Thread(loader4);
	
	public ThreadLoader2 loader2=new ThreadLoader2(this);
	public Thread thread2=new Thread(loader2);
	
	public ThreadLoader5 loader5=new ThreadLoader5(this);
	public Thread thread5=new Thread(loader5);
	
	// Venn
	public int bX,bY;
	
	// Order genes
	public static PopupOrder popupOrder;
	public static PopupSort popupSort;
	public static PopupMenu popupMenu;
	public static CheckBox check1;
	public static CheckBox check3;
	
	// Allow to draw 
	public static boolean isAllowedDrawing = false;
	public PFont metaBold = loadFont("Arial-BoldMT-18.vlw");
	public SliderDissimilarity sliderDissimilarity = new SliderDissimilarity(this, 60);
	public Slider slider = new Slider(this, 0.5f);
	
	// Grouping animation
	public static int stateAnimation =0;
	public static int bGroup =-1;
	public static int sGroup =-1;
	public static int sX1 =-1;
	public static int sX2 =-1;
	public static int sY1 =-1;
	public static int sY2 =-1;
	public float sX = PApplet.min(sX1,sX2);
	public float sY = PApplet.min(sY1,sY2);
	public float sW = PApplet.abs(sX1-sX2);
	public float sH = PApplet.abs(sY1-sY2);
	
	// Brushing matrix
	public float size=0;
	public static float marginX = 120;
	public static float marginY = 130;
	public float matrixSize2=430;
	public float matrixY2=250;
	
	public Integrator groupY = new Integrator(matrixY2,.5f,.2f);
	public static float lastX=0;
	public static float lastY=0;
	public static int numChars =0;
	public static int numTaxa =0;
	public static String[][] charTable;
	public static String[] taxonomyStrings;
	public static String[] families;
	public static String[] tribes;
	public static String[] genuses;
	public static String[] species;
	public static ArrayList[] extraStrings;
	
	public static String colorScale = "redgreen";
	public static Color[] colors = new Color[10];
	public static ArrayList<String> listTribes = new ArrayList<String>();
	public static ArrayList<String> listGenuses = new ArrayList<String>();
	public static ArrayList<String> listSpecies = new ArrayList<String>();
	public static ArrayList<String> sTribes = new ArrayList<String>();
	public static ArrayList<String> sGenuses = new ArrayList<String>();
	public static ArrayList<String> sSpecies = new ArrayList<String>();
	public static Map<String, Integer> tribeGenusMap = new HashMap<String, Integer>();
	public static int[][] r1 = new int[listTribes.size()][listGenuses.size()];
	public static int[][] r2 = new int[listGenuses.size()][listSpecies.size()];
	float[] xx1 = new float[listTribes.size()];
	float[] yy1 = new float[listTribes.size()];
	float[] xx2 = new float[listGenuses.size()];
	float[] yy2 = new float[listGenuses.size()];
	float[] xx3 = new float[listSpecies.size()];
	float[] yy3 = new float[listSpecies.size()];
	public static boolean isBrushing = false;
	public static ArrayList<PImage>[] listGenusImages;
	public static float runningtime = 0;
	
	
	public static void main(String args[]){
	  PApplet.main(new String[] { SimilarityMatrix_1_4.class.getName() });
    }

	public void setup() {
		textFont(metaBold,14);
		size(1440, 900);
		//size(2000, 1200);
		//size(1200, 700);
		if (frame != null) {
		    frame.setResizable(true);
		  }
		background(0); 
		frameRate(12);
		curveTightness(0.7f); 
		smooth();
		colors[0] = new Color(141, 211, 199);
		colors[1] = new Color(200, 200, 100);
		colors[2] = new Color(190, 186, 218);
		colors[3] = new Color(242, 205, 229);
		colors[4] = new Color(128, 177, 211);
		colors[5] = new Color(253, 180,  98);
		colors[6] = new Color(179, 222, 105);
		colors[7] = new Color(251, 128, 114);
		colors[8] = new Color(180, 180, 190);
		colors[9] = new Color(188, 128, 189);

		buttonBrowse = new ButtonBrowse(this);
		popupOrder  = new PopupOrder(this);
		popupSort = new PopupSort(this);
		popupMenu = new PopupMenu(this);
		check1 = new CheckBox(this, "Lensing");
		check3 = new CheckBox(this, "Highlighting groups");
		
		//VEN DIAGRAM
		if (!currentFile.equals("")){
			thread1=new Thread(loader1);
			thread1.start();
		}
		// enable the mouse wheel, for zooming
		addMouseWheelListener(new java.awt.event.MouseWheelListener() {
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
				mouseWheel(evt.getWheelRotation());
			}
		});
	}	
	
	public void draw() {
		background(255);
		// Draw 
		try{
			//this.fill(255,0,0);
			//this.text(runningtime+"", 100,100);
			
			if (isAllowedDrawing){
				if (currentFile.equals("")){
					int ccc = this.frameCount*6%255;
					this.fill(ccc, 255-ccc,(ccc*3)%255);
					this.textAlign(PApplet.LEFT);
					this.textSize(20);
					this.text("Please select a input file", 300,250);
					float x6 =74;
					float y6 =25;
					this.stroke(ccc, 255-ccc,(ccc*3)%255);
					this.line(74,25,300,233);
					this.noStroke();
					this.triangle(x6, y6, x6+4, y6+13, x6+13, y6+4);
				}
				else{
					if (PopupMenu.s==2){ // Draw taxa selection
						drawTaxa(marginX, marginY-75);
						
						ArrayList<Integer> a = new ArrayList<Integer>();
						for (int i=0;i<tribes.length;i++){
							if (tribes[i]!=null //&& sTribes.contains(tribes[i])
									//&& sGenuses.contains(genuses[i]) 
									&& species[i]!=null && sSpecies.contains(species[i])){
								a.add(i);
							}	
						}
						ArrayList<Integer> charList = this.drawTaxaCharacterTableByTaxaList(a,marginX+1100,marginY);
						drawElementList(tribes.length, charList,marginX+900,marginY+300, matrixSize2, new Color(0,0,0));
						// Draw images 
						/*if (sGenuses.size()>0){
							float iWidth = 200;
							String s = sGenuses.get(0);
							int indexGenus = listGenuses.indexOf(s);
							if (indexGenus>=0){
								for (int im=0; im<listGenusImages[indexGenus].size();im++){
									float row = im/2;
									float col = im%2;
									this.image(listGenusImages[indexGenus].get(im), 1000+col*(iWidth+10), 100+row*(iWidth+10));
								}
							}
							else{
								System.out.println("Something wrong with genuses");
							}
						}*/
						
					}	
					else if (PopupMenu.s==1 && stateAnimation==1){
						if (sGroup<0){
							check1.draw(this.width-340, 2);
							if (PopupMenu.s==1){
								check3.draw(this.width-340, 19);
								sliderDissimilarity.draw(this.width-340, 35);
							}	
							popupOrder.draw(this.width-177);
							slider.draw(this.width-500, 14);
						}
						popupSort.draw(this.width-88);
						
						int maxElement = 0;
						if (leaderSortedMap!=null){
							for (Map.Entry<Integer, Integer> entry : leaderSortedMap.entrySet()) {
								int index = entry.getKey();
								int numE = locals[index].size();
								if (numE>maxElement)
									maxElement = numE;
							}	
						}
						// Draw Matrices
						drawSecondMatrix(maxElement,marginX+800);
						drawGroups(maxElement);
						
						
						if (sGroup>=0){
							this.drawTaxaCharacterTableByCharacterList(locals[sGroup],marginX+matrixSize2+1000,marginY);
						}
						else if (bGroup>=0){
							this.drawTaxaCharacterTableByCharacterList(locals[bGroup],marginX+matrixSize2+1000,marginY);
						}
					}
					else {
						check1.draw(this.width-340, 2);
						popupOrder.draw(this.width-177);
						drawFirstMatrix();
						slider.draw(this.width-500, 14);
						
					}
				}
			}
			buttonBrowse.draw();
			popupMenu.draw(74);
			
		}
		catch (Exception e){
			System.out.println();
			System.out.println("*******************Catch ERROR*******************");
			e.printStackTrace();
			return;
		}
	}	
	public ArrayList<PImage> getImages(String name) {
		ArrayList<PImage> images = new ArrayList<PImage>();
		String result = excutePost("http://en.wikipedia.org/wiki/"+name,"");
		String[] ps1 = result.split(".jpg\"");
		ArrayList<String> a = new ArrayList<String>();
		for (int i = 0; i<ps1.length;i++){
			String[] ps2 = ps1[i].split("src=\"");
			//for (int j = 0; j<ps2.length;j++){
			String str = ps2[ps2.length-1];
			if (str.startsWith("//upload.wikimedia.org") && str.length()<500){
				a.add("http:"+str+".jpg");
			}
			if (name.contains("Zamiaceae")){
			//	System.out.println(ps1[i]);
			}
			
		}
		
		for (int i = 0; i<a.size();i++){
			try{
				PImage image = this.loadImage(a.get(i));
				image.resize(180, 180);
				images.add(image);
			}
			catch (Exception e) {
			    e.printStackTrace();
			    continue;
			} 
		}
		return images;
	}
	public static String excutePost(String targetURL, String urlParameters) {
		  HttpURLConnection connection = null;  
		  try {
		    //Create connection
		    URL url = new URL(targetURL);
		    connection = (HttpURLConnection)url.openConnection();
		    connection.setRequestMethod("POST");
		    connection.setRequestProperty("Content-Type", 
		        "application/x-www-form-urlencoded");

		    connection.setRequestProperty("Content-Length", 
		        Integer.toString(urlParameters.getBytes().length));
		    connection.setRequestProperty("Content-Language", "en-US");  

		    connection.setUseCaches(false);
		    connection.setDoOutput(true);

		    //Send request
		    DataOutputStream wr = new DataOutputStream (
		        connection.getOutputStream());
		    wr.writeBytes(urlParameters);
		    wr.close();

		    //Get Response  
		    InputStream is = connection.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+ 
		    String line;
		    while((line = rd.readLine()) != null) {
		      response.append(line);
		      response.append('\r');
		    }
		    rd.close();
		    return response.toString();
		  } catch (Exception e) {
		    e.printStackTrace();
		    return "";
		  } finally {
		    if(connection != null) {
		      connection.disconnect(); 
		    }
		  }
		}
	
	
	public ArrayList<String> orderAlphabetically(ArrayList<String> a) {
		Map<Integer, String> unsortMap = new HashMap<Integer, String>();
		for (int i=0;i<a.size();i++){
			unsortMap.put(i, a.get(i));
		}
		ArrayList<String> b = new ArrayList<String>();
		Map<Integer, String> sorted = Gene.sortByComparator4(unsortMap);
		for (Map.Entry<Integer,String> entry : sorted.entrySet()) {
			b.add(entry.getValue());
		}
		return b;
	}
		
	
	public void drawTaxa(float xx, float yy) {
		float fadingTextSat =50;
		float fadingLineSat =10;
		
		// Column TRIBES
		float gapX = 300;
		this.fill(0);
		this.textSize(12);
		this.textAlign(PApplet.RIGHT);
		this.text(listTribes.size()+" TRIBES", xx,yy-10);
		float gapY1 = (this.height-yy)/listTribes.size();
		for (int i=0;i<listTribes.size();i++){
			xx1[i] = xx;
			yy1[i] = yy+i*gapY1+gapY1/2;
			this.fill(colors[i].getRed(), colors[i].getGreen(), colors[i].getBlue());
			if (isBrushing){
				if (sTribes.contains(listTribes.get(i)))
					this.fill(colors[i].getRed(), colors[i].getGreen(), colors[i].getBlue());
				else
					this.fill(colors[i].getRed(), colors[i].getGreen(), colors[i].getBlue(),fadingTextSat);
			}
			this.text(listTribes.get(i),xx1[i],yy1[i]);
		}
		
		
		// Column genera
		this.textAlign(PApplet.CENTER);
		this.fill(0);
		this.textSize(12);
		this.text(listGenuses.size()+" GENERA", xx+gapX,yy-10);
		float gapY2 = (this.height-yy+3f)/listGenuses.size();
		this.textSize(10);
		for (int i=0;i<listGenuses.size();i++){
			xx2[i] = gapX+xx;
			yy2[i] = yy+i*gapY2;
			this.fill(0);
			if (isBrushing){
				if (sGenuses.contains(listGenuses.get(i))){
					this.fill(0);
					this.textSize(11);
				}	
				else{
					this.fill(0,fadingTextSat);
					this.textSize(10);
				}	
			}
			this.text(listGenuses.get(i),xx2[i],yy2[i]);
			
			float textW = this.textWidth(listGenuses.get(i));
			for (int j=0;j<listTribes.size();j++){
				if (r1[j][i]>0){
					this.strokeWeight(PApplet.pow(r1[j][i],0.45f)-0.9f);
					this.stroke(colors[j].getRed(), colors[j].getGreen(), colors[j].getBlue(), 180);
					if (isBrushing){
						if (sGenuses.contains(listGenuses.get(i)) && sTribes.contains(listTribes.get(j))){
							this.stroke(colors[j].getRed(), colors[j].getGreen(), colors[j].getBlue());
							this.strokeWeight(PApplet.pow(r1[j][i],0.45f));
						}	
						else{
							this.stroke(colors[j].getRed(), colors[j].getGreen(), colors[j].getBlue(), fadingLineSat);
							
						}	
					}
					this.line(xx+5, yy1[j]-3, xx2[i]-textW/2-5, yy2[i]-3);
				}
			}
				
		}
		
		// Column SPECIES
		this.textAlign(PApplet.LEFT);
		this.textSize(12);
		this.text(listSpecies.size()+" SPECIES", xx+gapX*2,yy-10);
		float gapY3 = (this.height-yy-1f)/listSpecies.size();
		this.textSize(10);
		for (int i=0;i<listSpecies.size();i++){
			xx3[i] = 2*gapX+xx;
			yy3[i] = yy+i*gapY3;
			this.fill(0);
			if (isBrushing){
				if (sSpecies.contains(listSpecies.get(i))){
					this.fill(0);
					this.textSize(11);
				}	
				else{
					this.fill(0,fadingTextSat*0.6f);
					this.textSize(10);
				}	
			}
			this.text(listSpecies.get(i),xx3[i],yy3[i]);
			//if (listSpecies.get(i).equals("californica"))
			//	this.text(listSpecies.get(i),xx3[i]+60,yy3[i]);
			this.stroke(0,100);
			for (int j=0;j<listGenuses.size();j++){
				float textW = this.textWidth(listGenuses.get(j));
				if (r2[j][i]>0){
					this.strokeWeight(PApplet.pow(r2[j][i],0.5f)-0.75f);
					int indexTribe = tribeGenusMap.get(listGenuses.get(j));
					this.stroke(colors[indexTribe].getRed(), colors[indexTribe].getGreen(), colors[indexTribe].getBlue(), 200);
					if (isBrushing){
						if (sGenuses.contains(listGenuses.get(j)) && sSpecies.contains(listSpecies.get(i))){
							this.stroke(colors[indexTribe].getRed(), colors[indexTribe].getGreen(), colors[indexTribe].getBlue());
							this.strokeWeight(PApplet.pow(r2[j][i],0.5f)+0.5f);
						}	
						else{
							this.stroke(colors[indexTribe].getRed(), colors[indexTribe].getGreen(), colors[indexTribe].getBlue(), fadingLineSat);
							
						}	
					}
					this.line(xx+gapX+textW/2+3, yy2[j]-3, xx3[i]-2, yy3[i]-3);
				}
			}
		}
	}
		
		
	public void drawFirstMatrix() {
		size = (this.height-100f)/ggg.size();
		size = size*0.8f;
		if (size>100)
			size=100;
		
		// Checking state of group transition
		if (leaderSortedMap!=null && stateAnimation==0){
			float maxDis = 0;
			for (Map.Entry<Integer, Integer> entry : leaderSortedMap.entrySet()) {
				int index = entry.getKey();
				for (int i=1;i<locals[index].size();i++){
					int child = locals[index].get(i);
					float dis = PApplet.abs(ggg.get(index).iX.value-ggg.get(child).iX.value);
					if (dis>maxDis)
						maxDis = dis;
				}
			}
			if (maxDis<2){
				stateAnimation=1;
			}
		}
		drawGenes();
		
		float x2 = 2;
		float y2 = 34;
		this.fill(0,100);
		this.textAlign(PApplet.LEFT);
		this.textSize(11);
		String[] str = currentFile.split("/");
        String nameFile = str[str.length-1];
  	    this.text("File: "+nameFile, x2, y2);
  	    this.text("Num charaters: "+ggg.size(), x2, y2+14);
	}
	
	
	
	public void drawGroups(int maxElement) {
		if (leaderSortedMap==null) return;
		size = (this.height-100f)/leaderSortedMap.size();
		size = size*0.74f;
		if (size>100)
			size=100;
		
		this.noStroke();
		this.fill(255,200);
		this.rect(marginX, marginY, size*leaderSortedMap.size()+25, lastY-marginY+10);  // white matrix background
		
		
		// Compute lensing
		if (check1.s){
			bX = (int) ((mouseX-marginX)/size);
			bY = (int) ((mouseY-marginY)/size);
		}
		else{
			bX = leaderSortedMap.size()+10;
			bY = leaderSortedMap.size()+10;
		}
		float lensingSize = PApplet.map(size, 0, 100, 10, 100);	
		
		int num = 7; // Number of items in one side of lensing
		
		int order = 0;
		for (Map.Entry<Integer, Integer> entry : leaderSortedMap.entrySet()) {
			int index = entry.getKey();
			if (bX-num<=order && order<=bX+num) {
				ggg.get(index).iW.target(lensingSize);
				int num2 = order-(bX-num);
				if (bX-num>=0)
					setValue(ggg.get(index).iX, marginX +(bX-num)*size+num2*lensingSize);
				else
					setValue(ggg.get(index).iX, marginX +order*lensingSize);
			}	
			else{
				ggg.get(index).iW.target(size);
				if (order<bX-num)
					setValue(ggg.get(index).iX, marginX +order*size);
				else if (order>bX+num){
					if (bX-num>=0)
						setValue(ggg.get(index).iX, marginX +(order-(num*2+1))*size+(num*2+1)*lensingSize);
					else{
						int num3= bX+num+1;
						if (num3>0)
							setValue(ggg.get(index).iX, marginX +(order-num3)*size+num3*lensingSize);
					}	
				}	
			}
			if (ggg.get(index).iX.value> lastX)
				lastX = ggg.get(index).iX.value;
			order++;
		}
		
		order = 0;
		for (Map.Entry<Integer, Integer> entry : leaderSortedMap.entrySet()) {
			int index = entry.getKey();
			if (bY-num<=order && order<=bY+num){
				ggg.get(index).iH.target(lensingSize);
				int num2 = order-(bY-num);
				if (bY-num>=0)
					setValue(ggg.get(index).iY, marginY +(bY-num)*size+num2*lensingSize);
				else
					setValue(ggg.get(index).iY, marginY +order*lensingSize);
			}	
			else{
				ggg.get(index).iH.target(size);
				if (order<bY-num)
					setValue(ggg.get(index).iY, marginY +order*size);
				else if (order>bY+num){
					if (bY-num>=0)
						setValue(ggg.get(index).iY, marginY +(order-(num*2+1))*size+(num*2+1)*lensingSize);
					else{
						int num3= bY+num+1;
						if (num3>0)
							setValue(ggg.get(index).iY, marginY +(order-num3)*size+num3*lensingSize);
					}	
				}	
			}	
			if (ggg.get(index).iY.value> lastY)
				lastY = ggg.get(index).iY.value;
			order++;
		}
		
		for (Map.Entry<Integer, Integer> entry : leaderSortedMap.entrySet()) {
			int index = entry.getKey();
			ggg.get(index).iH.update();
			ggg.get(index).iW.update();
			ggg.get(index).iX.update();
			ggg.get(index).iY.update();
		}
		
		// Draw gene names on X and Y axes
		order=0;
		for (Map.Entry<Integer, Integer> entry : leaderSortedMap.entrySet()) {
			int index = entry.getKey();
			int numE = locals[index].size();
			float ww = ggg.get(index).iW.value;
			String name = ggg.get(index).name;
			this.fill(0,80);
			float fontSize = PApplet.map(PApplet.sqrt(numE), 1, PApplet.sqrt(maxElement), 9, 12);
			if (bX-num<=order && order<=bX+num) 
				fontSize = PApplet.map(PApplet.sqrt(numE), 1, PApplet.sqrt(maxElement), 12, 14);
				
			if (locals[index].size()>1){
				name = locals[index].size()+" characters";
				this.fill(0);
			}	
			if (bGroup>=0 || sGroup>=0)
				this.fill(0,80);
			
			float xx =  ggg.get(index).iX.value;
			float yy =  ggg.get(index).iY.value;
			
			if (index==sGroup){
				this.fill(150,0,0);
				fontSize+=2;
			}	
			else if (index==bGroup){
				this.fill(255,100,0);
				fontSize+=1;
			}	
			this.textSize(fontSize);
			float num3 = locals[index].size();
			float step =1;
			if (locals[index].size()>ww+2){
				num3 = ww+2;
				step = (locals[index].size()-1f)/(ww+2);
			}
				
			
			if (ww>1){
				this.textAlign(PApplet.LEFT);
				float al = -PApplet.PI/3;
				this.translate(xx+ww,marginY-ww*2);
				this.rotate(al);
				this.text(name, 0,0);
				this.rotate(-al);
				this.translate(-(xx+ww), -(marginY-ww*2));
			}
			float hh =ggg.get(index).iH.value;
			if (hh>1){
				this.textAlign(PApplet.RIGHT);
				this.text(name, marginX, yy+hh/2+fontSize/3);
			}
			
			// Thumbnails *********************************
			//if (index == bGroup)
			//	System.out.println(name+"	"+	num3+ "	ww="+leaderSortedMap.size());
			if (ww>1)
				drawThumbnail(index, xx+ww/2, marginY-ww*2,ww, (int)num3, step);
			order++;
		}
		

		this.noStroke();
		for (Map.Entry<Integer, Integer> entryI : leaderSortedMap.entrySet()) {
			int indexI = entryI.getKey();
			// Check if this is grouping
			float yy =  ggg.get(indexI).iY.value;
			float hh = ggg.get(indexI).iH.value;
			
			int numEx = locals[indexI].size();
			
			for (Map.Entry<Integer, Integer> entryJ : leaderSortedMap.entrySet()) {
				int indexJ = entryJ.getKey();
				float xx =  ggg.get(indexJ).iX.value;
				float ww =ggg.get(indexJ).iW.value;
				
				// Draw background
				if (indexI!=indexJ && check3.s) {
					int numEy = locals[indexJ].size();
					int maxNumE = PApplet.max(numEx, numEy);
					float dense = PApplet.map(PApplet.sqrt(maxNumE), 1, PApplet.sqrt(maxElement), 1, 180);
					if (maxNumE==1)
						dense=0;
					this.fill(0,dense);
					this.noStroke();
					this.rect(xx, yy, ww, hh);
				}
				if (data[indexI][indexJ]>=slider.val){
					Color c = ColorScales.getColor(data[indexI][indexJ], colorScale, 1);
					this.fill(c.darker().getRGB());
					this.noStroke();
					this.rect(xx,yy, ww, hh);
				}
			}
		}
	}
	public void drawThumbnail(int leaderIndex, float x3, float y3, float w3, float num, float step) {
		this.noStroke();
		for (int o1=0;o1<num;o1++){
			int e1 = locals[leaderIndex].get((int)(o1*step));
			// Check if this is grouping
			float yy =  y3+o1;//ggg.get(e).iY.value;
			for (int o2=0;o2<o1;o2++){
				int e2 = locals[leaderIndex].get((int)(o2*step));
				float xx = x3+o2; //ggg.get(o2).iX.value;
				if (data[e1][e2]>= slider.val){
					Color c = ColorScales.getColor(data[e1][e2], colorScale, 1);
					this.fill(c.darker().getRGB());
					this.rect(xx,yy, 1, 1);
				}
			}
		} 
		
	}
		
	// Draw Second Matrix
	public void drawSecondMatrix(int maxElement, float xx) {
		if (sGroup>=0){
			float sizeS = locals[sGroup].size()*20;
			if (sizeS>matrixSize2)
				sizeS = matrixSize2;
			groupY.target(matrixY2 -150);
			groupY.update();
			
			if (bGroup>=0)
				drawElementList(bGroup, locals[bGroup],xx,groupY.value+sizeS*0.9f+120, matrixSize2, new Color(200,100,0));
			drawElementList(sGroup,locals[sGroup],xx,groupY.value, matrixSize2, new Color(150,0,0));
		
			// Draw rectangle selection
			sX = PApplet.min(sX1,sX2);
			sY = PApplet.min(sY1,sY2);
			sW = PApplet.abs(sX1-sX2);
			sH = PApplet.abs(sY1-sY2);
			if (this.mousePressed){
				this.noFill();
				this.stroke(0,200,0);
				this.rect(sX, sY, sW, sH);
			}
		}	
		else if (bGroup>=0){
			groupY.set(matrixY2);
			groupY.update();
			drawElementList(bGroup, locals[bGroup],xx,groupY.value, matrixSize2, new Color(200,100,0));
		}	
		
	}
	
	public String getString(String s) {
		if (s!=null)
			return s;
		else
			return "";
	}
		
	// Draw TaxaCharacterTable By CharacterList
	public void drawTaxaCharacterTableByCharacterList(ArrayList<Integer> a, float xx, float yy) {
		ArrayList<Integer> taxa1 = new ArrayList<Integer>();
		ArrayList<Integer> taxa2 = new ArrayList<Integer>();
		for (int o1=0;o1<a.size();o1++){
			int c = a.get(o1);
			
			Map<Integer, String> unsortMap = new HashMap<Integer, String>();
			for (int i=0;i<numTaxa;i++){
				if (charTable[i][c]!=null && !taxa1.contains(i)){
					taxa1.add(i);
					if (PopupSort.s==0)
						unsortMap.put(i, charTable[i][c]);
				}	
			}
			Map<Integer, String> sorted = Gene.sortByComparator4(unsortMap);
			for (Map.Entry<Integer,String> entry : sorted.entrySet()) {
				taxa2.add(entry.getKey());
			}
		}
		if (PopupSort.s==1){
			taxa2 = new ArrayList<Integer>();  // reset the taxa2 list
			Map<Integer, String> unsortMap = new HashMap<Integer, String>();
			for (int i=0;i<taxa1.size();i++){
				int index =  taxa1.get(i);
				unsortMap.put(index, getString(tribes[index]) + getString(genuses[index]) + getString(species[index]));
			}
			Map<Integer, String> sorted = Gene.sortByComparator4(unsortMap);
			for (Map.Entry<Integer,String> entry : sorted.entrySet()) {
				taxa2.add(entry.getKey());
			}
		}
		else if (PopupSort.s==2){
			taxa2 = new ArrayList<Integer>();  // reset the taxa2 list
			Map<Integer, String> unsortMap = new HashMap<Integer, String>();
			for (int i=0;i<taxa1.size();i++){
				int index =  taxa1.get(i);
				unsortMap.put(index,  getString(genuses[index]) + getString(tribes[index]) + getString(species[index]));
			}
			Map<Integer, String> sorted = Gene.sortByComparator4(unsortMap);
			for (Map.Entry<Integer,String> entry : sorted.entrySet()) {
				taxa2.add(entry.getKey());
			}
		}
		else if (PopupSort.s==3){
			taxa2 = new ArrayList<Integer>();  // reset the taxa2 list
			Map<Integer, String> unsortMap = new HashMap<Integer, String>();
			for (int i=0;i<taxa1.size();i++){
				int index =  taxa1.get(i);
				unsortMap.put(index, getString(species[index]) + getString(tribes[index]) + getString(genuses[index]));
			}
			Map<Integer, String> sorted = Gene.sortByComparator4(unsortMap);
			for (Map.Entry<Integer,String> entry : sorted.entrySet()) {
				taxa2.add(entry.getKey());
			}
		}
			
		
		float gapY =13;
		float gapCharacter = 25;
		float gapX = 80;
		
		// Draw background
		for (int i=0;i<taxa2.size();i++){
			float yy2 = yy+i*gapY+10;
			if (i%2==1)
				this.fill(250,150,100,150);
			else
				this.fill(220,220,220);
			this.noStroke();
			this.rect(xx-gapX*4-2, yy2-8, gapX*4+a.size()*gapCharacter+250,gapY-0.5f);
		}
		// Draw vertical separated lines
		float lastY =0;
		for (int o1=0;o1<a.size();o1++){
			float xx2 = xx+o1*gapCharacter+10; 
			int e1 = a.get(o1);
			
			// Print taxa-character
			this.textAlign(PApplet.LEFT);
			for (int i=0;i<taxa2.size();i++){
				int row = taxa2.get(i);
				float yy2 = yy+i*gapY+10;
				if (charTable[row][e1]!=null){
					lastY = yy2;
				}	
			}
			this.stroke(255,200);
			this.line(xx2,yy, xx2,lastY);
		}
		
		
		// Print taxa
		this.textAlign(PApplet.LEFT);
		for (int i=0;i<taxa2.size();i++){
			this.textSize(11);
			int row = taxa2.get(i);
			float yy2 = yy+i*gapY+11;
			
			this.fill(0);
			if (families[row]!=null)
				this.text(families[row], xx-gapX*4,yy2);
			if (tribes[row]!=null)
				this.text(tribes[row],   xx-gapX*3,yy2);
			if (genuses[row]!=null)
				this.text(genuses[row], xx-gapX*2,yy2);
			if (species[row]!=null)
				this.text(species[row], xx-gapX*1,yy2);
			// Draw variable names
			if (i==0){
				this.textSize(12);
				this.text("Number of Taxa = "+taxa2.size(), xx-gapX*3,yy2-35);
				this.text("FAMILY", xx-gapX*4,yy2-12);
				this.text("TRIBE", xx-gapX*3,yy2-12);
				this.text("GENUS", xx-gapX*2,yy2-12);
				this.text("SPECIES", xx-gapX,yy2-12);
			}
		}
		
		// Print chararters
		this.fill(0,220);
		this.textSize(11);
		for (int o1=0;o1<a.size();o1++){
			float xx2 = xx+o1*gapCharacter+11; 
			int e1 = a.get(o1);
			String name = ggg.get(e1).name;
			this.textAlign(PApplet.LEFT);
			float al = -PApplet.PI/4;
			this.translate(xx2+gapCharacter/10,yy);
			this.rotate(al);
			this.text(name, 0,0);
			this.rotate(-al);
			this.translate(-(xx2+gapCharacter/10), -(yy));
			
			// Print taxa-character
			this.textAlign(PApplet.LEFT);
			for (int i=0;i<taxa2.size();i++){
				int row = taxa2.get(i);
				float yy2 = yy+i*gapY+11;
				if (charTable[row][e1]!=null){
					this.text(charTable[row][e1], xx2,yy2);
				}	
			}
		}
	}
	
	// Draw TaxaCharacterTable By TaxaList
	public ArrayList<Integer> drawTaxaCharacterTableByTaxaList(ArrayList<Integer> b, float xx, float yy) {
		ArrayList<Integer> c1 = new ArrayList<Integer>();
		for (int o1=0;o1<b.size();o1++){
			int index = b.get(o1);
			
			Map<Integer, String> unsortMap = new HashMap<Integer, String>();
			for (int c=0;c<numChars;c++){
				if (charTable[index][c]!=null && !c1.contains(c)){
					c1.add(c);
				}	
			}
		}
		Map<Integer, String> unsortMap = new HashMap<Integer, String>();
		for (int o1=0;o1<b.size();o1++){
			int index = b.get(o1);
			unsortMap.put(index, getString(tribes[index]) + getString(genuses[index]) + getString(species[index]));
		}
		Map<Integer, String> sorted = Gene.sortByComparator4(unsortMap);
		ArrayList<Integer> b2 = new ArrayList<Integer>();
		for (Map.Entry<Integer,String> entry : sorted.entrySet()) {
			b2.add(entry.getKey());
		}
		
			
		
		float gapY =13;
		float gapCharacter = 25;
		float gapX = 80;
		
		// Draw background
		for (int i=0;i<b2.size();i++){
			float yy2 = yy+i*gapY+10;
			if (i%2==1)
				this.fill(250,150,100,150);
			else
				this.fill(220,220,220);
			this.noStroke();
			this.rect(xx-gapX*4-2, yy2-8, gapX*4+c1.size()*gapCharacter+250,gapY-0.5f);
		}
		// Draw vertical separated lines
		float lastY =0;
		for (int o1=0;o1<c1.size();o1++){
			float xx2 = xx+o1*gapCharacter+10; 
			int e1 = c1.get(o1);
			
			// Print taxa-character
			this.textAlign(PApplet.LEFT);
			for (int i=0;i<b2.size();i++){
				int row = b2.get(i);
				float yy2 = yy+i*gapY+10;
				if (charTable[row][e1]!=null){
					lastY = yy2;
				}	
			}
			this.stroke(255,200);
			this.line(xx2,yy, xx2,lastY);
		}
		
		
		// Print taxa
		this.textAlign(PApplet.LEFT);
		for (int i=0;i<b2.size();i++){
			this.textSize(11);
			int row = b2.get(i);
			float yy2 = yy+i*gapY+11;
			
			this.fill(0);
			if (families[row]!=null)
				this.text(families[row], xx-gapX*4,yy2);
			if (tribes[row]!=null)
				this.text(tribes[row],   xx-gapX*3,yy2);
			if (genuses[row]!=null)
				this.text(genuses[row], xx-gapX*2,yy2);
			if (species[row]!=null)
				this.text(species[row], xx-gapX*1,yy2);
			// Draw variable names
			if (i==0){
				this.textSize(12);
				this.text("Number of Taxa = "+b2.size(), xx-gapX*3,yy2-35);
				this.text("FAMILY", xx-gapX*4,yy2-12);
				this.text("TRIBE", xx-gapX*3,yy2-12);
				this.text("GENUS", xx-gapX*2,yy2-12);
				this.text("SPECIES", xx-gapX,yy2-12);
			}
		}
		
		// Print chararters
		this.fill(0,220);
		this.textSize(11);
		for (int o1=0;o1<c1.size();o1++){
			float xx2 = xx+o1*gapCharacter+11; 
			int e1 = c1.get(o1);
			String name = ggg.get(e1).name;
			this.textAlign(PApplet.LEFT);
			float al = -PApplet.PI/4;
			this.translate(xx2+gapCharacter/10,yy);
			this.rotate(al);
			this.text(name, 0,0);
			this.rotate(-al);
			this.translate(-(xx2+gapCharacter/10), -(yy));
			
			// Print taxa-character
			this.textAlign(PApplet.LEFT);
			for (int i=0;i<b2.size();i++){
				int row = b2.get(i);
				float yy2 = yy+i*gapY+11;
				if (charTable[row][e1]!=null){
					this.text(charTable[row][e1], xx2,yy2);
				}	
			}
		}
		return c1;
	}
	
	
	public void drawElementList(int leaderIndex, ArrayList<Integer> a, float xx2, float yy2, float matrixSize2, Color color){
		this.fill(color.getRGB());
		this.textAlign(PApplet.RIGHT);
		this.textSize(12);
		int lNumE = a.size();
		if (lNumE>0)
		this.text(lNumE+" characters in this group", xx2, yy2-22);
		
		this.noStroke();
		float gap = matrixSize2/a.size();
		if (gap>15)
			gap=15;
		for (int o1=0;o1<a.size();o1++){
			int e1 = a.get(o1);
			// Check if this is grouping
			float yy =  yy2+o1*gap;//ggg.get(e).iY.value;
			for (int o2=0;o2<a.size();o2++){
				int e2 = a.get(o2);
				float xx = xx2+o2*gap; //ggg.get(o2).iX.value;
				if (data[e1][e2]>= slider.val){
					Color c = ColorScales.getColor(data[e1][e2], colorScale, 1);
					this.noStroke();
					this.fill(c.darker().getRGB());
					this.rect(xx,yy, gap*0.97f, gap*0.97f);
				}
			}
			
			// Draw characters on X and Y axis
			float fontSize = PApplet.map(gap, 0, 15, 5, 10);
			this.fill(0);
			if (e1==leaderIndex)
				this.fill(color.getRGB());
			if (ggg.get(e1).isSelected)
				this.fill(0,120,0);
			
			this.textSize(fontSize);
			if (gap>1){
				float xx = xx2+o1*gap; //ggg.get(o2).iX.value;
				String name = ggg.get(e1).name;
				this.textAlign(PApplet.LEFT);
				float al = -PApplet.PI/4;
				this.translate(xx+gap/3,yy2);
				this.rotate(al);
				this.text(name, 0,0);
				this.rotate(-al);
				this.translate(-(xx+gap/3), -(yy2));
				if (leaderIndex!= e1 && leaderIndex== sGroup &&isInReactangle(xx+gap/3,yy2)) 
					ggg.get(e1).isSelected = true;
				this.textAlign(PApplet.RIGHT);
				this.text(name, xx2-1, yy+gap*0.7f);
				if (leaderIndex!= e1 && leaderIndex== sGroup &&isInReactangle(xx2-10,yy+gap*0.7f)) 
					ggg.get(e1).isSelected = true;
			}
		}
	}
	public boolean isInReactangle(float x_, float y_){
		if (sX<=x_ && x_<=sX+sW && sY<=y_ && y_<=sY+sH){
			return true;
		}
		return false;
	}
			
		
	public void drawGenes() {
		// Compute lensing
		if (check1.s){
			bX = (int) ((mouseX-marginX)/size);
			bY = (int) ((mouseY-marginY)/size);
		}
		else{
			bX = ggg.size()+10;
			bY = ggg.size()+10;
		}
		float lensingSize = PApplet.map(size, 0, 100, 11, 50);	
		
		int num = 8; // Number of items in one side of lensing
		for (int i=0;i<ggg.size();i++){
			int order = ggg.get(i).order;
			if (bX-num<=order && order<=bX+num) {
				ggg.get(i).iW.target(lensingSize);
				int num2 = order-(bX-num); 
				if (bX-num>=0)
					setValue(ggg.get(i).iX, marginX +(bX-num)*size+num2*lensingSize);
				else
					setValue(ggg.get(i).iX, marginX +order*lensingSize);
			}	
			else{
				ggg.get(i).iW.target(size);
				if (order<bX-num)
					setValue(ggg.get(i).iX, marginX +order*size);
				else if (order>bX+num){
					if (bX-num>=0)
						setValue(ggg.get(i).iX, marginX +(order-(num*2+1))*size+(num*2+1)*lensingSize);
					else{
						int num3= bX+num+1;
						if (num3>0)
							setValue(ggg.get(i).iX, marginX +(order-num3)*size+num3*lensingSize);
						else
							setValue(ggg.get(i).iX, marginX +order*size);
					}	
				}	
			}	
			if (ggg.get(i).iX.value> lastX)
				lastX = ggg.get(i).iX.value;
			
		}
		for (int j=0;j<ggg.size();j++){
			int order = ggg.get(j).order;
			if (bY-num<=order && order<=bY+num){
				ggg.get(j).iH.target(lensingSize);
				int num2 = order-(bY-num);
				if (bY-num>=0)
					setValue(ggg.get(j).iY, marginY +(bY-num)*size+num2*lensingSize);
				else
					setValue(ggg.get(j).iY, marginY +order*lensingSize);
			}	
			else{
				ggg.get(j).iH.target(size);
				if (order<bY-num)
					setValue(ggg.get(j).iY, marginY +order*size);
				else if (order>bY+num){
					if (bY-num>=0)
						setValue(ggg.get(j).iY, marginY +(order-(num*2+1))*size+(num*2+1)*lensingSize);
					else{
						int num3= bY+num+1;
						if (num3>0)
							setValue(ggg.get(j).iY, marginY +(order-num3)*size+num3*lensingSize);
						else
							setValue(ggg.get(j).iY, marginY +order*size);
					}	
					
				}	
			}	
			if (ggg.get(j).iY.value> lastY)
				lastY = ggg.get(j).iY.value;
		}
		
		for (int i=0;i<ggg.size();i++){
			ggg.get(i).iH.update();
			ggg.get(i).iW.update();
			ggg.get(i).iX.update();
			ggg.get(i).iY.update();
		}
		
		// Draw gene names on X and Y axes
		this.fill(0);
		this.textSize(10);
		
		for (int i=0;i<ggg.size();i++){
			float ww = ggg.get(i).iW.value;
			if (ww>8){
				float xx =  ggg.get(i).iX.value;
				this.textAlign(PApplet.LEFT);
				float al = -PApplet.PI/2;
				//this.translate(xx+ww/2,marginY-2);
				this.rotate(al);
				this.text(ggg.get(i).name, -marginY,xx);
				this.rotate(-al);
				//this.translate(-(xx+ww/2), -(marginY-2));
			}
		}	
		for (int i=0;i<ggg.size();i++){
			float hh =ggg.get(i).iH.value;
			if (hh>5){
				float yy =  ggg.get(i).iY.value;
				this.textAlign(PApplet.RIGHT);
				this.text(ggg.get(i).name, marginX-2, yy+hh/2);
			}
		}
		
		this.noStroke();
		for (int i=0;i<ggg.size();i++){
			// Check if this is grouping
			float yy =  ggg.get(i).iY.value;
			float hh = ggg.get(i).iH.value;
			for (int j=0;j<ggg.size();j++){
				float xx =  ggg.get(j).iX.value;
				float ww =ggg.get(j).iW.value;
				if (data[i][j]>= slider.val){
					Color c = ColorScales.getColor(data[i][j], colorScale, 1);
					this.noStroke();
					this.fill(c.darker().getRGB());
					this.rect(xx,yy, ww, hh);
				}
			}
		}
	}	
	
	
	
	public void setValue(Integrator inter, float value) {
		if (ggg.size()<3000){
			inter.target(value);
		}
		else{
			inter.set(value);
		}
	}
				
	public void mousePressed() {
		if (sliderDissimilarity.bSlider>=0)
				sliderDissimilarity.checkSelectedSlider1();
		if (slider.bSlider>=0)
			slider.checkSelectedSlider1();
		if (sGroup>=0){
			sX1 = mouseX;
			sY1 = mouseY;
			sX2 = mouseX;
			sY2 = mouseY;
		}
	}
	public void mouseReleased() {
		if (sliderDissimilarity.sSlider>=0)
			sliderDissimilarity.checkSelectedSlider2();
		if (slider.sSlider>=0)
			slider.checkSelectedSlider2();
		if (sGroup>=0){
			sX1 = -10;
			sY1 = -10;
			sX2 = -10;
			sY2 = -10;
		}
	}
	public void mouseDragged() {
		if (sliderDissimilarity.bSlider>=0)
			sliderDissimilarity.checkSelectedSlider3();
		if (slider.bSlider>=0)
			slider.checkSelectedSlider3();
		if (sGroup>=0){
			sX2 = mouseX;
			sY2 = mouseY;
		}
	}
		
	public void mouseMoved() {
		isBrushing = false;
		if (PopupMenu.s==1 && stateAnimation==1 && !this.mousePressed){
			bGroup = -1;
			for (Map.Entry<Integer, Integer> entryI : leaderSortedMap.entrySet()) {
				int index = entryI.getKey();
				// Check if this is grouping
				float xx =  ggg.get(index).iX.value;
				float yy =  ggg.get(index).iY.value;
				float ww =  ggg.get(index).iW.value;
				float hh =ggg.get(index).iH.value;
				String name = ggg.get(index).name;
				int numE = locals[index].size();
				
				// Draw genes in compound
				float wid = 20+this.textWidth(name);
				if (numE>=1 && marginX-wid<=mouseX && mouseX<=marginX && yy<=mouseY && mouseY<=yy+hh){
					bGroup = index;
				}
				else if (numE>=1 && xx<=mouseX && mouseX<=xx+ww && marginY-wid<=mouseY && mouseY<=marginY){
					bGroup = index;
				}
			}
		}
		else if (PopupMenu.s==2){
			String s = "";
			sTribes = new ArrayList<String>();
			sGenuses = new ArrayList<String>();
			sSpecies = new ArrayList<String>();
				
			for (int i1=0;i1<listTribes.size();i1++){
				if (xx1[i1]-80<=mouseX && mouseX<=xx1[i1] &&
						yy1[i1]-10<=mouseY && mouseY<=yy1[i1]+20){
					s = listTribes.get(i1);
					sTribes.add(s);
					
					for (int k=0;k<tribes.length;k++){
						if (tribes[k]!=null && tribes[k].equals(s)){
							if (genuses[k]!=null  && !sGenuses.contains(genuses[k]))
								sGenuses.add(genuses[k]);
							if (species[k]!=null  && !sSpecies.contains(species[k]))
								sSpecies.add(species[k]);
						}
					}
					
				}	
			}
			for (int i1=0;i1<listGenuses.size();i1++){
				if (xx2[i1]-40<=mouseX && mouseX<=xx2[i1]+40 &&
						yy2[i1]-6<=mouseY && mouseY<=yy2[i1]+1){
					s = listGenuses.get(i1);
					sGenuses.add(s);
					
					for (int k=0;k<tribes.length;k++){
						if (genuses[k]!=null && genuses[k].equals(s)){
							if (tribes[k]!=null  && !sTribes.contains(tribes[k]))
								sTribes.add(tribes[k]);
							if (species[k]!=null  && !sSpecies.contains(species[k]))
								sSpecies.add(species[k]);
						}
					}
					
				}	
			}
			
			for (int i1=0;i1<listSpecies.size();i1++){
				if (xx3[i1]<=mouseX && mouseX<=xx3[i1]+80 &&
						yy3[i1]-1<=mouseY && mouseY<=yy3[i1]+0.5f){
					s = listSpecies.get(i1);
					sSpecies.add(s);
					
					for (int k=0;k<tribes.length;k++){
						if (species[k]!=null && species[k].equals(s)){
							if (tribes[k]!=null  && !sTribes.contains(tribes[k]))
								sTribes.add(tribes[k]);
							if (genuses[k]!=null  && !sGenuses.contains(genuses[k]))
								sGenuses.add(genuses[k]);
						}
					}
					
				}	
			}
			if (sTribes.size()>0 || sGenuses.size()>0 || sSpecies.size()>0)
				isBrushing = true;
			
			//System.out.println("sTribes="+sTribes);
			//System.out.println("sGenuses="+sGenuses);
			//System.out.println(sSpecies.size()+ " sSpecies="+sSpecies);
			
		}
	}
		
	public void mouseClicked() {
		if (buttonBrowse.b>=0){
			thread4=new Thread(loader4);
			thread4.start();
		}
		else if (popupOrder.b>=0){
			popupOrder.mouseClicked();
		}
		else if (popupSort.b>=0){
			popupSort.mouseClicked();
		}
		else if (popupMenu.b>=0){
			popupMenu.mouseClicked();
			if (popupMenu.s==1){  
				thread5.stop();
				thread5 = new Thread(loader5);
				thread5.start();
				
			}	
			else {
				main.SimilarityMatrix_1_4.stateAnimation=0;
				Gene.orderByReadingOrder();
			}	
		}
		else if (check1.b){
			check1.mouseClicked();
		}
		else if (check3.b){
			check3.mouseClicked();
		}
		else if (bGroup>=0){
			sGroup = bGroup;
			bGroup = -77;
		}
		else{
			sGroup =-100;
			// Release the retangle selection
			for (int i=0; i<ggg.size(); i++){
				ggg.get(i).isSelected = false;
			}
		}
		
	}
		
	
	public String loadFile (Frame f, String title, String defDir, String fileType) {
		  FileDialog fd = new FileDialog(f, title, FileDialog.LOAD);
		  fd.setFile(fileType);
		  fd.setDirectory(defDir);
		  fd.setLocation(50, 50);
		  fd.show();
		  String path = fd.getDirectory()+fd.getFile();
	      return path;
	}
	
	
	public void keyPressed() {
		if (this.key == 'q' || this.key == 'Q'){
			Gene.reveseGenesForDrawing();
		}
		else if (this.key == 'w' || this.key == 'W'){
			Gene.swapGenesForDrawing();
		}
		else if (this.key == 'l' || this.key == 'L'){
			check1.s = !check1.s;
		}
		else if (this.key == '=' || this.key == '+'){  // Join 2 groups
			if (bGroup>=0 && sGroup>=0 && bGroup!=sGroup){   // Make sure 2 groups are different
				for (int i = 0; i<locals[bGroup].size();i++){
					int e = locals[bGroup].get(i);
					locals[sGroup].add(e);
				}
				locals[bGroup] = new ArrayList<Integer>();
				leaderSortedMap.remove(bGroup);
				bGroup = -1;
			}       
		}
		else if (this.key == '-' || this.key == '_'){  // DisJoin 2 groups
			if (sGroup>=0){
				ArrayList<Integer> a = new ArrayList<Integer>();
				for (int i = 0; i<locals[sGroup].size();i++){
					int e = locals[sGroup].get(i);
					if (e==sGroup) continue;     // can not remove the leader
					if (ggg.get(e).isSelected){
						locals[sGroup].remove(i);
						a.add(e);
						i--;
					}	
				}
				// Create a new group
				if (a.size()==0) {
					System.out.println("********ERROR******** a.size()="+a.size());
					return;
				}
				int first = a.get(0);
				if (locals[first].size()>0){
					System.out.println("********ERROR******** locals[first].size()="+locals[first].size());
					return;
				}
				for (int i = 0; i<a.size();i++){
					int e = a.get(i);
					locals[first].add(e);
				}
				leaderSortedMap.put(first, leaderSortedMap.size());	
				
				// Release the retangle selection
				for (int i=0; i<ggg.size(); i++){
					ggg.get(i).isSelected = false;
				}
				
			}
		}	
			
	}
	
	
	class ThreadLoader1 implements Runnable {
		PApplet parent;
		public ThreadLoader1(PApplet parent_) {
			parent = parent_;
		}
		
		@SuppressWarnings("unchecked")
		public void run() {
			isAllowedDrawing =  false;
			
			ggg = new ArrayList<Gene>();
			leaderSortedMap = null;
			
			
			String[] lines = parent.loadStrings(currentFile);
			System.out.println();
			System.out.println(lines.length);
			System.out.println("***************** Load data: "+currentFile+" ***************************");
			data = new float[lines.length-1][lines.length-1];
			for (int i=0; i< lines.length;i++){
				String[] ps = lines[i].split(",");
				if (i==0){
					for (int j=1; j< ps.length;j++){
						ggg.add(new Gene(ps[j],ggg.size()));
					}
				}
				else{
					int indexY =i-1;
					for (int j=1; j< ps.length;j++){
						int indexX =j-1;
						float value = Float.parseFloat(ps[j]); 
						
						if (value>0){
							String name1 = ggg.get(indexX).name;
							String name2 = ggg.get(indexY).name;
							message = (name1+"\t"+name2);
							// Store results for visualization
							data[indexX][indexY] = value;
						}	
				 	}
				}
			}
			
			float[][] data1 = new float[data.length][data[0].length] ;
			for (int i=0;i<data.length;i++){
				for (int j=0; j< data[0].length;j++){
					data1[i][j] = data[i][j];
				}
			}
				
			/*
			int numChars =20000;
			data = new float[numChars][numChars];
			ggg = new ArrayList<Gene>();
			for (int i=0; i< numChars;i++){
				if (i==0){
					for (int j=1; j< numChars;j++){
						ggg.add(new Gene("Char"+j,ggg.size()));
					}
				}
				for (int j=0; j< numChars;j++){
					int index1 = i%data1.length;
					int index2 = j%data1.length;
					data[i][j] = data1[index1][index2];
					//System.out.println(" index1 = "+index1+" index2="+index2+"	"+data[i][j]);
				}
			}*/
			stateAnimation=0;
			isAllowedDrawing =  true;  //******************* Start drawing **************
			
			// Compute the summary for each Gene
			PopupOrder.s=2;
			
			// Group at the beginning 
			thread5.stop();
			thread5 = new Thread(loader5);
			thread5.start();
			//System.out.println(message);
			
			
			// Read the hierarchy		**********************************************************************
			lines = parent.loadStrings("./HongData/MatrixTaxaChararters.csv");
			System.out.println();
			System.out.println("***************** Load data: "+"MatrixTaxaChararters.csv"+" ***************************");
			numTaxa = lines.length-1;
			numChars = ggg.size();
			int numChar2 = lines[0].split(",").length-1;
			if (numChars!=numChar2)
				System.out.println("Different numbers of chararters in the two input files: "+numChars+"	"+numChar2);
			
			charTable = new String[lines.length-1][numChars];
			taxonomyStrings = new String[lines.length-1];
			families = new String[lines.length-1];
			tribes = new String[lines.length-1];
			genuses = new String[lines.length-1];
			species = new String[lines.length-1];
			extraStrings = new ArrayList[lines.length-1];
			for (int i=1; i<lines.length;i++){
				String[] pieces = lines[i].split(",");
				int numExtraCols =  pieces.length-numChar2;
				//	System.out.println(pieces.length+"	pieces[0]="+pieces[0]+"	pieces[1]="+pieces[1]+"	pieces[2]="+pieces[2]+"	***numExtraCols="+numExtraCols);
				for (int j=numExtraCols;j<pieces.length;j++){
					if (pieces[j].equals("") || pieces[j].equals("\"\""))
						;//charTable[i-1][j-numExtraCols] = null;
					else	
						charTable[i-1][j-numExtraCols] = pieces[j].replace("\"", "").trim();
				}
				// Read hierarchy ******************************************************
				taxonomyStrings[i-1] = pieces[0].replace("\"", "");
				String[] pieces2 =  pieces[0].replace("\"", "").split(";");
				for (int k=0;k<pieces2.length;k++){
					if (pieces2[k].contains("FAMILY="))
						families[i-1] = pieces2[k].replace("FAMILY=", "").trim().toLowerCase();
					else if (pieces2[k].contains("TRIBE="))
						tribes[i-1] = pieces2[k].replace("TRIBE=", "").trim().toLowerCase();
					else if (pieces2[k].contains("GENUS="))
						genuses[i-1] = pieces2[k].replace("GENUS=", "").trim().toLowerCase();
					else if (pieces2[k].contains("SPECIES="))
						species[i-1] = pieces2[k].replace("SPECIES=", "".trim().toLowerCase());
				}
				//
				extraStrings[i-1] = new ArrayList<String>();
				for (int k=1;k<numExtraCols;k++){
					extraStrings[i-1].add(pieces[k]);
				}
			}
			
			// ********* Drawing relationships of taxa hierarchy
			
			
			for (int i=0;i<tribes.length;i++){
				if (tribes[i]!=null && !listTribes.contains(tribes[i]))	
					listTribes.add(tribes[i]);
			}
			listTribes = orderAlphabetically(listTribes);
			

			// Order second column
			for (int i=0;i<genuses.length;i++){
				if (genuses[i]!=null && !listGenuses.contains(genuses[i]))	
					listGenuses.add(genuses[i]);
			}
			Map<String, Integer> unsortMap = new HashMap<String, Integer>();
			ArrayList<String> temList = new ArrayList<String>();
			for (int i=0;i<listGenuses.size();i++){
				int count1 = 0;
				for (int k=0;k<tribes.length;k++){
					if (tribes[k]!=null && genuses[k]!=null && genuses[k].equals(listGenuses.get(i)) && !temList.contains(tribes[k]+genuses[k])){	
						int index1 = listTribes.indexOf(tribes[k]);
						count1+=index1;
						temList.add(tribes[k]+genuses[k]);
						tribeGenusMap.put(genuses[k], index1);
					}	
				}
				unsortMap.put(listGenuses.get(i), count1);
			}
			listGenuses = new ArrayList<String>();
			Map<String,Integer> sorted = Gene.sortByComparator(unsortMap);
			for (Map.Entry<String,Integer> entry : sorted.entrySet()) {
				listGenuses.add(entry.getKey());
			}
			
			// Order last column
			for (int i=0;i<species.length;i++){
				if (species[i]!=null && !listSpecies.contains(species[i]))	
					listSpecies.add(species[i]);
			}
			unsortMap = new HashMap<String, Integer>();
			temList = new ArrayList<String>();
			for (int i=0;i<listSpecies.size();i++){
				int count1 = 0;
				int count2 = 0;
				for (int k=0;k<species.length;k++){
					if (genuses[k]!=null && species[k]!=null && species[k].equals(listSpecies.get(i)) && !temList.contains(genuses[k]+species[k])){	
						int index1 = listGenuses.indexOf(genuses[k]);
						count1+=index1;
						count2++;
						temList.add(genuses[k]+species[k]);
					}	
				}
				unsortMap.put(listSpecies.get(i), count1/count2);
			}
			listSpecies = new ArrayList<String>();
			sorted = Gene.sortByComparator(unsortMap);
			for (Map.Entry<String,Integer> entry : sorted.entrySet()) {
				listSpecies.add(entry.getKey());
			}
			
			// Relationships
			r1 = new int[listTribes.size()][listGenuses.size()];
			r2 = new int[listGenuses.size()][listSpecies.size()];
			for (int i=0;i<tribes.length;i++){
				if (tribes[i]!=null && genuses[i]!=null){	
					int index1 = listTribes.indexOf(tribes[i]);
					int index2 = listGenuses.indexOf(genuses[i]);
					//System.out.println(tribes[i]+"	"+index1+"	"+genuses[i]+"	"+index2);
					r1[index1][index2]++;
				}	
			}
			
			// Relationships
			for (int i=0;i<genuses.length;i++){
				if (genuses[i]!=null && species[i]!=null){	
					int index1 = listGenuses.indexOf(genuses[i]);
					int index2 = listSpecies.indexOf(species[i]);
					r2[index1][index2]++;
				}	
			}
			xx1 = new float[listTribes.size()];
			yy1 = new float[listTribes.size()];
			xx2 = new float[listGenuses.size()];
			yy2 = new float[listGenuses.size()];
			xx3 = new float[listSpecies.size()];
			yy3 = new float[listSpecies.size()];
		
			/*
			listGenusImages = new ArrayList[listGenuses.size()];
			for(int i=0; i<listGenuses.size();i++){
				listGenusImages[i] = getImages(listGenuses.get(i));
			}*/
		}
	}
	 
	// Sort Reactions by score (average positions of proteins)
	public static Map<Integer, Float> sortByComparator2(Map<Integer, Float> unsortMap, boolean decreasing) {
		// Convert Map to List
		List<Map.Entry<Integer, Float>> list = 
			new LinkedList<Map.Entry<Integer, Float>>(unsortMap.entrySet());
 
		// Sort list with comparator, to compare the Map values
		if (decreasing){
			Collections.sort(list, new Comparator<Map.Entry<Integer, Float>>() {
				public int compare(Map.Entry<Integer, Float> o1,
	                                           Map.Entry<Integer, Float> o2) {
						return -(o1.getValue()).compareTo(o2.getValue());
				}
			});
		}
		else{
			Collections.sort(list, new Comparator<Map.Entry<Integer, Float>>() {
				public int compare(Map.Entry<Integer, Float> o1,
	                                           Map.Entry<Integer, Float> o2) {
						return (o1.getValue()).compareTo(o2.getValue());
				}
			});
		}
 
		// Convert sorted map back to a Map
		Map<Integer, Float> sortedMap = new LinkedHashMap<Integer, Float>();
		for (Iterator<Map.Entry<Integer, Float>> it = list.iterator(); it.hasNext();) {
			Map.Entry<Integer, Float> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	// This function returns all the files in a directory as an array of Strings
	public  ArrayList<String> listFileNames(String dir, String imgType) {
		File file = new File(dir);
		ArrayList<String> a = new ArrayList<String>();
		if (file.isDirectory()) { // Do
			String names[] = file.list();
			for (int i = 0; i < names.length; i++) {
				ArrayList<String> b = listFileNames(dir + "/" + names[i], imgType);
				for (int j = 0; j < b.size(); j++) {
					a.add(b.get(j));	
				}
				
			}
		} else if (dir.endsWith(imgType)) {
			a.add(dir);
		}
		return a;
	}
	
	public int getProteinOrderByName(String name) {
		for (int i=0;i<ggg.size();i++){
			if (ggg.get(i).name.equals(name))
				return i;
		}
		return -1;
	}
	
	
	
	// Thread for grouping
	class ThreadLoader4 implements Runnable {
		PApplet parent;
		public ThreadLoader4(PApplet p) {
			parent = p;
		}
		public void run() {
			String fileName =  loadFile(new Frame(), "Open your file", "..", ".txt");
			if (fileName.equals("..null"))
				return;
			else{
				currentFile = fileName;
				//VEN DIAGRAM
				thread1=new Thread(loader1);
				thread1.start();
			}
		}
	}	
	
	// Thread for grouping
	class ThreadLoader2 implements Runnable {
		PApplet parent;
		public ThreadLoader2(PApplet p) {
			parent = p;
		}
		public void run() {
			Gene.orderBySimilarity();
		}
	}	
	
	// Thread for grouping
	class ThreadLoader5 implements Runnable {
		PApplet parent;
		public ThreadLoader5(PApplet p) {
			parent = p;
		}
		public void run() {
			System.out.println("---------ThreadLoader5");
			Gene.orderByReadingOrder();
			System.out.println("---------ThreadLoader51");
			Gene.groupBySimilarity(sliderDissimilarity.val);
			System.out.println("---------ThreadLoader52");
			
		}
	}	
	void mouseWheel(int delta) {
		if (this.keyPressed)
			marginY += delta;
		else 	
			marginX += delta;
	}

	
	
}
