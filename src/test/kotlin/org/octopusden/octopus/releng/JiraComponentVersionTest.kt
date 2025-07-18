package org.octopusden.octopus.releng

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.octopusden.octopus.releng.JiraComponentVersionProvider.getJiraComponentVersion
import org.octopusden.octopus.releng.JiraComponentVersionProvider.getJiraComponentVersionWithoutLineVersionFormat
import org.octopusden.octopus.releng.dto.JiraComponentVersion
import org.octopusden.releng.versions.VersionNames

class JiraComponentVersionTest {
    private val versionNames = VersionNames(
        "serviceBranch", "service", "minor"
    )
    private val jiraComponentVersionFormatter = JiraComponentVersionFormatter(versionNames)


    @Test
    fun testGetLineVersionWhenFormatIsNotSpecified() {
        val jiraComponentVersion: JiraComponentVersion = getJiraComponentVersionWithoutLineVersionFormat(jiraComponentVersionFormatter, false)
        assertEquals(jiraComponentVersion.majorVersion, jiraComponentVersion.lineVersion)
    }

    @Test
    fun testGetLineVersionWhenFormatIsSpecified() {
        val jiraComponentVersion: JiraComponentVersion = getJiraComponentVersion(jiraComponentVersionFormatter, false)
        assertEquals("testcomponent-Line.2.15", jiraComponentVersion.lineVersion)
    }
}
