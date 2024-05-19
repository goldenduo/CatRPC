plugins {
    kotlin("jvm") version Versions.KOTLIN
    id("com.github.gmazzo.buildconfig") version Versions.BUILDCONFIG
}
buildConfig {
    packageName="com.goldenduo.catrpc"
    buildConfigField("VERSION", 1)
    buildConfigField("DEBUG", true)
    buildConfigField("INFO", false)
}
tasks.test {
    useJUnitPlatform()
}
dependencies {
    api("io.netty:netty-all:4.1.109.Final")
    implementation("org.reflections:reflections:0.10.2")
    implementation("com.caucho:hessian:4.0.66")
    implementation("org.objenesis:objenesis:3.4")
    implementation("io.protostuff:protostuff-core:${Versions.PROTOSTUFF}")
    implementation("io.protostuff:protostuff-runtime:${Versions.PROTOSTUFF}")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.KOTLIN_COROUTINES}")
    implementation("com.github.sarveswaran-m:util.concurrent.blockingMap:0.91")


    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(8)
}