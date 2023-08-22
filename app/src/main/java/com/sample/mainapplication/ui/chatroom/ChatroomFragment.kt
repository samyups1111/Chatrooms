package com.sample.mainapplication.ui.chatroom

import android.app.AlertDialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sample.mainapplication.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatroomFragment : Fragment() {

    private lateinit var titleTextView: TextView
    private lateinit var chatroomRecyclerView: RecyclerView
    private lateinit var chatroomAdapter: ChatroomAdapter
    private lateinit var createNewRoomButton: Button
    private val vm: ChatroomViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chatroom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleTextView = view.findViewById(R.id.chatroom_title_textview)
        chatroomRecyclerView = view.findViewById(R.id.chatroom_recyclerview)
        createNewRoomButton = view.findViewById(R.id.create_chatroom_button)
        setNavBar()
        loadChatrooms()
        chatroomRecyclerView.apply {
            chatroomAdapter = ChatroomAdapter()
            adapter = chatroomAdapter
            layoutManager = LinearLayoutManager(view.context)
        }
        chatroomAdapter.onItemClick = { chatroomName ->
            val action = ChatroomFragmentDirections.actionChatroomFragmentToMessageFragment(chatroomName)
            view.findNavController().navigate(action)
        }
        createNewRoomButton.setOnClickListener {
            showCreateChatroomDialog()
        }
    }

    private fun loadChatrooms() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                vm.getChatroomsRealTime()
                    .catch {
                        Log.d(ContentValues.TAG, "exception $it")
                    }.collect {
                        chatroomAdapter.updateList(it)
                    }
            }
        }
    }

    private fun showCreateChatroomDialog() {
        val builder = AlertDialog.Builder(view?.context)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_fragment_create_new_chatroom, null)
        val editText: EditText = dialogLayout.findViewById(R.id.create_new_chatroom_edittext)

        with(builder) {
            setTitle(R.string.create_new_chatroom)
            setPositiveButton(R.string.ok) { dialog, which ->
                val newRoomName = editText.text.toString()
                vm.createNewChatroom(newRoomName)
                val action = ChatroomFragmentDirections.actionChatroomFragmentToMessageFragment(newRoomName)
                view?.findNavController()?.navigate(action)
            }
            setNegativeButton(R.string.cancel) { dialog, which ->

            }
            setView(dialogLayout)
            show()
        }
    }

    private fun setNavBar() {
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.visibility = View.VISIBLE
    }
}