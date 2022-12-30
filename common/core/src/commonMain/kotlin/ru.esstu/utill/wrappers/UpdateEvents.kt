package ru.esstu.utill.wrappers

sealed class UpdateEvents<T>(val data: T) {
    class Add<T>(data: T) : UpdateEvents<T>(data)
    class Remove<T>(data: T) : UpdateEvents<T>(data)
    class Update<T>(data: T) : UpdateEvents<T>(data)
}