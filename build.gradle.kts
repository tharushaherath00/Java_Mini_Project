plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // MySQL JDBC driver for database connectivity
    implementation("mysql:mysql-connector-java:8.0.33") // Use the latest version if needed

    // Existing test dependencies
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}