plugins {
    `java-library`
}
repositories {

    jcenter()
}

dependencies {
    testCompile(rootProject.extra.get("junit5")!!)

    annotationProcessor(project(":annotation-processing"))
    compile(project(":annotation-processing"))
}
