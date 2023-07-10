package org.octopusden.octopus.releng

import org.octopusden.octopus.releng.dto.JiraComponentVersion

enum class VersionType(val name2Display: String, val action: String, val column: String) {
    BUILD("Build", "build", "BUILD_VERSION") {
        override fun getVersion(jiraComponentVersion: JiraComponentVersion) = jiraComponentVersion.buildVersion
    },
    RC("Release Candidate", "rc", "RCVERSION") {
        override fun getVersion(jiraComponentVersion: JiraComponentVersion) = jiraComponentVersion.rCversion
    },
    RELEASE("Release", "release", "RELEASE_VERSION") {
        override fun getVersion(jiraComponentVersion: JiraComponentVersion) = jiraComponentVersion.releaseVersion;
    };

    abstract fun getVersion(jiraComponentVersion: JiraComponentVersion): String

    override fun toString(): String {
        return name2Display;
    }
}

