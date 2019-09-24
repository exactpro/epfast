val junitVersion by extra { "5.5.2" }
plugins {
    java
    checkstyle
}

group = "com.exactpro"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

allprojects{
    apply(plugin="java")
    apply(plugin="checkstyle")

    checkstyle {
        toolVersion="7.8.1"
        configFile=rootProject.file("config/checkstyle/checkstyle.xml")
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

