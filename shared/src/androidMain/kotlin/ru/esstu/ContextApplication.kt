package ru.esstu

import android.annotation.SuppressLint
import android.content.Context
import androidx.startup.Initializer
import java.util.*

class ContextApplication private constructor(val context: Context){


    companion object{
        @SuppressLint("StaticFieldLeak")
        private var instance: ContextApplication? = null

        fun init(context: Context): ContextApplication{
            return if (instance == null){
                instance = ContextApplication(context)
                instance!!
            }else{
                instance!!
            }
        }

        fun getContextApplication(): ContextApplication{
            return instance!!
        }
    }
}