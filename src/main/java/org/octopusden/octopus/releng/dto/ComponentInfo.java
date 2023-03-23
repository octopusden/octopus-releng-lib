package org.octopusden.octopus.releng.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComponentInfo {
    private final String versionPrefix;
    private final String versionFormat;

    @JsonCreator
    public ComponentInfo(@JsonProperty("versionPrefix") String versionPrefix,
                         @JsonProperty("versionFormat") String versionFormat) {
        this.versionPrefix = versionPrefix;
        this.versionFormat = versionFormat;
    }


    public String getVersionPrefix() {
        return versionPrefix;
    }

    public String getVersionFormat() {
        return versionFormat;
    }

    @Override
    public String toString() {
        return "ComponentInfo{" +
                "versionPrefix='" + versionPrefix + '\'' +
                ", versionFormat='" + versionFormat + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ComponentInfo that = (ComponentInfo) o;

        return new EqualsBuilder()
                .append(versionPrefix, that.versionPrefix)
                .append(versionFormat, that.versionFormat)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(versionPrefix)
                .append(versionFormat)
                .toHashCode();
    }
}
