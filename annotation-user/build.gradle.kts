val junitVersion: String by rootProject.extra

plugins {
    `java-library`
}

dependencies {
    annotationProcessor(project(":annotation-processing"))
    compile(project(":annotation-processing"))

    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}
