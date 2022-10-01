import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    kotlin("jvm") version "1.6.10"
}

group = "com.github.frcsty"
version = "2.3.1"

val libsPath = "com.github.frcsty.frozenjoin"
val javaVersion = JavaVersion.VERSION_16

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.codemc.org/repository/maven-public")
    maven("https://jitpack.io/")
}

dependencies {
    implementation(projects.actionLib)
    implementation("me.mattstudios.utils:matt-framework:1.4.6")
    implementation("org.bstats:bstats-bukkit:2.2.1")
    compileOnly("io.papermc.paper:paper-api:1.17-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.1")
}

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

tasks {
    withType<ProcessResources> {
        expand("version" to project.version)
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = javaVersion.majorVersion
        }
    }

    withType<ShadowJar> {
        relocate("org.bstats", "${libsPath}.bstats")
        relocate("me.mattstudios.mf", "${libsPath}.mf-utils")
        relocate("kotlin", "${libsPath}.kotlin")
        dependencies {
            exclude(dependency("org.jetbrains:annotations"))
        }

        archiveClassifier.set(null as String?)
    }
}
