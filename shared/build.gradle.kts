plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    kotlin("plugin.serialization")
}

kotlin {
    androidTarget()

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation("io.ktor:ktor-client-core:3.3.3")
                implementation("io.github.aakira:napier:2.7.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
                implementation("io.ktor:ktor-client-content-negotiation:3.3.3")
                implementation("io.ktor:ktor-serialization-kotlinx-json:3.3.3")
                implementation("io.insert-koin:koin-core:4.1.1")
                implementation("io.ktor:ktor-client-cio:3.3.3")
                implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel:2.9.6")
                implementation("org.slf4j:slf4j-simple:2.0.17")
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.activity:activity-compose:1.12.1")
                api("androidx.appcompat:appcompat:1.7.1")
                api("androidx.core:core-ktx:1.17.0")
                implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
                implementation("io.ktor:ktor-client-okhttp:3.3.3")
                implementation("io.insert-koin:koin-android:4.1.1")
                implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.10.0")
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)
            }
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.myapplication.common"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}
