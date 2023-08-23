package com.jyoliar.mongo

import org.springframework.data.mapping.toDotPath
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties

fun projection(any: KClass<*>, vararg excludes: KProperty<*>): ProjectionOperation {
    val excludeProperties = excludes.map { it.toDotPath() }

    var projection = Aggregation.project()

    any.declaredMemberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .filterNot { excludeProperties.contains(it.toDotPath()) }
        .forEach {
            projection = projection.and(it.toDotPath()).`as`(it.toDotPath())
        }

    return projection
}


fun ProjectionOperation.ProjectionOperationBuilder.`as`(kProperty1: KProperty1<*, *>): ProjectionOperation {
    return this.`as`(kProperty1.toDotPath())
}