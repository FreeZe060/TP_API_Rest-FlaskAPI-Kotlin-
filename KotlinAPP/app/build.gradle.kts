plugins {
    kotlin("multiplatform") version "1.8.20" // Version de Kotlin
    id("org.jetbrains.compose") version "1.5.0" // Version de Compose
    application // Plugin pour l'application (permet de définir la tâche `run`)
}

repositories {
    mavenCentral()
    google()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

kotlin {
    jvm() // Cibler uniquement la plateforme JVM

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs) // Dépendance Compose pour le desktop
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
                implementation("io.ktor:ktor-client-core:2.0.0") // Client HTTP Ktor pour l'API
                implementation("io.ktor:ktor-client-cio:2.0.0")
            }
        }
    }
}

// Supprimer ou configurer différemment la tâche `run` du plugin `application`
// tasks.named("run").configure {
//     enabled = false // Désactive la tâche `run` par défaut
// }

application {
    // Pas besoin de définir une tâche `run` ici
    // mainClass.set("org.example.AppKt") // Définir la classe principale ici pour Compose
}

// Configuration spécifique à Compose Desktop
compose.desktop {
    application {
        mainClass = "org.example.AppKt" // Classe principale de l'application Compose
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21) // Java 21
    }
}
