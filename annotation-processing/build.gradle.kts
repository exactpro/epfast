val junitVersion: String by rootProject.extra

plugins {
    `java-library`
}

sourceSets {
    create("integrationTest") {
        this.annotationProcessorPath += sourceSets.main.get().output
    }
}

val junitImplementation: Configuration by configurations.creating {
}

val junitRuntimeOnly: Configuration by configurations.creating {
}

configurations["testImplementation"].extendsFrom(junitImplementation)
val integrationTestImplementation: Configuration by configurations.getting {
    extendsFrom(junitImplementation)
}

configurations["testRuntimeOnly"].extendsFrom(junitRuntimeOnly)
configurations["integrationTestRuntimeOnly"].extendsFrom(junitRuntimeOnly)

configurations["integrationTestAnnotationProcessor"]
    .extendsFrom(configurations.runtimeOnly.get(), configurations.implementation.get())

dependencies {
    annotationProcessor("com.google.auto.service:auto-service:1.0")
    implementation("com.google.auto.service:auto-service:1.0")
    implementation("com.github.spullara.mustache.java:compiler:0.9.6")
    implementation(project(":epfast-annotations"))

    junitImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    junitRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

    testImplementation("com.google.testing.compile:compile-testing:0.19")
    testRuntimeOnly(project(":epfast-annotations"))

    integrationTestImplementation(project(":epfast-annotations"))
}

val integrationTest = task<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"
    mustRunAfter(tasks.test)

    useJUnitPlatform()
    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
}

tasks.check {
    dependsOn(integrationTest)
}
