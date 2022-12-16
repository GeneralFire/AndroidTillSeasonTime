package com.example.myapplication

import SeasonTime.SeasonsEnum
import SeasonTime.TillSeasonTime
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    fun setSeasonData(aboveTv: TextView, progress: ProgressBar, underTv: TextView ) {
        Log.d("season", "setSeasonData Start")
        val seasonTimeManager = TillSeasonTime()
        aboveTv.text = seasonTimeManager.getTimeTillSeasonAsString(SeasonsEnum.Spring)
        progress.setProgress((seasonTimeManager.getDateSeasonProgress() * 100).toInt())
        underTv.text = seasonTimeManager.getDateSeasonProgress().toString()
        Log.d("season", "setSeasonData End")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("onCreate", "onCreate Start")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val aboveTv: TextView = findViewById(R.id.aboveProgressTV)
        val progressPb: ProgressBar = findViewById(R.id.progressSB)
        val underTv: TextView = findViewById(R.id.underProgressTV)
        setSeasonData(aboveTv, progressPb, underTv)

        val t: Thread = object : Thread() {
            override fun run() {
                try {
                    while (!isInterrupted) {
                        sleep(1000)
                        runOnUiThread { setSeasonData(aboveTv, progressPb, underTv) }
                    }
                } catch (e: InterruptedException) {
                }
            }
        }

        t.start()
        Log.d("onCreate", "onCreate End")
    }


}