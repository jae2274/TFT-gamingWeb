package com.jyoliar.mongo


import org.springframework.data.mapping.toDotPath
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.isEqualTo
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1


fun and(vararg filters: Criteria?): Criteria {
    return Criteria().andOperator(filters.filterNotNull())
}


fun or(vararg filters: Criteria?): Criteria {
    return Criteria().orOperator(*filters)
}

fun KProperty<*>.isNull(): Criteria = Criteria(this.toDotPath()).isNull()

operator fun String.div(kProperty1: KProperty1<*, *>): String = this + "." + kProperty1.toDotPath()

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
infix fun <@kotlin.internal.OnlyInputTypes T> KProperty<T>.eq(value: T) =
    Criteria(this.toDotPath()).isEqualTo(value)

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
infix fun <@kotlin.internal.OnlyInputTypes T> KProperty<T>.notIn(value: Collection<T>): Criteria =
    Criteria(this.toDotPath()).nin(value)

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
infix fun <@kotlin.internal.OnlyInputTypes T : Any> KProperty<T?>.greater(value: T): Criteria =
    Criteria(this.toDotPath()).gt(value)

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
infix fun <@kotlin.internal.OnlyInputTypes T : Any> KProperty<T?>.greaterEqual(value: T): Criteria =
    Criteria(this.toDotPath()).gte(value)

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
infix fun <@kotlin.internal.OnlyInputTypes T : Any> KProperty<T?>.lesser(value: T): Criteria =
    Criteria(this.toDotPath()).lt(value)

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
infix fun <@kotlin.internal.OnlyInputTypes T : Any> KProperty<T?>.lesserEqual(value: T): Criteria =
    Criteria(this.toDotPath()).lte(value)