package com.example.testpractice

import android.os.AsyncTask
import android.util.Log
import java.lang.Exception

// doInBackground の引数の型
// onProgressUpdate　の引数の型
// onPostExecute　の戻り値の型
class progressBarAsyncTask() : AsyncTask<Void, Int, Void >() {
    private var _callback : Callback? = null

    interface Callback {
        fun onBegin()
        fun onProgress(loop_i : Int)
        fun onEnd()
        fun onCanceled()
    }

    fun setOnCallback(callback: Callback) {
        _callback = callback
    }

    override fun onPreExecute() {
        _callback?.onBegin()
    }

    override fun doInBackground(vararg p0: Void?): Void? {
        try {
            for (loop_i in 1..100) {
                // main スレッドの処理
                publishProgress(loop_i)
                Thread.sleep(50)
            }
        } catch (e : Exception) {
            Log.e("dbg", "[asynctask] ここにキャンセル時の処理を書く", e)
        }

        return null
    }

    override fun onProgressUpdate(vararg values: Int?) {
        _callback?.onProgress(values[0]!!)
    }
    override fun onPostExecute(result: Void?) {
        _callback?.onEnd()
    }
    override fun onCancelled() {
        Log.e("dbg", "[asynctask] ここにキャンセル時の処理を書く 2")
    }

}