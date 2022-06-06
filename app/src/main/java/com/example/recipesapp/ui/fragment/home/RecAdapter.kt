package com.example.recipesapp.ui.fragment.home

import android.provider.ContactsContract
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.coroutines.NonDisposableHandle.parent
import java.lang.reflect.Array

class RecAdapter(private val list: List<FoodModel>) :
    RecyclerView.Adapter<RecAdapter.RecViewHolder>() {

    val favList = mutableListOf<String>()
    var onItemClick: ((FoodModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rec_item, parent, false)
        return RecViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecViewHolder, position: Int) {
        val favourites = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.email.toString().replace(".", ""))
            .child("favoriteList")

        favourites.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    if(!(snap.getValue(String::class.java)!! in favList)){
                        favList.add(snap.getValue(String::class.java)!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(holder.itemView.context, error.message, Toast.LENGTH_LONG).show()
            }

        })


        holder.name.text = list[position].name
        holder.recipe.text = list[position].recipe!![0]
        Picasso.with(holder.itemView.context)
            .load(list[position].imgUrl)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(list[position])
        }
        var a = false
        favList.forEach {
            if (it == holder.name.text) {
                holder.favorite.setImageResource(R.drawable.ic_favorites_2)
                a = true
            }
        }
        if(!a) holder.favorite.setImageResource(R.drawable.ic_favorites)



        holder.favorite.setOnClickListener {
            var exists = false
            favList.forEach {if (it == holder.name.text) exists=true}
            if (!exists){
                holder.favorite.setImageResource(R.drawable.ic_favorites_2)
                favList.add(holder.name.text.toString())
                favourites.setValue(favList)
            }else{
                holder.favorite.setImageResource(R.drawable.ic_favorites)
                favList.remove(holder.name.text.toString())
                favourites.setValue(favList)

            }


        }

    }

    override fun getItemCount(): Int = list.size

    class RecViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.tv_food_name)
        val recipe = itemView.findViewById<TextView>(R.id.tv_food_recipe)
        val image = itemView.findViewById<ImageView>(R.id.img_food)
        val favorite = itemView.findViewById<ImageView>(R.id.img_favorite)


    }
}