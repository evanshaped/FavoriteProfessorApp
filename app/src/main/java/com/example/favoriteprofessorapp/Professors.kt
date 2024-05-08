package com.example.favoriteprofessorapp

import android.content.Context
import android.util.Log

class Professors {
    private var professors : ArrayList<String> = arrayListOf<String>()

    fun addProfessor(profName : String) {
        professors.add(profName)
    }

    fun removeProfessor(profName : String) {
        professors.remove(profName)
    }

    fun getProfessor(name : String) : String? {
        if (professors.contains(name)) {
            return name
        } else {
            return null
        }
    }

    // Used to store favortie professors in persistent data
    // After adding/removing professor from favorites, this function updates shared preferences
    fun updateProfessorsPreferences(key:String, context:Context) {
        val sharedPreferences = context.getSharedPreferences(context.packageName + "_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val profString = this.getProfessorsPrefString()
        editor.putString(key, profString)
        editor.apply()
        Log.w("Professors", "Saved favorites to preferences: $profString")
    }

    fun getProfessorsPrefString(): String {
        return professors.joinToString ( separator="," ) {it}
    }

    fun getProfessorNamesArray(): Array<String> {
        return professors.map { it }.toTypedArray()
    }

    override fun toString(): String {
        var ret = ""
        for (i in 0..<professors.size) {
            ret += professors[i] + " "
        }
        return ret
    }
}