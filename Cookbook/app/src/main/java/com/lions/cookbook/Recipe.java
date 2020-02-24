package com.lions.cookbook;
import java.util.*;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Entity(primaryKeys = {"title","username"},tableName = "recipe_database")

public class Recipe {
    @NonNull
    private String title;
    @NonNull
    private String username;
    private int servingSize;
    @TypeConverters(RecipeListConverter.class)
    private List<Ingredient> ingredients;
    @TypeConverters(RecipeListConverter.class)
    private List<String> tags;
    @TypeConverters(RecipeListConverter.class)
    private List<String> steps;

    public Recipe(String tit, String user, int size, List<Ingredient> ings, List<String>tag, List<String> step){
        this.title = tit;
        this.username = user;
        this.servingSize = size;
        this.ingredients = ings;
        this.tags = tag;
        this.steps = step;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setServingSize(int size){
        this.servingSize = size;
    }

    public void setIngredients(List<Ingredient> ingredients){
        this.ingredients = ingredients;
    }

    public void setTags(List<String> tags){
        this.tags = tags;
    }

    public void setSteps(List<String> steps){
        this.steps = steps;
    }

    public String getTitle(){
        return this.title;
    }

    public String getUsername() { return this.username; }

    public int getServingSize(){
        return this.servingSize;
    }

    public List<Ingredient> getIngredients(){
        return this.ingredients;
    }

    public List<String> getTags(){
        return this.tags;
    }

    public List<String> getSteps(){
        return this.steps;
    }
}

