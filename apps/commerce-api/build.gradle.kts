dependencies {
    // add-ons
    implementation(project(":modules:jpa"))
    implementation(project(":modules:rediss"))
    implementation(project(":supports:jackson"))
    implementation(project(":supports:logging"))
    implementation(project(":supports:monitoring"))

    // web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${project.properties["springDocOpenApiVersion"]}")

    implementation("org.springframework.retry:spring-retry")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    // querydsl
    implementation("com.querydsl:querydsl-jpa::jakarta")

    // feign
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    // resilience
    implementation("io.github.resilience4j:resilience4j-spring-boot3:2.0.2")


    // test-fixtures
    testImplementation(testFixtures(project(":modules:jpa")))
    testImplementation(testFixtures(project(":modules:rediss")))
}
