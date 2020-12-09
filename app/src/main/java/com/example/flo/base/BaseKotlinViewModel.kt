package com.example.flo.base

import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.flo.util.SnackbarMessage
import com.example.flo.util.SnackbarMessageString
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseKotlinViewModel : ViewModel(), Observable {

    private val snackbarMessage = SnackbarMessage()
    private val snackbarMessageString = SnackbarMessageString()

    private val compositeDisposable = CompositeDisposable()

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun showSnackbar(stringResourceId: Int) {
        snackbarMessage.value = stringResourceId
    }

    fun showSnackbar(str: String) {
        snackbarMessageString.value = str
    }

    fun observeSnackbarMessage(lifeCycleOwner: LifecycleOwner, ob: (Int) -> Unit) {
        snackbarMessage.observe(lifeCycleOwner, ob)
    }

    fun observeSnackbarMessageStr(lifeCycleOwner: LifecycleOwner, ob: (String) -> Unit) {
        snackbarMessageString.observe(lifeCycleOwner, ob)
    }

    private val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()

    override fun addOnPropertyChangedCallback(
        callback: Observable.OnPropertyChangedCallback) {
        callbacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback(
        callback: Observable.OnPropertyChangedCallback) {
        callbacks.remove(callback)
    }

    /**
     * Notifies observers that all properties of this instance have changed.
     */
    fun notifyChange() {
        callbacks.notifyCallbacks(this, 0, null)
    }

    /**
     * Notifies observers that a specific property has changed. The getter for the
     * property that changes should be marked with the @Bindable annotation to
     * generate a field in the BR class to be used as the fieldId parameter.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    fun notifyPropertyChanged(fieldId: Int) {
        callbacks.notifyCallbacks(this, fieldId, null)
    }
}