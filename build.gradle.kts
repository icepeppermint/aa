plugins {
    id("java")
}

allprojects {
    group = "io.aa"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")

    dependencies {
        implementation("io.netty:netty-all:4.1.96.Final")
        implementation("org.reactivestreams:reactive-streams:1.0.4")
        implementation("io.reactivex.rxjava3:rxjava:3.1.7")
        implementation("com.google.guava:guava:32.1.2-jre")
        testImplementation("org.awaitility:awaitility:4.2.0")
        testImplementation(platform("org.junit:junit-bom:5.9.1"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    tasks.test {
        useJUnitPlatform()
    }
}