package com.example.mobil.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobil.data.CatalogRepository
import com.example.mobil.model.CatalogData
import kotlinx.coroutines.launch

class CatalogViewModel(
    private val repository: CatalogRepository
) : ViewModel() {

    private val _catalog =
        MutableLiveData<CatalogData>()

    val catalog: LiveData<CatalogData> =
        _catalog

    private val _isLoading =
        MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean> =
        _isLoading

    private val _isError =
        MutableLiveData<Boolean>()

    val isError: LiveData<Boolean> =
        _isError

    private val _isOffline =
        MutableLiveData<Boolean>()

    val isOffline: LiveData<Boolean> =
        _isOffline

    fun loadCatalog() {

        viewModelScope.launch {

            _isLoading.value = true
            _isError.value = false
            _isOffline.value = false

            var cachedCatalog: CatalogData? = null

            try {

                cachedCatalog =
                    repository.getCachedCatalog()

                if (cachedCatalog != null) {
                    _catalog.value =
                        cachedCatalog
                }

                val freshCatalog =
                    repository.refreshCatalog()

                _catalog.value =
                    freshCatalog

                _isError.value = false
                _isOffline.value = false

            } catch (e: Exception) {

                if (cachedCatalog != null) {

                    _catalog.value =
                        cachedCatalog

                    _isError.value = false
                    _isOffline.value = true

                } else {

                    _isError.value = true
                    _isOffline.value = true
                }

            } finally {

                _isLoading.value = false
            }
        }
    }
}