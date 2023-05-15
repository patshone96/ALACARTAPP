package com.example.alacartapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

public class DishDetailedScreen extends AppCompatActivity {

    ImageView ivDetailedImage;
    TextView tvDetailedName;
    TextView tvDetailedPrice;
    TextView tvDetailedAllergens;
    TextView tvDetailedIngredients;
    TextView tvDetailedDescription;
    ImageView ivTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detailed_screen);

        //Retrive the activity that calls this class
        Intent intent = getIntent();


        //Receive the variable "dish" sent from the parent activity
        // This variable is casted to Dish (deserialized) so we can access its attributes

        Dish dish = (Dish) intent.getSerializableExtra("dish");

        //Assign each element on the layout to a variable
        ivDetailedImage = findViewById(R.id.ivDetailedImage);
        tvDetailedName = findViewById(R.id.tvDetailedName);
        tvDetailedPrice = findViewById(R.id.tvDetailedPrice);
        tvDetailedAllergens = findViewById(R.id.tvDetaileAllergens);
        tvDetailedIngredients = findViewById(R.id.tvDetailedIngredients);
        tvDetailedDescription = findViewById(R.id.tvDetailedDescription);
        ivTitle = findViewById(R.id.ivTitle);

        //Set a scroll behavior to elements that will need it
        tvDetailedIngredients.setMovementMethod(new ScrollingMovementMethod());
        tvDetailedDescription.setMovementMethod(new ScrollingMovementMethod());

        Glide.with(this)
                .load(dish.getImage_url())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(ivDetailedImage);

        tvDetailedName.setText(dish.getName());
        tvDetailedPrice.setText(dish.getPrice());
        tvDetailedIngredients.setText(dish.getStringIngredients());
        tvDetailedAllergens.setText(dish.getStringAllergens());
        tvDetailedDescription.setText(dish.getDescription());

        ivTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence text = "Menú principal";
                Toast.makeText(getApplicationContext(), text , Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}