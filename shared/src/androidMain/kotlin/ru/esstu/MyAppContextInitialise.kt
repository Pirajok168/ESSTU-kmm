package ru.esstu

import android.app.Application
import android.content.Context
import androidx.startup.AppInitializer
import androidx.startup.Initializer
import java.util.*


class MyAppContextInitialise: Initializer<ContextApplication> {
    override fun create(context: Context): ContextApplication {
        return ContextApplication.init(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }

}