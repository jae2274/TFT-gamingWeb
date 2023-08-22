import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("kapt") version "1.6.21"
}



group = "com.tft"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}
allOpen {
    annotation("org.springframework.data.mongodb.core.mapping.Document")
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

extra["springCloudVersion"] = "2021.0.4"


dependencies {
    // https://mvnrepository.com/artifact/org.jsoup/jsoup

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("com.querydsl:querydsl-mongodb:5.0.0") {
        exclude("org.mongodb", "mongo-java-driver")
    }

    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.3")


    implementation("com.h2database:h2")
    kapt("org.hibernate.javax.persistence:hibernate-jpa-2.1-api:1.0.2.Final")

    testImplementation("org.springframework.boot:spring-boot-starter-test")


// https://mvnrepository.com/artifact/com.amazonaws.serverless/aws-serverless-java-container-springboot2
    implementation("com.amazonaws.serverless:aws-serverless-java-container-springboot2:1.9.3")

//    // Spring Cloud Function 을 위한 의존성
//    implementation("org.springframework.cloud:spring-cloud-function-adapter-aws")
//    implementation("org.springframework.cloud:spring-cloud-function-kotlin")
//    implementation("org.springframework.cloud:spring-cloud-starter-function-web")
//
//    // https://mvnrepository.com/artifact/com.amazonaws/aws-lambda-java-core
//    implementation("com.amazonaws:aws-lambda-java-core:1.2.3")
//    // https://mvnrepository.com/artifact/com.amazonaws/aws-lambda-java-events
//    implementation("com.amazonaws:aws-lambda-java-events:3.11.2")
}



dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}
tasks.withType<Jar> { duplicatesStrategy = DuplicatesStrategy.EXCLUDE }


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.assemble {
    dependsOn("shadowJar")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveFileName.set("batch.jar")
    dependencies {
        exclude("org.springframework.cloud:spring-cloud-function-web")
    }
    mergeServiceFiles()
    append("META-INF/spring.handlers")
    append("META-INF/spring.schemas")
    append("META-INF/spring.tooling")
    transform(com.github.jengelman.gradle.plugins.shadow.transformers.PropertiesFileTransformer::class.java) {
        paths.add("META-INF/spring.factories")
        mergeStrategy = "append"
    }
}

kapt {
    correctErrorTypes = true
}