package com.example.porthole24

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import java.io.File

class MainActivity: FlutterActivity() {
    private val CHANNEL = "com.example.porthole24/mediastore"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "notifyMediaStore" -> {
                    val path = call.argument<String>("path")
                    path?.let {
                        notifyMediaStore(it)
                        result.success(1) // Indicate success
                    } ?: result.error("UNAVAILABLE", "Video path not available.", null)
                }
                else -> result.notImplemented()
            }
        }
    }

    private fun notifyMediaStore(path: String) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(File(path))
        mediaScanIntent.data = contentUri
        sendBroadcast(mediaScanIntent)
    }
}
