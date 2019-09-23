val junitVersion = "5.5.2"
plugins {
    java
}

group = "com.exactpro"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects{
    apply(plugin="java")
    dependencies{
        testImplementation("org.junit.jupiter:junit-jupiter:"+junitVersion)
    }
    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

