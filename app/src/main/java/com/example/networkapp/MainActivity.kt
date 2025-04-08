package com.example.networkapp

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONObject

// TODO (1: Fix any bugs)
// TODO (2: Add function saveComic(...) to save comic info when downloaded
// TODO (3: Automatically load previously saved comic when app starts)

class MainActivity : AppCompatActivity() {

    private lateinit var requestQueue: RequestQueue
    lateinit var titleTextView: TextView
    lateinit var descriptionTextView: TextView
    lateinit var numberEditText: EditText
    lateinit var showButton: Button
    lateinit var comicImageView: ImageView
    private lateinit var sharedPreferences: SharedPreferences
    //string constants
    private val PREF_NAME = "comic_data"
    private val KEY_TITLE = "title"
    private val KEY_ALT = "alt"
    private val KEY_IMG = "img"
    private val KEY_NUM = "num"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //this queue will be used to store the request
        //volley is used to make network calls
        requestQueue = Volley.newRequestQueue(this)

        titleTextView = findViewById<TextView>(R.id.comicTitleTextView)
        descriptionTextView = findViewById<TextView>(R.id.comicDescriptionTextView)
        numberEditText = findViewById<EditText>(R.id.comicNumberEditText)
        showButton = findViewById<Button>(R.id.showComicButton)
        comicImageView = findViewById<ImageView>(R.id.comicImageView)


        //this is a shared preference file, and it is set to private so that it can only be accessed by this app
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)

        loadComic()

        showButton.setOnClickListener {
            //calls downloadComic function to create a url
            downloadComic(numberEditText.text.toString())
        }

    }

    // Fetches comic from web as JSONObject
    private fun downloadComic (comicId: String) {
        val url = "https://xkcd.com/$comicId/info.0.json"
        requestQueue.add (
            JsonObjectRequest(url
                , {
                    saveComic(it)
                    showComic(it)
                  }
                , {}
            )
        )
    }

    // Display a comic for a given comic JSON object
    private fun showComic (comicObject: JSONObject) {
        titleTextView.text = comicObject.getString(KEY_TITLE)
        descriptionTextView.text = comicObject.getString(KEY_ALT)
        Picasso.get().load(comicObject.getString(KEY_IMG)).into(comicImageView)
    }

    // Implement this function
    private fun saveComic(comicObject: JSONObject) {
        val editor = sharedPreferences.edit()
        //STORING THE DATA IN SHARED PREFERENCES
        editor.putString(KEY_TITLE, comicObject.getString(KEY_TITLE))
        editor.putString(KEY_ALT, comicObject.getString(KEY_ALT))
        editor.putString(KEY_IMG, comicObject.getString(KEY_IMG))
        editor.putString(KEY_NUM, comicObject.getString(KEY_NUM))
        editor.apply()
        Toast.makeText(this, "Comic saved", Toast.LENGTH_SHORT).show()
    }

    private fun loadComic() {
        if(sharedPreferences.contains(KEY_TITLE)) {
            //if the comic is saved, load it
            //LOAD THE DATA FROM SHARED PREFERENCES
            val title = sharedPreferences.getString(KEY_TITLE, "")
            val alt = sharedPreferences.getString(KEY_ALT, "")
            val img = sharedPreferences.getString(KEY_IMG, "")
            val num = sharedPreferences.getString(KEY_NUM, "")
            //set the textviews and imageview with the saved data
            titleTextView.text = title
            descriptionTextView.text = alt
            //picasso is used to load the image
            Picasso.get().load(img).into(comicImageView)
            //set the edittext with the comic number
            numberEditText.setText(num)

            Toast.makeText(this, "Loaded saved comic", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "No saved comic", Toast.LENGTH_SHORT).show()
        }
    }


}