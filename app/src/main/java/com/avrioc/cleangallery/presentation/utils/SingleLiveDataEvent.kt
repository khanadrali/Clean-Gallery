package com.avrioc.cleangallery.presentation.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class SingleLiveDataEvent<T> : MutableLiveData<T>() {

    private var hasBeenHandled = false

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer { t ->
            if (t != null && !hasBeenHandled) {
                hasBeenHandled = true
                observer.onChanged(t)
            }
        })
    }

   override fun postValue(value: T) {
        hasBeenHandled = false
        super.postValue(value)
    }
}
