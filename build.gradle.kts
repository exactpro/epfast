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
        toolVersion = "8.26"
        configFile = rootProject.file("config/checkstyle/checkstyle.xml")
    }

    tasks.checkstyleTest {
        source += fileTree("src/test/resources") {
            include("**/*.java")
        }
    }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    compile ("io.netty", "netty-all",  "4.1.5.Final")
}
