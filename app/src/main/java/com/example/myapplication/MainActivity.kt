package com.example.myapplication

import SeasonTime.SeasonsEnum
import SeasonTime.TillSeasonTime
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("TAG", "onCreate")

        val tv: TextView = findViewById(R.id.textView)

        val tst = TillSeasonTime()
        tv.text = tst.getFormatedTimeTillSeason(SeasonsEnum.Spring)

    }
}