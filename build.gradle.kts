val assertjVersion by extra { "3.15.0" }
val junitVersion by extra { "5.6.0" }
val nettyVersion by extra { "4.1.45.Final" }

plugins {
    kotlin("jvm") version "1.3.61"
    `java-library`
    checkstyle
}

group = "com.exactpro"
version = "0.1-SNAPSHOT"

allprojects {
    apply(plugin="java")
    apply(plugin="checkstyle")

    repositories {
        mavenCentral()
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    checkstyle {
        toolVersion = "8.26"
        configFile = rootProject.file("config/checkstyle/checkstyle.xml")
    }

    tasks.test {
        useJUnitPlatform()
    }

    tasks.checkstyleTest {
        source += fileTree("src/test/resources") {
            include("**/*.java")
        }
    }
}

dependencies {
    implementation("io.netty:netty-all:$nettyVersion")

    testImplementation(kotlin("stdlib-jdk8"))
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}
