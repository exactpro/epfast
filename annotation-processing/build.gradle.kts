val junitVersion: String by rootProject.extra

plugins {
    `java-library`
    java
}

sourceSets {
    create("integrationTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
        annotationProcessorPath += sourceSets.main.get().output
    }

}

configurations["integrationTestImplementation"].extendsFrom(configurations.testImplementation.get())
configurations["integrationTestRuntimeOnly"].extendsFrom(configurations.testRuntimeOnly.get())
configurations["integrationTestAnnotationProcessor"].extendsFrom(configurations.testRuntimeOnly.get(), configurations.testImplementation.get())

configurations["annotationProcessor"].extendsFrom(configurations.runtimeOnly.get(), configurations.implementation.get())
val integrationTest = task<Test>("integrationTest") {
    useJUnitPlatform()
    description = "Runs integration tests."
    group = "verification"

    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
    mustRunAfter("test")
}

tasks.check { dependsOn(integrationTest) }

dependencies {

    implementation("com.google.auto.service:auto-service:1.0-rc6")
    implementation("com.github.spullara.mustache.java:compiler:0.9.6")
    implementation(project(":epfast-annotations"))
    testImplementation(project(":epfast-annotations"))
    testImplementation("com.google.testing.compile:compile-testing:0.18")
    testImplementation("com.google.truth:truth:1.0")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}
