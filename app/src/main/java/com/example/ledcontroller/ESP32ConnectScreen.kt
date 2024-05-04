package com.example.ledcontroller

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ledcontroller.databinding.ActivityEsp32ConnectScreenBinding
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.port
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import kotlinx.coroutines.runBlocking

class ESP32ConnectScreen : AppCompatActivity() {

    var binding:ActivityEsp32ConnectScreenBinding? = null

    val client = HttpClient(CIO) {
        expectSuccess = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEsp32ConnectScreenBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.sendButton.setOnClickListener {
            var ssid = binding!!.ssid.text.toString().trim()
            var password = binding!!.password.text.toString().trim()

            if (ssid.isEmpty() or password.isEmpty()){
                Toast.makeText(this, "You have to insert both values", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                runBlocking {
                    sendWifiCredentials(ssid, password)
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        client.close()
    }

    private suspend fun sendWifiCredentials(ssid: String, password: String) {
        try {
            val response: HttpResponse = client.request("http://192.168.4.1/post?ssid=$ssid&password=$password"){
                port = 80
                method = HttpMethod.Post
            }
            Log.d("My_tag_", "passed" + response.status)
        } catch (e: Exception) {
            Log.d("My_tag_", "notpassed" + e.toString())
        }
    }
}