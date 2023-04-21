plugins {
    java
    application
    id("org.springframework.boot") version "3.0.0"
    id("io.spring.dependency-management") version "1.1.0"
    id("com.google.cloud.tools.jib") version "3.3.1"
    id("pl.allegro.tech.build.axion-release") version "1.14.0"
    id ("jacoco")
    id("org.sonarqube") version "3.5.0.2730"
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
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.flywaydb:flyway-core")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor ("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.0")
    implementation(platform("software.amazon.awssdk:bom:2.15.0"))
    implementation("software.amazon.awssdk:s3")
    implementation("software.amazon.awssdk:cognitoidentityprovider")
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.security:spring-security-test")
    implementation("com.nimbusds:nimbus-jose-jwt:9.31")

    // cdk vs sdk
    // cognito vs cognitoidentityprovider
    // Development Kit (SDK) is a set of libraries that allow you to integrate your application with AWS Services. The AWS Cloud Development Kit (CDK) is a framework that allows you to provision Cloud infrastructure using code.


}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Basic Config
val mainClassPath = "com.m2z.tools.managementservice.ManagementServiceApplication"
application {
    mainClass.set(mainClassPath)
}

//System.setProperty("spring.profiles.active", (if (project.hasProperty("springProfiles")) project.property("springProfiles") else "") as String)
// for some reason we can't set spring profile build.gradle is not being executed (checked with breakpoints)
tasks.bootRun {// Example selecting profiles: ./gradlew bootRun -PspringProfiles=dev
    systemProperties.put("spring.profiles.active",
            if (project.hasProperty("springProfiles")) project.property("springProfiles") else "")
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

// Containerization (Fixed to local directly to docker-deamon)
jib {
    from {
        image = "eclipse-temurin:17-jre"
    }
    container {
        ports = listOf("8080")
        format = com.google.cloud.tools.jib.api.buildplan.ImageFormat.OCI
        containerizingMode = "packaged"
        mainClass = mainClassPath
//        labels:
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

// Containerization (Fixed to remote directly to container registry)
tasks.named("jib") {
    doFirst {
        jib {
            to {
                if (project.hasProperty("remoteRegistry") && project.hasProperty("remoteImage")) {
                    val remoteRegistry = project.properties["remoteRegistry"]
                    val remoteImage = project.properties["remoteImage"]
                    image = "$remoteRegistry/$remoteImage"
                } else if (System.getProperty("AWS_ECR_IMAGE_ID") != null && System.getProperty("AWS_ECR_URL") != null) {
                    val remoteRegistry = System.getProperty("AWS_ECR_URL")
                    val remoteImage = System.getProperty("AWS_ECR_IMAGE_ID")
                    image = "$remoteRegistry/$remoteImage"
                } else {
                    throw GradleException("Both 'registry url' & 'registry image' are required")
                }
                if (!project.hasProperty("cd")) {
                    credHelper.helper = "ecr-login"
                }
                tags = setOf("latest"/*, version.toString()*/)
            }
        }
    }
}

// Tests
testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
            dependencies {
                // Note that this is equivalent to adding dependencies to testImplementation in the top-level dependencies block
            }
        }
        val integrationTest by registering(JvmTestSuite::class) {
            dependencies {
                implementation(project)
                implementation("org.springframework.boot:spring-boot-starter-test")
            }
            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                    }
                }
            }
        }
    }
}
tasks.named("check") {
    dependsOn(testing.suites.named("integrationTest"))
}

// Jacoco
tasks.jacocoTestReport {
    val integrationTest = testing.suites.named("integrationTest")
    executionData(tasks.test, integrationTest)
    reports {
        xml.required.set(true)
    }
    dependsOn(tasks.test,integrationTest)
}

// SonarQube
sonar {
    properties {
        // NEW in sonar task
        sourceSets.add(sourceSets["integrationTest"])
        sourceSets.add(sourceSets["test"])
        // Scoping DEPRECATED sonarqube task
        // how did I get to this point started debugging looking at variable types return types etc... methods etc... very different from groovy
        //  property("sonar.tests",
        //        sourceSets["integrationTest"].allSource.srcDirs.toList()
        //                + sourceSets["test"].allSource.srcDirs.toList()
        //  )
        // Project Properties
        property("sonar.branch.name", gitBranch())
        property("sonar.host.url", project.properties.get("SONAR_HOST_URL")!!)
        property("sonar.organization", project.properties.get("SONAR_ORGANIZATION")!!)
        property("sonar.projectKey", project.properties.get("SONAR_PROJECTKEY")!!)
        property("sonar.login", project.properties.get("SONAR_LOGIN")!!)

//        GRADLE PROPERTIES
//        property("sonar.host.url", System.getProperty("SONAR_HOST_URL"))
//        property("sonar.organization", System.getProperty("SONAR_ORGANIZATION"))
//        property("sonar.projectKey", System.getProperty("SONAR_PROJECTKEY"))
//        property("sonar.login", System.getProperty("SONAR_LOGIN"))
    }
}