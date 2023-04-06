package com.example.alacartapp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        TextView image_url;
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
            image_url.setText(dish.getImage_url());
            name.setText(dish.getName());
            price.setText(String.format("%s", dish.getPrice()));
            allergens.setText(dish.getStringAllergens());
        }
    }
}
