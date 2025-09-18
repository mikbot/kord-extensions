import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
	kotlin("jvm")
}

kotlin {
	explicitApi = ExplicitApiMode.Disabled
}

fun fixExplicitApiModeArg() {
	val clazz = ExplicitApiMode.Disabled.javaClass
	val field = clazz.getDeclaredField("cliOption")

	with(field) {
		isAccessible = true
		set(ExplicitApiMode.Disabled, "disable")
	}
}
