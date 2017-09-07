package com.ray.cookiescook.model;

public class IngredientsItem{
	private double quantity;
	private String measure;
	private String ingredient;

	public void setQuantity(double quantity){
		this.quantity = quantity;
	}

	public double getQuantity(){
		return quantity;
	}

	public void setMeasure(String measure){
		this.measure = measure;
	}

	public String getMeasure(){
		return measure;
	}

	public void setIngredient(String ingredient){
		this.ingredient = ingredient;
	}

	public String getIngredient(){
		return ingredient;
	}

	@Override
 	public String toString(){
		return 
			"IngredientsItem{" + 
			"quantity = '" + quantity + '\'' + 
			",measure = '" + measure + '\'' + 
			",ingredient = '" + ingredient + '\'' + 
			"}";
		}
}
