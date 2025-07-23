package org.octopusden.octopus.releng.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.octopusden.octopus.releng.JiraComponentVersionFormatter;

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

    @JsonProperty
    private final boolean isHotfixEnabled;

    @JsonIgnore
    private String lineVersion = null;

    @JsonIgnore
    private String minorVersion = null;

    @JsonIgnore
    private String releaseVersion = null;

    @JsonIgnore
    private String buildVersion = null;

    @JsonIgnore
    private String hotfixVersion = null;

    @JsonCreator
    public JiraComponentVersion(@JsonProperty("componentVersion") ComponentVersion componentVersion,
                                @JsonProperty("component") JiraComponent component,
                                JiraComponentVersionFormatter jiraComponentVersionFormatter,
                                @JsonProperty("isHotfixEnabled") Boolean isHotfixEnabled) {
        this.componentVersion = componentVersion;
        this.component = component;
        this.jiraComponentVersionFormatter = jiraComponentVersionFormatter;
        this.isHotfixEnabled = isHotfixEnabled != null ? isHotfixEnabled : false; // Default to false if not specified

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
                releaseVersion = getVersion();
            } else {
                releaseVersion = jiraComponentVersionFormatter.getReleaseVersion(getComponent(), getVersion());
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
                minorVersion = jiraComponentVersionFormatter.getMajorVersion(getComponent(), getVersion());
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
            String buildVersionFormat = jiraComponentVersionFormatter.getBuildVersionFormat(getComponent());
            if (buildVersionFormat == null) {
                buildVersion = getVersion();
            } else {
                buildVersion = jiraComponentVersionFormatter.getBuildVersion(getComponent(), getVersion());
            }
        }
        return buildVersion;
    }

    @JsonIgnore
    public String getHotfixVersion() {
        if (isHotfixEnabled && hotfixVersion == null) {
            String hotfixVersionFormat = jiraComponentVersionFormatter.getHotfixVersionFormat(getComponent());
            if (hotfixVersionFormat != null) {
                hotfixVersion = jiraComponentVersionFormatter.getHotfixVersion(getComponent(), getVersion());
            }
        }
        return hotfixVersion;
    }

    @JsonIgnore
    public String getRCVersion() {
        return getReleaseVersion() + RC_SUFFIX;
    }

    @JsonIgnore
    public boolean isHotfixEnabled() {
        return isHotfixEnabled;
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
                ", minorVersion='" + getMajorVersion() + '\'' +
                ", releaseVersion='" + getReleaseVersion() + '\'' +
                ", buildVersion='" + getBuildVersion() + '\'' +
                ", hotfixVersion='" + getHotfixVersion() + '\'' +
                ", isHotfixEnabled=" + isHotfixEnabled +
                '}';
    }
}
