package com.example.recipesapp.ui.fragment.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.squareup.picasso.Picasso
import kotlinx.coroutines.NonDisposableHandle.parent

class RecAdapter(private val list: List<FoodModel>):
    RecyclerView.Adapter<RecAdapter.RecViewHolder>(){

    var onItemClick:((FoodModel) -> Unit)? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rec_item,parent,false)
        return RecViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecViewHolder, position: Int) {
        holder.name.text = list[position].name
        holder.recipe.text = list[position].recipe!![0]
        Picasso.with(holder.itemView.context)
            .load(list[position].imgUrl)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(list[position])
        }
    }

    override fun getItemCount(): Int = list.size

    class RecViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.tv_food_name)
        val recipe = itemView.findViewById<TextView>(R.id.tv_food_recipe)
        val image = itemView.findViewById<ImageView>(R.id.img_food)

    }
}