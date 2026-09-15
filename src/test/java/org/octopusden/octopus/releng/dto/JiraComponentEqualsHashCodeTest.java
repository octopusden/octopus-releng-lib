package org.octopusden.octopus.releng.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.octopusden.releng.versions.ComponentVersionFormat;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@code equals} deliberately excludes {@code displayName} — a display label is not part of a
 * component's identity — but {@code hashCode} included it, so two components that {@code equals}
 * reports equal could land in different hash buckets. That breaks the
 * {@code equals}/{@code hashCode} contract and makes every hash-based collection of
 * {@link JiraComponent} unsound.
 */
class JiraComponentEqualsHashCodeTest {

    private static JiraComponent componentNamed(String displayName) {
        return new JiraComponent(
                "PRJ",
                displayName,
                ComponentVersionFormat.create("$major", "$major.$minor", "$major.$minor.$service", "$major", null),
                new ComponentInfo("prefix", "$versionPrefix-$baseVersionFormat"),
                false,
                false);
    }

    @Test
    @DisplayName("components differing only in displayName are equal, so their hash codes must match")
    void hashCodeAgreesWithEquals() {
        JiraComponent a = componentNamed("One Name");
        JiraComponent b = componentNamed("A Different Name");

        assertEquals(a, b, "displayName is intentionally excluded from equals");
        assertEquals(a.hashCode(), b.hashCode(), "equal objects must have equal hash codes");
    }

    @Test
    @DisplayName("a hash-based collection deduplicates components that differ only in displayName")
    void hashSetDeduplicates() {
        Set<JiraComponent> set = new HashSet<>();
        set.add(componentNamed("One Name"));
        set.add(componentNamed("A Different Name"));

        assertEquals(1, set.size(), "equal components must collapse to a single element");
        assertTrue(set.contains(componentNamed("Yet Another Name")), "lookup must not depend on displayName");
    }

    @Test
    @DisplayName("a genuine identity difference still separates two components")
    void differentProjectKeysStayDistinct() {
        JiraComponent a = componentNamed("Same Name");
        JiraComponent b = new JiraComponent(
                "OTHER",
                "Same Name",
                ComponentVersionFormat.create("$major", "$major.$minor", "$major.$minor.$service", "$major", null),
                new ComponentInfo("prefix", "$versionPrefix-$baseVersionFormat"),
                false,
                false);

        Set<JiraComponent> set = new HashSet<>();
        set.add(a);
        set.add(b);

        assertEquals(2, set.size());
    }
}
