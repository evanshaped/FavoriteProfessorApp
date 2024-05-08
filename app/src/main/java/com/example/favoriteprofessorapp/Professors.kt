package com.example.favoriteprofessorapp

import android.content.Context

class Professors {
    private var professors : Array<Professor> = arrayOf<Professor>()

    fun checkForProfessor(name : String) : Boolean {
        for (i in 0 until professors.size) {
            if (professors.get(i).getName().equals(name)) {
                return true
            }
        }
        return false
    }

    fun addProfessor(professor : Professor) {
        professors += professor
    }

    fun removeProfessor(professor : Professor) {
        professors = professors.filter{it != professor}.toTypedArray()
    }

    fun getProfessor(name : String) : Professor? {
        for (i in 0..professors.size) {
            if (professors.get(i).getName().equals(name))
                return professors.get(i)
        }
        return null
    }

    // Used to store favortie professors in persistent data
    // After adding/removing professor from favorites, this function updates shared preferences
    fun updateProfessorsPreferences(key:String, context:Context) {
        val sharedPreferences = context.getSharedPreferences(context.packageName + "_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, this.getProfessorsPrefString())
        editor.apply()
    }

    fun getProfessorsPrefString(): String {
        return professors.joinToString ( separator="," ) {it.getName()}
    }

    fun getProfessorNamesArray(): Array<String> {
        return professors.map { it.getName() }.toTypedArray()
    }

    override fun toString(): String {
        var ret = ""
        for (i in 0..<professors.size) {
            ret += professors[i].getName() + " "
        }
        return ret
    }
}