pluginManagement {
    repositories {
        mavenLocal()
        maven { url = uri("/opt/local-maven-repo") }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        maven { url = uri("/opt/local-maven-repo") }
        google()
        mavenCentral()
    }
}

rootProject.name = "AIHer"
include(":app")