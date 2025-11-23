package com.andrei.githubbrowser.data

interface Mapper<T1,T2> {

    fun map(from: T1) : T2
}