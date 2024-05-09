package com.example.favoriteprofessorapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONObject

class AddReview : AppCompatActivity() {
    private lateinit var userInput : EditText
    private lateinit var prof_db : DatabaseReference
    private lateinit var rat_bar : RatingBar
    private lateinit var profName : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_review)

        profName = intent.getStringExtra("professorName")!!

        Log.w("AddReview", "Adding review for professor $profName")

        var firebase : FirebaseDatabase = FirebaseDatabase.getInstance()
        prof_db = firebase.getReference("Professors")

        userInput = findViewById(R.id.user_input)
        rat_bar = findViewById(R.id.rating)
        var title : TextView = findViewById(R.id.title)
        title.text = "Write a Review for " + profName
        var width = resources.displayMetrics.widthPixels
        userInput.width=width-20
    }

    fun addReview(v : View){
        var rating : Float = rat_bar.getRating()
        var review : String = userInput.getText().toString()
        val reviewText = "'$review'"

        Log.w("AddReview","Adding review text: "+reviewText + "rating: " + rating)

        val profKey = "'$profName'"
        Log.w("AddReview", "fetching firebase info for professor $profKey")
        val profRef : DatabaseReference = prof_db.child(profKey)
        val ratingsRef : DatabaseReference = profRef.child("Ratings")
        val reviewsRef : DatabaseReference = profRef.child("Reviews")

        // Append rating to the Ratings array
        ratingsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val nextIndex = dataSnapshot.childrenCount
                ratingsRef.child(nextIndex.toString()).setValue(rating)
                Log.w("AddReview", "At index $nextIndex, added rating: $rating")
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("AddReview","Database error: ${databaseError.message}")
            }
        })

        // Append review to the Reviews array
        reviewsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val nextIndex = dataSnapshot.childrenCount
                reviewsRef.child(nextIndex.toString()).setValue(reviewText)
                Log.w("AddReview", "At index $nextIndex, added review: $reviewText")
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("AddReview","Database error: ${databaseError.message}")
            }
        })

//        var snapshot : DataSnapshot? = MainActivity.professors_snapshot
//        var valueObject : Any? = snapshot!!.value
//        var value : String = valueObject.toString()
//        var curr : JSONObject = JSONObject(value)
//        var teach_dict = curr.get(SearchActivity.clicked_professor)

        finish()
    }

    fun goHome(v: View){
        Log.w("MainActivity","Going home!!")
        try{
            var myIntent : Intent = Intent(this@AddReview, MainActivity::class.java)
            startActivity(myIntent)
        } catch (e: Exception) {
            Log.e("MainActivity", "Error navigating to MainActivity: ${e.message}")
        }
    }

    fun goFavs(v:View){
        var myIntent : Intent = Intent(this@AddReview, FavoritesActivity::class.java)
        startActivity(myIntent)
    }

    fun goReview(v: View){
        var myIntent : Intent = Intent(this@AddReview, AddReview::class.java)
        startActivity(myIntent)
    }
}