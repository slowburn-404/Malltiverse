import java.util.Properties

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.ksp.kotlin) apply false
    alias(libs.plugins.compose.compiler) apply false
}
//import local.properties
val properties = Properties()
file("local.properties").inputStream().use { properties.load(it) }