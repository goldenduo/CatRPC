plugins {
    java
    kotlin("jvm") version Versions.KOTLIN
}


dependencies {
    implementation(project(":rpc"))
//    implementation("com.github.goldenduo:catrpc:b30a3b5122")
}

kotlin {
    jvmToolchain(8)
}