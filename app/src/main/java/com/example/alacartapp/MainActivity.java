package com.example.alacartapp;



import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    // PRUEBAS BBDD FIRESTORE





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


        // Access a Cloud Firestore instance from your Activity

        FirebaseFirestore db = FirebaseFirestore.getInstance();


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

      /*  dishes.add(dish);
        dishes.add(dish);
        dishes.add(dish);
*/

        // Add a new document with a generated ID
//        Map<String, Object> dishList = new HashMap<>();
//        dishList.put(dish.getName(), dish);



// Get a reference to the Firestore collection
        DocumentReference collectionRef = db.collection("dishes").document();

// Create a new document with a custom ID
        String customId = "myCustomId";
        DocumentReference docRef = db.collection("myCollection").document(customId);

// Set the data for the document
        Map<String, Object> data = new HashMap<>();
        data.put("field1", "value1");
        data.put("field2", "value2");



      /* db.collection("dishes")
                .add(dish)
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

*/




        CollectionReference cr = db.collection("dishes");

        Task<QuerySnapshot> future = cr.get();

        future.addOnSuccessListener(querySnapshot -> {
            List<DocumentSnapshot> documents = querySnapshot.getDocuments();

            AdapterDishes adapterDishes = new AdapterDishes(dishes);

            for (DocumentSnapshot document: documents) {
                Dish plato = document.toObject(Dish.class);
                dishes.add(plato);
                
            }

            recyclerView.setAdapter(adapterDishes);

                adapterDishes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent.putExtra("dish", (Serializable) dishes.get(recyclerView.getChildAdapterPosition(view)));

                        //iniciamos la nueva actividad
                        startActivity(intent);
                    }
                });
        }).addOnFailureListener(Throwable::printStackTrace);

        //One Document
//
//        DocumentReference dr = db.collection("dishes").document("lDTayge77ktlyphbfyJ2");
//
//        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot.exists()) {
//                    Dish dish1 = documentSnapshot.toObject(Dish.class);
//                        dishes.add(dish1);
//                } else {
//                    System.out.println("No such document!");
//                }
//
//                AdapterDishes adapterDishes = new AdapterDishes(dishes);
//
//                recyclerView.setAdapter(adapterDishes);
//
//                adapterDishes.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        intent.putExtra("dish", (Serializable) dishes.get(recyclerView.getChildAdapterPosition(view)));
//
//                        //iniciamos la nueva actividad
//                        startActivity(intent);
//                    }
//                });
//            }
//        });





        // DRAWER
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Add filters
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.share:
                        Toast.makeText(MainActivity.this,
                                "Share Selected", Toast.LENGTH_SHORT).show();
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