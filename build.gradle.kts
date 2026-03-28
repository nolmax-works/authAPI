plugins {
    id("java")
}

group = "com.qtpc.tech.nolmax.authapi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "cutiepcRepoReleases"
        url = uri("https://maven.qtpc.tech/releases")
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.slf4j:slf4j-simple:2.0.17")
    implementation("tools.jackson.dataformat:jackson-dataformat-yaml:3.1.0")
    implementation("io.javalin:javalin:7.1.0")
    implementation("com.nolmax.database:Nolmax_chat:1.3.1")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.qtpc.tech.nolmax.authapi.Main"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.test {
    useJUnitPlatform()
}