// ### Project Information #############################################################################################
private class ProjectInfo { // TODO change project info
    val longName: String = "Scala 3 Project Template"
    val description: String = "A template for configuring Scala 3 projects."

    val repositoryOwner: String = "jahrim"
    val repositoryName: String = "scala3-project-template"

    val artifactGroup: String = "io.github.jahrim"
    val artifactId: String = project.name
    val implementationClass: String = "main.main"

    val license = "The MIT License"
    val licenseUrl = "https://opensource.org/licenses/MIT"

    val website = "https://github.com/$repositoryOwner/$repositoryName"
    val tags = listOf("scala3", "project template")
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
    implementation(libs.scala)
    implementation(libs.scallop)
    testImplementation(libs.scalatest)
    testImplementation(libs.scalatestplusjunit)
}

application {
    mainClass.set(projectInfo.implementationClass)
    tasks.withType(JavaExec::class.java){ args() }
}

spotless {
    isEnforceCheck = false
    scala { scalafmt(libs.versions.scalafmt.version.get()).configFile(".scalafmt.conf") }
    tasks.compileScala.get().dependsOn(tasks.spotlessApply)
}

// ### Publishing ######################################################################################################
group = projectInfo.artifactGroup
gitSemVer {
    buildMetadataSeparator.set("-")
    assignGitSemanticVersion()
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
                // TODO change developers
                developers {
                    developer {
                        name.set("Jahrim Gabriele Cesario")
                        email.set("jahrim.cesario2@studio.unibo.it")
                        url.set("https://jahrim.github.io")
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