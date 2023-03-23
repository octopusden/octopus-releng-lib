package org.octopusden.octopus.releng.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class IssueProblemsReport @JsonCreator constructor(
        @JsonProperty(value = "issueKey", required = true) val issueKey: String,
        @JsonProperty(value = "hasProblems", required = true) val hasProblems: Boolean,
        @JsonProperty(value = "description", required = false) val description: String? = null,
        @JsonProperty(value = "recommendations", required = false) val recommendations: String? = null)
