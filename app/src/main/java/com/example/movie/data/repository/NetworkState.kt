package com.example.movie.data.repository

enum class Status{
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState (val status: Status, val msg: String){
    companion object{
        val LOADED: NetworkState
        val LOADING: NetworkState
        val ERROR: NetworkState
        val ENDOFLIST: NetworkState

        init {
            LOADED = NetworkState(Status.SUCCESS, msg = "Success")
            LOADING = NetworkState(Status.RUNNING, msg = "Running")
            ERROR = NetworkState(Status.FAILED, msg = "Something went wrong!")
            ENDOFLIST = NetworkState(Status.FAILED, msg = "You have reached the end")
        }
    }
}