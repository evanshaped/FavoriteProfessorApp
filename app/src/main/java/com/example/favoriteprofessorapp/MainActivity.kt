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
    private lateinit var classes_listener : ClassesListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        var firebase : FirebaseDatabase = FirebaseDatabase.getInstance()
        classes = firebase.getReference("Classes")
        professors = firebase.getReference("Professors")

        classes_listener = ClassesListener()
        classes.addValueEventListener(classes_listener)

        var professor_listener : ProfessorListener = ProfessorListener()
        professors.addValueEventListener(professor_listener)

        //var myIntent : Intent = Intent(this, SearchActivity::class.java)
        //startActivity(myIntent)
    }

    fun getClasses(searched_classes : String) : JSONArray {
        return classes_listener.getClasses(searched_classes)
    }

    inner class ClassesListener : ValueEventListener {
        var valueObject : Any? = null
        override fun onDataChange(snapshot: DataSnapshot) {
            var key : String? = snapshot.key
            valueObject = snapshot.value
            if (valueObject != null) {
                var value : String = valueObject.toString()
                var jsonObject : JSONObject = JSONObject(value)
                var jsonArray : JSONArray = jsonObject.getJSONArray("CMSC320")
                var professor1 : String = jsonArray.getString(0)
            } else {
                Log.w("MainActivity", "No value found")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w("MainActivity", "reading failure: " + error.message)
        }

        fun getClasses(searched_classes : String) : JSONArray {
            var value : String = valueObject.toString()
            var jsonObject : JSONObject = JSONObject(value)
            var jsonArray : JSONArray = jsonObject.getJSONArray(searched_classes)
            return jsonArray
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
        lateinit var classes : DatabaseReference
        lateinit var professors : DatabaseReference
    }
}