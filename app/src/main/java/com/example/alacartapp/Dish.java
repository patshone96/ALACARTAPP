package com.example.alacartapp;

import android.app.AlertDialog;

import java.io.Serializable;
import java.security.AllPermission;
import java.util.ArrayList;

public class Dish implements Serializable {

    private String name;
    private ArrayList<String> ingredients;
    private ArrayList<String> allergens;
    private Double price;
    private String description;
    private String image_url;

    public Dish(){}

    public Dish(String name, ArrayList<String> ingredients, ArrayList<String> allergens, Double price, String description, String image_url) {
        this.name = name;
        this.ingredients = ingredients;
        this.allergens = allergens;
        this.price = price;
        this.description = description;
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getAllergens() {
        return allergens;
    }

    public void setAllergens(ArrayList<String> allergens) {
        this.allergens = allergens;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getStringAllergens(){
        String out = "";

        for(int i = 0; i < this.allergens.size(); i++){
            out+= this.allergens.get(i) + " ";
        }

        return out;

    }
}
