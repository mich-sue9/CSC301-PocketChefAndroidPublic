package com.lions.cookbook;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Observer;

public class PrivateUserProfileModel implements PrivateUserProfileContract.PrivateUserProfileModel {
    FirebaseAuth mAuth;
    DatabaseReference db;
    private ArrayList<Observer> observers = new ArrayList<Observer>();
    //these variables below hold user info read from db
    private String username;
    private String fullname;
    private String phoneNumber;
    private ArrayList<String> recipes;


    public PrivateUserProfileModel(DatabaseReference db){
        this.mAuth = FirebaseAuth.getInstance();
        this.db = db;
        findUsername();
        findFullname();
        findPhoneNumber();
        findRecipes();
        Log.d("private profile init", "private profile initialization");
    }

    public void signOut(){
        FirebaseUser profileOwner = mAuth.getCurrentUser();
        mAuth.signOut();
    }

    public String getEmail(){
        FirebaseUser currUser = mAuth.getCurrentUser();
        return currUser.getEmail();
    }

    public String getUsername(){
        return this.username;
    }

    public String getFullname(){
        return this.fullname;
    }

    public String getPhoneNumber(){
        return this.phoneNumber;
    }

    public ArrayList<String> getRecipes(){
        return this.recipes;
    }

    public void attach(Observer observer){
        this.observers.add(observer);
    }

    public void notifyAllObservers(){
        for (Observer observer : this.observers) {
            observer.update();
        }
    }

    //referred to https://subscription.packtpub.com/book/web_development/9781788624718/1/ch01lvl1sec12/reading-and-writing-to-realtime-database
    public void findUsername(){
        FirebaseUser profileOwner = mAuth.getCurrentUser();
        ValueEventListener userNameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username = dataSnapshot.getValue(String.class);
                notifyAllObservers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                username = null;
            }
        };
        db.child("users").child(profileOwner.getUid()).child("username").addListenerForSingleValueEvent(userNameListener);
    }

    public void findFullname(){
        FirebaseUser profileOwner = mAuth.getCurrentUser();
        ValueEventListener fullNameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fullname = dataSnapshot.getValue(String.class);
                notifyAllObservers();
                Log.d("fullname", "found full name: ".concat(fullname));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                fullname = null;
            }
        };
        db.child("users").child(profileOwner.getUid()).child("fullname").addValueEventListener(fullNameListener);
    }

    public void findPhoneNumber(){
        FirebaseUser profileOwner = mAuth.getCurrentUser();
        ValueEventListener phoneListener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                phoneNumber = dataSnapshot.getValue(String.class);
                notifyAllObservers();
                Log.d("phone", "found phone name: ".concat(phoneNumber));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                phoneNumber = null;
            }
        };
        db.child("users").child(profileOwner.getUid()).child("phone").addValueEventListener(phoneListener);
    }

    public void findRecipes(){
        FirebaseUser profileOwner = mAuth.getCurrentUser();
        ValueEventListener recipeListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot recipeSnapshot: dataSnapshot.getChildren()) {
                    recipes.add(recipeSnapshot.getValue(Recipe.class).getTitle());
                }
                notifyAllObservers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                recipes = null;
            }
        };
        db.child("users").child("users").child(profileOwner.getUid()).child("cookbook").addValueEventListener(recipeListener);
    }

    public void setPassword(String newPassword){
        FirebaseUser currUser = mAuth.getCurrentUser();
        currUser.updatePassword(newPassword);
    }

    public void setPhoneNumber(String newNumber){
        this.phoneNumber = newNumber;
        FirebaseUser currUser = mAuth.getCurrentUser();
        db.child("users").child(currUser.getUid()).child("phone").setValue(newNumber);
        notifyAllObservers();
    }

    public void setFullName(String newName){
        this.fullname = newName;
        FirebaseUser currUser = mAuth.getCurrentUser();
        db.child("users").child(currUser.getUid()).child("fullName").setValue(newName);
        notifyAllObservers();
    }
}