package com.example.area58

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.FileProvider
import java.io.File

class MainActivity : AppCompatActivity() {

    lateinit var videoUri: Uri
    val REQUEST_VIDEO_CAPTURE = 1
    lateinit var video_view: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<AppCompatButton>(R.id.button)
        video_view = findViewById<VideoView>(R.id.video_view)
        button.setOnClickListener {
            showToast("Recording Video!")
            recordVideo()
        }
    }

    private fun recordVideo() {
        val videoFile = createVideoFile()

        if (videoFile != null){
            videoUri = FileProvider.getUriForFile(this, "com.example.area58.fileprovider", videoFile)
        }
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
        startActivityForResult(intent, REQUEST_VIDEO_CAPTURE)
    }

    private fun createVideoFile(): File {
        val filename = "my_video"
        val storage = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            filename, ".mp4", storage,
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {
            video_view.setVideoURI(videoUri)
            video_view.start()
        }

    }
}