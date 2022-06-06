package com.example.recipesapp.ui.fragment.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentProfileBinding
import com.example.recipesapp.ui.fragment.home.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        val user = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.email.toString().replace(".", ""))

        user.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val user = snapshot.getValue(UserModel::class.java)!!
                        binding.tvUsername.text = user.username
                        binding.tvEmail.text = user.email
                        binding.tvPassword.text = user.password
                    }catch (e:Exception){
                        Toast.makeText(activity,e.message,Toast.LENGTH_LONG).show()
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity,error.message,Toast.LENGTH_LONG).show()
            }

        })

        return binding.root
    }


}