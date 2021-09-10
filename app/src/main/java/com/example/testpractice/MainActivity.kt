package com.example.testpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var text : TextView
    private lateinit var progressBar : ProgressBar
    private lateinit var button01: Button
    private lateinit var button02: Button
    private lateinit var cancelButton: Button

    private var job01 : Job? = null
    private var space01 = CoroutineScope(Dispatchers.Default)


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
        button01 = findViewById<Button>(R.id.mainDoAsyncButton01)
        button01.setOnClickListener {
            job01 = space01.launch {
                    progressTestCoroutine()
            }
        }

        cancelButton = findViewById<Button>(R.id.mainCancelAsyncButton)
        cancelButton.setOnClickListener {
//                space01.cancel()
            job01?.cancel()

            button01.isEnabled = true
            text.text = "キャンセルされました"
            progressBar.progress = 0
        }
    }


    private suspend fun progressTestCoroutine() {
        try {
            withContext(Dispatchers.Main) {
                button01.isEnabled = false
                text.text = "開始！"
            }
            for (loop_i in 1..100) {
                withContext(Dispatchers.Main) {
                    progressBar.progress = loop_i
                }
                Thread.sleep(50)
            }
            withContext(Dispatchers.Main) {
                button01.isEnabled = true
                text.text = "終了！"
            }
        } catch (e : CancellationException) {
            Log.e(localClassName, "ここにキャンセル時の処理を記述", e)

        }
    }
} // end class