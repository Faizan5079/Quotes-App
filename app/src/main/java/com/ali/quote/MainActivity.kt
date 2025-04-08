package com.ali.quote

import RetrofitInstance
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ali.quote.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import quotesmodel


class MainActivity : AppCompatActivity() {
    private  val  BASE_URL = "https://zenquotes.io/api/"
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val shareButton: Button = findViewById(R.id.btnshare)

        shareButton.setOnClickListener {
            share("check out this awesome content!")
           //sharetext("Check out this awesome content!")
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find the toolbar
       // val toolbar: Toolbar = findViewById(R.id.toolbar)
       // setSupportActionBar(toolbar)
 getQuotes()
        binding.nextbutton.setOnClickListener {
            getQuotes()

        }

    }
    private fun getQuotes(){
        setInProgress(true)
        GlobalScope.launch {
        try {
   val response = RetrofitInstance.quotesApi.getRandomQuotes()
            runOnUiThread {
                setInProgress(false)
                response.body()?.first()?.let{
                    setUI(it)
                }
            }
        }catch (e : Exception){
            runOnUiThread {
                setInProgress(false)
                Toast.makeText(applicationContext,"something went wrong",Toast.LENGTH_SHORT).show()
            }
        }
        }
    }
    private fun  setUI(quote : quotesmodel){
        binding.quoteTv.text = quote.q
        binding.authorTv.text = quote.a

    }
    private fun setInProgress(inProgress : Boolean){
     if (inProgress){
         binding.progressbar.visibility = View.VISIBLE
        binding.nextbutton.visibility = View.GONE
    }else{
        binding.progressbar.visibility = View.GONE
        binding.nextbutton.visibility = View.VISIBLE
    }
}

    fun share(text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "$text $BASE_URL")
        }
        startActivity(Intent.createChooser(intent, "Share via"))

    }
}
