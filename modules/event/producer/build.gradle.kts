plugins {
    `java-library`
}

dependencies {
    api(project(":modules:event:core"))
    implementation(project(":modules:kafka"))
    implementation(project(":modules:jpa"))
}
