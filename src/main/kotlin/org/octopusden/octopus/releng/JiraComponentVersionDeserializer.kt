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
import org.octopusden.releng.versions.VersionNames

class JiraComponentVersionDeserializer(
    private val versionNames: VersionNames
) : JsonDeserializer<JiraComponentVersion>() {

    override fun deserialize(jsonParser: JsonParser, context: DeserializationContext): JiraComponentVersion {
        val node: JsonNode = jsonParser.codec!!.readTree(jsonParser)
        val jiraComponentVersion = getJiraComponentVersion(node)
        return jiraComponentVersion
    }

    fun getJiraComponentVersion(node: JsonNode): JiraComponentVersion {
        val componentVersion = getComponentVersion(node)
        val jiraComponent = getJiraComponent(node)
        val jiraComponentVersionFormatter = JiraComponentVersionFormatter(versionNames)
        val isHotFixEnabled = getIsHotFixEnabled(node)
        return JiraComponentVersion(componentVersion, jiraComponent, jiraComponentVersionFormatter, isHotFixEnabled)
    }

    private fun getIsHotFixEnabled(node: JsonNode): Boolean {
        return if (node.get(HOT_FIX_ENABLED) != null) {
            node.get(HOT_FIX_ENABLED).booleanValue()
        } else {
            false
        }
    }

    private fun getComponentVersion(node: JsonNode): ComponentVersion? {
        val searchNode = if (node.get(COMPONENT_VERSION) != null) {
            node.get(COMPONENT_VERSION)
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
        if (!parentNode.has(COMPONENT)) {
            return null
        }

        val node = parentNode.get(COMPONENT)
        assert(node.isContainerNode)

        val projectKeyNode = getStringNode(node, "projectKey")
        val componentVersionFormat = getComponentVersionFormat(node)
        val componentInfo = getComponentInfo(node)
        val technical = getBooleanNode(node, "technical")

        return JiraComponent(
            projectKeyNode.toString(),
            getDisplayName(node),
            componentVersionFormat,
            componentInfo,
            technical
        )

    }

    fun getComponentVersionFormat(parentNode: JsonNode): ComponentVersionFormat? {
        if (!parentNode.has(COMPONENT_VERSION_FORMAT)) {
            return null
        }
        val node = parentNode.get(COMPONENT_VERSION_FORMAT)
        assert(node.isContainerNode)

        val majorVersionFormat = getStringNode(node, "majorVersionFormat")
        val releaseVersionFormat = getStringNode(node, "releaseVersionFormat")
        val buildVersionFormat = getStringNode(node, "buildVersionFormat")
        val lineVersionFormat = getStringNode(node, "lineVersionFormat")
        val hotfixVersionFormat = getStringNode(node, "hotfixVersionFormat")

        return ComponentVersionFormat.create(
            majorVersionFormat,
            releaseVersionFormat,
            buildVersionFormat,
            lineVersionFormat,
            hotfixVersionFormat
        )
    }

    fun getComponentInfo(parentNode: JsonNode): ComponentInfo? {
        val componentInfoNode = if (parentNode.has("componentInfo")) {
            parentNode.get("componentInfo")
        } else {
            parentNode.get(CUSTOMER_INFO)
        }
        componentInfoNode ?: return null;

        val versionPrefixNode = getStringNode(componentInfoNode, "versionPrefix")
        val versionFormatNode = getStringNode(componentInfoNode, "versionFormat")

        return ComponentInfo(versionPrefixNode, versionFormatNode)
    }

    fun getDisplayName(node: JsonNode): String? {
        if (node.has(DISPLAY_NAME)) {
            return getStringNode(node, DISPLAY_NAME)
        }
        if (node.has(CUSTOMER_INFO)) {
            val customerInfoNode = node.get(CUSTOMER_INFO)
            if (customerInfoNode.has(DISPLAY_NAME)) {
                return customerInfoNode.get(DISPLAY_NAME).asText()
            }
        }
        return ""
    }

    companion object {
        private const val COMPONENT = "component"
        private const val COMPONENT_VERSION_FORMAT = "componentVersionFormat"
        private const val COMPONENT_VERSION = "componentVersion"
        private const val HOT_FIX_ENABLED = "isHotFixEnabled"
        private const val CUSTOMER_INFO = "customerInfo"
        private const val DISPLAY_NAME = "displayName"
    }
}