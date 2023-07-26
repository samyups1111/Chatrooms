package com.sample.mainapplication.ui.chat

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.mainapplication.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private val vm by viewModels<ChatViewModel>()
    private lateinit var enterMessageEt: EditText
    private lateinit var submitButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        recyclerView = view.findViewById(R.id.chat_recyclerview)
        messageAdapter = MessageAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = messageAdapter
        }
        enterMessageEt = view.findViewById(R.id.enter_message_edit_text)
        submitButton = view.findViewById(R.id.submit_button)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                vm.getMessagesFlow()
                    .catch {
                        Log.d(TAG, "exception $it")
                    }.collect {
                        messageAdapter.updateList(listOf(it))
                    }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        submitButton.setOnClickListener {
            val message = enterMessageEt.text.toString()
            vm.setMessage(message)
        }
    }
}