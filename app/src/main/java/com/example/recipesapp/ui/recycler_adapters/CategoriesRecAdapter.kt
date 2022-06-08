package com.example.recipesapp.ui.recycler_adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.ui.models.CategoriesModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class CategoriesRecAdapter(private var categoryList: List<CategoriesModel>) :
    RecyclerView.Adapter<CategoriesRecAdapter.MyViewHolder>() {

    var onItemClick: ((CategoriesModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.category_rec_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.name.text = categoryList[position].name
        holder.background.setBackgroundColor(Color.parseColor(categoryList[position].background.toString()))

        Picasso.with(holder.itemView.context)
            .load(categoryList[position].imgUrl)
            .into(holder.img)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(categoryList[position])
        }
    }

    override fun getItemCount(): Int = categoryList.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.categoryName)
        val img = itemView.findViewById<ImageView>(R.id.categoryImage)
        val background = itemView.findViewById<ConstraintLayout>(R.id.cardview)
    }

}