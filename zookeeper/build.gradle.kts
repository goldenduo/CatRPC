plugins {
    kotlin("jvm") version Versions.KOTLIN
}


dependencies {
    implementation(project(":common"))
}

kotlin {
    jvmToolchain(8)
}