package com.jyoliar.mongo


import com.mongodb.client.model.PushOptions
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.mapping.toDotPath
import org.springframework.data.mongodb.core.query.Update
import kotlin.reflect.KProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
infix fun <@kotlin.internal.OnlyInputTypes T> KProperty<T>.setTo(value: T): SetTo<T> = SetTo(this, value)

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
fun <@kotlin.internal.OnlyInputTypes T> setValue(property: KProperty<T?>, value: T?): Update =
    Update().set(property.toDotPath(), value)

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
fun <@kotlin.internal.OnlyInputTypes T> Update.set(property: KProperty<T?>, value: T?): Update =
    this.set(property.toDotPath(), value)


fun set(vararg properties: SetTo<*>): Update {
    return properties.fold(Update()) { update: Update, setTo: SetTo<*> ->
        update.set(setTo.property.toDotPath(), setTo.value)
    }
}

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
fun <@kotlin.internal.OnlyInputTypes T> unset(property: KProperty<T>): Update = Update().unset(property.toDotPath())

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
fun <@kotlin.internal.OnlyInputTypes T> setOnInsert(property: KProperty<T?>, value: T): Update =
    Update().setOnInsert(property.toDotPath(), value)

fun Update.setValues(any: Any, vararg excludes: KProperty<*>): Update {
    val excludeProperties = excludes.map { it.toDotPath() }

    any::class.declaredMemberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .filterNot { excludeProperties.contains(it.toDotPath()) }
        .forEach { this.set(it.toDotPath(), it.getter.call(any)) }

    return this
}

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
fun <@kotlin.internal.OnlyInputTypes T> Update.setValueOnInsert(property: KProperty<T?>, value: T?): Update =
    this.setOnInsert(property.toDotPath(), value)

fun Update.setOnInsert(any: Any, vararg excludes: KProperty<*>): Update {
    val excludeProperties = excludes.map { it.toDotPath() }

    any::class.declaredMemberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .filterNot { excludeProperties.contains(it.toDotPath()) }
        .forEach { this.setOnInsert(it.toDotPath(), it.getter.call(any)) }

    return this
}

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
fun <@kotlin.internal.OnlyInputTypes T> rename(property: KProperty<T?>, newProperty: KProperty<T>): Update =
    Update().rename(property.toDotPath(), newProperty.toDotPath())

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
fun <@kotlin.internal.OnlyInputTypes T : Number?> Update.inc(property: KProperty<T>, number: Number): Update =
    this.inc(property.toDotPath(), number)

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
fun <@kotlin.internal.OnlyInputTypes T : Number?> multiply(property: KProperty<T>, number: Number): Update =
    Update().multiply(property.toDotPath(), number)

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
fun <@kotlin.internal.OnlyInputTypes T : Any> min(property: KProperty<T>, value: T): Update =
    Update().min(property.toDotPath(), value)

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
fun <@kotlin.internal.OnlyInputTypes T : Any> max(property: KProperty<T>, value: T): Update =
    Update().max(property.toDotPath(), value)

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
fun <@kotlin.internal.OnlyInputTypes T> currentDate(property: KProperty<T>): Update =
    Update().currentDate(property.toDotPath())

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
fun <@kotlin.internal.OnlyInputTypes T> currentTimestamp(property: KProperty<T>): Update =
    Update().currentTimestamp(property.toDotPath())

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
fun <@kotlin.internal.OnlyInputTypes T> addToSet(property: KProperty<Iterable<T>?>, value: T): Update =
    Update().addToSet(property.toDotPath(), value)

inline fun <reified T> Update.addEachToSet(property: KProperty<Iterable<T>?>, values: List<T>): Update =
    this.addToSet(property.toDotPath()).each(*(values.toTypedArray()))

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
fun <@kotlin.internal.OnlyInputTypes T> Update.push(property: KProperty<Iterable<T>?>, value: T): Update =
    this.push(property.toDotPath(), value)


inline fun <reified T> pushEach(
    property: KProperty<Iterable<T>?>,
    values: List<T?>,
    options: PushOptions = PushOptions(),
    slice: Int? = null,
    position: Int? = null,
    direction: Direction? = null,
    sort: Sort? = null,
): Update {
    return Update().push(property.toDotPath())
        .also { slice?.let { slice -> it.slice(slice) } }
        .also { position?.let { position -> it.atPosition(position) } }
        .also { direction?.let { direction -> it.sort(direction) } }
        .also { sort?.let { slice -> it.sort(slice) } }
        .each(*(values.toTypedArray()))
}

//TODO: 이건 잘 모르겠네... 추후 기능 추가
//fun <@kotlin.internal.OnlyInputTypes T> pull(property: KProperty<Iterable<T?>?>, value: T?): Update = Update().pull(property.toDotPath(), value)
//
//fun pullByFilter(property: KProperty<*>, filter: Criteria): Update =
//    Update().pull(property.toDotPath(), Query.query(filter))
//
//fun pullByFilter(filter: Update): Update = Update().pullByFilter(filter)


//fun <@kotlin.internal.OnlyInputTypes T> pullAll(property: KProperty<Iterable<T>?>, values: List<T?>?): Update =
//    Update().pullAll(property.toDotPath(), values ?: emptyList())


@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
fun <@kotlin.internal.OnlyInputTypes T> popFirst(property: KProperty<T>): Update =
    Update().pop(property.toDotPath(), Update.Position.FIRST)

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
fun <@kotlin.internal.OnlyInputTypes T> popLast(property: KProperty<T>): Update =
    Update().pop(property.toDotPath(), Update.Position.LAST)

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
fun <@kotlin.internal.OnlyInputTypes T : Number?> bitwiseAnd(property: KProperty<T>, value: Long): Update =
    Update().bitwise(property.toDotPath()).and(value)

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
fun <@kotlin.internal.OnlyInputTypes T : Number?> bitwiseOr(property: KProperty<T>, value: Long): Update =
    Update().bitwise(property.toDotPath()).or(value)

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
fun <@kotlin.internal.OnlyInputTypes T : Number?> bitwiseXor(property: KProperty<T>, value: Long): Update =
    Update().bitwise(property.toDotPath()).xor(value)

///**
// * Creates an InsertOneModel.
// */
//fun <@kotlin.internal.OnlyInputTypes T> insertOne(document: T): InsertOneModel<T> = InsertOneModel(document)
//
///**
// * Creates an UpdateOneModel.
// */
//fun <@kotlin.internal.OnlyInputTypes T> updateOne(filter: Update, update: Update, options: UpdateOptions = UpdateOptions()): UpdateOneModel<T> =
//    UpdateOneModel(filter, update, options)
//
///**
// * Creates an UpdateManyModel.
// */
//fun <@kotlin.internal.OnlyInputTypes T> updateMany(filter: Update, update: Update, options: UpdateOptions = UpdateOptions()): UpdateManyModel<T> =
//    UpdateManyModel(filter, update, options)
//
///**
// * Creates an ReplaceOneModel.
// */
//fun <@kotlin.internal.OnlyInputTypes T> replaceOne(filter: Update, replacement: T, options: ReplaceOptions = ReplaceOptions()): ReplaceOneModel<T> =
//    ReplaceOneModel(filter, replacement, options)
//
///**
// * Creates an DeleteOneModel.
// */
//fun <@kotlin.internal.OnlyInputTypes T> deleteOne(filter: Update, options: DeleteOptions = DeleteOptions()): DeleteOneModel<T> =
//    DeleteOneModel(filter, options)
//
///**
// * Creates an DeleteManyModel.
// */
//fun <@kotlin.internal.OnlyInputTypes T> deleteMany(filter: Update, options: DeleteOptions = DeleteOptions()): DeleteManyModel<T> =
//    DeleteManyModel(filter, options)
//
///**
// * Creates an [UpdateOptions] and set upsert to true.
// */
//fun upsert(): UpdateOptions = UpdateOptions().upsert(true)
//
///**
// * Creates an [ReplaceOptions] and set upsert to true.
// */
//fun replaceUpsert(): ReplaceOptions = ReplaceOptions().upsert(true)
//
///**
// * Creates an [FindOneAndUpdateOptions] and set upsert to true.
// */
//fun findOneAndUpdateUpsert(): FindOneAndUpdateOptions = FindOneAndUpdateOptions().upsert(true)

