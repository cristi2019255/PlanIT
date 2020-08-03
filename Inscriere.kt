package com.example.plan_it

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Inscriere : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscriere)

        var text_out=findViewById<TextView>(R.id.textView2)
        val intent:Intent=getIntent()
        text_out.append(intent.getStringExtra("company name"))
        text_out.append(intent.getStringExtra("company adress"))
        text_out.append(intent.getStringExtra("company imageUrl"))
    }
}
