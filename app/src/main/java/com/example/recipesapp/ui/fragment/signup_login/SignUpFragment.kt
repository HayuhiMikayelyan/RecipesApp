package com.example.recipesapp.ui.fragment.signup_login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.recipesapp.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth


class SignUpFragment : Fragment() {
    private lateinit var binding:FragmentSignUpBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater,container,false)

        binding.btnCreateAccaunt.setOnClickListener {

            if (check()){
                val email = binding.edtEmail.text.toString().trim()
                val password = binding.edtPassword.text.toString().trim()

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener({
                        if (it.isSuccessful){
                            Toast.makeText(activity,"You are registered",Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(activity,it.exception!!.message,Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }



        return binding.root
    }

    fun check(): Boolean {
        var rightPassword = true

        if (binding.edtEmail.text.toString().trim() == ""){
            Toast.makeText(activity,"Please enter email",Toast.LENGTH_SHORT).show()
            rightPassword=false
        }
        else if (binding.edtPassword.text.toString().trim() == ""){
            Toast.makeText(activity,"Please enter password",Toast.LENGTH_SHORT).show()
            rightPassword=false
        }
        else if(binding.edtRepeatPassword.text.toString().trim() != binding.edtPassword.text.toString().trim()){
            binding.edtPassword.text = null
            binding.edtRepeatPassword.text = null
            rightPassword=false
            Toast.makeText(activity,"Passwords don't match",Toast.LENGTH_SHORT).show()
        }

        return rightPassword

    }

}