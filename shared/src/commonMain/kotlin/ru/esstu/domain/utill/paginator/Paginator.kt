package ru.esstu.domain.utill.paginator

import ru.esstu.data.web.api.model.Response
import ru.esstu.data.web.api.model.ResponseError

class Paginator<Key, Item>(
    private val initialKey: Key,
    private val onLoad: (Boolean) -> Unit,                                  //изменение статуса загрузки страники true false
    private val onReset: suspend (initialKey: Key) -> Unit = { },           //вызывается при сбросе текущей страницы до начала загрузки данных
    private val onRequest: suspend (key: Key) -> Response<List<Item>>,      //загружает нужную страницу по ключу
    private val onLocalData: suspend (key: Key) -> Unit
    = {},                                 //
    private val getNextKey: suspend (currentKey: Key, List<Item>) -> Key,   //получает ключ следующей страницы
    private val onError: suspend (ResponseError) -> Unit,                   //вызывается при возникновении ишибки запроса
    private val onRefresh: suspend (items: List<Item>) -> Unit,             //вызывается при успешной перезагрузке первой страницы
    private val onNext: suspend (newKey: Key, items: List<Item>) -> Unit    //вызывается при успешной загрузке новой страницы
) {

    private var currentKey = initialKey
    private var isMakingRequest = false

    suspend fun loadNext() {
        if (isMakingRequest) return
        isMakingRequest = true
        onLoad(isMakingRequest)
        when (val page = onRequest(currentKey)) {
            is Response.Error -> onError(page.error)
            is Response.Success -> {
                onNext(currentKey, page.data)
                currentKey = getNextKey(currentKey, page.data)
            }
        }
        onLoad(false)
        isMakingRequest = false
    }

    suspend fun refresh() {
        isMakingRequest = true
        onLoad(isMakingRequest)
        reset()
        onLocalData(currentKey)
        when (val page = onRequest(currentKey)) {
            is Response.Error -> onError(page.error)
            is Response.Success -> {
                onRefresh(page.data)
                currentKey = getNextKey(currentKey, page.data)
            }
        }
        onLoad(false)
        isMakingRequest = false
    }

    suspend fun reset() {
        currentKey = initialKey
        onReset(currentKey)
    }
}