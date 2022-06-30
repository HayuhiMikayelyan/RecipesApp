package com.example.recipesapp.ui.fragment.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentProfileBinding
import com.example.recipesapp.databinding.FragmentProfileEditBinding
import com.example.recipesapp.ui.activity.StartActivity
import com.example.recipesapp.ui.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileEditFragment : Fragment() {

    lateinit var binding: FragmentProfileEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileEditBinding.inflate(inflater, container, false)
        val users = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        users.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)!!
                binding.edtUsernameEdit.setText(user.username)
                binding.edtEmailEdit.setText(user.email)
                binding.edtPasswordEdit.setText(user.password)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
            }

        })

        binding.tvSave.setOnClickListener {
            val currentUser = FirebaseAuth.getInstance().currentUser!!
            currentUser.updateEmail(binding.edtEmailEdit.text.toString())
            currentUser.updatePassword(binding.edtPasswordEdit.text.toString())
            users.child("email").setValue(binding.edtEmailEdit.text.toString())
            users.child("password").setValue(binding.edtPasswordEdit.text.toString())
            users.child("username").setValue(binding.edtUsernameEdit.text.toString())

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogout.setOnClickListener {
            val firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signOut()
            startActivity(Intent(requireActivity(), StartActivity::class.java))
        }
    }


}