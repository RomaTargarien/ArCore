package com.example.mycore

import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val AUTHORITY = "com.example.mycore.fileprovider"

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        FirebaseApp.initializeApp(this)
        val storage = FirebaseStorage.getInstance()
        val modelRef = storage.getReference().child("Cyber.glb")


        val files = filesDir.listFiles()
        val file = files?.find {
            it.name.endsWith("Cyber.glb")
        }
        file?.let {
            Log.d("TAG",it.toUri().toString())

        }
        val modelUrl = Uri.parse("Cyber.glb")

        Log.d("TAG",Environment.getExternalStorageDirectory().absolutePath)



        findViewById<Button>(R.id.download).setOnClickListener {
            try {
                val storage = storage.getReference("Cyber.glb")
                storage.downloadUrl.addOnSuccessListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    val uri = Uri.parse("https://arvr.google.com/scene-viewer/1.0").buildUpon()
                        .appendQueryParameter(
                            "file",
                            it.toString()
                        )
                        .appendQueryParameter("mode", "ar_only")
                        .build()
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.setData(uri)
                    intent.setPackage("com.google.ar.core")
                    startActivity(intent)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }


    }
}