package home.howework.chat

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.ValueEventListener
//import com.google.firebase.database.ktx.database
//import com.google.firebase.ktx.Firebase
import home.howework.chat.databinding.ActivityChatPlaceBinding
import home.howework.chat.databinding.ActivityMainBinding

class ChatPlace : AppCompatActivity() {
    lateinit var binding: ActivityChatPlaceBinding
    private lateinit var userArray: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mDbRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userArray = ArrayList()
        adapter = UserAdapter(this, userArray)
        binding.usersList.layoutManager = LinearLayoutManager(this)
        binding.usersList.adapter = adapter
        mDbRef = FirebaseDatabase.getInstance().getReference()
        mAuth = FirebaseAuth.getInstance()

        mDbRef.child("user").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if (currentUser != null) {
                        Toast.makeText(
                            this@ChatPlace,
                            "Пользователь :${currentUser.name.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (mAuth.currentUser?.uid != currentUser?.uid) {
                        userArray.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            mAuth.signOut()
            val intent = Intent(this, MainActivity::class.java)
             finish()
            startActivity(intent)
            return true
        }
        return true
    }
}


