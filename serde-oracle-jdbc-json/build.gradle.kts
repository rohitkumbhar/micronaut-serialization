plugins {
    id("io.micronaut.build.internal.serde-module")
}

configurations.all {
    exclude("io.micronaut", "micronaut-jackson-databind")
    exclude("io.micronaut", "micronaut-jackson-core")
}

dependencies {
    annotationProcessor(mn.micronaut.inject.java)
    annotationProcessor(projects.serdeProcessor)

    api(mn.micronaut.context)
    api(projects.serdeApi)
    implementation(projects.serdeSupport)
    implementation(libs.oracle.jdbc.driver)
    compileOnly(libs.graal.svm)
    compileOnly(mn.micronaut.jackson.databind)
    testAnnotationProcessor(mn.micronaut.inject.java)
    testAnnotationProcessor(projects.serdeProcessor)

    testImplementation(mn.jackson.annotations)
    testImplementation(projects.serdeProcessor)
    testImplementation(projects.serdeTck)
    testImplementation(mn.micronaut.inject.java.test)
    testImplementation(mnTest.micronaut.test.junit5)
    testImplementation(mn.micronaut.http.server.netty)
    testImplementation(mn.micronaut.http.client)
    testRuntimeOnly(
        "org.junit.jupiter:junit-jupiter-engine"
    )
    testCompileOnly(mn.micronaut.inject.groovy)
    testImplementation(mnTest.micronaut.test.spock)
    testImplementation(mnReactor.micronaut.reactor)
}

tasks {
    test {
        useJUnitPlatform()
    }
}
