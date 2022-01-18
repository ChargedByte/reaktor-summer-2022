val exposedVersion: String by project
val flywayVersion: String by project
val guiceVersion: String by project
val hikariCpVersion: String by project
val kotlinVersion: String by project
val ktorVersion: String by project
val logbackVersion: String by project
val postgreSqlVersion: String by project

plugins {
    application
    kotlin("jvm")
}

group = "dev.chargedbyte"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")

    implementation("com.google.inject:guice:$guiceVersion")

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-locations:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")

    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-jackson:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")

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

application {
    mainClass.set("dev.chargedbyte.reaktor_summer_2022.ApplicationKt")
}
