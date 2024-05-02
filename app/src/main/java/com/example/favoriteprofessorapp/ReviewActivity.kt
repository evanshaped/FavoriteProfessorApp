package com.example.favoriteprofessorapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject

class ReviewActivity : AppCompatActivity() {
    private lateinit var textView : TextView
    private lateinit var listView : ListView
    private var reviews : ArrayList<String> = ArrayList<String>()
    private var ratings : ArrayList<String> = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews)

        textView = findViewById(R.id.professor_searched)
        textView.setText(SearchActivity.clicked_professor)

        listView = findViewById(R.id.review_list)

        var valueObject = MainActivity.professors_snapshot?.value
        if (valueObject != null) {
            var value: String = valueObject.toString()
            var jsonObject : JSONObject = JSONObject(value)
            displayReviews(jsonObject)
        }
    }

    fun displayReviews(jsonObject : JSONObject) {
        Log.w("MainActivity", "in display reviews")
        // get the list of reviews to display in list view
        var jsonObject2 : JSONObject = jsonObject.getJSONObject(SearchActivity.clicked_professor)
        var jsonReviewsArray : JSONArray = jsonObject2.getJSONArray("Reviews")
        var jsonRatingsArray : JSONArray = jsonObject2.getJSONArray("Ratings")
        var ratingsImages : ArrayList<Int> = ArrayList<Int>()

        for (i in 0 until jsonReviewsArray.length()) {
            reviews.add(jsonReviewsArray.getString(i))
            ratings.add(jsonRatingsArray.getString(i))
            ratingsImages.add(R.drawable.five_star)
        }

        //var adapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, reviews)
        //listView.adapter = adapter

        var adapter : CustomListAdapter = CustomListAdapter(this, reviews, ratingsImages)
        listView.adapter = adapter
    }

    fun processReview(v : View){
        var myIntent : Intent = Intent(this@ReviewActivity, AddReview::class.java)
        startActivity(myIntent)
    }
//    fun goHome{
//      var myIntent : Intent = Intent(this@ReviewActivity, MainActivity::class.java)
//      startActivity(myIntent)
//    }
//
//    fun goFavs{
//      var myIntent : Intent = Intent(this@ReviewActivity, Favorites::class.java)
//      startActivity(myIntent)
//    }


    fun goReview(v: View){
        var myIntent : Intent = Intent(this@ReviewActivity, AddReview::class.java)
        startActivity(myIntent)
    }
}