package com.example.favoriteprofessorapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var classes_db : DatabaseReference
    private lateinit var professors_db : DatabaseReference
    private lateinit var classes_listener : ClassesListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        var firebase : FirebaseDatabase = FirebaseDatabase.getInstance()
        classes_db = firebase.getReference("Classes")
        professors_db = firebase.getReference("Professors")

        classes_listener = ClassesListener()
        classes_db.addValueEventListener(classes_listener)

        var professor_listener : ProfessorListener = ProfessorListener()
        professors_db.addValueEventListener(professor_listener)

        professors = Professors()
        favorites = Professors()

        loadSearchResults()
    }
    fun loadSearchResults() {
        var searchIntent : Intent = Intent(this, SearchActivity::class.java)
        startActivity(searchIntent)
    }

    inner class ClassesListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            classes_snapshot = snapshot
            var key : String? = snapshot.key
            var valueObject = snapshot.value
            if (valueObject != null) {
                var value : String = valueObject.toString()
                var jsonObject : JSONObject = JSONObject(value)
                var jsonArray : JSONArray = jsonObject.getJSONArray("CMSC320")
                var professor1 : String = jsonArray.getString(0)

                // this will eventually be called based on when the user clicks search
                // but just keeping it here for now to get it to work
                loadSearchResults()
            } else {
                Log.w("MainActivity", "No value found")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w("MainActivity", "reading failure: " + error.message)
        }
    }

    inner class ProfessorListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            professors_snapshot = snapshot
            var key : String? = snapshot.key
            var valueObject : Any? = snapshot.value
            if (valueObject != null) {
                var value : String = valueObject.toString()
                var jsonObject : JSONObject = JSONObject(value)
                var jsonObject2 : JSONObject = jsonObject.getJSONObject("Nelson Padua-Perez")
                var jsonArray : JSONArray = jsonObject2.getJSONArray("Reviews")
                var review1 : String = jsonArray.getString(0)
            } else {
                Log.w("MainActivity", "No value found")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w("MainActivity", "reading failure: " + error.message)
        }
    }

    //    fun goHome{
//      var myIntent : Intent = Intent(this@MainActivity, MainActivity::class.java)
//      startActivity(myIntent)
//    }
//
//    fun goFavs{
//      var myIntent : Intent = Intent(this@MainActivity, Favorites::class.java)
//      startActivity(myIntent)
//    }

    fun goReview(v: View){
        var myIntent : Intent = Intent(this@MainActivity, AddReview::class.java)
        startActivity(myIntent)
    }
    companion object {
        var classes_snapshot : DataSnapshot? = null
        var professors_snapshot : DataSnapshot? = null
        lateinit var professors : Professors
        lateinit var favorites : Professors
    }
}