group = "com.lilamaris.stockwolf"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":event:core"))
    implementation(project(":event:atomicity:supports:jpa"))

    implementation("org.springframework:spring-context")
}

tasks.test {
    useJUnitPlatform()
}