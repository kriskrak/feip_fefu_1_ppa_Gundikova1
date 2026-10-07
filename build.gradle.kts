plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.devtools.ksp") version "2.3.10" apply false
    id("org.jlleitschuh.gradle.ktlint") version "13.1.0" apply false
}