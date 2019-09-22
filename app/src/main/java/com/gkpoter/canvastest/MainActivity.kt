package com.gkpoter.canvastest

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gkpoter.canvastest.widget.GridTextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv = findViewById<GridTextView>(R.id.tv)
        tv.setGridNumOfWidth(
            9,
            floatArrayOf(0.11f, 0.11f, 0.11f, 0.11f, 0.12f, 0.11f, 0.11f, 0.11f, 0.11f)
        )
        tv.setGridNumofHeiht(2, floatArrayOf(0.5f, 0.5f))
        tv.setText(
            arrayOf(
                "ad", "d1", "2g", "ad", "", "d1", "1l", "d1", "cc",
                "b4", "vv", "02", "ad", "", "d1", "2g", "09", "2g"
            )
        )
        tv.setRoundBackground(true)
        tv.setRadius(50f,50f)
        tv.setRoundBackgroundColor(Color.RED)
    }
}
