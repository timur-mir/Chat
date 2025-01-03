package home.howework.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import home.howework.chat.databinding.ReceiveBinding
import home.howework.chat.databinding.SentBinding
import home.howework.chat.databinding.UserLayoutBinding

class MessageAdapter(val context: Context, val messsageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    class SentViewHolder(val binding: SentBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    class ReceiveViewHolder(val binding: ReceiveBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if(viewType==1){
                return ReceiveViewHolder(
                    ReceiveBinding
                        .inflate(
                            LayoutInflater.from(parent.context),
                            parent, false
                        )
                )
            }
        else{
                return SentViewHolder(
                    SentBinding
                        .inflate(
                            LayoutInflater.from(parent.context),
                            parent, false
                        )
                )
        }
    }

    override fun getItemCount(): Int {
       return messsageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messsageList[position]
        if (holder.javaClass == SentViewHolder::class.java) {
            val viewHolder = holder as SentViewHolder
            holder.binding.txtSentMessage.text = currentMessage.message
        } else {
            val viewHolder = holder as ReceiveViewHolder
            holder.binding.txtReceiveMessage.text = currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage=messsageList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }
        else {
            return ITEM_RECEIVE
        }
    }

}