package com.app.learningcoroutine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.app.learningcoroutine.navigation.Destination
import com.app.learningcoroutine.thermometer.LandingScreen
import com.app.learningcoroutine.thermometer.TemperatureMonitorScreen
import com.app.learningcoroutine.ui.theme.LearningCoroutineTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearningCoroutineTheme {

                val backStack = rememberNavBackStack(Destination.Landing)

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->


                    NavDisplay(
                        backStack = backStack,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        entryProvider = entryProvider {
                            entry<Destination.Landing> {
                                LandingScreen(
                                    onStartClick = {
                                        backStack.add(Destination.Home)
                                    }
                                )
                            }
                            entry<Destination.Home> {
                                TemperatureMonitorScreen()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LearningCoroutineTheme {
        Greeting("Android")
    }
}