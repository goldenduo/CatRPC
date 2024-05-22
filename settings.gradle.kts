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
    }
}
rootProject.name = "CatRPC"
include("app")
include("rpc")
