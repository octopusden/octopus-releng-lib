package org.octopusden.octopus.releng.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.octopusden.octopus.releng.JiraComponentVersionFormatter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraComponentVersion {
    @JsonIgnore
    public static final String RC_SUFFIX = "_RC";

    @JsonProperty
    private final ComponentVersion componentVersion;

    @JsonProperty
    private final JiraComponent component;
    @JsonIgnore
    private final JiraComponentVersionFormatter jiraComponentVersionFormatter;

    @JsonIgnore
    private String lineVersion = null;

    @JsonIgnore
    private String minorVersion = null;

    @JsonIgnore
    private String releaseVersion = null;

    @JsonIgnore
    private String buildVersion = null;

    @JsonCreator
    public JiraComponentVersion(@JsonProperty("componentVersion") ComponentVersion componentVersion,
                                @JsonProperty("component") JiraComponent component) {
        this.componentVersion = componentVersion;
        this.component = component;
        this.jiraComponentVersionFormatter = new JiraComponentVersionFormatter();
    }


    public JiraComponent getComponent() {
        return component;
    }

    @JsonIgnore
    public ComponentVersion getComponentVersion() {
        return componentVersion;
    }

    @JsonIgnore
    public String getReleaseVersion() {
        if (releaseVersion == null) {
            if (componentVersionFormatIsNotSpecified() || component.getComponentVersionFormat().getReleaseVersionFormat() == null) {
                releaseVersion = componentVersion.getVersion();
            } else {
                releaseVersion = jiraComponentVersionFormatter.getReleaseVersion(this);
            }
        }
        return releaseVersion;
    }

    @JsonIgnore
    public ComponentVersion getReleaseComponentVersion() {
        return getComponentVersion(getReleaseVersion());
    }

    @JsonIgnore
    public ComponentVersion getBuildComponentVersion() {
        return getComponentVersion(getBuildVersion());
    }

    @JsonIgnore
    public ComponentVersion getRCComponentVersion() {
        return getComponentVersion(getRCVersion());
    }

    @JsonIgnore
    private ComponentVersion getComponentVersion(String version) {
        return ComponentVersion.create(componentVersion.getComponentName(), version);
    }

    private boolean componentVersionFormatIsNotSpecified() {
        return component == null || component.getComponentVersionFormat() == null;
    }

    @JsonIgnore
    public String getMajorVersion() {
        if (minorVersion == null) {
            if (componentVersionFormatIsNotSpecified() || component.getComponentVersionFormat().getMajorVersionFormat() == null) {
                minorVersion = getVersion();
            } else {
                minorVersion = jiraComponentVersionFormatter.getMajorVersion(this);
            }
        }
        return minorVersion;
    }

    @JsonIgnore
    public String getLineVersion() {
        if (lineVersion == null) {
            if (componentVersionFormatIsNotSpecified() || component.getComponentVersionFormat().getLineVersionFormat() == null) {
                lineVersion = getMajorVersion();
            } else {
                lineVersion = jiraComponentVersionFormatter.getLineVersion(this);
            }
        }
        return lineVersion;
    }

    @JsonIgnore
    public String getVersion() {
        return componentVersion.getVersion();
    }

    @JsonIgnore
    public String getBuildVersion() {
        if (buildVersion == null) {
            String buildVersionFormat = jiraComponentVersionFormatter.getBuildVersionFormat(this);
            if (buildVersionFormat == null) {
                buildVersion = getVersion();
            } else {
                buildVersion = jiraComponentVersionFormatter.getBuildVersion(this);
            }
        }
        return buildVersion;
    }


    @JsonIgnore
    public String getRCVersion() {
        return getReleaseVersion() + RC_SUFFIX;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        JiraComponentVersion that = (JiraComponentVersion) o;

        return new EqualsBuilder()
                .append(componentVersion, that.componentVersion)
                .append(component, that.component)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(componentVersion)
                .append(component)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "JiraComponentVersion{" +
                "componentVersion=" + componentVersion +
                ", minorVersion='" + minorVersion + '\'' +
                ", releaseVersion='" + releaseVersion + '\'' +
                ", buildVersion='" + buildVersion + '\'' +
                '}';
    }
}
