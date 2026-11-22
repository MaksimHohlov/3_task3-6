package com.example.a3_task3_6

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a3_task3_6.ui.theme._3_task36Theme

class MainActivity : ComponentActivity() {
    private val TAG = "111"

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Вызван метод onCreate")
        enableEdgeToEdge()
        setContent {
            _3_task36Theme {
                // размер экрана
                val windowSizeClass = calculateWindowSizeClass(this)

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(
                        widthSizeClass = windowSizeClass.widthSizeClass,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onStart() { super.onStart(); Log.d(TAG, "Вызван метод onStart") }
    override fun onResume() { super.onResume(); Log.d(TAG, "Вызван метод onResume") }
    override fun onPause() { super.onPause(); Log.d(TAG, "Вызван метод onPause") }
    override fun onStop() { super.onStop(); Log.d(TAG, "Вызван метод onStop") }
    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "Вызван метод onDestroy") }
}

@Composable
fun AppNavigation(
    widthSizeClass: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier
    ) {
        composable<Home> {
            AdaptiveLayout(
                widthSizeClass = widthSizeClass,
                onNavigateToDetails = { navController.navigate(Details) }
            )
        }
        composable<Details> {
            DetailsScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}

@Composable
fun HomeScreen(
    onNavigateToDetails: () -> Unit,
    viewModel: CounterViewModel = viewModel() // счетчик
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Главный экран",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Счетчик: ${viewModel.count}", style = MaterialTheme.typography.bodyLarge)
        Button(onClick = { viewModel.increment() }) {
            Text("Увеличить")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onNavigateToDetails) {
            Text("Перейти на экран Details")
        }
    }
}

@Composable
fun DetailsScreen(
    onNavigateBack: () -> Unit,
    isSplitView: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Экран деталей (Details)",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "какая-то информация.",
            style = MaterialTheme.typography.bodyLarge
        )

        if (!isSplitView) {
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onNavigateBack) {
                Text("Назад на главный экран")
            }
        }
    }
}

@Composable
fun AdaptiveLayout(
    widthSizeClass: WindowWidthSizeClass,
    onNavigateToDetails: () -> Unit,
    viewModel: CounterViewModel = viewModel()
) {
    when (widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            // узкий экран
            HomeScreen(
                onNavigateToDetails = onNavigateToDetails,
                viewModel = viewModel
            )
        }
        WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> {
            // широкий экран
            Row(modifier = Modifier.fillMaxSize()) {

                // Левая колонка
                Box(modifier = Modifier.weight(1f)) {
                    HomeScreen(
                        onNavigateToDetails = {}, // Кнопка  не нужна
                        viewModel = viewModel
                    )
                }

                VerticalDivider(modifier = Modifier.fillMaxHeight())

                // Правая колонка
                Box(modifier = Modifier.weight(1f)) {
                    DetailsScreen(
                        onNavigateBack = {},
                        isSplitView = true
                    )
                }
            }
        }
    }
}
