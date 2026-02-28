group = "com.lilamaris.stockwolf"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":event:event-core"))
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.kafka:spring-kafka")
}

tasks.test {
    useJUnitPlatform()
}