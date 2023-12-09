package com.example.simpletodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.simpletodo.ui.theme.SimpleToDoTheme
import com.example.simpletodo.ui.theme.backgroundColor


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferencesManager = SharedPreferencesManager(this)
        val mainViewModel: MainViewModel by viewModels {
            MainViewModelFactory(sharedPreferencesManager)
        }
        setContent {
            SimpleToDoTheme() {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = backgroundColor,
                ) {
                    MainScreen(mainViewModel)
                }
            }
        }
    }
}