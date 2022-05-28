package com.example.recipesapp.ui.fragment.signup_login

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentLogInBinding
import com.example.recipesapp.ui.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers.Main

class LogInFragment : Fragment() {

    private lateinit var binding:FragmentLogInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogInBinding.inflate(inflater,container,false)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLogIn.setOnClickListener {
            validateData()
        }

        binding.resetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_resetPasswordFragment)
        }

        return binding.root
    }

    private fun validateData() {
        email = binding.edtEmailLogin.text.toString().trim()
        password = binding.edtPasswordLogin.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(activity,"Invalid email format",Toast.LENGTH_SHORT).show()
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(activity,"Please enter password",Toast.LENGTH_SHORT).show()
        }else{
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        val dialog = progressDialog(requireActivity())
        dialog.show()
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                dialog.hide()
                val intent = Intent (getActivity(), MainActivity::class.java)
                getActivity()?.startActivity(intent)
            }
            .addOnFailureListener{
                dialog.hide()
                Toast.makeText(activity,it.message,Toast.LENGTH_SHORT).show()
            }
    }


    private fun progressDialog(context: Context): Dialog {
        val dialog = Dialog(context)
        val inflate = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null)
        dialog.setContentView(inflate)
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        return dialog
    }
}