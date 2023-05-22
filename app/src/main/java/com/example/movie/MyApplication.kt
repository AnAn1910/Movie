package com.example.movie

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference

class MyApplication : Application() {

    private var mDatabaseReference: DatabaseReference? = null

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

    fun getDatabaseReference(): DatabaseReference? {
        return mDatabaseReference
    }

    companion object {
        operator fun get(context: Context?): MyApplication? {
            return context?.applicationContext as MyApplication?
        }
    }
}