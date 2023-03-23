package org.octopusden.octopus.releng

import org.octopusden.octopus.releng.JiraComponentVersionProvider.getJiraComponentVersion
import org.octopusden.octopus.releng.JiraComponentVersionProvider.getJiraComponentVersionWithoutLineVersionFormat
import org.octopusden.octopus.releng.dto.JiraComponentVersion
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class JiraComponentVersionTest {

    @Test
    fun testGetLineVersionWhenFormatIsNotSpecified() {
        val jiraComponentVersion: JiraComponentVersion = getJiraComponentVersionWithoutLineVersionFormat()
        assertEquals(jiraComponentVersion.majorVersion, jiraComponentVersion.lineVersion)
    }

    @Test
    fun testGetLineVersionWhenFormatIsSpecified() {
        val jiraComponentVersion: JiraComponentVersion = getJiraComponentVersion()
        assertEquals("testcomponent-Line.2.15", jiraComponentVersion.lineVersion)
    }
}
