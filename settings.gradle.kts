pluginManagement {
    repositories {
        google()  // This is where you define the repositories
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()  // This is where repositories are defined
        mavenCentral()
    }
}

rootProject.name = "ExpiryAlert"
include(":app")
