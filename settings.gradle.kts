rootProject.name = "loopers-ecommerce"

include(
    ":apps:commerce-api",
    ":apps:pg-simulator",
    ":modules:jpa",
    ":modules:rediss",
    ":supports:jackson",
    ":supports:logging",
    ":supports:monitoring",
    "apps:commerce-streamer",
    "apps:ranking-batch",
    "modules:kafka",
    "modules:event:core",
    "modules:event:producer"
)

// configurations
pluginManagement {
    val springBootVersion: String by settings
    val springDependencyManagementVersion: String by settings

    repositories {
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
        gradlePluginPortal()
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.springframework.boot" -> useVersion(springBootVersion)
                "io.spring.dependency-management" -> useVersion(springDependencyManagementVersion)
            }
        }
    }
}

include("apps:ranking-batch")
