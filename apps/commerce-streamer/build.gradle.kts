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
    implementation(project(":modules:kafka"))
    implementation(project(":modules:event:core"))
    implementation(project(":supports:jackson"))
    implementation(project(":supports:logging"))
    implementation(project(":supports:monitoring"))

    // web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // test-fixtures
    testImplementation(testFixtures(project(":modules:jpa")))
//    testImplementation(testFixtures(project(":modules:rediss")))

//    testImplementation(platform("org.junit:junit-bom:5.10.0"))
//    testImplementation("org.junit.jupiter:junit-jupiter")
}
