package com.example.google_photo_sample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.google_photo_sample.adapter.PhotoAdapter
import com.example.google_photo_sample.auth.GoogleAuthClient
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.google_photo_sample.ui.TopBar

class MainActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var recyclerView: RecyclerView
    private lateinit var topBar: TopBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.photoRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 4)

        topBar = findViewById(R.id.topBar)


        topBar.onlogoutButtonClick = {
            googleSignInClient.signOut().addOnCompleteListener {
                signIn()
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope("https://www.googleapis.com/auth/photoslibrary"))
            .requestIdToken(BuildConfig.CLIENT_ID)
            .requestServerAuthCode(BuildConfig.CLIENT_ID)
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        signIn()
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }


    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        handleSignInResult(task)
    }



    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.result
            val name = account.displayName
            val email = account.email
            val authCode = account.serverAuthCode

            topBar.setUserInfo(name, email)


            if (authCode != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val accessToken = GoogleAuthClient.exchangeCodeForAccessToken(authCode)

                        val photoList = GoogleAuthClient.fetchPhotos(accessToken)

                        runOnUiThread {
                            recyclerView.adapter = PhotoAdapter(photoList)
                        }

                    } catch (e: Exception) {
                        Log.e("AccessTokenError", "get access token failed", e)
                    }
                }
            }

        } catch (e: Exception) {
            Log.e("GoogleSignIn", "Sign-in failed", e)
        }
    }
}
