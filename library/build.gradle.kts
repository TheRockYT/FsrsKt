import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.jetbrains.dokka)
}

group = "one.felsen.fsrskt"
version = "0.0.1-SNAPSHOT"
description = "This project is a Kotlin Multiplatform library that implements the FSRS-6 (Free Spaced Repetition Scheduler) algorithm."

kotlin {
    jvm()
    androidLibrary {
        namespace = "org.jetbrains.kotlinx.multiplatform.library.template"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withJava() // enable java compilation support
        withHostTestBuilder {}.configure {}
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }
    iosArm64()
    iosSimulatorArm64()
    linuxX64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            implementation(libs.android.documentation.plugin)
        }
    }
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)

    signAllPublications()

    coordinates(group.toString(), "fsrskt", version.toString())

    pom {
        name.set("FsrsKt")
        description.set("This project is a Kotlin Multiplatform library that implements the FSRS-6 (Free Spaced Repetition Scheduler) algorithm.")
        inceptionYear.set("2026")
        url.set("https://github.com/therockyt/fsrskt/")
        licenses {
            license {
                name.set("GNU GENERAL PUBLIC LICENSE, Version 3")
                url.set("https://www.gnu.org/licenses/gpl-3.0.html")
                distribution.set("https://www.gnu.org/licenses/gpl-3.0.html")
            }
        }
        developers {
            developer {
                id.set("TheRockYT")
                name.set("TheRockYT")
                url.set("https://github.com/therockyt/")
            }
        }
        scm {
            url.set("https://github.com/therockyt/fsrskt/")
            connection.set("scm:git:git://github.com/therockyt/fsrskt.git")
            developerConnection.set("scm:git:ssh://git@github.com/therockyt/fsrskt.git")
        }
    }
}

dokka {
    pluginsConfiguration.html {
        moduleName.set(rootProject.name)
        moduleVersion.set(project.version.toString())

        footerMessage.set("Made with <3")
        separateInheritedMembers.set(true)
    }
}
