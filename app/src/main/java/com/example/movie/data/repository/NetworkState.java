package com.example.movie.data.repository;

enum class Status{
    RUNNING,
    SECCESS,
    FAILED
}

public class NetworkState(val status: Status, val String) {
    companion object{
        val LOADED: NetworkState
        val LOADING: NetworkState
        val ERROR: NetworkState

        init {
            LOADED = NetworkState(Status.SECCESS, "Success")
            LOADING = NetworkState(Status.RUNNING, "Running")
            ERROR = NetworkState(Status.FAILED, "Something went wrong")
        }
    }
}
