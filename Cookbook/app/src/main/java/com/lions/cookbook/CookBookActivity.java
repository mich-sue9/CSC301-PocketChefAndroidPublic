package com.lions.cookbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.ArrayList;
import java.util.List;

/**
 * VIEW: Displays the CookBook Screen
 */
public class CookBookActivity extends AppCompatActivity implements CookBookContract.CookBookMVPView{
    private CookBookPresent presenter;
    private CookBookModel model1;
    private DatabaseReference mDatabase;
    private ArrayAdapter<String> arrayAdapter;

    //Good Tutorial on listView: https://abhiandroid.com/ui/listview for texts and pictures
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cook_book_activity);

        //Set up values
        mDatabase = FirebaseDatabase.getInstance().getReference();
        model1 = new CookBookModel(mDatabase);
        presenter = new CookBookPresent(this, model1);

        //Populate with List of Recipe names
        final ListView RecipeList = (ListView)findViewById(R.id.recipeList); //Fill in with actual id of List view
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, presenter.getRecipeNames());
        RecipeList.setAdapter(arrayAdapter);
        RecipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String recipeName = (String) RecipeList.getItemAtPosition(i);
                presenter.handleRecipeClicked(recipeName);
            }
        });

        //Set up Navigation panel
        BottomNavigationView navigationPanel = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigationPanel.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_create:
                                Intent intent1 = new Intent(CookBookActivity.this, CreateRecipeActivity.class);
                                startActivity(intent1);
                                break;

                            case R.id.navigation_cookbook:
                                Intent intent2 = new Intent(CookBookActivity.this, CookBookActivity.class);
                                startActivity(intent2);
                                break;
                        }
                        return false;
                    }
                });

    }



    @Override
    public void goToCreateRecipeScreen() {
        Intent intent = new Intent(this, CreateRecipeActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Taking user to CreateRecipe Screen", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goToViewRecipe(String clickedRecipe) {
        Intent intent = new Intent(this, ViewRecipeActivity.class);
        intent.putExtra("RECIPE", clickedRecipe);
        Log.d("TEST", "Created recipe to be trasnferd to new intent");
        startActivity(intent);
        Log.d("TEST", "Starting new intent");

    }

}
