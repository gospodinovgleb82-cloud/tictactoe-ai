package com.team.tictactoe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            startActivity(Intent(this, SelectActivity::class.java))
        }

        findViewById<Button>(R.id.btnExit).setOnClickListener {
            finish()
        }
    }
}