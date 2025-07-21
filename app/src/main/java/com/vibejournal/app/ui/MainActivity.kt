package com.vibejournal.app.ui

import android.Manifest
import android.content.Intent
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.vibejournal.app.R
import com.vibejournal.app.databinding.ActivityMainBinding
import com.vibejournal.app.viewmodel.MainViewModel
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false
    private var audioFile: File? = null

    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
        private const val REQUEST_SIGN_IN = 1001
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setupGoogleSignIn()
        setupUI()
        checkPermissions()
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(
                Scope("https://www.googleapis.com/auth/documents"),
                Scope("https://www.googleapis.com/auth/drive.file")
            )
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun setupUI() {
        binding.apply {
            // Sign in button
            btnSignIn.setOnClickListener {
                signIn()
            }

            // Record button
            btnRecord.setOnClickListener {
                if (isRecording) {
                    stopRecording()
                } else {
                    startRecording()
                }
            }

            // Select journal button
            btnSelectJournal.setOnClickListener {
                startActivity(Intent(this@MainActivity, JournalSelectionActivity::class.java))
            }
        }

        // Observe ViewModel
        viewModel.isSignedIn.observe(this) { isSignedIn ->
            updateUIForSignInState(isSignedIn)
        }

        viewModel.selectedJournal.observe(this) { journal ->
            binding.tvSelectedJournal.text = journal?.title ?: "No journal selected"
        }

        viewModel.isProcessing.observe(this) { isProcessing ->
            binding.progressBar.visibility = if (isProcessing) View.VISIBLE else View.GONE
            binding.btnRecord.isEnabled = !isProcessing
        }
    }

    private fun updateUIForSignInState(isSignedIn: Boolean) {
        binding.apply {
            btnSignIn.visibility = if (isSignedIn) View.GONE else View.VISIBLE
            layoutMainContent.visibility = if (isSignedIn) View.VISIBLE else View.GONE
        }
    }

    private fun checkPermissions() {
        if (!EasyPermissions.hasPermissions(this, *REQUIRED_PERMISSIONS)) {
            EasyPermissions.requestPermissions(
                this,
                "This app needs microphone and storage permissions to record audio.",
                REQUEST_RECORD_AUDIO_PERMISSION,
                *REQUIRED_PERMISSIONS
            )
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, REQUEST_SIGN_IN)
    }

    private fun startRecording() {
        if (!EasyPermissions.hasPermissions(this, *REQUIRED_PERMISSIONS)) {
            checkPermissions()
            return
        }

        try {
            // Create audio file
            val audioDir = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "recordings")
            if (!audioDir.exists()) {
                audioDir.mkdirs()
            }
            audioFile = File(audioDir, "recording_${System.currentTimeMillis()}.3gp")

            // Setup MediaRecorder
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(audioFile?.absolutePath)
                prepare()
                start()
            }

            isRecording = true
            binding.btnRecord.setImageResource(R.drawable.ic_stop)
            binding.btnRecord.backgroundTintList = getColorStateList(R.color.red_dark)
            binding.tvStatus.text = "Recording..."

        } catch (e: IOException) {
            Toast.makeText(this, "Failed to start recording", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            isRecording = false
            
            binding.btnRecord.setImageResource(R.drawable.ic_mic)
            binding.btnRecord.backgroundTintList = getColorStateList(R.color.red_primary)
            binding.tvStatus.text = "Processing..."

            // Process the recorded audio
            audioFile?.let { file ->
                viewModel.processAudioFile(file)
            }

        } catch (e: Exception) {
            Toast.makeText(this, "Failed to stop recording", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            viewModel.handleSignInResult(task)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        // Permissions granted
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(this, "Permissions required for recording", Toast.LENGTH_LONG).show()
    }
}
