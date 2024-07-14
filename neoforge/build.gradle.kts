import net.neoforged.moddev.shadow.org.codehaus.plexus.util.StringUtils

plugins {
    `java-library`
    idea
    id("net.neoforged.moddev") version "0.1.126"
}

version = project.property("mod_version") as String
group = project.property("mod_group_id")  as String

repositories {
    mavenLocal()
}

base.archivesName = "quinns-click-through"

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

neoForge {
    version = providers.gradleProperty("neo_version")

    parchment {
        mappingsVersion = providers.gradleProperty("parchment_mappings_version")
        minecraftVersion = providers.gradleProperty("parchment_minecraft_version")
    }

    validateAccessTransformers = true

    runs {
        create("client") {
            client()

            systemProperty("neoforge.enabledGameTestNamespaces", "quinnsclickthrough")

            // Recommended logging data for a userdev environment
            // The markers can be added/remove as needed separated by commas.
            // "SCAN": For mods scan.
            // "REGISTRIES": For firing of registry events.
            // "REGISTRYDUMP": For getting the contents of all registries.
            //systemProperty("forge.logging.markers", "REGISTRIES")

            // Recommended logging level for the console
            // You can set various levels here.
            // Please read: https://stackoverflow.com/questions/2031163/when-to-use-the-different-log-levels
            logLevel = org.slf4j.event.Level.INFO
            ideName = "NeoForge - ${StringUtils.capitalise(name)}"
        }
    }

    mods {
        create("quinnsclickthrough") {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main.get().resources.srcDir("src/generated/resources")

tasks.withType<ProcessResources>().configureEach {
    val replaceProperties = mutableMapOf(
        "minecraft_version"       to project.property("minecraft_version"),
        "minecraft_version_range" to project.property("minecraft_version_range"),
        "neo_version"             to project.property("neo_version"),
        "neo_version_range"       to project.property("neo_version_range"),
        "loader_version_range"    to project.property("loader_version_range"),
        "mod_version"             to project.property("mod_version"),
    )

    inputs.properties(replaceProperties)

    filesMatching(listOf("META-INF/neoforge.mods.toml")) {
        expand(replaceProperties)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8" // Use the UTF-8 charset for Java compilation
}

// IDEA no longer automatically downloads sources/javadoc jars for dependencies, so we need to explicitly enable the behavior.
idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
