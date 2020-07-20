plugins {
    kotlin("jvm") version "1.3.72"
}

group = "com.motorro"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.reactivex.rxjava3", "rxjava", "3.0.4")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter","junit-jupiter", "5.6.2")
    testImplementation("com.nhaarman.mockitokotlin2","mockito-kotlin", "2.2.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}