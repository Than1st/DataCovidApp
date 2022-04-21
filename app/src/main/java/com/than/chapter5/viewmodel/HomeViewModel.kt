package com.than.chapter5.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.than.chapter5.model.User

class HomeViewModel: ViewModel() {
    var dataUser : MutableLiveData<User> = MutableLiveData()

    fun getDataUser(user: User){
        dataUser.postValue(user)
    }
}