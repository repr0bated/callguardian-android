package com.example.callguardian.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.callguardian.R
import com.example.callguardian.databinding.DialogContactSyncBinding
import com.example.callguardian.databinding.ItemContactChangeBinding
import com.example.callguardian.service.ContactChange
import com.example.callguardian.service.ContactInfoField
import com.example.callguardian.service.ContactSyncResult
import com.example.callguardian.ui.theme.ConfidenceHigh
import com.example.callguardian.ui.theme.ConfidenceMedium
import com.example.callguardian.ui.theme.ConfidenceLow
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactSyncDialogFragment : DialogFragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: DialogContactSyncBinding? = null
    private val binding get() = _binding!!

    private lateinit var syncResult: ContactSyncResult.ChangesDetected
    private val selectedChanges = mutableSetOf<Int>()
    private val selectedNewInfo = mutableSetOf<Int>()

    companion object {
        private const val ARG_SYNC_RESULT = "sync_result"

        fun newInstance(syncResult: ContactSyncResult.ChangesDetected): ContactSyncDialogFragment {
            return ContactSyncDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_SYNC_RESULT, syncResult)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        syncResult = arguments?.getParcelable(ARG_SYNC_RESULT)
            ?: throw IllegalArgumentException("Sync result must be provided")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogContactSyncBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        setupButtons()
        populateData()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Review Contact Changes")
            .setView(binding.root)
            .setPositiveButton("Apply Changes") { _, _ ->
                applySelectedChanges()
            }
            .setNegativeButton("Cancel", null)
            .create()
    }

    private fun setupRecyclerViews() {
        // Setup changes RecyclerView
        binding.changesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ChangesAdapter()
        }

        // Setup new info RecyclerView
        binding.newInfoRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = NewInfoAdapter()
        }
    }

    private fun setupButtons() {
        binding.selectAllChangesButton.setOnClickListener {
            selectedChanges.clear()
            selectedChanges.addAll(syncResult.changes.indices)
            binding.changesRecyclerView.adapter?.notifyDataSetChanged()
        }

        binding.clearChangesButton.setOnClickListener {
            selectedChanges.clear()
            binding.changesRecyclerView.adapter?.notifyDataSetChanged()
        }

        binding.selectAllNewInfoButton.setOnClickListener {
            selectedNewInfo.clear()
            selectedNewInfo.addAll(syncResult.newInfo.indices)
            binding.newInfoRecyclerView.adapter?.notifyDataSetChanged()
        }

        binding.clearNewInfoButton.setOnClickListener {
            selectedNewInfo.clear()
            binding.newInfoRecyclerView.adapter?.notifyDataSetChanged()
        }
    }

    private fun populateData() {
        binding.contactNameTextView.text = syncResult.contactInfo.displayName
        binding.phoneNumberTextView.text = syncResult.contactInfo.phoneNumber

        // Update button states based on selections
        updateButtonStates()
    }

    private fun updateButtonStates() {
        binding.selectAllChangesButton.isEnabled = syncResult.changes.isNotEmpty()
        binding.clearChangesButton.isEnabled = selectedChanges.isNotEmpty()
        binding.selectAllNewInfoButton.isEnabled = syncResult.newInfo.isNotEmpty()
        binding.clearNewInfoButton.isEnabled = selectedNewInfo.isNotEmpty()

        val hasSelections = selectedChanges.isNotEmpty() || selectedNewInfo.isNotEmpty()
        (dialog as? MaterialAlertDialogBuilder)?.apply {
            // The dialog buttons are handled by the DialogFragment lifecycle
        }
    }

    private fun applySelectedChanges() {
        val approvedChanges = selectedChanges.map { syncResult.changes[it] }
        val approvedNewInfo = selectedNewInfo.map { syncResult.newInfo[it] }

        viewModel.applyContactSyncChanges(
            syncResult.contactInfo,
            approvedChanges,
            approvedNewInfo
        )
    }

    private inner class ChangesAdapter : RecyclerView.Adapter<ChangeViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChangeViewHolder {
            val binding = ItemContactChangeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ChangeViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ChangeViewHolder, position: Int) {
            holder.bind(syncResult.changes[position], position)
        }

        override fun getItemCount() = syncResult.changes.size
    }

    private inner class NewInfoAdapter : RecyclerView.Adapter<NewInfoViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewInfoViewHolder {
            val binding = ItemContactChangeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return NewInfoViewHolder(binding)
        }

        override fun onBindViewHolder(holder: NewInfoViewHolder, position: Int) {
            holder.bind(syncResult.newInfo[position], position)
        }

        override fun getItemCount() = syncResult.newInfo.size
    }

    private inner class ChangeViewHolder(private val binding: ItemContactChangeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(change: ContactChange, position: Int) {
            binding.apply {
                fieldNameTextView.text = change.field
                currentValueTextView.text = "Current: ${change.currentValue}"
                proposedValueTextView.text = "Proposed: ${change.proposedValue}"
                confidenceTextView.text = "Confidence: ${(change.confidence * 100).toInt()}%"

                // Set confidence color
                val confidenceColor = when {
                    change.confidence >= 0.8 -> ConfidenceHigh
                    change.confidence >= 0.6 -> ConfidenceMedium
                    else -> ConfidenceLow
                }
                confidenceTextView.setTextColor(confidenceColor)

                checkbox.isChecked = selectedChanges.contains(position)
                checkbox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedChanges.add(position)
                    } else {
                        selectedChanges.remove(position)
                    }
                    updateButtonStates()
                }
            }
        }
    }

    private inner class NewInfoViewHolder(private val binding: ItemContactChangeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(infoField: ContactInfoField, position: Int) {
            binding.apply {
                fieldNameTextView.text = infoField.field
                currentValueTextView.text = "New information"
                proposedValueTextView.text = infoField.value
                confidenceTextView.text = "Category: ${infoField.category.name}"

                checkbox.isChecked = selectedNewInfo.contains(position)
                checkbox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedNewInfo.add(position)
                    } else {
                        selectedNewInfo.remove(position)
                    }
                    updateButtonStates()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

