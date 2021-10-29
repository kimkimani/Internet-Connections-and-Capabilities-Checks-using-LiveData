package com.application.internet.connections.and.capabilities.using.real_time

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.application.internet.connections.and.capabilities.using.real_time.LiveDataInternetConnections
import com.application.internet.connections.and.capabilities.using.real_time.R

class MainActivity : AppCompatActivity() {

    private lateinit var TextView1 : TextView
    private lateinit var TextView2 : TextView
    private lateinit var cld : LiveDataInternetConnections

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TextView1 = findViewById(R.id.connected)
        TextView2 = findViewById(R.id.not_connected)
        cld = LiveDataInternetConnections(application)
        cld.observe(this, { isConnected ->
            if (isConnected) {
                TextView1.visibility = View.VISIBLE
                TextView2.visibility = View.GONE
            }else{
                TextView1.visibility = View.GONE
                TextView2.visibility = View.VISIBLE
            }
        })
    }
}