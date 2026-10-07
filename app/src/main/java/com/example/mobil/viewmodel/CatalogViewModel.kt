package com.example.mobil.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobil.data.CatalogRepository
import com.example.mobil.model.CatalogData

class CatalogViewModel(
    private val repository: CatalogRepository
) : ViewModel() {

    private val _catalog = MutableLiveData<CatalogData>()
    val catalog: LiveData<CatalogData> = _catalog

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun loadCatalog() {
        _isLoading.value = true
        _isError.value = false

        try {
            _catalog.value = repository.loadCatalog()
            _isError.value = false
        } catch (e: Exception) {
            _isError.value = true
        } finally {
            _isLoading.value = false
        }
    }
}