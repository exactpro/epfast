plugins {
    `java-library`
}

repositories {
    jcenter()
}

dependencies {
    annotationProcessor("com.google.auto.service:auto-service:1.0-rc6")

    compileOnly("com.google.auto.service:auto-service:1.0-rc6")

    testCompile(rootProject.extra.get("junit5")!!)
}
