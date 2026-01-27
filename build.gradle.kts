plugins {
	java
	id("org.springframework.boot") version "4.0.2" apply false
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.lilamaris.stockwolf"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

subprojects {
	apply(plugin = "java")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}

repositories {
	mavenCentral()
}
