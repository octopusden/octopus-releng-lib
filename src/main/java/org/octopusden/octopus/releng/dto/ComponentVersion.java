package org.octopusden.octopus.releng.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public final class ComponentVersion implements Serializable {

    private static final int MAGIC_NUMBER = 31;
    private static final long serialVersionUID = 7531635422579671535L;
    private final String componentName;
    private final String version;

    private ComponentVersion(String componentName, String version) {
        Objects.requireNonNull(componentName, "componentName can't be null");
        Objects.requireNonNull(version, "version can't be null");
        this.componentName = componentName;
        this.version = version;
    }

    @JsonCreator
    public static ComponentVersion create(@JsonProperty("componentName") String componentName, @JsonProperty("version") String version) {
        return new ComponentVersion(componentName, version);
    }

    public String getComponentName() {
        return componentName;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return componentName + ":" + version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComponentVersion that = (ComponentVersion) o;
        if (!componentName.equals(that.componentName)) {
            return false;
        }
        return version.equals(that.version);
    }

    @Override
    public int hashCode() {
        int result = componentName.hashCode();
        result = MAGIC_NUMBER * result + version.hashCode();
        return result;
    }
}
