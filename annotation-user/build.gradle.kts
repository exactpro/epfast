plugins {
    `java-library`
}

repositories {

    jcenter()
}

dependencies {
    annotationProcessor(project(":annotation-processing"))
    compile(project(":annotation-processing"))
    testImplementation("junit:junit:4.12")
}
