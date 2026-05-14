package com.wholesale.manager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wholesale.manager.di.AppModule
import com.wholesale.manager.presentation.auth.AuthScreen
import com.wholesale.manager.presentation.auth.AuthViewModel
import com.wholesale.manager.presentation.auth.AuthViewModelFactory
import com.wholesale.manager.presentation.main.MainScreen
import com.wholesale.manager.presentation.theme.WholesaleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WholesaleTheme {
                val authViewModel: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(AppModule.provideUserUseCases(this))
                )
                val authState by authViewModel.state.collectAsState()

                if (authState.currentUser != null) {
                    MainScreen(authViewModel = authViewModel)
                } else {
                    AuthScreen(viewModel = authViewModel)
                }
            }
        }
    }
}
