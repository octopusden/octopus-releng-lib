package org.octopusden.octopus.releng

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.octopusden.octopus.releng.utils.toPrettyString
import java.net.URI
import java.net.URISyntaxException
import java.util.Date

data class Revision(
    val path: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
open class SmallChangeSet(
    val id: String,
    val repository: String,
    val date: Date,
    val author: String,
    val comment: String,
) {
    fun shortenMessage(message: String) =
        if (message.length > 15) {
            message.substring(0, 14).trim() + ".."
        } else {
            message
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SmallChangeSet) return false

        if (id != other.id) return false
        if (repository != other.repository) return false
        if (date != other.date) return false
        if (author != other.author) return false
        if (comment != other.comment) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + repository.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + comment.hashCode()
        return result
    }

    override fun toString(): String = "CS('$repository':${date.toPrettyString()}:$author cmt='${shortenMessage(comment)}')"
}

open class Changeset(
    id: String,
    repository: String,
    val branch: String,
    date: Date,
    author: String,
    val url: String,
    comment: String,
    val revisions: Collection<Revision> = emptyList(),
) : SmallChangeSet(id, repository, date, author, comment) {
    override fun toString(): String =
        "CS('${repository.shortRepoPath()}':$branch:${date.toPrettyString()}:$author cmt='${shortenMessage(comment)}')"

    fun toStringWithRevision() =
        "CS('${repository.shortRepoPath()}':$branch:${date.toPrettyString()}:$author, cmt='${shortenMessage(
            comment,
        )}', rev=${revisions.joinToString { "{${it.path}}" }})"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Changeset) return false
        if (!super.equals(other)) return false

        if (branch != other.branch) return false
        if (url != other.url) return false
        if (revisions != other.revisions) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + branch.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + revisions.hashCode()
        return result
    }
}

// The fallback is the contract, not an oversight: a string that is not a URI is returned unchanged,
// which is what both tests in ChangesetTest pin. There is nothing to report and nowhere to report it
// from - this file has no logger and the caller only wants a display string.
@Suppress("SwallowedException")
fun String.shortRepoPath(): String =
    try {
        // getPath() is null for an opaque URI - "mailto:a@b.com", "urn:x" - which parses without
        // throwing, so the null fallback is not the same case as the catch below.
        URI(this).path ?: this
    } catch (e: URISyntaxException) {
        this
    }

object ChangesetSerializer {
    private val mapper = jacksonObjectMapper()

    fun serialize(changesets: List<Changeset>) = mapper.writeValueAsString(changesets)

    fun deserialize(value: String) = mapper.readValue(value, Array<Changeset>::class.java).toList()
}
