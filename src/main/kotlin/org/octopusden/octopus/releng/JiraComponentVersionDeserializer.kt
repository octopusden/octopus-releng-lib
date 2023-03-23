package org.octopusden.octopus.releng

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import org.octopusden.octopus.releng.dto.ComponentInfo
import org.octopusden.octopus.releng.dto.ComponentVersion
import org.octopusden.octopus.releng.dto.JiraComponent
import org.octopusden.octopus.releng.dto.JiraComponentVersion
import org.octopusden.releng.versions.ComponentVersionFormat

class JiraComponentVersionDeserializer : JsonDeserializer<JiraComponentVersion>() {

    override fun deserialize(jsonParser: JsonParser, context: DeserializationContext): JiraComponentVersion {
        val node: JsonNode = jsonParser.codec!!.readTree(jsonParser)
        val jiraComponentVersion = getJiraComponentVersion(node)
        return jiraComponentVersion
    }

    fun getJiraComponentVersion(node: JsonNode): JiraComponentVersion {
        val componentVersion = getComponentVersion(node)
        val jiraComponent = getJiraComponent(node)

        val jiraComponentVersion = JiraComponentVersion(componentVersion, jiraComponent)
        return jiraComponentVersion
    }

    private fun getComponentVersion(node: JsonNode): ComponentVersion? {
        val searchNode = if (node.get("componentVersion") != null) {
            node.get("componentVersion")
        } else {
            node
        }

        val componentName = getStringNode(searchNode, "componentName")
        val version = getStringNode(searchNode, "version")
        val componentVersion = ComponentVersion.create(componentName, version)
        return componentVersion
    }


    private fun getStringNode(node: JsonNode, name: String): String? {
        return if (node.has(name)) {
            val componentNameNode = node.get(name)
            assert(componentNameNode.isTextual)
            componentNameNode.asText()
        } else {
            null
        }
    }

    private fun getBooleanNode(node: JsonNode, name: String): Boolean {
        return node.has(name) && node.get(name).isBoolean && node.get(name).asBoolean(false)
    }

    fun getJiraComponent(parentNode: JsonNode): JiraComponent? {
        if (!parentNode.has("component")) {
            return null
        }

        val node = parentNode.get("component")
        assert(node.isContainerNode)

        val projectKeyNode = getStringNode(node, "projectKey")
        val componentVersionFormat = getComponentVersionFormat(node)
        val componentInfo = getComponentInfo(node)
        val technical = getBooleanNode(node, "technical")

        return JiraComponent(projectKeyNode.toString(), getDisplayName(node), componentVersionFormat, componentInfo, technical)

    }

    fun getComponentVersionFormat(parentNode: JsonNode): ComponentVersionFormat? {
        if (!parentNode.has("componentVersionFormat")) {
            return null
        }
        val node = parentNode.get("componentVersionFormat")
        assert(node.isContainerNode)

        val majorVersionFormat = getStringNode(node, "majorVersionFormat")
        val releaseVersionFormat = getStringNode(node, "releaseVersionFormat")
        val buildVersionFormat = getStringNode(node, "buildVersionFormat")
        val lineVersionFormat = getStringNode(node, "lineVersionFormat")

        return ComponentVersionFormat.create(majorVersionFormat, releaseVersionFormat, buildVersionFormat, lineVersionFormat)
    }

    fun getComponentInfo(parentNode: JsonNode): ComponentInfo? {
        val componentInfoNode = if (parentNode.has("componentInfo")) {
            parentNode.get("componentInfo")
        } else {
            parentNode.get("customerInfo")
        }
        componentInfoNode ?: return null;

        val versionPrefixNode = getStringNode(componentInfoNode, "versionPrefix")
        val versionFormatNode = getStringNode(componentInfoNode, "versionFormat")

        return ComponentInfo(versionPrefixNode, versionFormatNode)
    }

    fun getDisplayName(node: JsonNode): String? {
        if (node.has("displayName")) {
            return getStringNode(node, "displayName")
        }
        if (node.has("customerInfo")) {
            val customerInfoNode = node.get("customerInfo")
            if (customerInfoNode.has("displayName")) {
                return customerInfoNode.get("displayName").asText()
            }
        }
        return ""
    }
}