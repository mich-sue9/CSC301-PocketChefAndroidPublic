package com.lions.cookbook;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONObject;

import java.util.ArrayList;

public class CreateAccountModel implements CreateAccountContract.CreateAccountMVPModel {
    private DatabaseReference db;

    public CreateAccountModel(DatabaseReference db){
        this.db = db;
    }

    public Boolean passwordStrong(String password){
        return (password.length() >= 6);
    }

    public Boolean validEmail(String email){
        return (email.endsWith("@gmail.com"));
    }

    public Boolean addNewUser(String email,String userPassword){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (passwordStrong(userPassword)){
            if (validEmail(email)){
                mAuth.createUserWithEmailAndPassword(email, userPassword);
                FirebaseUser new_user = mAuth.getCurrentUser();
                //add user to recipe database
                db.child("recipes").child(new_user.getUid().toString()).setValue("");
                return true;
            }
        }
        return false;
    }

    public void setUsername(String username){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser new_user = mAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username).build();
    }

    public void storeUserInfo(String userName, String firstName, String lastName, String phone) {
        String fullname = firstName.concat((" ").concat((lastName)));
        db.child("usernames").child(userName).setValue(fullname, phone);
    }
}
