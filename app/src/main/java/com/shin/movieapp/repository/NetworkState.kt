package com.shin.movieapp.repository

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState(val status: Status, val msg: String) {
    companion object {
        val LOADING: NetworkState = NetworkState(Status.RUNNING, "Running")
        val LOADED: NetworkState = NetworkState(Status.SUCCESS, "Success")
        val ERROR: NetworkState = NetworkState(Status.FAILED, "Failed")
        val END: NetworkState = NetworkState(Status.FAILED, "You have reached the end")
    }
}