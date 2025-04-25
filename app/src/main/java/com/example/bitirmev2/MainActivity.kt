package com.example.bitirmev2

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.bitirmev2.data.AppDatabase
import com.example.bitirmev2.data.LocalKinRepository
import com.example.bitirmev2.data.LocalMessageRepository
import com.example.bitirmev2.data.LocalUserRepository
import com.example.bitirmev2.nearby.NearbyManager
import com.example.bitirmev2.ui.theme.Bitirmev2Theme

class MainActivity : ComponentActivity() {

    private val requestCodePermissions = 1001
    private val requestCodeNotifications = 1002

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.NEARBY_WIFI_DEVICES
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkAndRequestPermissions()
        checkNotificationPermission()

        // Room veritabanı DAO ayarı
        val db = AppDatabase.getDatabase(applicationContext)
        LocalMessageRepository.dao = db.helpMessageDao()
        LocalUserRepository.dao = db.userProfileDao()
        LocalKinRepository.dao = db.nextOfKinDao()

        // Nearby başlat
        NearbyManager.init(this)
        NearbyManager.startDiscovery()
        NearbyManager.startAdvertising("Cihaz-${System.currentTimeMillis()}")

        // UI başlat
        setContent {
            Bitirmev2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val missing = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missing.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, missing.toTypedArray(), requestCodePermissions)
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    requestCodeNotifications
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == requestCodeNotifications) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Bildirim izni verildi
            } else {
                // Bildirim izni reddedildi
            }
        }
    }
}
