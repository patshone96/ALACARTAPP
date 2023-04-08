package com.example.alacartapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // PRUEBAS BBDD FIRESTORE

    // Access a Cloud Firestore instance from your Activity

    FirebaseFirestore db = FirebaseFirestore.getInstance();



    //Variables used for the RecyclerView
    ArrayList<Dish> dishes;
    RecyclerView recyclerView;

    //Variables used for the drawer
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);*/





        // RECYCLER
        Intent intent = new Intent(this, DishDetailedScreen.class);

        recyclerView = findViewById(R.id.recyclerDish);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        dishes = new ArrayList<>();


        ArrayList<String> ingredientes = new ArrayList<>();

        ingredientes.add("Pescado");
        ingredientes.add("Caldo");

        ArrayList<String> alergens = new ArrayList<>();
        alergens.add("Cacahuetes");

        Dish dish = new Dish("Marinado", ingredientes, alergens, 15.5, "Muy sabroso", "https://www.cocinacaserayfacil.net/wp-content/uploads/2015/10/Espagueti-a-la-bolo%C3%B1esa.jpg");

        dishes.add(dish);
        dishes.add(dish);
        dishes.add(dish);


        // Add a new document with a generated ID
        Map<String, Object> dishList = new HashMap<>();
        dishList.put(dish.getName(), dish);


        db.collection("dishes")
                .add(dishList)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });








        AdapterDishes adapterDishes = new AdapterDishes(dishes);

        recyclerView.setAdapter(adapterDishes);


        adapterDishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("dish", (Serializable) dishes.get(recyclerView.getChildAdapterPosition(view)));

                //iniciamos la nueva actividad
                startActivity(intent);
            }
        });



        // DRAWER

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.share:
                        Toast.makeText(MainActivity.this, "Share Selected", Toast.LENGTH_SHORT).show();
                }

                return false;
            }

        });

    }

    @Override
    public void onBackPressed() {


        //EXIT THE APLICATION

        //Settle the exit intent
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //Dialog constructor
        AlertDialog.Builder constructor = new AlertDialog.Builder(this);

        //Set the title and the message
        constructor.setTitle("Se va a cerrar la app").setMessage("¿Quieres salir de la app?");

        //Make the dialog cancellable
        //Same as using the negativebUTTON
        constructor.setCancelable(true);

        //stablish the negative button using a lambda
        constructor.setNegativeButton("No", (DialogInterface.OnClickListener)
                (dialog, which) -> {
                    dialog.cancel();
                });

        //Stablish the positive button the same way
        constructor.setPositiveButton("Sí", (DialogInterface.OnClickListener)
                (dialog, which) -> {
                    //Lanzamos el intent de salida
                    startActivity(intent);
                });

        //IF THE DRAWER IS OPEN
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            // Instantiate an Alertdialog,using the constructor as a parameter and launches it
            AlertDialog alert = constructor.create();
            alert.show();
        }

    }
}