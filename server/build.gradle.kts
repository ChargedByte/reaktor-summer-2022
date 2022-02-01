val exposedVersion: String by project
val flywayVersion: String by project
val guiceVersion: String by project
val hikariCpVersion: String by project
val jacksonVersion: String by project
val kotlinVersion: String by project
val ktorVersion: String by project
val logbackVersion: String by project
val postgreSqlVersion: String by project

plugins {
    application
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
    id("com.google.cloud.tools.jib")
}

group = "dev.chargedbyte"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")

    implementation("com.google.inject:guice:$guiceVersion")

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-locations:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")

    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-jackson:$ktorVersion")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")

    implementation("com.zaxxer:HikariCP:$hikariCpVersion")

    implementation("org.flywaydb:flyway-core:$flywayVersion")

    implementation("org.postgresql:postgresql:$postgreSqlVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}

tasks.withType<Jar> {
    archiveBaseName.set("reaktor-summer-2022-server")
}

application {
    mainClass.set("dev.chargedbyte.reaktor_summer_2022.ApplicationKt")
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
    }
}

jib {
    to {
        image = "reaktor-summer-2022-server"

        val version = version.toString().split(".")
        val major = version[0]
        val minor = version[1]
        val patch = version[2]

        tags = setOf(major, "$major.$minor", "$major.$minor.$patch")
    }

    from {
        image = "eclipse-temurin:11-jre-alpine"
    }

    container {
        ports = listOf("8080/tcp")
    }
}
