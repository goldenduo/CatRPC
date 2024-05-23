pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

dependencyResolutionManagement {
    repositories{
        mavenCentral()
        google()
        maven { url = uri("https://jitpack.io") }
    }
}
rootProject.name = "CatRPC"
include("app")
include("rpc")
