package net.nikhilroy.logoquiz

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import net.nikhilroy.logoquiz.viewmodels.LogoQuizViewModel
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    val TAG = "Logo Quiz"
    var viewModel : LogoQuizViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setClickListeners()
        initializeObservers()
        startGame()
    }

    @SuppressLint("DefaultLocale")
    fun setClickListeners() {
        nextButton.setOnClickListener{
            val answer = answerEditText.text.toString().toUpperCase().trim()
            viewModel?.evalAnswer(answer)
        }
    }

    fun initializeObservers() {
        viewModel = ViewModelProvider(this).get(LogoQuizViewModel::class.java)
        viewModel?.logoImageUrl?.observe(this, logoImageViewObserver)
        viewModel?.gameTitle?.observe(this, gameTitleObserver)
        viewModel?.gameRunning?.observe(this, endGameObserver)
        viewModel?.currentAnswerState?.observe(this, currentAnswerStateObserver)
        viewModel?.goBack?.observe(this, goBackObserver)
    }

    fun startGame() = runBlocking{
        val json = async { getLogoAssets() }
        viewModel?.startGame(json.await())
    }

    fun getLogoAssets(): String? {
        var json: String? = null
        try {
            val inputStream: InputStream = assets.open("logo.txt")
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return json
        }
        return json
    }

    fun endGame() {
        toast("Game Over")
        nextButton.setOnClickListener{
            //null
        }
    }

    fun toast(message: String = TAG, time: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message,time).show()
    }

    fun loadResources(url: String) {
        //load image resource
        Picasso.get().load(url).into(logoImageView)

        //refresh answer text
        answerEditText.setText("")
    }

    override fun onBackPressed() {
        viewModel?.goBack()
    }

    //mvvm
    private val logoImageViewObserver = Observer<String> { value ->
        value?.let {
            loadResources(it)
        }
    }

    private val gameTitleObserver = Observer<String> { value ->
        value?.let {
            titleTextView.text = it
        }
    }

    private val endGameObserver = Observer<Boolean> { value ->
        value?.let {
            if(!it) {
                endGame()
            }
        }
    }

    private val currentAnswerStateObserver = Observer<Boolean> { value ->
        value?.let {
            if(it) {
                toast("Correct")
            }else toast("Wrong")
        }
    }

    private val goBackObserver = Observer<Boolean> { value ->
        value?.let {
            if(it) {
              finish()
            }
        }
    }
}
