package com.andrei.githubbrowser.ui.base.mapper

import com.andrei.githubbrowser.R
import com.andrei.githubbrowser.data.Mapper
import com.andrei.githubbrowser.data.model.bo.RepoBo
import com.andrei.githubbrowser.ui.model.RepoUiModel
import com.andrei.githubbrowser.utils.StringProvider
import java.text.NumberFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class MapperRepoBoToRepoUIModel @Inject constructor(
    private val stringProvider: StringProvider
) : Mapper<RepoBo, RepoUiModel> {

    companion object {
        private const val DATE_FORMAT_PATTERN = "MMM dd, yyyy"
    }

    override fun map(from: RepoBo): RepoUiModel {
        val numberFormat = NumberFormat.getInstance(Locale.getDefault())

        val formattedDate = try {
            ZonedDateTime.parse(from.updatedAt)
                .format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN, Locale.getDefault()))
        } catch (e: Exception) {
            e.printStackTrace()
            from.updatedAt
        }

        return RepoUiModel(
            name = from.name,
            ownerName = from.ownerName,
            ownerAvatarUrl = from.ownerAvatar,
            repoUrl = from.repoUrl,
            forks = numberFormat.format(from.forks),
            stars = numberFormat.format(from.stars),
            language = from.language
                ?: stringProvider.getString(R.string.repo_details_unknown_language),
            updatedAt = formattedDate,
            description = from.description
                ?: stringProvider.getString(R.string.repo_details_no_description)
        )
    }
}