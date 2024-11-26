package com.example.taskmanagement.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagement.databinding.ItemTaskManagementBinding

class TaskManagementAdapter(private val list: MutableList<TaskManagementData>) :
    RecyclerView.Adapter<TaskManagementAdapter.TaskManagementViewHolder>() {

    private var listener: TaskManagementAdapterClickInterface? = null

    // Method to set the listener for click events
    fun setListener(listener: TaskManagementAdapterClickInterface) {
        this.listener = listener
    }

    // ViewHolder class for binding views
    inner class TaskManagementViewHolder(val binding: ItemTaskManagementBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Inflate item layout and create ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskManagementViewHolder {
        val binding = ItemTaskManagementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskManagementViewHolder(binding)
    }

    // Bind data to views and set up click listeners
    override fun onBindViewHolder(holder: TaskManagementViewHolder, position: Int) {
        val task = list[position]

        with(holder.binding) {
            // Set task details in the item view
            taskManagementTask.text = task.task
            endDateTime.text = task.endDateTime
            taskStatus.text = task.taskStatus

            // Set checkbox state based on `isCompleted`
            checkboxCompleted.isChecked = task.isCompleted

            // Handle checkbox toggle to update task status
            checkboxCompleted.setOnCheckedChangeListener { _, isChecked ->
                // Update task status based on the new checkbox state
                task.isCompleted = isChecked
                task.taskStatus = if (isChecked) "Completed" else "Pending"

                // Notify listener about the task update
                listener?.onTaskCompletionStatusChanged(task)
            }

            // Handle delete button click
            deleteTask.setOnClickListener {
                listener?.onDeleteTaskBtnClicked(task)
            }

            // Handle edit button click
            editTask.setOnClickListener {
                listener?.onEditTaskBtnClicked(task)
            }
        }
    }

    // Return the size of the task list
    override fun getItemCount(): Int = list.size

    // Interface for click event callbacks
    interface TaskManagementAdapterClickInterface {
        fun onDeleteTaskBtnClicked(taskManagementData: TaskManagementData)
        fun onEditTaskBtnClicked(taskManagementData: TaskManagementData)
        fun onTaskCompletionStatusChanged(taskManagementData: TaskManagementData)
    }
}
