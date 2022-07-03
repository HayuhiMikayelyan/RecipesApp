package com.example.recipesapp.ui.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentProfileBinding
import com.example.recipesapp.ui.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val users = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        users.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)!!
                binding.tvProfileName.text = user.username
                binding.tvProfileEmail.text = user.email
                users.child("imgUrl").addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            val imgUrl = snapshot.getValue(String::class.java)
                            Picasso.with(requireContext()).load(imgUrl).into(binding.imgProfile)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
                    }

                })
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
            }

        })
        binding.imgSettings.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProfileEditFragment())
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

}