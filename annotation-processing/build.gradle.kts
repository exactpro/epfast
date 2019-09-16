plugins {
    `java-library`
}

repositories {
    jcenter()
}

dependencies {
    annotationProcessor("com.google.auto.service:auto-service:1.0-rc6")
    compileOnly("com.google.auto.service:auto-service:1.0-rc6")
    // Use JUnit test framework
    testImplementation("junit:junit:4.12")
}
