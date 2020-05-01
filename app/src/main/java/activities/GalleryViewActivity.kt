//package activities
package com.example.universityproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GalleryViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_view)

        val recyclerView = findViewById<RecyclerView>(R.id.galleryGrid)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        findViewById<RecyclerView>(R.id.galleryGrid).layoutManager = GridLayoutManager(this, 3)
    }

}
