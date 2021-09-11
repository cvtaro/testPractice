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

    private var _progressBarAsyncTask : progressBarAsyncTask? = null

    private var isButton01Pushed : Boolean = false
    private var isButton02Pushed : Boolean = false


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
            isButton01Pushed = true
            job01 = space01.launch {
                    progressTestCoroutine()
            }
        }
        button02 = findViewById(R.id.mainDoAsyncButton02)
        button02.setOnClickListener {
            isButton02Pushed = true
            _progressBarAsyncTask = progressBarAsyncTask()
            _progressBarAsyncTask?.setOnCallback(progressBarAsyncTaskCallbacks)
            _progressBarAsyncTask?.execute()
        }
        // キャンセル
        cancelButton = findViewById<Button>(R.id.mainCancelAsyncButton)
        cancelButton.setOnClickListener {
            // キャンセル処理
            if (isButton01Pushed) {
                job01?.cancel()
            } else if (isButton02Pushed) {
                _progressBarAsyncTask?.cancel(true)
            }
            // 後処理
            if (isButton01Pushed || isButton02Pushed) {
                text.text = "キャンセルされました"
                progressBar.progress = 0
                button01.isEnabled = true
                button02.isEnabled = true
                isButton01Pushed = false
                isButton02Pushed = false
            } else {
                text.text = "リセットされました"
                progressBar.progress = 0
            }
        }
    }

    // Coroutine
    private suspend fun progressTestCoroutine() {
        try {
            // onPreExecuteと同等
            withContext(Dispatchers.Main) {
                button01.isEnabled = false
                button02.isEnabled = false
                text.text = "開始！ coroutine"
            }

            // doInBackgroundと同等
            for (loop_i in 1..100) {
                withContext(Dispatchers.Main) {
                    progressBar.progress = loop_i
                }
                Thread.sleep(50)
            }

            // onPostExecuteと同等
            withContext(Dispatchers.Main) {
                button01.isEnabled = true
                button02.isEnabled = true
                text.text = "終了！ coroutine"
            }
        } catch (e : CancellationException) {
            // onCancelledと同等
            Log.e("dbg", "[coroutine] ここにキャンセル時の処理を書く", e)
        }
    }

    // AsyncTask
    var progressBarAsyncTaskCallbacks = object : progressBarAsyncTask.Callback {
        override fun onBegin() {
            button01.isEnabled = false
            button02.isEnabled = false
            text.text = "開始！ asynctask"
        }
        override fun onProgress(loop_i: Int) {
            progressBar.progress = loop_i
        }
        override fun onEnd() {
            button01.isEnabled = true
            button02.isEnabled = true
            text.text = "終了！ asynctask"
        }
        override fun onCanceled() {
        }
    }
} // end class