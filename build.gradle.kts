val junitVersion by extra { "5.5.2" }

plugins {
    java
    checkstyle
}

group = "com.exactpro"
version = "0.1-SNAPSHOT"

allprojects{
    apply(plugin="java")
    apply(plugin="checkstyle")

    repositories {
        mavenCentral()
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.named<Test>("test") {
        useJUnitPlatform()
    }

    checkstyle {
        toolVersion = "7.8.2"
        configFile = rootProject.file("config/checkstyle/checkstyle.xml")
    }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    compile ("io.netty", "netty-all",  "4.1.5.Final")
    compile ("io.netty",  "netty-transport-rxtx",  "4.1.5.Final")
}
