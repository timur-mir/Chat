package home.howework.chat

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import home.howework.chat.databinding.ReceiveBinding
import home.howework.chat.databinding.SentBinding
import home.howework.chat.databinding.UserLayoutBinding
private const val PREFERENCE_NAME = "specialStorage"
private const val SHARED_PREF_SECRETKEY = "SavedDate"
class MessageAdapter(val context: Context, val messsageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var prefs = context.getSharedPreferences(PREFERENCE_NAME, AppCompatActivity.MODE_PRIVATE)
    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2
    var secretFashionKey:String? =""
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messsageList[position]
        if (holder.javaClass == SentViewHolder::class.java) {
            val viewHolder = holder as SentViewHolder
            secretFashionKey = prefs.getString(SHARED_PREF_SECRETKEY, null)
            if (prefs.getString(SHARED_PREF_SECRETKEY, null) != null) {
                if (currentMessage.message != null) {
                    holder.binding.txtSentMessage.text = decryptAES(
                        currentMessage.message!!,
                        stringFashionKeyToSecretKey(secretFashionKey!!)
                    )
                }
            }
        }else {

            val viewHolder = holder as ReceiveViewHolder
            secretFashionKey = prefs.getString(SHARED_PREF_SECRETKEY, null)
            if (prefs.getString(SHARED_PREF_SECRETKEY, null) != null) {
                if (currentMessage.message != null) {
                    holder.binding.txtReceiveMessage.text = decryptAES(
                        currentMessage.message!!,
                        stringFashionKeyToSecretKey(secretFashionKey!!)
                    )
                }
            }

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