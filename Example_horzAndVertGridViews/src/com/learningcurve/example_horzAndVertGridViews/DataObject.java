package com.learningcurve.example_horzAndVertGridViews;

public class DataObject {
	
	private int color;
	private String name;
	
	public DataObject(String name, int color){
		this.color = color;
		this.name = name;
	}
	
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
