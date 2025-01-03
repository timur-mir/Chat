package home.howework.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.ValueEventListener
//import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import home.howework.chat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth=FirebaseAuth.getInstance()
        binding.sign.setOnClickListener{
            val intent= Intent(this,SignUp::class.java)
            startActivity(intent)
        }
        binding.login.setOnClickListener{
            val email=binding.username.text.toString()
            val password=binding.password.text.toString()
          //  Patterns.EMAIL_ADDRESS.matcher(email).matches()
            login(email,password)
        }
    }
    private fun login(email:String,password:String) {
        if (TextUtils.isEmpty(email)) {
            binding.username.setError("Укажите почту")
            binding.username.requestFocus()
        } else if (TextUtils.isEmpty(password)) {
            binding.password.setError("Укажите пароль")
            binding.password.requestFocus()
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, ChatPlace::class.java)
                        finish()
                        startActivity(intent)
                    } else {

                        Toast.makeText(
                            baseContext,
                            "Пользователя с таким именем нет",
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }
        }
    }
}

