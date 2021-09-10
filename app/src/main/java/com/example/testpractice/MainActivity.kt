package com.example.testpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.DEBUG
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlin.time.measureTime

class MainActivity : AppCompatActivity() {

    private lateinit var text : TextView
    private lateinit var progressBar : ProgressBar
    private lateinit var button01: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text = findViewById(R.id.mainText)
        text.apply {
            text = ""
        }
        progressBar = findViewById(R.id.mainProgressBar)
        progressBar.apply {
            min = 0
            max = 100
            progress = 10
        }

        // クリックイベント
        button01 = findViewById<Button>(R.id.mainButton01)
        button01.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                progressTest()
            }
        }
    }


    private suspend fun progressTest() {
        withContext(Dispatchers.Main) {
            button01.isEnabled = false
        }
        for (loop_i in 1..100) {
            withContext(Dispatchers.Main) {
                progressBar.progress = loop_i
            }
            Thread.sleep(25)
        }
        withContext(Dispatchers.Main) {
            button01.isEnabled = true
        }
    }
} // end class