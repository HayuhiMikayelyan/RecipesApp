package com.example.recipesapp.ui.fragment.signup_login

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentResetPasswordBinding
import com.example.recipesapp.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ResetPasswordFragment : Fragment() {
    private lateinit var binding:FragmentResetPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResetPasswordBinding.inflate(inflater,container,false)

        val firebaseAuth = FirebaseAuth.getInstance()
        binding.btnResetPassword.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            if(email.isEmpty()){
                Toast.makeText(activity,"Please enter email", Toast.LENGTH_SHORT).show()
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(activity,"Invalid email format",Toast.LENGTH_SHORT).show()
            }else{
                val dialog = progressDialog(requireActivity())
                dialog.show()
                firebaseAuth.sendPasswordResetEmail(email)

                    .addOnCompleteListener {
                        dialog.hide()
                        if (it.isSuccessful){
                            Toast.makeText(activity,"Check your email to reset password!",Toast.LENGTH_LONG).show()
                            GlobalScope.launch {
                                delay(1000)
                            }
                            findNavController().navigate(R.id.action_resetPasswordFragment_to_logInFragment)
                        }else{
                            Toast.makeText(activity,it.exception!!.message,Toast.LENGTH_LONG).show()
                        }
                    }

            }
        }

        return binding.root
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