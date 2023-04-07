package com.example.alacartapp;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;
import java.util.List;

public class AdapterDishes extends RecyclerView.Adapter<AdapterDishes.ViewHolderDishes>
implements View.OnClickListener{


    ArrayList<Dish> dishes;
    private View.OnClickListener listener;

    public AdapterDishes(ArrayList<Dish> dishes) {
        this.dishes = dishes;
    }




    @NonNull
    @Override
    public AdapterDishes.ViewHolderDishes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dishes_list, null, false);

        view.setOnClickListener(this);

        return new ViewHolderDishes(view);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDishes.ViewHolderDishes holder, int position) {
               holder.assignDish(dishes.get(position));

    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    public class ViewHolderDishes extends RecyclerView.ViewHolder {

        ImageView image_url;
        TextView name;
        TextView price;
        TextView allergens;


        public ViewHolderDishes(@NonNull View itemView) {
            super(itemView);

            image_url = itemView.findViewById(R.id.image_url);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            allergens = itemView.findViewById(R.id.allergens);



        }

        @SuppressLint("DefaultLocale")
        public void assignDish(Dish dish) {
            Glide.with(itemView)
                    .load(dish.getImage_url())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(image_url);
            name.setText(dish.getName());
            price.setText(String.format("%s", dish.getPrice()));
            allergens.setText(dish.getStringAllergens());
        }
    }
}
