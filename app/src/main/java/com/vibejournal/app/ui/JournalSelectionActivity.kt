package com.vibejournal.app.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.vibejournal.app.R
import com.vibejournal.app.adapter.JournalAdapter
import com.vibejournal.app.databinding.ActivityJournalSelectionBinding
import com.vibejournal.app.model.GoogleDoc
import com.vibejournal.app.viewmodel.JournalSelectionViewModel

class JournalSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJournalSelectionBinding
    private lateinit var viewModel: JournalSelectionViewModel
    private lateinit var adapter: JournalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJournalSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[JournalSelectionViewModel::class.java]
        
        setupUI()
        setupRecyclerView()
        observeViewModel()
        
        viewModel.loadJournals()
    }

    private fun setupUI() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                finish()
            }
            
            btnCreateNew.setOnClickListener {
                viewModel.createNewJournal("My Journal")
            }
            
            btnRefresh.setOnClickListener {
                viewModel.loadJournals()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = JournalAdapter { journal ->
            viewModel.selectJournal(journal)
            finish()
        }
        
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@JournalSelectionActivity)
            adapter = this@JournalSelectionActivity.adapter
        }
    }

    private fun observeViewModel() {
        viewModel.journals.observe(this) { journals ->
            adapter.submitList(journals)
            binding.emptyState.visibility = if (journals.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }
}
