package com.example.favoriteprofessorapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        loadSearchResults()
    }

    fun loadSearchResults() {
        var myIntent : Intent = Intent(this, SearchActivity::class.java)
        startActivity(myIntent)
    }

    inner class ClassesListener : ValueEventListener {
        var valueObject : Any? = null
        override fun onDataChange(snapshot: DataSnapshot) {
            classes_snapshot = snapshot
            var key : String? = snapshot.key
            valueObject = snapshot.value
            if (valueObject != null) {
                var value : String = valueObject.toString()
                var jsonObject : JSONObject = JSONObject(value)
                var jsonArray : JSONArray = jsonObject.getJSONArray("CMSC320")
                var professor1 : String = jsonArray.getString(0)
                //Log.w("MainActivity", "Professor: " + professor1)

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
            var key : String? = snapshot.key
            var valueObject : Any? = snapshot.value
            if (valueObject != null) {
                var value : String = valueObject.toString()
                var jsonObject : JSONObject = JSONObject(value)
                var jsonObject2 : JSONObject = jsonObject.getJSONObject("Nelson")
                var jsonArray : JSONArray = jsonObject2.getJSONArray("Review")
                var review1 : String = jsonArray.getString(0)
            } else {
                Log.w("MainActivity", "No value found")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w("MainActivity", "reading failure: " + error.message)
        }
    }

    companion object {
        var classes_snapshot : DataSnapshot? = null
        lateinit var professors : Professors
    }
}