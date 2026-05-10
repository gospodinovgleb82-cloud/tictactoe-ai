package com.team.tictactoe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnExit  = findViewById<Button>(R.id.btnExit)

        btnStart.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }

        btnExit.setOnClickListener {
            finish()
        }
    }
}