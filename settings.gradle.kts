pluginManagement {
    val kotlinVersion: String by settings

    repositories {
        gradlePluginPortal()
    }

    plugins {
        kotlin("jvm") version kotlinVersion
    }
}

rootProject.name = "reaktor-summer-2022"

include("server")
