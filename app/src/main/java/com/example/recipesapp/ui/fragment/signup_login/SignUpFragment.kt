package com.example.recipesapp.ui.fragment.signup_login

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentSignUpBinding
import com.example.recipesapp.ui.activity.MainActivity
import com.example.recipesapp.ui.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.btnCreateAccount.setOnClickListener {

            if (check()) {
                val username = binding.edtUsername.text.toString().trim()
                val email = binding.edtEmail.text.toString().trim()
                val password = binding.edtPassword.text.toString().trim()
                val dialog = progressDialog(requireActivity())
                dialog.show()
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener({
                        dialog.hide()
                        if (it.isSuccessful) {

                            FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                .setValue(UserModel(email,password,username))

                            val intent = Intent(getActivity(), MainActivity::class.java)
                            getActivity()?.startActivity(intent)
                        } else {
                            Toast.makeText(activity, it.exception!!.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
            }
        }

        binding.tvLogIn.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_logInFragment)
        }

        return binding.root
    }


    private fun check(): Boolean {
        var rightPassword = true

        when {
            binding.edtUsername.text.toString().trim() == "" -> {
                Toast.makeText(activity, "Please enter username", Toast.LENGTH_SHORT).show()
                rightPassword = false
            }
            binding.edtEmail.text.toString().trim() == "" -> {
                Toast.makeText(activity, "Please enter email", Toast.LENGTH_SHORT).show()
                rightPassword = false
            }
            binding.edtPassword.text.toString().trim() == "" -> {
                Toast.makeText(activity, "Please enter password", Toast.LENGTH_SHORT).show()
                rightPassword = false
            }
            binding.edtRepeatPassword.text.toString()
                .trim() != binding.edtPassword.text.toString().trim() -> {
                binding.edtPassword.text = null
                binding.edtRepeatPassword.text = null
                rightPassword = false
                Toast.makeText(activity, "Passwords don't match", Toast.LENGTH_SHORT).show()
            }
        }

        return rightPassword

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