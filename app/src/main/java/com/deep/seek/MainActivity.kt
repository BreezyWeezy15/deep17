package com.deep.seek

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deep.seek.models.ChatRequest
import com.deep.seek.models.ChatResponse
import com.deep.seek.models.Message
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sendMessageImg: ImageView
    private lateinit var msgEditText: EditText
    private lateinit var viewModel: ChatViewModel
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        adapter = ChatAdapter()

        recyclerView = findViewById(R.id.rv)
        sendMessageImg  = findViewById(R.id.sendMessage)
        msgEditText = findViewById(R.id.messageEdit)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel.messages.observe(this) { messages ->
            adapter.setMessages(messages)
            recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
        }

        sendMessageImg.setOnClickListener {

            val userMessage = msgEditText.text.toString()

            if(userMessage.isEmpty()){
                Toast.makeText(this,"Query should not be empty",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            viewModel.sendMessage(userMessage)
            msgEditText.text.clear()
        }
    }

}