package com.example.workoutapp.models.helpers

interface DeepCopyable<T : DeepCopyable<T>> {
    fun deepCopy(): T
}