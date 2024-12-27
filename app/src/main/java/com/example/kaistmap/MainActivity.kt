package com.example.kaistmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kaistmap.ui.theme.KAISTMapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)
    }
}




// ************   USING COMPOSABLE   **************

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            KAISTMapTheme {
//                val navController = rememberNavController() // Remember the NavController
//
//                // Set up the navigation host with the start destination
//                NavHost(
//                    navController = navController, // Connect NavHost with NavController
//                    startDestination = "greetingPage" // Define the initial screen
//                ) {
//                    composable("greetingPage") { // Define the greeting page screen
//                        Greeting(navController = navController)
//                    }
//                    composable("secondPage") { // Define the second page screen
//                        SecondPage()
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun Greeting(navController: NavHostController) {
//    // Greeting text
//    Text(
//        text = "Welcome to\nKorea Advanced Institute of Science and Technology,\n Android!",
//        modifier = Modifier.padding(16.dp)
//    )
//
//    // Button to navigate to the second screen
//    Button(
//        onClick = { navController.navigate("secondPage") },
//        modifier = Modifier.padding(16.dp)
//    ) {
//        Text(text = "Go to Next Screen")
//    }
//}
//
//
//
//
//@Composable
//fun SecondPage() {
//    Scaffold(
//        content = { innerPadding ->
//            // Second screen content
//            Text(
//                text = "This is where the map will be displayed",
//                modifier = Modifier.padding(innerPadding) // Apply padding to text inside scaffold content
//            )
//        }
//    )
//}
//
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    KAISTMapTheme {
//        Greeting(navController = rememberNavController()) // Preview with a NavController
//    }
//}
