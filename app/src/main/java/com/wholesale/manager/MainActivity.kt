package com.wholesale.manager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.wholesale.manager.presentation.main.MainScreen
import com.wholesale.manager.presentation.theme.WholesaleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WholesaleTheme {
                MainScreen()
            }
        }
    }
}
