plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    // add-ons
    implementation(project(":modules:jpa"))
    implementation(project(":modules:rediss"))
    implementation(project(":supports:jackson"))

    // web
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.batch:spring-batch-test")

    // test-fixtures
    testImplementation(testFixtures(project(":modules:jpa")))
//    testImplementation(testFixtures(project(":modules:rediss")))

//    testImplementation(platform("org.junit:junit-bom:5.10.0"))
//    testImplementation("org.junit.jupiter:junit-jupiter")
}
