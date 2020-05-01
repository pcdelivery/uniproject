package com.example.universityproject

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import com.example.universityproject.data.ImageAdapter
import com.example.universityproject.data.databases.QuestionDatabase
import kotlinx.android.synthetic.main.activity_user_album.*
import java.io.File
import java.lang.Exception
import java.net.URI

class UserAlbumActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user_album)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        gridView.adapter = ImageAdapter(this, 12)
        gridView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if (position < gridView.adapter.count) {
                val intent = Intent(applicationContext, FullImageActivity::class.java)
                intent.putExtra("IMAGE_ID", position)
                startActivity(intent)
            }
        }

        /*
        val database = QuestionDatabase(this).readableDatabase
        val cursor = database.rawQuery("SELECT * FROM question_table", null)

        cursor.moveToFirst()
        findViewById<TextView>(R.id.test_que).text = cursor.getString(1)
        findViewById<TextView>(R.id.test_ans).text = cursor.getString(3)
        val image = File(cursor.getString(2))
        findViewById<ImageView>(R.id.que_img).setImageURI(Uri.fromFile(image))
        cursor.close()
        */
    }

//    override fun onPostCreate(savedInstanceState: Bundle?) {
//        super.onPostCreate(savedInstanceState)
//    }
}
