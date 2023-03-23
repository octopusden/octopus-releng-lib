package org.octopusden.octopus.releng;

import org.octopusden.octopus.releng.dto.ComponentVersion;
import org.octopusden.octopus.releng.dto.JiraComponent;
import org.octopusden.octopus.releng.dto.JiraComponentVersion;
import org.octopusden.releng.versions.ComponentVersionFormat;
import org.junit.jupiter.api.Test;

import static org.octopusden.octopus.releng.JiraComponentVersionProvider.getJiraComponentVersion;
import static org.junit.jupiter.api.Assertions.*;

class JiraComponentVersionFormatterTest {
    private final JiraComponentVersion jiraComponentVersion = getJiraComponentVersion();
    private final JiraComponentVersionFormatter jiraComponentVersionFormatter = new JiraComponentVersionFormatter();

    @Test
    void testGetJiraComponentVersion() {
        assertEquals("testcomponent-2.15.1505.147-1128", jiraComponentVersion.getBuildVersion());
    }

    @Test
    void testGetLineVersion() {
        assertEquals("testcomponent-Line.2.15", jiraComponentVersionFormatter.getLineVersion(jiraComponentVersion));
    }

    @Test
    void testNotMatchesWithNullBuildVersionFormat() {
        JiraComponentVersion jiraComponentVersion = getJiraComponentVersionWithNullBuildVersion();
        assertFalse(jiraComponentVersionFormatter.matchesBuildVersionFormat(jiraComponentVersion, "1.2.3"));
    }

    @Test
    void testMatchesMajorVersionFormat() {
        assertTrue(jiraComponentVersionFormatter.matchesMajorVersionFormat(jiraComponentVersion, "testcomponent-2.15"));
    }

    @Test
    void testDoesNotMatcherMajorVersionFormat() {
        assertFalse(jiraComponentVersionFormatter.matchesMajorVersionFormat(jiraComponentVersion, "testcomponent-2.15.2"));

    }

    @Test
    void testMatchesReleaseVersionFormat() {
        assertTrue(jiraComponentVersionFormatter.matchesReleaseVersionFormat(jiraComponentVersion, "testcomponent-2.15.1505-147"));
    }

    @Test
    void testDoesNotMatcherReleaseVersionFormat() {
        assertFalse(jiraComponentVersionFormatter.matchesReleaseVersionFormat(jiraComponentVersion, "testcomponent-2.15.1505.147"));
    }

    @Test
    void testMatchesBuildVersionFormat() {
        assertTrue(jiraComponentVersionFormatter.matchesBuildVersionFormat(jiraComponentVersion, "testcomponent-2.15.1505.147-1128"));
    }

    @Test
    void testDoesNotMatcherBuildVersionFormat() {
        assertFalse(jiraComponentVersionFormatter.matchesBuildVersionFormat(jiraComponentVersion, "testcomponent-2.15.1505.147.1128"));
    }

    @Test
    void testMajorVersionMatchesAnyFormat() {
        assertTrue(jiraComponentVersionFormatter.matchesAny(jiraComponentVersion, "testcomponent-2.15"));
    }

    @Test
    void testReleaseVersionMatchesAnyFormat() {
        assertTrue(jiraComponentVersionFormatter.matchesAny(jiraComponentVersion, "testcomponent-2.15.1505-147"));
    }

    @Test
    void testRCVersionMatchesAnyFormat() {
        assertTrue(jiraComponentVersionFormatter.matchesAny(jiraComponentVersion, "testcomponent-2.15.1505-147_RC"));
    }

    @Test
    void testRCVersionMatchesRC() {
        assertTrue(jiraComponentVersionFormatter.matchesRCVersionFormat(jiraComponentVersion, "testcomponent-2.15.1505-147_RC"));
    }

    @Test
    void testBuildNumberMatchesAnyFormat() {
        assertTrue(jiraComponentVersionFormatter.matchesAny(jiraComponentVersion, "testcomponent-2.15.1505.147-1128"));
    }

    @Test
    void testIncorrectVersionDoesNotMatchesAnyFormat() {
        assertFalse(jiraComponentVersionFormatter.matchesAny(jiraComponentVersion, "testcomponent-2.15.1505.147.1128"));
    }

    @Test
    void testNonStrictAnyMatcher() {
        assertTrue(jiraComponentVersionFormatter.matchesAny(jiraComponentVersion, "2.15.1505.147-1128", false));
        assertTrue(jiraComponentVersionFormatter.matchesAny(jiraComponentVersion, "2.15.1505", false));
        assertFalse(jiraComponentVersionFormatter.matchesAny(jiraComponentVersion, "2", false));
    }

    private JiraComponentVersion getJiraComponentVersionWithNullBuildVersion() {
        ComponentVersionFormat componentVersionFormat = ComponentVersionFormat.create("$major.$minor", "$major.$minor.$service-$fix",
                null, null);
        JiraComponent jiraComponent = new JiraComponent("C1", "C1", componentVersionFormat, null, true);
        ComponentVersion componentVersion = ComponentVersion.create("c1", "version");
        return new JiraComponentVersion(componentVersion, jiraComponent);
    }
}
