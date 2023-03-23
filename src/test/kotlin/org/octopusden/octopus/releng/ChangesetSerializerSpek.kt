package org.octopusden.octopus.releng

import org.junit.jupiter.api.Assertions.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.*

class ChangesetSerializerSpek : Spek({
    describe("Changesets serialization") {
        context("Changeset serialize and deserialize operations") {
            it("should be correct result for not empty list") {
                val changesets = listOf(
                        Changeset("id1", "repo1", "main", Date(), "author1", "url1", "comment1", listOf(Revision("11"), Revision("12"), Revision("13"))),
                        Changeset("id2", "repo2", "main", Date(), "author2", "url2", "comment2", listOf(Revision("21"), Revision("22"), Revision("23")))
                )
                val json = ChangesetSerializer.serialize(changesets)
                assertEquals(changesets, ChangesetSerializer.deserialize(json))
            }
        }
    }
})