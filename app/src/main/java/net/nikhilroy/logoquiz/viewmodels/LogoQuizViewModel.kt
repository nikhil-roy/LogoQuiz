package net.nikhilroy.logoquiz.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.nikhilroy.logoquiz.models.LogoQuizModel
import java.lang.reflect.Type

class LogoQuizViewModel : ViewModel() {
    val TAG = "Logo Quiz"
    var logoQuizModel: List<LogoQuizModel>? = null
    var currentLogo = 0
    var score = 0
    val logoImageUrl = MutableLiveData<String>()
    val gameTitle = MutableLiveData<String>()
    val gameRunning = MutableLiveData<Boolean>()
    val currentAnswerState = MutableLiveData<Boolean>()
    val goBack = MutableLiveData<Boolean>()
    val gameAnswerState : HashMap<String, Boolean>? = HashMap()

    fun loadLogoQuizModel(json: String?) {
        val gson = Gson()
        val listType: Type = object : TypeToken<List<LogoQuizModel?>?>() {}.type
        logoQuizModel = gson.fromJson(json ?: "", listType)
    }

    fun setGameTitle() {
        gameTitle.value = "$TAG, Current Score: $score"
    }


    fun setImageUrl() {
        logoImageUrl.value = logoQuizModel?.get(currentLogo)?.imgUrl ?: ""
    }

    fun startGame(json: String?) {
        loadLogoQuizModel(json)
        setImageUrl()
        gameRunning.value = true
    }

    fun evalAnswer(answer: String) {
        if(answer.equals(logoQuizModel?.get(currentLogo)?.name)) {
            currentAnswerState.value = true
            score++
            gameAnswerState?.set(logoQuizModel?.get(currentLogo)?.name?:"", true)
            currentLogo++
        }else {
            currentAnswerState.value = false
            gameAnswerState?.set(logoQuizModel?.get(currentLogo)?.name?:"", false)
            currentLogo++
        }

        if(currentLogo == logoQuizModel?.size) {
            gameRunning.value = false

        } else {
            setImageUrl()
        }

        setGameTitle()
    }

    fun goBack() {
        if (currentLogo == 0 || gameRunning.value == false) {
            goBack.value = true
        }
        else {
            currentLogo--
            if(gameAnswerState?.get(logoQuizModel?.get(currentLogo)?.name?:"") == true) {
                score--
                setGameTitle()
            }
            setImageUrl()
        }
    }
}