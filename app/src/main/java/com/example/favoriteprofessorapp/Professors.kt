package com.example.favoriteprofessorapp

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
}