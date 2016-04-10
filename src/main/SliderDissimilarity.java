package main;

import java.awt.Color;
import java.text.DecimalFormat;

import processing.core.PApplet;

public class SliderDissimilarity{
	int count =0;
	public int pair =-1;
	public MultiLayerMatrix_2 parent;
	public float x=0,y=0;
	public int w; 
	public int u =-1;
	public float val =-1;
	
	
	public int bSlider = -1;
	public int sSlider = -1;
	public int ggg =2;
	public static Integrator percent = new Integrator(100,.5f,.25f);

	
	public SliderDissimilarity(MultiLayerMatrix_2 parent_, float initial){
		parent = parent_;
		w= 100*ggg;
		update();
		u = (int) (initial*ggg);
		update();
	}
		
	public void update(){
		val = (float) u/(ggg);
	}
		
	public void draw(float x_, float y_){
		x = x_;
		y = y_;
		checkBrushingSlider();
		
		float xx2 = x+u;
		DecimalFormat df = new DecimalFormat("#.##");
		parent.stroke(Color.GRAY.getRGB());
		for (int j=0; j<=10; j++ ){
			parent.line(x+j*10*ggg, y+9, x+j*10*ggg, y+14);
			if (j==10) break;
			for (int k=2; k<10; k+=2){
				parent.line(x+j*10*ggg+k*ggg, y+9, x+j*10*ggg+k*ggg, y+11);
			}
		}
		// Draw percentage
		percent.update();
		if (percent.value<99.5f){
			int v = (parent.frameCount*10)%255;
			float v2  =v/254f;
			Color c = ColorScales.getColor(v2, main.MultiLayerMatrix_2.colorScale, 1f);
			parent.fill(c.darker().getRGB());
			parent.noStroke();
			parent.arc(x-16, y+10, 16, 16, 0, percent.value*PApplet.PI/50);
		}
		
		//Upper range
		Color c;
		if (sSlider==1){
			c= Color.RED;
		}	
		else if (bSlider==1){
			c= Color.BLACK;
		}	
		else{
			c = new Color(100,100,100);
		}
		parent.textSize(11);
		parent.noStroke();
		parent.fill(c.getRGB());
		parent.triangle(xx2-5, y+20, xx2+5, y+20, xx2, y+10);
		
		parent.textAlign(PApplet.CENTER);
		parent.textSize(11);
		parent.text(df.format(val), xx2,y+8);
		parent.textAlign(PApplet.LEFT);
		
		if (parent.leaderSortedMap!=null){
			parent.textAlign(PApplet.LEFT);
			parent.fill(0);
			parent.text(parent.leaderSortedMap.size()+" groups", x+w+6,y+15);
		}
		count++;
	    if (count==10000)
	    	count=200;
	    parent.textSize(14);
	}
	
	
	
	public void checkBrushingSlider() {
		float xx2 = x+u;
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		if (xx2-20<mX && mX < xx2+20 && y<mY && mY<y+25){
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
		update();
		parent.thread5.stop();
		parent.thread5 = new Thread(parent.loader5);
		parent.thread5.start();
	}	
	public int checkSelectedSlider3() {
		if (sSlider==1){
			u += (parent.mouseX - parent.pmouseX);
			if (u<1) u=1;
			if (u>w)  u=w;
			update();
		}
		return sSlider;
	}
		
}