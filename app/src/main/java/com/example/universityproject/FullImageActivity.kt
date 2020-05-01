package com.example.universityproject

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.universityproject.data.ImageAdapter
import kotlinx.android.synthetic.main.activity_full_image.*
import java.net.URI

class FullImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_image)

        val position = intent.getIntExtra("IMAGE_ID", 0)
        val imageAdapter = ImageAdapter(this, 12)


        // @todo getBy resource maybe?
//        findViewById<ImageView>(R.id.fullAlbumPicture).setImageResource(imageAdapter.getItem(position))

        fullAlbumPicture.setImageURI(Uri.parse(imageAdapter.getItem(position).toString()))
    }
}
