package main;

import processing.core.PApplet;

public class CheckBox{
	public boolean s;
	public boolean b;
	public PApplet parent;
	public float x = 0;
	public float y = 0;
	public String text = "";
	
	public CheckBox(PApplet parent_, String text_){
		parent = parent_;
		text = text_;
	}
	
		
	public void draw(float  x_, float y_){
		x = x_;
		y = y_;
		
		checkBrushing();
		parent.textAlign(PApplet.LEFT);
		parent.textSize(10);
		parent.stroke(0);
		if (b)
			parent.fill(180);
		else
			parent.noFill();
		parent.rect(x, y, 12, 12);
		parent.fill(0);
		if (s){
			parent.noStroke();
			parent.fill(0);
			parent.ellipse(x+6, y+6, 10, 10);
		}
		else if (b)
			parent.fill(100);
				
		parent.text(text,x+15,y+10);
	}
	
	public void checkBrushing() {
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		if (x-10<mX && mX < x+150 && y<mY && mY<y+20){
			b=true;
		}
		else
			b = false;
	}
	
	public void mouseClicked() {
		if (b){
			s = !s;
		}
	}
	
}