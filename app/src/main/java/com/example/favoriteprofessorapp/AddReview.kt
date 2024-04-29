package com.example.favoriteprofessorapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONObject

class AddReview : AppCompatActivity() {
    private lateinit var userInput : EditText
    private lateinit var prof_db : DatabaseReference
    private lateinit var rat_bar : RatingBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_review)
        var firebase : FirebaseDatabase = FirebaseDatabase.getInstance()
        prof_db = firebase.getReference("Professors")

        userInput = findViewById(R.id.user_input)
        rat_bar = findViewById(R.id.rating)
        var title : TextView = findViewById(R.id.title)
        title.text = "Write a Review for " + SearchActivity.clicked_professor
        var width = resources.displayMetrics.widthPixels
        userInput.width=width-20
    }

    fun addReview(v : View){
        var inp = userInput.getText()
        var rating = rat_bar.getRating()
//        var snapshot : DataSnapshot? = MainActivity.professors_snapshot
//        var valueObject : Any? = snapshot!!.value
//        var value : String = valueObject.toString()
//        var curr : JSONObject = JSONObject(value)
//        var teach_dict = curr.get(SearchActivity.clicked_professor)

        Log.w("MainActivity","text: "+inp + "rating: " + rating)
        var myIntent : Intent = Intent(this@AddReview, ReviewActivity::class.java)
        startActivity(myIntent)
    }
}