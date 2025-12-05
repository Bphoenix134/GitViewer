package data.remote.mapper

import data.remote.dto.RepoContentDto
import data.remote.dto.RepoDetailedDto
import data.remote.dto.RepoDto
import domain.model.Repo
import domain.model.RepoContent
import domain.model.RepoDetailed

fun RepoDto.toDomain() = Repo(
    id = id,
    name = name,
    fullName = fullName,
    owner = owner?.login ?: "",
    description = description,
    stars = stars,
    forks = forks,
    language = language
)

fun RepoDetailedDto.toDomain() = RepoDetailed(
    id = id,
    name = name,
    fullName = fullName,
    owner = owner.login,
    description = description,
    stars = stars,
    forks = forks,
    issues = issues,
    language = language,
    createdAt = createdAt,
    updatedAt = updatedAt,
    pushedAt = pushedAt
)

fun RepoContentDto.toDomain() = RepoContent(
    name = name,
    path = path,
    type = type,
    downloadUrl = downloadUrl
)