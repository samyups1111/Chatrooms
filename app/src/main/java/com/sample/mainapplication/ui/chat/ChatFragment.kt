package com.sample.mainapplication.ui.chat

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
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
    private val args : ChatFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        enterMessageEt = view.findViewById(R.id.enter_message_edit_text)
        setupSubmitButton(view)
        setupRecyclerView(view)
        loadMessages()
        return view
    }

    private fun setupSubmitButton(view: View) {
        submitButton = view.findViewById(R.id.submit_button)
        submitButton.setOnClickListener {
            val message = enterMessageEt.text.toString()
            vm.writeNewMessage(args.pokemonName, "sam", message)
            enterMessageEt.text.clear()
            it.hideKeyboard()
        }
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.chat_recyclerview)
        messageAdapter = MessageAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = messageAdapter
        }
    }

    private fun loadMessages() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                vm.getMessagesFlow(args.pokemonName)
                    .catch {
                        Log.d(TAG, "exception $it")
                    }.collect {
                        messageAdapter.updateList(it)
                    }
            }
        }
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}