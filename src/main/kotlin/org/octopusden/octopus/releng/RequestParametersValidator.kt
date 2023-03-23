package org.octopusden.octopus.releng

class RequestParametersValidator {

    companion object {
        private const val DEPENDENCY_FORMAT = "([\\S&&[^,]]+:[\\S&&[^,]]+)"
        private const val DEPENDENCIES_FORMAT = "$DEPENDENCY_FORMAT(,$DEPENDENCY_FORMAT)*"
        private val REGEXP_DEPENDENCIES_FORMAT = Regex(DEPENDENCIES_FORMAT)
    }

    fun validate(component: String?, version: String?, dependencies: String?): ValidationResult {

        val validationResult = ValidationResult()
        if (component.isNullOrBlank()) {
            validationResult.addError("Component can't be empty")
        }
        if (version.isNullOrBlank()) {
            validationResult.addError("Version can't be empty")
        }
        if (!dependencies.isNullOrBlank() && !dependencies.matches(REGEXP_DEPENDENCIES_FORMAT)) {
            validationResult.addError("Dependencies don't match format component1:version1,component2:version2 (regexp: $DEPENDENCIES_FORMAT)")
        }
        return validationResult
    }
}