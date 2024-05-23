import com.goldenduo.buildsrc.ProjectSetting

plugins {
    kotlin("jvm") version Versions.KOTLIN
    id("com.github.gmazzo.buildconfig") version Versions.BUILDCONFIG
    `maven-publish`
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

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
    from("LICENCE.md") {
        into("META-INF")
    }
}
configure<PublishingExtension> {
    publications {
        register("release", MavenPublication::class) {
            groupId = ProjectSetting.groupId
            artifactId = ProjectSetting.artifactId
            version = version
            from(components["java"])
            artifact(sourcesJar)
            pom {
                packaging = "jar"
                name.set(ProjectSetting.name)
                description.set(ProjectSetting.description)
            }
        }
    }
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
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
    implementation("commons-codec:commons-codec:1.15")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(8)
}
