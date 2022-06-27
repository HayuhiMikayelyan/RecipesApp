package com.example.recipesapp.ui.fragment.home.main

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentHomeBinding
import com.example.recipesapp.ui.models.CategoriesModel
import com.example.recipesapp.ui.models.FoodModel
import com.example.recipesapp.ui.recycler_adapters.FoodRecAdapter
import com.google.firebase.database.*


class HomeFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var foodList: MutableList<FoodModel>
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: FoodRecAdapter
    private var searchByName = true
    lateinit var spinnerList:Array<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        spinnerList = resources.getStringArray(R.array.spinner)
        binding.spinner.adapter =
            ArrayAdapter(requireActivity(), R.layout.spinner_item, spinnerList)

        binding.rec.layoutManager = LinearLayoutManager(activity)
        foodList = mutableListOf()

        val category = arguments?.getParcelable<CategoriesModel>("Category")!!
        binding.fragHome.setBackgroundColor(Color.parseColor(category.background.toString()))
        binding.tvCategoryName.text = category.name
        getFoodList(category.name!!)


        return binding.root
    }


    private fun getFoodList(path: String) {

        adapter = FoodRecAdapter(foodList)
        binding.rec.adapter = adapter
        database = FirebaseDatabase.getInstance().getReference(path)
        database.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    for (foodSnapshot in snapshot.children) {
                        val food = foodSnapshot.getValue(FoodModel::class.java)
                        foodList.add(food!!)
                        adapter.notifyDataSetChanged()
                    }
                    binding.progress.visibility = ProgressBar.GONE


                    adapter.onItemClick = {
                        onItemClick(it)
                    }
                    binding.edtSearch.doOnTextChanged { text, _, _, _ ->
                        search(text.toString())
                        adapter.onItemClick = {
                            onItemClick(it)
                        }
                    }
                    binding.spinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                p2: Int,
                                p3: Long
                            ) {
                                if (spinnerList[p2] == "Name") {
                                    searchByName = true
                                    binding.edtSearch.setHint(R.string.search_by_name)
                                } else {
                                    searchByName = false
                                    binding.edtSearch.setHint(R.string.search_by_ing)
                                }
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                searchByName = true
                            }

                        }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun search(text: String) {

        val list = mutableListOf<FoodModel>()
        foodList.forEach {
            if (searchByName){
                if (it.name!!.lowercase().contains(text.lowercase())) {
                    if(!(it in list)) list.add(it)
                }
            }else{
                it.ingredients!!.forEach { ingredient->
                    if (ingredient.lowercase().contains(text.lowercase())) {
                        if(!(it in list)) list.add(it)
                    }
                }
            }
        }
        adapter = FoodRecAdapter(list)
        adapter.notifyDataSetChanged()
        binding.rec.adapter = adapter
    }

    private fun onItemClick(it: FoodModel) {
        val bundle = Bundle()
        val fragment = DetailFragment()

        bundle.putParcelable("Food", it)
        fragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
        binding.edtSearch.setText("")

    }
}