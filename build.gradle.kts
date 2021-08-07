group = "com.github.ExplodingDragon.CodeGuide"

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
    for (childProject in childProjects.values) {
        delete(childProject.buildDir)
    }
}
