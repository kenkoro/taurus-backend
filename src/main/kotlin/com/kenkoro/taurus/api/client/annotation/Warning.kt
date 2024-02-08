package com.kenkoro.taurus.api.client.annotation

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS, AnnotationTarget.EXPRESSION)
annotation class Warning(val message: String)