package com.matag.admin.auth.validators

interface Validator<T> {
    fun validate(field: T?)
}
