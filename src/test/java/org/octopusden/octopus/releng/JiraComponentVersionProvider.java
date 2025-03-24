package org.octopusden.octopus.releng;

import org.octopusden.octopus.releng.dto.ComponentInfo;
import org.octopusden.octopus.releng.dto.ComponentVersion;
import org.octopusden.octopus.releng.dto.JiraComponent;
import org.octopusden.octopus.releng.dto.JiraComponentVersion;
import org.octopusden.releng.versions.ComponentVersionFormat;

public class JiraComponentVersionProvider {

    public static JiraComponentVersion getJiraComponentVersion(JiraComponentVersionFormatter jiraComponentVersionFormatter) {
        ComponentVersionFormat componentVersionFormat = ComponentVersionFormat.create(
                "$major.$minor",
                "$major.$minor.$service",
                "$major.$minor.$service.$fix",
                "Line.$major.$minor",
                "$major.$minor.$service.$fix-$build");
        ComponentInfo componentInfo = new ComponentInfo("testcomponent", "$versionPrefix-$baseVersionFormat");
        JiraComponent jiraComponent = new JiraComponent("APP", "TestComponent Application", componentVersionFormat, componentInfo, true);
        ComponentVersion componentVersion = ComponentVersion.create("app-testcomponent", "2.15.1505.147-1128");
        return new JiraComponentVersion(componentVersion, jiraComponent, jiraComponentVersionFormatter);
    }

    public static JiraComponentVersion getJiraComponentVersionWithoutLineVersionFormat(JiraComponentVersionFormatter jiraComponentVersionFormatter) {
        ComponentVersionFormat componentVersionFormat = ComponentVersionFormat.create("$major.$minor", "$major.$minor.$service-$fix",
                "$major.$minor.$service.$fix-$build", null, null);
        ComponentInfo componentInfo = new ComponentInfo("testcomponent", "$versionPrefix-$baseVersionFormat");
        JiraComponent jiraComponent = new JiraComponent("APP", "TestComponent Application", componentVersionFormat, componentInfo, true);
        ComponentVersion componentVersion = ComponentVersion.create("app-testcomponent", "2.15.1505.147-1128");
        return new JiraComponentVersion(
                componentVersion,
                jiraComponent,
                jiraComponentVersionFormatter
        );
    }
}
