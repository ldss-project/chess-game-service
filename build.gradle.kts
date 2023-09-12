// ### Project Information #############################################################################################
private class ProjectInfo {
    val longName: String = "Chess Game Service"
    val description: String = "A service which handles the configuration, execution and termination of chess games."

    val repositoryOwner: String = "ldss-project"
    val repositoryName: String = "chess-game-service"

    val artifactGroup: String = "io.github.jahrim.chess"
    val artifactId: String = project.name
    val implementationClass: String = "io.github.jahrim.chess.game.service.main.main"

    val license = "The MIT License"
    val licenseUrl = "https://opensource.org/licenses/MIT"

    val website = "https://github.com/$repositoryOwner/$repositoryName"
    val tags = listOf("scala3", "chess", "game")
}
private val projectInfo: ProjectInfo = ProjectInfo()

// ### Build Configuration #############################################################################################
plugins {
    with(libs.plugins){
        `java-library`
        scala
        application
        alias(spotless)
        alias(wartremover)
        alias(git.semantic.versioning)
        alias(publish.on.central)
    }
}

repositories { mavenCentral() }

dependencies {
    compileOnly(libs.bundles.scalafmt)
    implementation(libs.tuprolog)
    implementation(libs.scala)
    implementation(libs.scallop)
    implementation(libs.hexarc)
    implementation(libs.bundles.vertx.web)
    implementation(libs.chess.engine)
    testImplementation(libs.scalatest)
    testImplementation(libs.scalatestplusjunit)
}

application {
    mainClass.set(projectInfo.implementationClass)
    tasks.withType(JavaExec::class.java){
        properties["statisticsService"]?.apply { args("--statistics-service", this) }
        properties["httpHost"]?.apply { args("--http-host", this) }
        properties["httpPort"]?.apply { args("--http-port", this) }
        properties["allowedOrigins"]?.apply { args("--allowed-origins", this) }
    }
}

spotless {
    isEnforceCheck = false
    scala {
        scalafmt(libs.versions.scalafmt.version.get()).configFile(".scalafmt.conf")
        licenseHeader("/*\n${file("LICENSE").readText()}\n*/", "package ")
    }
    tasks.compileScala.get().dependsOn(tasks.spotlessApply)
}

// ### Publishing ######################################################################################################
group = projectInfo.artifactGroup
gitSemVer {
    buildMetadataSeparator.set("-")
    assignGitSemanticVersion()
}

tasks.jar {
    dependsOn(configurations.runtimeClasspath)
    from(sourceSets.main.get().output)
    from(configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) })

    manifest.attributes["Main-Class"] = projectInfo.implementationClass
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.javadocJar {
    dependsOn(tasks.scaladoc)
    from(tasks.scaladoc.get().destinationDir)
}

publishOnCentral {
    configureMavenCentral.set(true)
    projectDescription.set(projectInfo.description)
    projectLongName.set(projectInfo.longName)
    licenseName.set(projectInfo.license)
    licenseUrl.set(projectInfo.licenseUrl)
    repoOwner.set(projectInfo.repositoryOwner)
    projectUrl.set(projectInfo.website)
    scmConnection.set("scm:git:$projectUrl")
}

publishing {
    publications {
        withType<MavenPublication> {
            pom {
                developers {
                    developer {
                        name.set("Jahrim Gabriele Cesario")
                        email.set("jahrim.cesario2@studio.unibo.it")
                        url.set("https://jahrim.github.io")
                    }
                    developer {
                        name.set("Madina Kentpayeva")
                        email.set("madina.kentpayeva@studio.unibo.it")
                        url.set("https://madina9229.github.io")
                    }
                }
            }
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
}