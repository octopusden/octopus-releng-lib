package org.octopusden.octopus.releng.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.octopusden.octopus.releng.JiraComponentVersionFormatter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraComponentVersion {

    public static final String RC_SUFFIX = "_RC";

    @JsonProperty
    public final ComponentVersion componentVersion;

    @JsonProperty
    public final JiraComponent component;

    @JsonIgnore
    public final String lineVersion;

    @JsonIgnore
    public final String minorVersion;

    @JsonIgnore
    public final String releaseVersion;

    @JsonIgnore
    public final String buildVersion;

    @JsonIgnore
    public final ComponentVersion releaseComponentVersion;

    @JsonIgnore
    public final ComponentVersion buildComponentVersion;

    @JsonIgnore
    public final ComponentVersion rCComponentVersion;

    @JsonIgnore
    public final String version;

    @JsonIgnore
    public final String rCversion;

    @JsonIgnore
    public final String majorVersion;

    public static Builder builder(JiraComponentVersionFormatter jiraComponentVersionFormatter) {
        return new Builder(jiraComponentVersionFormatter);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private final JiraComponentVersionFormatter jiraComponentVersionFormatter;
        private ComponentVersion componentVersion;
        private JiraComponent component;

        private String version;

        private String majorVersion;

        private String releaseVersion;

        private String rCversion;

        private ComponentVersion releaseComponentVersion;

        private String buildVersion;
        private ComponentVersion buildComponentVersion;
        private ComponentVersion rCComponentVersion;
        private String minorVersion;
        private String lineVersion;

        public Builder(JiraComponentVersionFormatter jiraComponentVersionFormatter) {
            this.jiraComponentVersionFormatter = jiraComponentVersionFormatter;
        }

        public Builder componentVersion(ComponentVersion componentVersion) {
            this.componentVersion = componentVersion;
            return this;
        }

        public Builder componentVersionByNameAndVersion(String componentName, String version) {
            this.componentVersion = ComponentVersion.create(componentName, version);
            return this;
        }

        public Builder component(JiraComponent component) {
            this.component = component;
            return this;
        }


        private String getReleaseVersion() {
            return (componentVersionFormatIsNotSpecified() || component.getComponentVersionFormat().getReleaseVersionFormat() == null) ?
                componentVersion.getVersion() : jiraComponentVersionFormatter.getReleaseVersion(component, version);
        }

        private ComponentVersion getComponentVersion(String version) {
            return ComponentVersion.create(componentVersion.getComponentName(), version);
        }

        private String getBuildVersion() {
            String buildVersionFormat = jiraComponentVersionFormatter.getBuildVersionFormat(component);
            if (buildVersionFormat == null) {
                return version;
            }
            return jiraComponentVersionFormatter.getBuildVersion(component, version);
        }

        private boolean componentVersionFormatIsNotSpecified() {
            return component == null || component.getComponentVersionFormat() == null;
        }

        private String getMinorVersion() {
            if (componentVersionFormatIsNotSpecified() || component.getComponentVersionFormat().getMajorVersionFormat() == null) {
                return version;
            }
            return jiraComponentVersionFormatter.getMajorVersion(component, version);
        }

        private String getLineVersion() {
            if (componentVersionFormatIsNotSpecified() || component.getComponentVersionFormat().getLineVersionFormat() == null) {
                return majorVersion;
            }
            return jiraComponentVersionFormatter.getLineVersion(component, version);
        }

        public JiraComponentVersion build() {
            version = componentVersion.getVersion();
            releaseVersion = getReleaseVersion();
            rCversion = releaseVersion + RC_SUFFIX;
            releaseComponentVersion = getComponentVersion(releaseVersion);
            buildVersion = getBuildVersion();
            buildComponentVersion = getComponentVersion(buildVersion);
            rCComponentVersion = getComponentVersion(rCversion);
            minorVersion = getMinorVersion();
            majorVersion = minorVersion;
            lineVersion = getLineVersion();
            return new JiraComponentVersion(this);
        }
    }

    private JiraComponentVersion(Builder builder) {
        this.componentVersion = builder.componentVersion;
        this.component = builder.component;
        this.version = builder.version;
        this.releaseVersion = builder.releaseVersion;
        this.rCversion = builder.rCversion;
        this.releaseComponentVersion = builder.releaseComponentVersion;
        this.buildVersion = builder.buildVersion;
        this.buildComponentVersion = builder.buildComponentVersion;
        this.rCComponentVersion = builder.rCComponentVersion;
        this.minorVersion = builder.minorVersion;
        this.majorVersion = builder.majorVersion;
        this.lineVersion = builder.lineVersion;
    }

    @JsonIgnore
    public ComponentVersion getComponentVersion() {
        return componentVersion;
    }

    @JsonIgnore
    public String getReleaseVersion() {
        return releaseVersion;
    }

    @JsonIgnore
    public ComponentVersion getReleaseComponentVersion() {
        return releaseComponentVersion;
    }

    public ComponentVersion getBuildComponentVersion() {
        return buildComponentVersion;
    }

    public ComponentVersion getRCComponentVersion() {
        return rCComponentVersion;
    }

    @JsonIgnore
    private ComponentVersion getComponentVersion(String version) {
        return ComponentVersion.create(componentVersion.getComponentName(), version);
    }

    private boolean componentVersionFormatIsNotSpecified() {
        return component == null || component.getComponentVersionFormat() == null;
    }

    public String getMajorVersion() {
        return majorVersion;
    }

    public String getLineVersion() {
        return lineVersion;
    }

    public String getVersion() {
        return version;
    }

    public String getBuildVersion() {
        return buildVersion;
    }

    public String getRCVersion() {
        return rCversion;
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
