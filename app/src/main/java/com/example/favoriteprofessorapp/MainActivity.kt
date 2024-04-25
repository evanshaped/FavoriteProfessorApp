package com.example.favoriteprofessorapp

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var firebase : FirebaseDatabase = FirebaseDatabase.getInstance()
        var classes : DatabaseReference = firebase.getReference("Classes")
        var professors : DatabaseReference = firebase.getReference("Professors")

        var classes_listener : ClassesListener = ClassesListener()
        classes.addValueEventListener(classes_listener)

        var professor_listener : ProfessorListener = ProfessorListener()
        professors.addValueEventListener(professor_listener)
    }

    inner class ClassesListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            var key : String? = snapshot.key
            //Log.w("MainActivity", "key is " + key)
            var valueObject : Any? = snapshot.value
            if (valueObject != null) {
                var value : String = valueObject.toString()
                var jsonObject : JSONObject = JSONObject(value)
                var jsonArray : JSONArray = jsonObject.getJSONArray("CMSC320")
                var professor1 : String = jsonArray.getString(0)
                //Log.w("MainActivity", "value is " + professor1)
            } else {
                //Log.w("MainActivity", "No value found")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w("MainActivity", "reading failure: " + error.message)
        }
    }

    inner class ProfessorListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            var key : String? = snapshot.key
            Log.w("MainActivity", "key is " + key)
            var valueObject : Any? = snapshot.value
            if (valueObject != null) {
                var value : String = valueObject.toString()
                //var jsonObject : JSONObject = JSONObject(value)
                //var jsonObject2 : JSONObject = jsonObject.getJSONObject("Nelson")
                //var jsonArray : JSONArray = jsonObject2.getJSONArray("Review")
                //var review1 : String = jsonArray.getString(0)
                Log.w("MainActivity", "value is " + value)
            } else {
                Log.w("MainActivity", "No value found")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w("MainActivity", "reading failure: " + error.message)
        }
    }
}