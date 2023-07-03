package org.octopusden.octopus.releng;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.octopusden.octopus.releng.dto.ComponentVersion;
import org.octopusden.octopus.releng.dto.JiraComponent;
import org.octopusden.octopus.releng.dto.JiraComponentVersion;
import org.octopusden.octopus.releng.utils.VersionNamesHelper;
import org.octopusden.releng.versions.ComponentVersionFormat;
import org.octopusden.releng.versions.KotlinVersionFormatter;
import org.octopusden.releng.versions.VersionFormatter;
import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class JiraComponentVersionSerializer {

    public String serialize(JiraComponentVersion jiraComponentVersion) throws JsonProcessingException {
        return serialize(jiraComponentVersion, false);
    }

    public String serialize(JiraComponentVersion jiraComponentVersion, boolean userPrettyOutput) throws JsonProcessingException {
        if (jiraComponentVersion == null) {
            return "";
        }
        ObjectMapper objectMapper = getObjectMapper(userPrettyOutput);
        return objectMapper.writeValueAsString(jiraComponentVersion);
    }

    private ObjectMapper getObjectMapper(boolean usePrettyOutput) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (usePrettyOutput) {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        }

        SimpleModule module = new SimpleModule();
        module.addDeserializer(JiraComponentVersion.class, new JiraComponentVersionDeserializer());
        objectMapper.registerModule(module);

        return objectMapper;
    }

    public String serializeList(List<JiraComponentVersion> jiraComponentVersionList) throws JsonProcessingException {
        if (jiraComponentVersionList == null) {
            return "";
        }
        ObjectMapper objectMapper = getObjectMapper(false);
        return objectMapper.writeValueAsString(jiraComponentVersionList);
    }

    public JiraComponentVersion deserialize(String jiraComponentVersionJson) throws IOException {
        Validate.notNull(jiraComponentVersionJson);
        jiraComponentVersionJson = jiraComponentVersionJson.replace("\n", "").replace("\r", "");
        if (isValidJSON(jiraComponentVersionJson)) {
            ObjectMapper objectMapper = getObjectMapper(false);
            return objectMapper.readValue(jiraComponentVersionJson, JiraComponentVersion.class);
        } else {
            return deserializeFromRequestString(jiraComponentVersionJson);
        }
    }

    public List<JiraComponentVersion> deserializeList(String jiraComponentVersionList) throws IOException {
        Validate.notNull(jiraComponentVersionList);
        jiraComponentVersionList = jiraComponentVersionList.replace("\n", "").replace("\r", "");
        if (isValidJSON(jiraComponentVersionList)) {
            ObjectMapper objectMapper = getObjectMapper(false);
            final CollectionType jiraComponentVersionCollectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, JiraComponentVersion.class);
            return objectMapper.readValue(jiraComponentVersionList, jiraComponentVersionCollectionType);
        } else {
            return deserializeListFromRequestString(jiraComponentVersionList);
        }
    }

    private List<JiraComponentVersion> deserializeListFromRequestString(String jiraComponentVersionList) {
        String[] subComponentsArray = jiraComponentVersionList.split(",");
        List<JiraComponentVersion> jiraComponentVersions = new ArrayList<>();
        for (String subComponent : subComponentsArray) {
            JiraComponentVersion subJiraComponent = deserializeFromRequestString(subComponent);
            jiraComponentVersions.add(subJiraComponent);

        }
        return jiraComponentVersions;
    }

    private JiraComponentVersion deserializeFromRequestString(String component) {
        String[] componentItems = component.split(":");

        if (componentItems.length != 4 && componentItems.length != 5) {
            throw new IllegalArgumentException(MessageFormat.format("Unable to parse component {0}. Should be in format " +
                    "componentName:projectKey:majorVersionFormat:releaseVersionFormat:releaseVersion", component));
        }


        int offset = componentItems.length % 4;
        String projectKey = componentItems[offset];
        String majorVersionFormat = componentItems[offset + 1];
        String releaseVersionFormat = componentItems[offset + 2];
        String releaseVersion = componentItems[offset + 3];

        ComponentVersionFormat versionFormat = ComponentVersionFormat.create(majorVersionFormat, releaseVersionFormat);

        VersionFormatter versionFormatter = new KotlinVersionFormatter(VersionNamesHelper.INSTANCE);
        if (!versionFormatter.matchesFormat(versionFormat.getReleaseVersionFormat(), releaseVersion)) {
            throw new IllegalArgumentException(String.format("In module %s unable to parse  version %s. Should be in format %s", projectKey, releaseVersion,
                    releaseVersionFormat));
        }


        String componentName = projectKey;
        if (offset == 1) {
            componentName = componentItems[0];
        }

        JiraComponent jiraComponent = new JiraComponent(projectKey, "", versionFormat, null, false);
        return new JiraComponentVersion(ComponentVersion.create(componentName, releaseVersion), jiraComponent);
    }

    boolean isValidJSON(final String json) {
        ObjectMapper objectMapper = getObjectMapper(false);
        try {
            objectMapper.readTree(json);
            return true;
        } catch (IOException ignore) {
        }
        return false;
    }

}
