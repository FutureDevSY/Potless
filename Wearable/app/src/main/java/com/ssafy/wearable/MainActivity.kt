package com.ssafy.wearable

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.ssafy.wearable.api.ApiResponse
import com.ssafy.wearable.api.RetrofitInstance
import com.ssafy.wearable.ui.theme.WearableAppTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import androidx.compose.foundation.Image

class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private val isActivated = mutableStateOf(false)

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            if (isActivated.value) {
                Log.d("MainActivity", "Permission granted and isActivated is true")
                startLocationUpdates()
            }
        } else {
            Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(this, "알림 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearableAppTheme {
                MainScreen()
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createLocationRequest()
        createLocationCallback()

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "인터넷이 연결되어 있어야 합니다.", Toast.LENGTH_LONG).show()
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            Log.d("MainActivity", "Location permission already granted")
            if (isActivated.value) {
                startLocationUpdates()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // 알림 채널 생성
        createNotificationChannel()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            10000 // 최대 업데이트 간격을 설정
        ).apply {
            setMinUpdateDistanceMeters(5.0f) // 1미터마다 위치 업데이트
        }.build()
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Log.d("MainActivity", "onLocationResult called")
                if (isActivated.value) {
                    locationResult.lastLocation?.let { location ->
                        sendLocationToServer(location)
                    } ?: run {
                        Log.d("MainActivity", "Location result is null")
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun sendLocationToServer(location: Location) {
        if (!isActivated.value) {
            Log.d("MainActivity", "sendLocationToServer called but isActivated is false")
            return
        }

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "인터넷 연결이 필요합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val x = location.longitude
        val y = location.latitude

        RetrofitInstance.api.sendLocation(x, y).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("MainActivity", "Response received: Status: ${it.status}, Message: ${it.message}, Data: ${it.data}")
                        if (it.data) {
                            showNotification("알림", "전방에 포트홀이 있습니다.")
                            vibratePhone()
                        }
                    } ?: run {
                        Log.d("MainActivity", "Response body is null")
                    }
                } else {
                    Log.d("MainActivity", "Response failed with status code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d("MainActivity", "Response error: ${t.message}")
            }
        })

    }

    private fun showNotification(title: String, message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = 1
        val channelId = "wearable_notification_channel"

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // 작은 아이콘 설정
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
        Log.d("MainActivity", "Notification shown")
    }

    private fun vibratePhone() {
        Log.d("MainActivity", "Vibrating phone")
        val vibrator = ContextCompat.getSystemService(this, Vibrator::class.java)
        vibrator?.let {
            val vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
            it.vibrate(vibrationEffect)
            Log.d("MainActivity", "Phone vibrated")
        }
    }

    private fun createNotificationChannel() {
        val channelId = "wearable_notification_channel"
        val channelName = "Wearable Notification Channel"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = "Notification channel for wearable app alerts"
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @Composable
    fun MainScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    isActivated.value = !isActivated.value
                    if (isActivated.value) {
                        Log.d("MainActivity", "Main screen activated")
                        startLocationUpdates()
                        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    } else {
                        Log.d("MainActivity", "Main screen deactivated")
                        stopLocationUpdates()
                        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Crossfade(targetState = isActivated.value, label = "activationCrossfade") { state ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(if (state) Color(0xFF4D4C7D) else Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text(
                            text = if (state) "ACTIVATE" else "DISABLED",
                            color = if (state) Color.White else Color(0xFF4D4C7D),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(top = 80.dp)
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Image(
                            painter = painterResource(id = R.drawable.weblogo),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .size(50.dp) // 이미지 크기를 줄임
                        )
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        WearableAppTheme {
            MainScreen()
        }
    }
}
