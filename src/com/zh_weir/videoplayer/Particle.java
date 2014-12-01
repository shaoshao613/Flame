package com.zh_weir.videoplayer;

public class Particle {
	
	public double speedX;
	public double speedY;
	public float locationX;
	public float locationY;
	public float radius;
	public int life;
	public int death;
	public int r;
	public int g;
	public int b;
	public int opacity;
	

	Particle(int w,int h){
		this.speedX=-6+Math.random()*12;
		this.speedY=-5+Math.random()*5;
		this.locationX=w/2;
		this.locationY=h/2;
		this.radius = (float) (0.5+Math.random()*1);
		this.life = (int) (10+Math.random()*10);
		this.death = (int) (this.life);
		this.r = 255;
		this.g = (int) Math.round(Math.random()*155+75);
		this.b = 0;
		
	};
}
