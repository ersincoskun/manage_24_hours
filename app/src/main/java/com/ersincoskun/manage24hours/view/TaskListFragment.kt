package com.ersincoskun.manage24hours.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.ersincoskun.manage24hours.R
import com.ersincoskun.manage24hours.adapter.TaskAdapter
import com.ersincoskun.manage24hours.databinding.FragmentTaskListBinding
import com.ersincoskun.manage24hours.model.Task
import com.ersincoskun.manage24hours.viewmodel.TaskListViewModel
import java.util.*


class TaskListFragment : Fragment() {
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    private val adapter = TaskAdapter(listOf())
    private var temporaryList = mutableListOf<Task>()
    private var newList = mutableListOf<Task>()
    private val viewModel: TaskListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        clickActions()
        observeData()
        binding.taskListRecyclerView.adapter = adapter
        itemTouchHelper.attachToRecyclerView(binding.taskListRecyclerView)
    }

    private fun observeData() {
        viewModel.getAllTask(requireContext())
        viewModel.allTask.observe(viewLifecycleOwner, Observer<List<Task>> {
            newList.clear()
            temporaryList.clear()
            adapter.addTask(it)
            newList.addAll(it)
            temporaryList.addAll(it)
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clickActions() {
        binding.addTaskFAB.setOnClickListener {
            if (newList.size != 0 && newList != temporaryList) {
                newList.map {
                    it.uuid = 0
                }
                viewModel.updateList(requireContext(), newList)
            }
            Navigation.findNavController(it)
                .navigate(R.id.action_taskListFragment_to_addTaskFragment)
        }
    }

    val itemTouchHelper by lazy {
        // 1. Note that I am specifying all 4 directions.
        //    Specifying START and END also allows
        //    more organic dragging than just specifying UP and DOWN.
        val simpleItemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or
                        ItemTouchHelper.DOWN or
                        ItemTouchHelper.START or
                        ItemTouchHelper.END, 0
            ) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {

                    val adapter = recyclerView.adapter as TaskAdapter
                    val from = viewHolder.adapterPosition
                    val to = target.adapterPosition
                    // 2. Update the backing model. Custom implementation in
                    //    MainRecyclerViewAdapter. You need to implement
                    //    reordering of the backing model inside the method.
                    Collections.swap(newList, from, to)

                    // 3. Tell adapter to render the model update.
                    adapter.notifyItemMoved(from, to)

                    return true
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    // 4. Code block for horizontal swipe.
                    //    ItemTouchHelper handles horizontal swipe as well, but
                    //    it is not relevant with reordering. Ignoring here.
                }
            }

        ItemTouchHelper(simpleItemTouchCallback)
    }

}