package org.octopusden.octopus.releng;

import org.octopusden.octopus.releng.dto.ComponentInfo;
import org.octopusden.octopus.releng.dto.JiraComponent;
import org.octopusden.octopus.releng.dto.JiraComponentVersion;
import org.octopusden.releng.versions.IVersionInfo;
import org.octopusden.releng.versions.KotlinVersionFormatter;
import org.octopusden.releng.versions.NumericVersion;
import org.octopusden.releng.versions.VersionFormatter;
import org.apache.commons.lang3.StringUtils;

public class JiraComponentVersionFormatter {
    private final VersionFormatter versionFormatter = new KotlinVersionFormatter();

    public String getReleaseVersion(JiraComponentVersion jiraComponentVersion) {
        return formatReleaseVersionFormat(jiraComponentVersion, jiraComponentVersion.getVersion());
    }

    public String getMajorVersion(JiraComponentVersion jiraComponentVersion) {
        return formatMajorVersionFormat(jiraComponentVersion, jiraComponentVersion.getVersion());
    }

    public String getBuildVersion(JiraComponentVersion jiraComponentVersion) {
        return formatBuildVersionFormat(jiraComponentVersion, jiraComponentVersion.getVersion());
    }

    public String getLineVersion(JiraComponentVersion jiraComponentVersion) {
        return formatLineVersionFormat(jiraComponentVersion, jiraComponentVersion.getVersion());
    }

    private String format(JiraComponentVersion jiraComponentVersion, String versionFormat, IVersionInfo versionInfo) {
        if (StringUtils.isBlank(versionFormat)) {
            return "";
        }
        if (isCustomComponent(jiraComponentVersion)) {
            ComponentInfo componentInfo = jiraComponentVersion.getComponent().getComponentInfo();
            return versionFormatter.formatToCustomerVersion(componentInfo.getVersionFormat(), versionFormat, componentInfo.getVersionPrefix(), versionInfo);
        } else {
            return versionFormatter.format(versionFormat, versionInfo);
        }
    }

    public boolean matchesReleaseVersionFormat(JiraComponentVersion jiraComponentVersion, String version, boolean strict) {
        String releaseVersionFormat = jiraComponentVersion.getComponent().getComponentVersionFormat().getReleaseVersionFormat();
        return matchesVersionFormat(jiraComponentVersion, version, releaseVersionFormat, strict);
    }

    public boolean matchesReleaseVersionFormat(JiraComponentVersion jiraComponentVersion, String version) {
        return matchesReleaseVersionFormat(jiraComponentVersion, version, true);
    }

    public boolean matchesMajorVersionFormat(JiraComponentVersion jiraComponentVersion, String version, boolean strict) {
        String majorVersionFormat = jiraComponentVersion.getComponent().getComponentVersionFormat().getMajorVersionFormat();
        return matchesVersionFormat(jiraComponentVersion, version, majorVersionFormat, strict);
    }

    public boolean matchesMajorVersionFormat(JiraComponentVersion jiraComponentVersion, String version) {
        return matchesMajorVersionFormat(jiraComponentVersion, version, true);
    }

    public boolean matchesBuildVersionFormat(JiraComponentVersion jiraComponentVersion, String version) {
        return matchesBuildVersionFormat(jiraComponentVersion, version, true);
    }

    public boolean matchesBuildVersionFormat(JiraComponentVersion jiraComponentVersion, String version, boolean strict) {
        String buildVersionFormat = getBuildVersionFormat(jiraComponentVersion);
        return matchesVersionFormat(jiraComponentVersion, version, buildVersionFormat, strict);
    }

    public boolean matchesLineVersionFormat(JiraComponentVersion jiraComponentVersion, String version) {
        return matchesLineVersionFormat(jiraComponentVersion, version, true);
    }

    public boolean matchesLineVersionFormat(JiraComponentVersion jiraComponentVersion, String version, boolean strict) {
        String lineVersionFormat = getLineVersionFormat(jiraComponentVersion);
        return matchesVersionFormat(jiraComponentVersion, version, lineVersionFormat, strict);
    }

    public boolean matchesAny(JiraComponentVersion jiraComponentVersion, String version) {
        return matchesAny(jiraComponentVersion, version, true);
    }


    public boolean matchesAny(JiraComponentVersion jiraComponentVersion, String version, boolean strict) {
        return matchesReleaseVersionFormat(jiraComponentVersion, version, strict) || matchesMajorVersionFormat(jiraComponentVersion, version, strict) ||
                matchesBuildVersionFormat(jiraComponentVersion, version, strict) || matchesRCVersionFormat(jiraComponentVersion, version, strict);

    }

    public boolean matchesRCVersionFormat(JiraComponentVersion jiraComponentVersion, String version) {
        return matchesRCVersionFormat(jiraComponentVersion, version, true);
    }

    public boolean matchesRCVersionFormat(JiraComponentVersion jiraComponentVersion, String version, boolean strict) {
        return version.endsWith("_RC") && matchesReleaseVersionFormat(jiraComponentVersion, version.replace("_RC", ""));
    }


    public String formatMajorVersionFormat(JiraComponentVersion jiraComponentVersion, String version) {
        String majorVersionFormat = jiraComponentVersion.getComponent().getComponentVersionFormat().getMajorVersionFormat();
        return format(jiraComponentVersion, majorVersionFormat, NumericVersion.parse(version));
    }

    public String formatReleaseVersionFormat(JiraComponentVersion jiraComponentVersion, String version) {
        String releaseVersionFormat = jiraComponentVersion.getComponent().getComponentVersionFormat().getReleaseVersionFormat();
        return format(jiraComponentVersion, releaseVersionFormat, NumericVersion.parse(version));
    }

    public String formatBuildVersionFormat(JiraComponentVersion jiraComponentVersion, String version) {
        String buildVersionFormat = getBuildVersionFormat(jiraComponentVersion);
        return format(jiraComponentVersion, buildVersionFormat, NumericVersion.parse(version));
    }

    public String formatLineVersionFormat(JiraComponentVersion jiraComponentVersion, String version) {
        String lineVersionFormat = getLineVersionFormat(jiraComponentVersion);
        return format(jiraComponentVersion, lineVersionFormat, NumericVersion.parse(version));
    }

    public String getBuildVersionFormat(JiraComponentVersion jiraComponentVersion) {
        JiraComponent component = jiraComponentVersion.getComponent();
        if (component != null) {
            if (StringUtils.isNotBlank(component.getComponentVersionFormat().getBuildVersionFormat())) {
                return component.getComponentVersionFormat().getBuildVersionFormat();
            } else {
                return component.getComponentVersionFormat().getReleaseVersionFormat();
            }
        } else {
            return null;
        }
    }

    public String getLineVersionFormat(JiraComponentVersion jiraComponentVersion) {
        JiraComponent component = jiraComponentVersion.getComponent();
        if (component != null) {
            if (StringUtils.isNotBlank(component.getComponentVersionFormat().getLineVersionFormat())) {
                return component.getComponentVersionFormat().getLineVersionFormat();
            } else {
                return component.getComponentVersionFormat().getMajorVersionFormat();
            }
        } else {
            return null;
        }
    }

    private boolean matchesVersionFormat(JiraComponentVersion jiraComponentVersion, String version, String versionFormat, boolean strict) {
        if (StringUtils.isBlank(versionFormat)) {
            return false;
        }
        if (!strict) {
            return versionFormatter.matchesNonStrictFormat(versionFormat, version);
        } else {
            JiraComponent component = jiraComponentVersion.getComponent();
            if (isCustomComponent(jiraComponentVersion)) {
                ComponentInfo componentInfo = component.getComponentInfo();

                return versionFormatter.matchesFormat(componentInfo.getVersionFormat(),
                        versionFormat, componentInfo.getVersionPrefix(), version);

            } else {
                return versionFormatter.matchesFormat(versionFormat, version);
            }
        }
    }

    public boolean isCustomComponent(JiraComponentVersion jiraComponentVersion) {
        return jiraComponentVersion.getComponent().getComponentInfo() != null &&
                StringUtils.isNotBlank(jiraComponentVersion.getComponent().getComponentInfo().getVersionPrefix());
    }

}
