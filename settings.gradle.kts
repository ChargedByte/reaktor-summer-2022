pluginManagement {
    val jibVersion: String by settings
    val kotlinVersion: String by settings
    val nodeGradleVersion: String by settings
    val shadowVersion: String by settings

    repositories {
        gradlePluginPortal()
    }

    plugins {
        kotlin("jvm") version kotlinVersion
        id("com.github.node-gradle.node") version nodeGradleVersion
        id("com.google.cloud.tools.jib") version jibVersion
        id("com.github.johnrengelman.shadow") version shadowVersion
    }
}

rootProject.name = "reaktor-summer-2022"

include("server")
include("client")
