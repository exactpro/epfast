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
    implementation("javax.xml.bind:jaxb-api:2.3.1")

    runtimeOnly("com.sun.xml.bind:jaxb-impl:2.4.0-b180830.0438")
    // istack-commons-runtime should be auto-dependency of jaxb-impl, but isn't for unknown reason
    runtimeOnly("com.sun.istack:istack-commons-runtime:3.0.10")


    testImplementation(kotlin("stdlib-jdk8"))
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

    // temporary. don't forget to remove
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.10.2")
    testImplementation("org.assertj:assertj-core:3.6.2")
}
