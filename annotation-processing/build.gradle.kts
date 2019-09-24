val junitVersion: String by rootProject.extra

plugins {
    `java-library`
}

repositories {
    jcenter()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    annotationProcessor("com.google.auto.service:auto-service:1.0-rc6")
    compileOnly("com.google.auto.service:auto-service:1.0-rc6")
}
