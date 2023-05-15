package com.example.alacartapp;

import androidx.annotation.NonNull;

//For widgets
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// For Firebase
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    //Variables used for the RecyclerView
    ArrayList<Dish> dishes;
    RecyclerView recyclerView;

    //Variables used for the drawer
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;


    EditText inputIngredient;
    Button btnIngredient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputIngredient = findViewById(R.id.inputIngredient);
        btnIngredient = findViewById(R.id.btnSearch);


        recyclerView = findViewById(R.id.recyclerDish);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        dishes = new ArrayList<>();



        // DRAWER
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





    }

    @Override
    protected void onResume() {
        super.onResume();

        //Reset dishes
        dishes.clear();

        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // RECYCLER
        Intent intent = new Intent(this, DishDetailedScreen.class);

        // Get a reference to the Firestore collection
        CollectionReference cr = db.collection("dishes");

        // Retrieve a series of SnapShots and store it on the variable future
        Task<QuerySnapshot> future = cr.get();

        // On success retrieval we loop over the documentSnapshots and cast the objects into Dish
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

                    //We initiate the new activity
                    startActivity(intent);
                }
            });
        }).addOnFailureListener(Throwable::printStackTrace);


        //Searchbar

        inputIngredient.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if(focus)
                    inputIngredient.setHint("");
                else
                    inputIngredient.setHint("Ingrediente");
            }
        });

        btnIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference collectionRef = db.collection("dishes");
                Query query;
                AdapterDishes adapterDishes = new AdapterDishes(dishes);


                String name = String.valueOf(inputIngredient.getText());
                String cap = "";
                if(name.length() >= 1 ){
                    cap = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                }


                //query = collectionRef.whereEqualTo("name", name);

                query = collectionRef.whereArrayContains("ingredients", cap);

                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            dishes.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Dish plato = document.toObject(Dish.class);
                                dishes.add(plato);
                            }
                            recyclerView.setAdapter(adapterDishes);

                            adapterDishes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    intent.putExtra("dish", (Serializable) dishes.get(recyclerView.getChildAdapterPosition(view)));

                                    //We initiate the new activity
                                    startActivity(intent);
                                }
                            });


                        } else {
                            try {
                                throw new Exception();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

            }
        });





        //Add filters
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference collectionRef = db.collection("dishes");
                        Query query;
                        AdapterDishes adapterDishes = new AdapterDishes(dishes);

                        switch(item.getItemId()){
                            case R.id.home:

                                // Retrieve a series of SnapShots and store it on the variable future
                                Task<QuerySnapshot> future = cr.get();

                                // On success retrieval we loop over the documentSnapshots and cast the objects into Dish
                                future.addOnSuccessListener(querySnapshot -> {
                                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();

                                   dishes.clear();

                                    for (DocumentSnapshot document: documents) {
                                        Dish plato = document.toObject(Dish.class);
                                        dishes.add(plato);

                                    }

                                    recyclerView.setAdapter(adapterDishes);

                                    adapterDishes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            intent.putExtra("dish", (Serializable) dishes.get(recyclerView.getChildAdapterPosition(view)));

                                            //We initiate the new activity
                                            startActivity(intent);
                                        }
                                    });
                                }).addOnFailureListener(Throwable::printStackTrace);




                                break;


                            case R.id.veggie:

                                query = collectionRef.whereArrayContains("diet", "vegetarian");

                                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            dishes.clear();
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Dish plato = document.toObject(Dish.class);
                                                dishes.add(plato);
                                            }
                                            recyclerView.setAdapter(adapterDishes);

                                            adapterDishes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    intent.putExtra("dish", (Serializable) dishes.get(recyclerView.getChildAdapterPosition(view)));

                                                    //We initiate the new activity
                                                    startActivity(intent);
                                                }
                                            });



                                        } else {
                                            try {
                                                throw new Exception();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });

                                break;

                            case R.id.gluten:

                                collectionRef.get()
                                        .addOnSuccessListener(querySnapshot -> {
                                            List<DocumentSnapshot> docsWithoutGluten = new ArrayList<>();
                                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                                List<String> allergens = (List<String>) document.get("allergens");
                                                if (allergens == null || !allergens.contains("gluten")) {
                                                    docsWithoutGluten.add(document);
                                                }
                                            }
                                            dishes.clear();
                                            for (DocumentSnapshot document : docsWithoutGluten) {
                                                Dish plato = document.toObject(Dish.class);
                                                dishes.add(plato);
                                            }

                                            recyclerView.setAdapter(adapterDishes);

                                            adapterDishes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    intent.putExtra("dish", (Serializable) dishes.get(recyclerView.getChildAdapterPosition(view)));

                                                    //We initiate the new activity
                                                    startActivity(intent);
                                                }
                                            });

                                        })
                                        .addOnFailureListener(Throwable::printStackTrace);



//                                query = collectionRef.whereArrayContains("allergens", "gluten");
//
//                                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                        if (task.isSuccessful()) {
//                                            dishes.clear();
//                                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                                Dish plato = document.toObject(Dish.class);
//                                                dishes.add(plato);
//                                            }
//                                            recyclerView.setAdapter(new AdapterDishes(dishes));
//
//                                        } else {
//                                            try {
//                                                throw new Exception();
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    }
//                                });

                                break;

                            case R.id.egg:

                                collectionRef.get()
                                        .addOnSuccessListener(querySnapshot -> {
                                            List<DocumentSnapshot> docsWithoutGluten = new ArrayList<>();
                                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                                List<String> allergens = (List<String>) document.get("allergens");
                                                if (allergens == null || !allergens.contains("huevo")) {
                                                    docsWithoutGluten.add(document);
                                                }
                                            }
                                            dishes.clear();
                                            for (DocumentSnapshot document : docsWithoutGluten) {
                                                Dish plato = document.toObject(Dish.class);
                                                dishes.add(plato);
                                            }

                                            recyclerView.setAdapter(adapterDishes);

                                            adapterDishes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    intent.putExtra("dish", (Serializable) dishes.get(recyclerView.getChildAdapterPosition(view)));

                                                    //We initiate the new activity
                                                    startActivity(intent);
                                                }
                                            });

                                        })
                                        .addOnFailureListener(Throwable::printStackTrace);


                                break;

                            case R.id.nuts:

                                collectionRef.get()
                                        .addOnSuccessListener(querySnapshot -> {
                                            List<DocumentSnapshot> docsWithoutGluten = new ArrayList<>();
                                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                                List<String> allergens = (List<String>) document.get("allergens");
                                                if (allergens == null || !allergens.contains("frutosSecos")) {
                                                    docsWithoutGluten.add(document);
                                                }
                                            }
                                            dishes.clear();
                                            for (DocumentSnapshot document : docsWithoutGluten) {
                                                Dish plato = document.toObject(Dish.class);
                                                dishes.add(plato);
                                            }

                                            recyclerView.setAdapter(adapterDishes);
                                            adapterDishes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    intent.putExtra("dish", (Serializable) dishes.get(recyclerView.getChildAdapterPosition(view)));

                                                    //We initiate the new activity
                                                    startActivity(intent);
                                                }
                                            });

                                        })
                                        .addOnFailureListener(Throwable::printStackTrace);

                                break;

                            case R.id.seafood:

                                collectionRef.get()
                                        .addOnSuccessListener(querySnapshot -> {
                                            List<DocumentSnapshot> docsWithoutGluten = new ArrayList<>();
                                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                                List<String> allergens = (List<String>) document.get("allergens");
                                                if (allergens == null || !allergens.contains("marisco")) {
                                                    docsWithoutGluten.add(document);
                                                }
                                            }
                                            dishes.clear();
                                            for (DocumentSnapshot document : docsWithoutGluten) {
                                                Dish plato = document.toObject(Dish.class);
                                                dishes.add(plato);
                                            }

                                            recyclerView.setAdapter(adapterDishes);

                                            adapterDishes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    intent.putExtra("dish", (Serializable) dishes.get(recyclerView.getChildAdapterPosition(view)));

                                                    //We initiate the new activity
                                                    startActivity(intent);
                                                }
                                            });

                                        })
                                        .addOnFailureListener(Throwable::printStackTrace);

                                break;
                        }
                        return false;
                    }
                });
    }

    @Override
    public void onBackPressed() {

        //EXIT THE APPLICATION

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

        //Establish the negative button using a lambda
        constructor.setNegativeButton("No", (DialogInterface.OnClickListener)
                (dialog, which) -> {
                    dialog.cancel();
                });

        //Establish the positive button the same way
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