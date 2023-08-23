package com.jyoliar.mongo

import kotlin.reflect.KProperty

data class SetTo<out T>(val property: KProperty<T>, val value: T?)