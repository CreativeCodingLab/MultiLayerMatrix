package main;

import java.awt.Color;
import java.text.DecimalFormat;

import processing.core.PApplet;

public class Slider{
	public int pair =-1;
	public PApplet parent;
	public float x=0,y=0;
	public int w; 
	public int u =-1;
	public float val =-1;
	
	
	public int bSlider = -1;
	public int sSlider = -1;
	public int ggg =10;
	
	
	public Slider(PApplet parent_, float initial){
		parent = parent_;
		w= ggg*10;
		update();
		u = (int) (initial*ggg);
		update();
	}
		
	public void update(){
		val = (float) u/(ggg*10);
	}
		
	public void draw(float x_, float y_){
		x = x_;
		y = y_;
		checkBrushingSlider();
		
		float xx2 = x+u;
		DecimalFormat df = new DecimalFormat("#.##");
		parent.stroke(0);
		parent.noFill();
		parent.rect(x,y,101,21);
		parent.noStroke();
		for (int k=5; k<100; k++ ){
			float v = (float) k/100;
			Color c = ColorScales.getColor(v, main.MultiLayerMatrix_2.colorScale, 1);
			if (v>=val)
				parent.fill(c.getRGB());
			else
				parent.fill(c.getRed(),c.getGreen(),c.getBlue(),15);
			parent.rect(x+k, y+1, 2, 20);
		}
	
		//Upper range
		Color c = new Color(0,0,0);
		if (sSlider==1){
			c= ColorScales.getColor(val, main.MultiLayerMatrix_2.colorScale, 1);
		}	
		else if (bSlider==1){
			c= Color.YELLOW;
		}	
		parent.fill(c.getRGB());
		parent.textSize(11);
		parent.noStroke();
		parent.triangle(xx2-5, y+30, xx2+5, y+30, xx2, y+20);
		
		parent.fill(0);
		parent.textAlign(PApplet.CENTER);
		parent.textSize(11);
		parent.text(df.format(val), xx2,y+18);
		parent.noStroke();
		
		parent.textSize(11);
		parent.text("Similarity", x+50,y-3);
		
		parent.fill(255,0,0);
		parent.textSize(11);
		parent.textAlign(PApplet.LEFT);
		parent.text("1", x+104,y+16);
	}
	
	
	
	public void checkBrushingSlider() {
		float xx2 = x+u;
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		
		if (xx2-20<mX && mX < xx2+20 && y+10<mY && mY<y+35){
			bSlider =1; 
			return;
		}
		bSlider =-1;
	}
	
	public void checkSelectedSlider1() {
		sSlider = bSlider;
	}
	public void checkSelectedSlider2() {
		sSlider = -1;
	}	
	public int checkSelectedSlider3() {
		if (sSlider==1){
			u += (parent.mouseX - parent.pmouseX);
			if (u<5) u=5;
			if (u>w)  u=w;
			update();
		}
		return sSlider;
	}
		
}