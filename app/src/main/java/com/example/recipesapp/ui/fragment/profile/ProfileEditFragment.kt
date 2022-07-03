package com.example.recipesapp.ui.fragment.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentProfileEditBinding
import com.example.recipesapp.ui.activity.StartActivity
import com.example.recipesapp.ui.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso


class ProfileEditFragment : Fragment() {

    lateinit var binding: FragmentProfileEditBinding
    lateinit var imageUri: Uri

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
                 users.child("imgUrl")
                    .addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val imgUrl = snapshot.getValue(String::class.java)
                            Picasso.with(requireContext()).load(imgUrl).into(binding.imgProfile2)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                        }
                    })
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

        binding.btnLogout.setOnClickListener {
            val firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signOut()
            startActivity(Intent(requireActivity(), StartActivity::class.java))
        }

        binding.tvChangeImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 100)
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data!!
            binding.imgProfile2.setImageURI(imageUri)
            getPhotoUrl()

        }
    }


    fun getPhotoUrl() {
        val imageFileName = "users/profilPic${System.currentTimeMillis()}.png"
        val upLoadTask = FirebaseStorage.getInstance().reference.child(imageFileName)
        upLoadTask.putFile(imageUri).addOnCompleteListener { Task1 ->
            if (Task1.isSuccessful) {
                upLoadTask.downloadUrl.addOnCompleteListener { Task2 ->
                    if (Task2.isSuccessful) {
                        val photoUrl = Task2.result.toString()
                        FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().currentUser!!.uid)
                            .child("imgUrl").setValue(photoUrl)
                    }
                }
            }
        }
    }

}