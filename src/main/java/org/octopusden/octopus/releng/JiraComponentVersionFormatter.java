package org.octopusden.octopus.releng;

import org.apache.commons.lang3.StringUtils;
import org.octopusden.octopus.releng.dto.ComponentInfo;
import org.octopusden.octopus.releng.dto.JiraComponent;
import org.octopusden.octopus.releng.dto.JiraComponentVersion;
import org.octopusden.releng.versions.IVersionInfo;
import org.octopusden.releng.versions.KotlinVersionFormatter;
import org.octopusden.releng.versions.NumericVersionFactory;
import org.octopusden.releng.versions.VersionFormatter;
import org.octopusden.releng.versions.VersionNames;

public class JiraComponentVersionFormatter {
    private final VersionFormatter versionFormatter;
    private final NumericVersionFactory numericVersionFactory;

    public JiraComponentVersionFormatter(VersionNames versionNames) {
        versionFormatter = new KotlinVersionFormatter(versionNames);
        numericVersionFactory = new NumericVersionFactory(versionNames);
    }

    public String getReleaseVersion(JiraComponent jiraComponent, String version) {
        return formatReleaseVersionFormat(jiraComponent, version);
    }

    public String getMajorVersion(JiraComponent jiraComponent, String version) {
        return formatMajorVersionFormat(jiraComponent, version);
    }

    public String getBuildVersion(JiraComponent jiraComponent, String version) {
        return formatBuildVersionFormat(jiraComponent, version);
    }

    public String getHotfixVersion(JiraComponent jiraComponent, String version) {
        return formatHotfixVersionFormat(jiraComponent, version);
    }

    public String getLineVersion(JiraComponentVersion jiraComponentVersion) {
        return getLineVersion(jiraComponentVersion.getComponent(), jiraComponentVersion.getVersion());
    }

    public String getLineVersion(JiraComponent jiraComponent, String version) {
        return formatLineVersionFormat(jiraComponent, version);
    }

    private String format(JiraComponent jiraComponent, String versionFormat, IVersionInfo versionInfo) {
        if (StringUtils.isBlank(versionFormat)) {
            return "";
        }
        if (isCustomComponent(jiraComponent)) {
            ComponentInfo componentInfo = jiraComponent.getComponentInfo();
            return versionFormatter.formatToCustomerVersion(componentInfo.getVersionFormat(), versionFormat, componentInfo.getVersionPrefix(), versionInfo);
        } else {
            return versionFormatter.format(versionFormat, versionInfo);
        }
    }

    public boolean matchesReleaseVersionFormat(JiraComponent jiraComponent, String version, boolean strict) {
        String releaseVersionFormat = jiraComponent.getComponentVersionFormat().getReleaseVersionFormat();
        return matchesVersionFormat(jiraComponent, version, releaseVersionFormat, strict);
    }

    public boolean matchesHotfixVersionFormat(JiraComponent jiraComponent, String version, boolean strict) {
        String hotfixVersionFormat = jiraComponent.getComponentVersionFormat().getHotfixVersionFormat();
        return matchesVersionFormat(jiraComponent, version, hotfixVersionFormat, strict);
    }

    public boolean matchesHotfixVersionFormat(JiraComponentVersion jiraComponentVersion, String version) {
        return matchesHotfixVersionFormat(jiraComponentVersion.getComponent(), version, true);
    }

    public boolean matchesReleaseVersionFormat(JiraComponentVersion jiraComponentVersion, String version) {
        return matchesReleaseVersionFormat(jiraComponentVersion.getComponent(), version, true);
    }

    public boolean matchesReleaseVersionFormat(JiraComponent jiraComponent, String version) {
        return matchesReleaseVersionFormat(jiraComponent, version, true);
    }

    public boolean matchesMajorVersionFormat(JiraComponent jiraComponent, String version, boolean strict) {
        String majorVersionFormat = jiraComponent.getComponentVersionFormat().getMajorVersionFormat();
        return matchesVersionFormat(jiraComponent, version, majorVersionFormat, strict);
    }

    public boolean matchesMajorVersionFormat(JiraComponentVersion jiraComponentVersion, String version) {
        return matchesMajorVersionFormat(jiraComponentVersion.getComponent(), version, true);
    }

    public boolean matchesBuildVersionFormat(JiraComponentVersion jiraComponentVersion, String version) {
        return matchesBuildVersionFormat(jiraComponentVersion.getComponent(), version, true);
    }

    public boolean matchesBuildVersionFormat(JiraComponent jiraComponent, String version, boolean strict) {
        String buildVersionFormat = getBuildVersionFormat(jiraComponent);
        return matchesVersionFormat(jiraComponent, version, buildVersionFormat, strict);
    }

    public boolean matchesLineVersionFormat(JiraComponent jiraComponent, String version) {
        return matchesLineVersionFormat(jiraComponent, version, true);
    }

    public boolean matchesLineVersionFormat(JiraComponent jiraComponent, String version, boolean strict) {
        String lineVersionFormat = getLineVersionFormat(jiraComponent);
        return matchesVersionFormat(jiraComponent, version, lineVersionFormat, strict);
    }

    public boolean matchesAny(JiraComponentVersion jiraComponentVersion, String version) {
        return matchesAny(jiraComponentVersion.getComponent(), version, true);
    }


    public boolean matchesAny(JiraComponentVersion jiraComponentVersion, String version, boolean strict) {
        return matchesAny(jiraComponentVersion.getComponent(), version, strict);
    }

    public boolean matchesAny(JiraComponent jiraComponent, String version, boolean strict) {
        return matchesReleaseVersionFormat(jiraComponent, version, strict) || matchesMajorVersionFormat(jiraComponent, version, strict) ||
                matchesBuildVersionFormat(jiraComponent, version, strict) || matchesRCVersionFormat(jiraComponent, version, strict) ||
                matchesHotfixVersionFormat(jiraComponent, version, strict);

    }

    public boolean matchesRCVersionFormat(JiraComponentVersion jiraComponentVersion, String version) {
        return matchesRCVersionFormat(jiraComponentVersion.getComponent(), version, true);
    }

    public boolean matchesRCVersionFormat(JiraComponent jiraComponent, String version, boolean strict) {
        return version.endsWith("_RC") && matchesReleaseVersionFormat(jiraComponent, version.replace("_RC", ""));
    }


    public String formatMajorVersionFormat(JiraComponent jiraComponent, String version) {
        String majorVersionFormat = jiraComponent.getComponentVersionFormat().getMajorVersionFormat();
        return format(jiraComponent, majorVersionFormat, numericVersionFactory.create(version));
    }

    public String formatReleaseVersionFormat(JiraComponent jiraComponent, String version) {
        String releaseVersionFormat = jiraComponent.getComponentVersionFormat().getReleaseVersionFormat();
        return format(jiraComponent, releaseVersionFormat, numericVersionFactory.create(version));
    }

    public String formatBuildVersionFormat(JiraComponent jiraComponent, String version) {
        String buildVersionFormat = getBuildVersionFormat(jiraComponent);
        return format(jiraComponent, buildVersionFormat, numericVersionFactory.create(version));
    }

    public String formatHotfixVersionFormat(JiraComponent jiraComponent, String version) {
        String hotfixVersionFormat = getHotfixVersionFormat(jiraComponent);
        return format(jiraComponent, hotfixVersionFormat, numericVersionFactory.create(version));
    }

    public String formatLineVersionFormat(JiraComponent jiraComponent, String version) {
        String lineVersionFormat = getLineVersionFormat(jiraComponent);
        return format(jiraComponent, lineVersionFormat, numericVersionFactory.create(version));
    }

    public String getBuildVersionFormat(JiraComponent jiraComponent) {
        if (jiraComponent != null) {
            if (StringUtils.isNotBlank(jiraComponent.getComponentVersionFormat().getBuildVersionFormat())) {
                return jiraComponent.getComponentVersionFormat().getBuildVersionFormat();
            } else {
                return jiraComponent.getComponentVersionFormat().getReleaseVersionFormat();
            }
        } else {
            return null;
        }
    }

    public String getHotfixVersionFormat(JiraComponent jiraComponent) {
        if (jiraComponent != null) {
            return jiraComponent.getComponentVersionFormat().getHotfixVersionFormat();
        }
        return null;
    }

    public String getLineVersionFormat(JiraComponent jiraComponent) {
        if (jiraComponent != null) {
            if (StringUtils.isNotBlank(jiraComponent.getComponentVersionFormat().getLineVersionFormat())) {
                return jiraComponent.getComponentVersionFormat().getLineVersionFormat();
            } else {
                return jiraComponent.getComponentVersionFormat().getMajorVersionFormat();
            }
        }
        return null;
    }

    private boolean matchesVersionFormat(JiraComponent jiraComponent, String version, String versionFormat, boolean strict) {
        if (StringUtils.isBlank(versionFormat)) {
            return false;
        }
        if (!strict) {
            return versionFormatter.matchesNonStrictFormat(versionFormat, version);
        } else {
            if (isCustomComponent(jiraComponent)) {
                ComponentInfo componentInfo = jiraComponent.getComponentInfo();

                return versionFormatter.matchesFormat(componentInfo.getVersionFormat(),
                        versionFormat, componentInfo.getVersionPrefix(), version);

            } else {
                return versionFormatter.matchesFormat(versionFormat, version);
            }
        }
    }

    public boolean isCustomComponent(JiraComponent jiraComponent) {
        return jiraComponent.getComponentInfo() != null &&
                StringUtils.isNotBlank(jiraComponent.getComponentInfo().getVersionPrefix());
    }

    public String normalizeVersion(JiraComponent component, String version, boolean isHotfixEnabled, boolean strict) {

        if (component != null ) {
            IVersionInfo numericVersion = numericVersionFactory.create(version);
            if (isHotfixEnabled && matchesHotfixVersionFormat(component, version, strict)) {
                return numericVersion.formatVersion(component.getComponentVersionFormat().getHotfixVersionFormat());
            }
            if (matchesBuildVersionFormat(component, version, strict)) {
                return numericVersion.formatVersion(getBuildVersionFormat(component));
            }
            if (matchesRCVersionFormat(component, version, strict)) {
                return numericVersion.formatVersion(component.getComponentVersionFormat().getReleaseVersionFormat());
            }
            if (matchesReleaseVersionFormat(component, version, strict)) {
                return numericVersion.formatVersion(component.getComponentVersionFormat().getReleaseVersionFormat());
            }
            if (matchesMajorVersionFormat(component, version, strict)) {
                return numericVersion.formatVersion(component.getComponentVersionFormat().getMajorVersionFormat());
            }
            if (matchesLineVersionFormat(component, version, strict)) {
                return numericVersion.formatVersion(getLineVersionFormat(component));
            }
        }
        return null;
    }


}
