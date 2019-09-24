val junitVersion by extra { "5.5.2" }
plugins {
    java
}

group = "com.exactpro"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

allprojects{
    apply(plugin="java")

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

