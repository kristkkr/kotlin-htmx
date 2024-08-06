package no.mikill.kotlin_htmx

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

data class PropertyAndValue<T>(
    val property: KProperty1<Any, T>,
    val value: T?
) {
    fun getJavaFieldAnnotations(): Array<Annotation>? {
        return property.javaField?.annotations
    }
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T> resolveProperty(
    instance: Any,
    propertyName: String,
): PropertyAndValue<T> {
    var currentInstance: Any? = instance
    var currentProperty: KProperty1<Any, Any>? = null

    propertyName.split(".").forEach { currentPropertyName ->
        val (listIndexMatchResult, propertyNameNoIndex) = Regex("""\\[(\\d+)\\]""").let {
            it.find(currentPropertyName) to it.replace(currentPropertyName, "")
        }

        val currentPropertyClass =
            if (currentProperty == null) currentInstance!!::class else currentProperty!!.returnType.classifier as KClass<*>

        currentProperty =
            currentPropertyClass.memberProperties.find { it.name == propertyNameNoIndex } as KProperty1<Any, Any>?
        currentInstance = if (currentInstance != null) currentProperty?.get(currentInstance!!) else null

        // Will have to go deeper if it is a list
        if (currentInstance is List<*> && listIndexMatchResult != null) {
            currentInstance = (currentInstance as List<*>)[listIndexMatchResult.groupValues[1].toInt()]!!
            currentProperty =
                currentInstance!!::class.memberProperties.find { it.name == propertyNameNoIndex } as KProperty1<Any, Any>?
        }
    }

    return PropertyAndValue(
        currentProperty as KProperty1<Any, T>,
        if (T::class == String::class) currentInstance.toString() as T else currentInstance as T
    )
}