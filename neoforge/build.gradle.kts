import net.neoforged.moddev.shadow.org.codehaus.plexus.util.StringUtils

plugins {
    `java-library`
    idea
    id("net.neoforged.moddev") version "1.0.21"
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
