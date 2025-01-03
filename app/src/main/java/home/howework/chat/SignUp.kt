package home.howework.chat

import android.content.Intent
import android.media.MediaPlayer.OnCompletionListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.SignInMethodQueryResult
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import home.howework.chat.databinding.ActivitySignUpBinding
import java.util.regex.Pattern

class SignUp : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        binding.signSignUp.setOnClickListener {
            val name = binding.nameUser.text.toString().trim()
            val email = binding.userEmailSignUp.text.toString().trim()
            val password = binding.passwordSignUp.text.toString().trim()
            signUp(name,email, password)
        }
    }


    private fun signUp(name:String,email: String, password: String) {
        // val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (TextUtils.isEmpty(email)) {
            binding.userEmailSignUp.setError("Укажите почту")
            binding.userEmailSignUp.requestFocus()
        } else if (TextUtils.isEmpty(password)) {
            binding.passwordSignUp.setError("Укажите пароль")
            binding.passwordSignUp.requestFocus()
        }
        else if (TextUtils.isEmpty(name)) {
            binding.nameUser.setError("Укажите имя")
            binding.nameUser.requestFocus()
        }
//        else   if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
//            Toast.makeText(this, "Формат введенной почты неверен", Toast.LENGTH_SHORT).show()
//        }
        else {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                       // val firebaseUser: FirebaseUser = task.result!!.user!!
                            addUserToDataBase(name,email,mAuth.currentUser?.uid!!)
                        val intent = Intent(this, MainActivity::class.java)
                        finish()
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Ошибка регистрации", Toast.LENGTH_SHORT).show()
                        Log.e("AUTH", "Error signing in", task.exception)
                    }
                }
        }

    }
   private fun addUserToDataBase(name: String,email: String,uid:String){
        mDbRef=FirebaseDatabase.getInstance().reference
       mDbRef.child("user").child(uid).setValue(User(name,email,uid))
    }
}

//private fun signUp2(email:String,password:String){
//        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(this){task->
//            if (task.isSuccessful) {
//                mAuth.createUserWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(this) { task ->
//                        if (task.isSuccessful) {
//                            val firebaseUser: FirebaseUser = task.result!!.user!!
//                            val intent = Intent(this, MainActivity::class.java)
//                            startActivity(intent)
//                        } else {
//                            Toast.makeText(this, "Ошибка регистрации", Toast.LENGTH_SHORT).show()
//                            Log.e("AUTH", "Error signing in", task.exception)
//                        }
//                    }
//            }
//            else {
//                mAuth.createUserWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(this) { task ->
//                        if (task.isSuccessful) {
//                            val firebaseUser: FirebaseUser = task.result!!.user!!
//                            val intent = Intent(this, MainActivity::class.java)
//                            startActivity(intent)
//                        } else {
//                            Toast.makeText(this, "Ошибка регистрации 53545", Toast.LENGTH_SHORT).show()
//                            Log.e("AUTH", "Error signing in", task.exception)
//                        }
//                    }
//                Toast.makeText(this, "Ошибка регистрации????", Toast.LENGTH_LONG).show()
//            }
//
//        }
//
//    }
//       binding.signSignUp.setOnClickListener{
//            if (binding.usernameSignUp.text!!.isNotEmpty()&&binding.passwordSignUp.text!!.isNotEmpty()) {
//                val email = binding.usernameSignUp.text.toString().trim()
//                val password = binding.passwordSignUp.text.toString().trim()
//                signUp(email, password)
//            }
//            else{
//                Toast.makeText(this, "Какое то поле или поля пустые", Toast.LENGTH_SHORT).show()
//            }
//        }