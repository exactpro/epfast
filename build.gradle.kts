apply(from="gradle/dependencies.gradle.kts")

plugins {
    java
}

group = "com.exactpro"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testCompile(rootProject.extra.get("junit5")!!)
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}