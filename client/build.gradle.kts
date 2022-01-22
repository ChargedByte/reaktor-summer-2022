import com.github.gradle.node.yarn.task.YarnInstallTask
import com.github.gradle.node.yarn.task.YarnTask

plugins {
    id("com.github.node-gradle.node")
}

node {
    download.set(true)
}

tasks {
    register<Delete>("clean") {
        delete(buildDir)

        delete("${projectDir}/.nuxt")
        delete("${projectDir}/dist")
    }

    register<YarnTask>("generate") {
        dependsOn(withType<YarnInstallTask>())
        args.set(listOf("generate", "--fail-on-error"))
    }

    register("build") {
        dependsOn(named("generate"))
    }
}
