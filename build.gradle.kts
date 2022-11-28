plugins {
    java
    application
    id("org.springframework.boot") version "3.0.0"
    id("io.spring.dependency-management") version "1.1.0"
    id("com.google.cloud.tools.jib") version "3.3.1"
    id("pl.allegro.tech.build.axion-release") version "1.14.0"
}

group = "com.m2z.tools"
version = scmVersion.version
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.flywaydb:flyway-core")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Basic Config
val mainClassPath = "com.m2z.tools.managementservice.ManagementServiceApplication"
application {
    mainClass.set(mainClassPath)
}

extensions.findByName("buildScan")?.withGroovyBuilder {
    setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
    setProperty("termsOfServiceAgree", "yes")
}

// Utility method
fun gitBranch(): String {
    var branch: String
    val proc = ProcessBuilder("git", "rev-parse", "--abbrev-ref", "HEAD").start()
    proc.inputStream.bufferedReader().use { branch = it.readLine()  }
    proc.waitFor()
    return branch
}

// Containerization (Local directly to docker-deamon)
jib {
    from {
        image = "eclipse-temurin:17-jre"
    }
    container {
        ports = listOf("8080")
        format = com.google.cloud.tools.jib.api.buildplan.ImageFormat.OCI
        containerizingMode = "packaged"
        mainClass = mainClassPath
    }
}

tasks.named("jibDockerBuild") {
    doFirst {
        jib {
            to {
                image = "management-service"
                credHelper.helper = "osxkeychain"
                tags = setOf("latest", gitBranch() + " " + version.toString())
            }
        }
    }
}

// Containerization (Remote directly to container registry)
tasks.named("jib") {
    doFirst {
        if (!project.hasProperty("remoteRegistry") || !project.hasProperty("remoteImage")) {
            throw GradleException("Both 'remoteRegistry' & 'remoteImage' variables are required")
        }
        val remoteRegistry = project.properties["remoteRegistry"]
        val remoteImage = project.properties["remoteImage"]
        jib {
            to {
                image = "$remoteRegistry/$remoteImage"
                if (!project.hasProperty("CI_CD")) {
                    credHelper.helper = "ecr-login"
                }
                tags = setOf("latest"/*, version.toString()*/)
            }
        }
    }
}