package com.example.classificationsupport

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.classificationsupport.navigation.AppNavigation
import com.example.classificationsupport.ui.components.SplashScreen
import com.example.classificationsupport.ui.theme.ClassificationSupportTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var isLoading by mutableStateOf(true)

        lifecycleScope.launch {
            delay(3000)
            isLoading = false
        }


        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            ClassificationSupportTheme {
                if (isLoading) {
                    SplashScreen()
                } else {
                    AppNavigation(navController)
                }
            }
        }
    }
}

@Composable
fun RequestCameraPermission(
    onPermissionGranted: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                onPermissionGranted()
            } else {
                Toast.makeText(context, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        val permission = Manifest.permission.CAMERA
        val isGranted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        if (!isGranted) {
            permissionLauncher.launch(permission)
        } else {
            onPermissionGranted()
        }
    }
}



@Preview(showBackground = true)
@Composable
fun Preview() {
    val navController = rememberNavController()
    ClassificationSupportTheme {
        AppNavigation(navController)
    }
}