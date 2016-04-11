package main;
import java.awt.Color;
import java.text.DecimalFormat;

import processing.core.PApplet;
import processing.core.PFont;

public class PopupOrder{
	public int b = -1;
	public static int s=-1;
	public MultiLayerMatrix_2 parent;
	public float x = 800;
	public int y = 0;
	public int w1 = 88;
	public int w = 200;
	public int h;
	public int itemH = 16;
	public Color cGray  = new Color(240,240,240);
	public static String[] items={"Sum similarity","Reading order", "Name", "Similarity"}; 
	
	public PopupOrder(MultiLayerMatrix_2 parent_){
		parent = parent_;
	}
	
	public void draw(float x_){
		x = x_;
		this.checkBrushing();
		if (main.MultiLayerMatrix_2.popupSort.b>=0)
			b=-2;
		
		if (b>=0){
			parent.fill(100);
			parent.stroke(0);
			parent.textSize(11);
			h=items.length*itemH+15;
			parent.fill(200);
			parent.stroke(0,150);
			parent.rect(x, y+20, w,h);
			// Max number of relations
			for (int i=0;i<items.length;i++){
				if (i==s){
					parent.noStroke();
					parent.fill(0);
					parent.rect(x+10,y+itemH*(i)+3+25,w-25,itemH+1);
					parent.fill(255,255,0);
				}
				else if (i==b){
					parent.fill(200,0,0);
				}
				else{
					parent.fill(0);
				}
				parent.textAlign(PApplet.LEFT);
				parent.text(items[i],x+20,y+itemH*(i+1)+25);  // 
			}	
		}
		parent.fill(180);
		parent.noStroke();
		parent.rect(x,y,w1,20);
		parent.fill(0);
		parent.textAlign(PApplet.CENTER);
		parent.textSize(11);
		parent.text("Order by",x+w1/2,y+15);
		
	}
	
	 public void mouseClicked() {
		if (b<items.length){
			s = b;
			if (items[s].equals("Sum similarity")) { 
				main.MultiLayerMatrix_2.stateAnimation=0;
				Gene.orderBySumSimilarity(parent);
			}	
			else if (items[s].equals("Reading order"))  {
				main.MultiLayerMatrix_2.stateAnimation=0;
				Gene.orderByReadingOrder();
			}	
			else if (items[s].equals("Name"))  {
				main.MultiLayerMatrix_2.stateAnimation=0;
				Gene.orderByName();
			}	
			else if (items[s].equals("Similarity"))  {
				parent.thread2 = new Thread(parent.loader2);
				parent.thread2.start();
			}	
			
		}
	}
	 
	public void checkBrushing() {
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		if (b==-1){
			if (x<mX && mX<x+w1 && y<=mY && mY<=itemH+5){
				b =100;
				return;
			}	
		}
		else{
			for (int i=0; i<items.length; i++){
				if (x<=mX && mX<=x+w && y+itemH*i+20<=mY && mY<=y+itemH*(i+1)+6+20){
					b =i;
					return;
				}	
			}
			if (x<=mX && mX<=x+w && y<=mY && mY<=y+h)
				return;
		}
		b =-1;
	}
	
	
	
}