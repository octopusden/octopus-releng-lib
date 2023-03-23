package org.octopusden.octopus.releng

class ValidationResult(val errors: MutableList<String> = mutableListOf()) {
    val valid: Boolean
        get() = errors.isEmpty()

    fun addError(error: String) {
        errors.add(error)
    }

    fun addErrors(error: List<String>) {
        errors.addAll(error)
    }
}