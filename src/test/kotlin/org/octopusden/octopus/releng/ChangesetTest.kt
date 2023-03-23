package org.octopusden.octopus.releng

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.net.URI
import java.net.URL
import java.util.*

internal class ChangesetTest {

    val CS = SmallChangeSet("96dad8fbcf3365a5bf46dd9b0f6d6ea574beea06",
            "ssh://git@github.com:octopusden/octopus-versions-api.git",
            Date(),
            "author",
            "comment")

    @Test
    fun testShortenRepoPath() {
        assertEquals("definitely-not-an-uri", "definitely-not-an-uri".shortRepoPath())
        assertEquals("/octopus-versions-api.git", CS.repository.shortRepoPath())
    }
}