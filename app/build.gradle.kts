plugins {
    kotlin("jvm") version Versions.KOTLIN
}


dependencies {
    implementation(project(":rpc"))
}

kotlin {
    jvmToolchain(8)
}