package com.android.optimizationvynilsapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.optimizationvynilsapp.models.Comment
import com.android.optimizationvynilsapp.network.NetworkServiceAdapter

class CommentsViewModel (application: Application, albumId: Int) :  AndroidViewModel(application) {


    /**
     * A playlist of videos that can be shown on the screen. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private val _comments = MutableLiveData<List<Comment>>()

    /**
     * A playlist of videos that can be shown on the screen. Views should use this to get access
     * to the data.
     */
    val comments: LiveData<List<Comment>>
        get() = _comments

    /**
     * Event triggered for network error. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    /**
     * Event triggered for network error. Views should use this to get access
     * to the data.
     */
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    /**
     * Flag to display the error message. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    /**
     * Flag to display the error message. Views should use this to get access
     * to the data.
     */
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown
    /**
     * init{} is called immediately when this ViewModel is created.
     */
    val id:Int = albumId

    init {
        refreshDataFromNetwork()
    }

    /**
     * Refresh data from network and pass it via LiveData. Use a coroutine launch to get to
     * background thread.
     */
    private fun refreshDataFromNetwork() {
        Log.d("netparam",id.toString())

        NetworkServiceAdapter.getInstance(getApplication()).getComments(id,{
            _comments.postValue(it)
            _eventNetworkError.value = false
            _isNetworkErrorShown.value = false
        },{
            _eventNetworkError.value = true
        })
    }

    /**
     * Resets the network error flag.
     */
    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    fun printByRating(lower:Int, upper:Int){
        val stringBuffer = StringBuffer()
        comments.value?.forEach {
            if(it.rating.toInt() in lower until upper){
                stringBuffer.append("${it.description}\n")
            }
        }
        Log.d("result", "Comentarios en rating: [ $lower , $upper ]: ${stringBuffer.toString()}")
    }


    fun printListOfCommentsStartingUpper(){
        if(!_comments.value.isNullOrEmpty()){
            Log.d("result", "Comentarios con mayúscula: ${_comments.value!!.filter { it.description[0].isUpperCase() }}")
        }
    }


    class Factory(val app: Application, val albumId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CommentsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CommentsViewModel(app, albumId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}