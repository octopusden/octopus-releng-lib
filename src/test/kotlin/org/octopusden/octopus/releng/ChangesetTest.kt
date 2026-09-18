package org.octopusden.octopus.releng

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URI
import java.net.URL
import java.util.Date

internal class ChangesetTest {
    val cs =
        SmallChangeSet(
            "96dad8fbcf3365a5bf46dd9b0f6d6ea574beea06",
            "ssh://git@github.com:octopusden/octopus-versions-api.git",
            Date(),
            "author",
            "comment",
        )

    @Test
    fun testShortenRepoPath() {
        assertEquals("definitely-not-an-uri", "definitely-not-an-uri".shortRepoPath())
        assertEquals("/octopus-versions-api.git", cs.repository.shortRepoPath())
    }

    @Test
    fun `an opaque uri has no path, so the original string is kept`() {
        // A syntactically VALID but opaque URI parses without throwing, and URI.getPath() is null for it.
        // The declared return type is non-null String, so the value must fall back to the input the same
        // way an unparseable string does.
        assertEquals("mailto:a@b.com", "mailto:a@b.com".shortRepoPath())
        assertEquals("urn:issue:1", "urn:issue:1".shortRepoPath())
    }
}
