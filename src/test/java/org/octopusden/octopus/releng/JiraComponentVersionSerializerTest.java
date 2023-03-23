package org.octopusden.octopus.releng;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.octopusden.octopus.releng.dto.JiraComponentVersion;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.octopusden.octopus.releng.JiraComponentVersionProvider.getJiraComponentVersion;
import static org.junit.jupiter.api.Assertions.*;

class JiraComponentVersionSerializerTest {

    private final JiraComponentVersionSerializer jiraComponentVersionSerializer = new JiraComponentVersionSerializer();

    @Test
    void testSerialize() throws Exception {
        JiraComponentVersion jiraComponentVersion = getJiraComponentVersion();
        String jiraComponentVersionJson = jiraComponentVersionSerializer.serialize(jiraComponentVersion, true);
        assertEquals(jiraComponentVersion, jiraComponentVersionSerializer.deserialize(jiraComponentVersionJson));
    }

    @Test
    void testSerializeList() throws IOException {
        List<JiraComponentVersion> jiraComponentVersionList = Collections.singletonList(getJiraComponentVersion());
        String jiraComponentVersionJson = jiraComponentVersionSerializer.serializeList(jiraComponentVersionList);
        assertEquals(jiraComponentVersionList, jiraComponentVersionSerializer.deserializeList(jiraComponentVersionJson));
    }

    @Test
    void testSerializeLegacyFormat() throws IOException {
        String legacyFormat = Utils.getJson(this.getClass().getResourceAsStream("/dependencies-legacy.json"));
        JiraComponentVersion jiraComponentVersion = jiraComponentVersionSerializer.deserialize(legacyFormat);
        assertEquals(getJiraComponentVersion(), jiraComponentVersion);
    }

    @Test
    void testSerializeNewFormat() throws IOException {
        String newFormat = Utils.getJson(this.getClass().getResourceAsStream("/dependencies-new.json"));
        JiraComponentVersion jiraComponentVersion = jiraComponentVersionSerializer.deserialize(newFormat);
        assertEquals(getJiraComponentVersion(), jiraComponentVersion);
    }

    @Test
    void testValidJson() throws JsonProcessingException {
        assertFalse(jiraComponentVersionSerializer.isValidJSON("asd:$major.$minor:$major.$minor.$service:2.0.1"));
        assertTrue(jiraComponentVersionSerializer.isValidJSON("{\"id\": \"295cd59f-4033-438c-9bf4-c571829f134e\"}"));
        String jiraComponentVersionJson = jiraComponentVersionSerializer.serialize(getJiraComponentVersion());
        assertTrue(jiraComponentVersionSerializer.isValidJSON(jiraComponentVersionJson));
    }
}