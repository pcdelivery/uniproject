package com.example.universityproject

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import java.net.URL

class AccountInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_info)

        findViewById<ImageView>(R.id.avatarView).setOnClickListener {
            val galleryIntent = Intent(this, GalleryViewActivity::class.java)
            startActivityForResult(galleryIntent, 10)
        }

        val doDownload = DownloadImageTask(findViewById(R.id.avatarView)).execute("https://imgur.com/8ntwC3q.png")

        findViewById<TextView>(R.id.accountInfoText).text = this.intent.getStringExtra(ACCOUNT_DETAILS_KEY)
        findViewById<ImageView>(R.id.avatarView).setOnClickListener {
            val intent = Intent(this, UserAlbumActivity::class.java)
            startActivity(intent)
        }
    }

    suspend fun loadAvi() : Boolean {
        val url = URL("https://imgur.com/8ntwC3q")
        val aviBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        findViewById<ImageView>(R.id.avatarView).setImageBitmap(aviBitmap)

        return true
    }
}
