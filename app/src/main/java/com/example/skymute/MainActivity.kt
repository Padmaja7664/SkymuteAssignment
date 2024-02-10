package com.example.skymute

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private val PICK_AUDIO_REQUEST = 2

    lateinit var image: ImageView
    lateinit var selectPhoto: Button
    lateinit var selectAudio: Button

    lateinit var selectedImageUri: Uri
    lateinit var selectedAudioUri: Uri

    lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //find the image, buttons of the  audio,image  from xml file

        image = findViewById(R.id.imgId)
        selectPhoto = findViewById(R.id.btnImg)
        selectAudio = findViewById(R.id.btnAudio)

        //for selecting the photo from user device

        selectPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        //for selecting the audio file from the user device
        selectAudio.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_AUDIO_REQUEST)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            // If the image is selected, display it on the screen
            if (requestCode == PICK_IMAGE_REQUEST) {
                // Get the selected image URI from the intent
                selectedImageUri = data?.data!!

                // Call the method to display the selected photo on the screen
                displaySelectedPhoto()
            }

            // If the audio is selected, attach it to the image
            else if (requestCode == PICK_AUDIO_REQUEST) {
                // Get the selected audio URI from the intent
                selectedAudioUri = data?.data!!

                // Call the method to prepare and play the selected audio
                prepareAndPlayAudio()
            }
        }
    }

    private fun displaySelectedPhoto() {
        selectedImageUri?.let {
            Picasso.get().load(selectedImageUri).into(image);
        }
    }

    private fun prepareAndPlayAudio() {
        selectedAudioUri?.let {
            mediaPlayer = MediaPlayer()
            try {
                mediaPlayer.setDataSource(applicationContext, it)
                mediaPlayer?.prepare()
                mediaPlayer?.start()

                mediaPlayer?.setOnCompletionListener(MediaPlayer.OnCompletionListener {
                    Toast.makeText(getApplicationContext(), "The audio has been ended", Toast.LENGTH_SHORT).show();
                })

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}