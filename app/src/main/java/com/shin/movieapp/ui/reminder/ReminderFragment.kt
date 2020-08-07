package com.shin.movieapp.ui.reminder

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shin.movieapp.databinding.FragmentReminderBinding
import com.shin.movieapp.helper.SwipeToDelete
import com.shin.movieapp.model.user.UserDatabase
import com.shin.movieapp.repository.reminder.ReminderRepository


class ReminderFragment : Fragment() {

    private var _binding : FragmentReminderBinding? = null
    private val binding get() = _binding!!
    private lateinit var reminderViewModel: ReminderViewModel
    private lateinit var reminderRepository: ReminderRepository
    private lateinit var reminderModelFactory: ReminderModelFactory
    private lateinit var reminderDataAdapter: ReminderDataAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReminderBinding.inflate(inflater, container, false)
        val view = binding.root

        val reminderDao = UserDatabase.getInstance(this.requireContext()).reminderDao()
        reminderRepository = ReminderRepository(reminderDao)
        reminderModelFactory = ReminderModelFactory(reminderRepository)
        reminderViewModel = ViewModelProviders.of(this, reminderModelFactory)
            .get(ReminderViewModel::class.java)

        // Display reminder list
        var linearLayoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)
        binding.reminderRecyclerview.setHasFixedSize(true)
        binding.reminderRecyclerview.layoutManager = linearLayoutManager

        reminderViewModel.listReminder.observe(viewLifecycleOwner, Observer {
            binding.reminderLoading.visibility = View.GONE
            binding.reminderRecyclerview.visibility = View.VISIBLE
            reminderDataAdapter = ReminderDataAdapter(this.requireActivity(), 1)
            it?.let {
                reminderDataAdapter.submitList(it)
            }
            binding.reminderRecyclerview.adapter = reminderDataAdapter
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Swipe to delete
        val item = object :SwipeToDelete(this.requireContext(), 0, ItemTouchHelper.LEFT){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                reminderDataAdapter.delete(viewHolder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(item)
        itemTouchHelper.attachToRecyclerView(binding.reminderRecyclerview)
    }

}