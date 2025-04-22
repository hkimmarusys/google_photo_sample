package com.example.google_photo_sample.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.example.google_photo_sample.R

class TopBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val nameTextView: TextView
    private val emailTextView: TextView
    private val logoutButton: ImageButton

    var onlogoutButtonClick: (() -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_top_bar, this, true)
        nameTextView = findViewById(R.id.userName)
        emailTextView = findViewById(R.id.userEmail)
        logoutButton = findViewById(R.id.logoutButton)

        logoutButton.setOnClickListener {
            onlogoutButtonClick?.invoke()
        }
    }

    fun setUserInfo(name: String?, email: String?) {
        nameTextView.text = "이름: ${name ?: ""}"
        emailTextView.text = "이메일: ${email ?: ""}"
    }
}
