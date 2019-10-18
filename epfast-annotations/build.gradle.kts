val junitVersion: String by rootProject.extra

plugins {
    `java-library`
}

dependencies {

    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}