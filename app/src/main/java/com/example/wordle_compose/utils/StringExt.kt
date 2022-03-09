package com.example.wordle_compose.utils

fun String.toGridArray(): CharArray {
    return CharArray(5) {index ->
        if (this.length > index) {
            this[index]
        } else {
            ' '
        }
    }
}