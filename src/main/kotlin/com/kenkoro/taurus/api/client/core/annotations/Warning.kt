package com.kenkoro.taurus.api.client.core.annotations

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS, AnnotationTarget.EXPRESSION)
annotation class Warning(val message: String)