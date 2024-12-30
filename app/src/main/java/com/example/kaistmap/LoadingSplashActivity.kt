package com.example.kaistmap

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import android.view.View


@Suppress("DEPRECATION")
class SplashScreen : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen)

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        // Find the TextView by its ID
        val textView = findViewById<TextView>(R.id.textView)

        // Original text
        val text = "오늘\n넙죽이는\n뭐먹지"

        // Create a SpannableString
        val spannable = SpannableString(text)

        // Set the first character of each line to a larger size
        spannable.setSpan(RelativeSizeSpan(1.2f), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)  // First char of first line
        spannable.setSpan(RelativeSizeSpan(1.2f), text.indexOf("\n") + 1, text.indexOf("\n") + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)  // First char of second line
        spannable.setSpan(RelativeSizeSpan(1.2f), text.lastIndexOf("\n") + 1, text.lastIndexOf("\n") + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)  // First char of third line

        // Set the modified spannable text
        textView.text = spannable



        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        //Normal Handler is deprecated , so we have to change the code little bit

        // Handler().postDelayed({
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500) // 1500 is the delayed time in milliseconds.
    }
}