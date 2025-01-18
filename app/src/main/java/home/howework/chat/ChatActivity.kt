package home.howework.chat

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import home.howework.chat.databinding.ActivityChatBinding
import home.howework.chat.databinding.ActivityMainBinding
import java.util.Base64
import javax.crypto.spec.SecretKeySpec

private const val PREFERENCE_NAME = "specialStorage"
private const val SHARED_PREF_SECRETKEY = "SavedDate"

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference
    var receiveRoom: String? = null
    var senderRoom: String? = null
    var secretFashionKey: String? = ""
    var message = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var prefs = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)


        val name = intent.getStringExtra("name")
        val receiveUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDbRef = FirebaseDatabase.getInstance().getReference()
        senderRoom = receiveUid + senderUid
        receiveRoom = senderUid + receiveUid
        supportActionBar?.title = name
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)
        binding.usersChat.layoutManager = LinearLayoutManager(this)
        binding.usersChat.adapter = messageAdapter
        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        binding.messBtn.setOnClickListener {
            message = binding.messBox.text.toString()
            if (getStateSharedPreference(this) == null) {
                val editor = prefs.edit()
                var secretKey = generatePermanentSecretKey()
                Toast.makeText(this, "$secretKey", Toast.LENGTH_LONG).show()
                editor.putString(SHARED_PREF_SECRETKEY, secretKeyToStringFashionKey(secretKey))
                editor.apply()
                message = encryptAES(message, secretKey)
                Log.d("SECRET", "$secretKey")
                val messageObject = Message(message, senderUid)
                mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        mDbRef.child("chats").child(receiveRoom!!).child("messages").push()
                            .setValue(messageObject)
                    }
                binding.messBox.setText("")
            } else {
                secretFashionKey = prefs.getString(SHARED_PREF_SECRETKEY, null)
                message = encryptAES(message, stringFashionKeyToSecretKey(secretFashionKey!!))
                val messageObject = Message(message, senderUid)
                mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        mDbRef.child("chats").child(receiveRoom!!).child("messages").push()
                            .setValue(messageObject)
                    }
                binding.messBox.setText("")
            }

        }
    }

    private fun getStateSharedPreference(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
        return prefs.getString(SHARED_PREF_SECRETKEY, null)
    }
}