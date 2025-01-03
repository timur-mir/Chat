package home.howework.chat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import home.howework.chat.databinding.UserLayoutBinding

class UserAdapter(val context: Context,val userList:ArrayList<User>): RecyclerView.Adapter<UserAdapter.UserHolder>() {
    class UserHolder(val binding:UserLayoutBinding):RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        return  UserHolder(
            UserLayoutBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
        )
    }

    override fun getItemCount(): Int {
      return userList.size
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val currentUser=userList[position]
        holder.binding.userName.text=currentUser.name
        holder.itemView.setOnClickListener{
            val intent= Intent(context,ChatActivity::class.java)
            intent.putExtra("name",currentUser.name)
            intent.putExtra("uid",FirebaseAuth.getInstance().currentUser?.uid)
            context.startActivity(intent)
        }
    }
}