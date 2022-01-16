package dev.chargedbyte.reaktor_summer_2022.config

import com.google.inject.AbstractModule
import io.ktor.application.*

class MainModule(private val application: Application) : AbstractModule() {
    override fun configure() {
        bind(Application::class.java).toInstance(application)
    }
}
