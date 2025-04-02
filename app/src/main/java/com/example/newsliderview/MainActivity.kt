package com.example.newsliderview

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var showBottomSheetButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the button by its ID
        showBottomSheetButton = findViewById(R.id.showBottomSheetButton)

        // Set an OnClickListener to show the BottomSheet
        showBottomSheetButton.setOnClickListener {
            // Create an instance of the BottomSheetFragment and show it
            val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
    }
}
