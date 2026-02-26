group = "com.lilamaris.stockwolf"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":inventory:inventory-contract"))

    implementation(project(":kernel"))

    implementation(project(":identity:identity-client"))

    implementation(project(":idempotency:idempotency-core"))
    implementation(project(":idempotency:idempotency-support-jpa"))
    implementation(project(":idempotency:idempotency-support-redis"))

    implementation(project(":event:event-core"))
    implementation(project(":event:event-support-jpa"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.kafka:spring-kafka")

    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
    testImplementation("org.testcontainers:postgresql:1.21.3")
    testImplementation("org.testcontainers:jdbc")
}

tasks.test {
    useJUnitPlatform()
}