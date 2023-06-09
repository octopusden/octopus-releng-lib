package org.octopusden.octopus.releng.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.octopusden.releng.versions.ComponentVersionFormat;
import org.apache.commons.lang3.builder.EqualsBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JiraComponent {
    private final String projectKey;
    private final String displayName;
    private final ComponentVersionFormat componentVersionFormat;
    private final ComponentInfo componentInfo;
    private final boolean technical;

    @JsonCreator
    public JiraComponent(@JsonProperty("projectKey") String projectKey, @JsonProperty("displayName") String displayName, @JsonProperty("componentVersionFormat") ComponentVersionFormat componentVersionFormat,
                         @JsonProperty("componentInfo") ComponentInfo componentInfo, @JsonProperty("technical") boolean technical) {
        this.projectKey = projectKey;
        this.displayName = displayName;
        this.componentVersionFormat = componentVersionFormat;
        this.componentInfo = componentInfo;
        this.technical = technical;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ComponentVersionFormat getComponentVersionFormat() {
        return componentVersionFormat;
    }

    public ComponentInfo getComponentInfo() {
        return componentInfo;
    }

    public boolean isTechnical() {
        return technical;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        JiraComponent that = (JiraComponent) o;

        return new EqualsBuilder()
                .append(projectKey, that.projectKey)
                .append(componentVersionFormat, that.componentVersionFormat)
                .append(componentInfo, that.componentInfo)
                .append(technical, that.technical)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
                .append(projectKey)
                .append(displayName)
                .append(componentVersionFormat)
                .append(componentInfo)
                .append(technical)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "JiraComponent{" +
                "projectKey='" + projectKey + '\'' +
                ", displayName='" + displayName + '\'' +
                ", componentVersionFormat=" + componentVersionFormat +
                ", componentInfo=" + componentInfo +
                ", technical=" + technical +
                '}';
    }
}
