val junitVersion: String by rootProject.extra

plugins {
    `java-library`
}

dependencies {
    annotationProcessor("com.google.auto.service:auto-service:1.0-rc6")
    compileOnly("com.google.auto.service:auto-service:1.0-rc6")
    implementation("com.github.spullara.mustache.java:compiler:0.9.6")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}
