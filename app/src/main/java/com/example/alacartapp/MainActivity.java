package com.example.alacartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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

        Dish dish = new Dish("Marinado", ingredientes, alergens, 15.5, "Muy sabroso", "https://www.marisco.es");

        dishes.add(dish);
        dishes.add(dish);
        dishes.add(dish);

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