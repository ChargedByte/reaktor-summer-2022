pluginManagement {
    val kotlinVersion: String by settings
    val nodeGradleVersion: String by settings

    repositories {
        gradlePluginPortal()
    }

    plugins {
        kotlin("jvm") version kotlinVersion
        id("com.github.node-gradle.node") version nodeGradleVersion
    }
}

rootProject.name = "reaktor-summer-2022"

include("server")
include("client")
