val junitVersion: String by rootProject.extra

plugins {
    `java-library`
}
repositories {

    jcenter()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    annotationProcessor(project(":annotation-processing"))
    compile(project(":annotation-processing"))
}
