package org.octopusden.octopus.releng.utils;

import org.octopusden.releng.versions.VersionNames;

public final class VersionNamesHelper {

    public static final VersionNames INSTANCE = new VersionNames(
            System.getProperty("serviceBranch"),
            System.getProperty("service"),
            System.getProperty("minor"));

    private VersionNamesHelper() {
    }
}
