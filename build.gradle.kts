plugins {
    id("fabric-loom")
    kotlin("jvm").version(System.getProperty("kotlin_version"))
}

base { archivesName.set(project.extra["archives_base_name"] as String) }
version = project.extra["mod_version"] as String
group = project.extra["maven_group"] as String

repositories {
    // MidnightLib
    maven {
        url = uri("https://api.modrinth.com/maven")
    }
    // Mod Menu / Trinkets
    maven {
        url = uri("https://maven.terraformersmc.com/releases")
    }
    // Trinkets
    maven {
        url = uri("https://ladysnake.jfrog.io/artifactory/mods")
    }
    // BackSlot
    maven {
        url = uri("https://api.modrinth.com/maven")
    }
    // Amecs Api (for BackSlot)
    maven {
        url = uri("https://maven.siphalor.de/")
    }
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    // Minecraft, Fabric and Kotlin Dependencies
    minecraft("com.mojang", "minecraft", project.extra["minecraft_version"] as String)
    mappings("net.fabricmc", "yarn", project.extra["yarn_mappings"] as String, null, "v2")
    modImplementation("net.fabricmc", "fabric-loader", project.extra["loader_version"] as String)
    modImplementation("net.fabricmc.fabric-api", "fabric-api", project.extra["fabric_version"] as String)
    modImplementation("net.fabricmc", "fabric-language-kotlin", project.extra["fabric_language_kotlin_version"] as String)

    // MidnightLib: https://github.com/TeamMidnightDust/MidnightLib/tree/architectury
    modImplementation("maven.modrinth:midnightlib:${project.extra["midnightlib_version"]}")
    include("maven.modrinth:midnightlib:${project.extra["midnightlib_version"]}")

    // Mod Menu: https://github.com/TerraformersMC/ModMenu
    modImplementation("com.terraformersmc:modmenu:${rootProject.extra["mod_menu_version"]}")

    // Trinkets: https://github.com/emilyploszaj/trinkets
    modImplementation("dev.emi:trinkets:${rootProject.extra["trinkets_version"]}")

    // BackSlot: https://github.com/Globox1997/BackSlot
    modImplementation("maven.modrinth:backslot:${rootProject.extra["backslot_version"]}")

    // Amecs Api (for BackSlot): https://github.com/Siphalor/amecs-api
    modImplementation("de.siphalor:amecsapi-${rootProject.extra["amecs_api_version"]}")
}

tasks {
    val javaVersion = JavaVersion.toVersion((project.extra["java_version"] as String).toInt())
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
        options.release.set(javaVersion.toString().toInt())
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> { kotlinOptions { jvmTarget = javaVersion.toString() } }
    jar { from("LICENSE") { rename { "${it}_${base.archivesName}" } } }
    processResources {
        filesMatching("fabric.mod.json") { expand(mutableMapOf("version" to project.extra["mod_version"] as String, "fabricloader" to project.extra["loader_version"] as String, "fabric_api" to project.extra["fabric_version"] as String, "fabric_language_kotlin" to project.extra["fabric_language_kotlin_version"] as String, "minecraft" to project.extra["minecraft_version"] as String, "java" to project.extra["java_version"] as String)) }
        filesMatching("*.mixins.json") { expand(mutableMapOf("java" to project.extra["java_version"] as String)) }
    }
    java {
        toolchain { languageVersion.set(JavaLanguageVersion.of(javaVersion.toString())) }
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        withSourcesJar()
    }
}