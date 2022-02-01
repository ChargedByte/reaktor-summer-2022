import com.github.gradle.node.yarn.task.YarnInstallTask
import com.github.gradle.node.yarn.task.YarnTask

plugins {
    id("com.github.node-gradle.node")
}

node {
    download.set(true)
    version.set("16.13.2")
}

tasks {
    register<Delete>("clean") {
        delete(buildDir)

        delete("${projectDir}/.nuxt")
        delete("${projectDir}/dist")
    }

    register<YarnTask>("build") {
        dependsOn(withType<YarnInstallTask>())
        args.set(listOf("build"))
    }
}
