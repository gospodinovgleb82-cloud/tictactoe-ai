package com.team.tictactoe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

        findViewById<Button>(R.id.btn3x3).setOnClickListener { startGame(3) }
        findViewById<Button>(R.id.btn4x4).setOnClickListener { startGame(4) }
        findViewById<Button>(R.id.btn5x5).setOnClickListener { startGame(5) }
        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }
    }

    private fun startGame(size: Int) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("BOARD_SIZE", size)
        startActivity(intent)
    }
}