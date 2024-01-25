/*
 * Copyright 2024 amoseui (Amos Lim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.kotlinKapt) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.io.gitlab.arturbosch.detekt)
    jacoco
//    alias(libs.plugins.aggregation.coverage)
//    alias(libs.plugins.aggregation.results)
}

subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
        kotlinOptions.freeCompilerArgs += listOf(
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=kotlin.time.ExperimentalTime",
        )
    }

    apply(plugin = rootProject.libs.plugins.spotless.get().pluginId)
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt")
            ktlint().editorConfigOverride(mapOf("ktlint_function_naming_ignore_when_annotated_with" to "Composable"))
            licenseHeaderFile(rootProject.file("spotless/spotless.license.kt"))
            trimTrailingWhitespace()
            endWithNewline()
        }
        kotlinGradle {
            target("**/*.kts")
            targetExclude("$buildDir/**/*.kts")
            ktlint()
            licenseHeaderFile(
                rootProject.file("spotless/spotless.license.kt"),
                "(^(?![\\/ ]\\*)@+.*$)"
            )
            trimTrailingWhitespace()
            endWithNewline()
        }
        format("xml") {
            target("**/*.xml")
            targetExclude("$buildDir/**/*.xml")
            licenseHeaderFile(
                rootProject.file("spotless/spotless.license.xml"),
                "(<[^!?])"
            )
            trimTrailingWhitespace()
            endWithNewline()
        }
    }
}

//tasks.register<JacocoReport>(name = "jacocoTestCoverageReport") {
//    dependsOn("testDebugUnitTest")
//
//    reports {
//        xml.required.set(true)
//        html.required.set(true)
//    }
//
//    sourceDirectories.setFrom(files("${project.projectDir}/src/main/java", "${project.buildDir}/generated/source/proto/debug/java"))
//
//    val fileFilter =
//        listOf(
//            "**/R.class",
//            "**/R$*.class",
//            "**/BuildConfig.*",
//            "**/Manifest*.*",
//            "**/*Test*.*",
//            "android/**/*.*",
//            "**/*Hilt*",
//            "**/hilt/**",
//        )
//
//    classDirectories.setFrom(
//        fileTree("${project.buildDir}/intermediates/classes/debug/transformDebugClassesWithAsm/dirs/com/amoseui/cameraspecs") {
//            exclude(fileFilter)
//        },
//    )
//
//    executionData.setFrom(
//        fileTree(project.buildDir) {
//            include(
//                "jacoco/testDebugUnitTest.exec",
//            )
//        },
//    )
//}

detekt {
    toolVersion = "1.18.1"
    config = files("config/detekt/detekt.yml")
    allRules = true
    buildUponDefaultConfig = false
    reports {
        html {
            enabled = true
        }
    }
}

jacoco {
    toolVersion = "0.8.7"
}

//testAggregation {
////    modules {
////        include(projects.demoProject.app, projects.demoProject.domain, projects.demoProject.login)
////        exclude(rootProject)
////    }
//    coverage {
//        exclude("**/*Hilt*", "**/hilt/**")
//    }
//}

//tasks.check {
//    dependsOn(tasks.jacocoAggregatedCoverageVerification)
//}

true // Needed to make the Suppress annotation work for the plugins block
