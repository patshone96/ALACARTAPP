package com.example.alacartapp;

import android.app.AlertDialog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.IOException;
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
    private ArrayList<String> diet;

    public Dish(){}

    public Dish(String name, ArrayList<String> ingredients, ArrayList<String> allergens, Double price, String description, String image_url) {
        this.name = name;
        this.ingredients = ingredients;
        this.allergens = allergens;
        this.price = price;
        this.description = description;
        this.image_url = image_url;
    }

    public Dish(String name, ArrayList<String> ingredients, ArrayList<String> allergens, Double price, String description, String image_url, ArrayList<String> diet) {
        this.name = name;
        this.ingredients = ingredients;
        this.allergens = allergens;
        this.price = price;
        this.description = description;
        this.image_url = image_url;
        this.diet = diet;
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

    public String getPrice() {
        return price + " €";
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

    public String getStringIngredients(){
        String out = "";

        for(int i = 0; i < this.ingredients.size(); i++){
            out+= "· " + this.ingredients.get(i) + " \n";
        }

        return out;

    }

    public ArrayList<String> getDiet() {
        return diet;
    }

    public void setDiet(ArrayList<String> diet) {
        this.diet = diet;
    }

    public String shortName(){
        if (this.name.length() > 15) return this.getName().substring(0, 15);
        return name;
    }

}
