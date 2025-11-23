package com.andrei.githubbrowser.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.andrei.githubbrowser.R
import com.andrei.githubbrowser.domain.RepoDetailsUiState
import com.andrei.githubbrowser.domain.details.RepoDetailsAction
import com.andrei.githubbrowser.domain.details.RepoDetailsResult
import com.andrei.githubbrowser.ui.base.padding_1_5x
import com.andrei.githubbrowser.ui.base.padding_1x
import com.andrei.githubbrowser.ui.base.padding_2x
import com.andrei.githubbrowser.ui.base.padding_4x
import com.andrei.githubbrowser.ui.base.padding_6x
import com.andrei.githubbrowser.ui.base.padding_8x
import com.andrei.githubbrowser.ui.model.RepoUiModel

@Composable
fun RepoDetailsScreenContainer(
    repoOwner: String,
    repoName: String,
    onBackPressed: () -> Unit
) {
    val viewModel: RepoDetailsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(repoOwner, repoName) {
        if (state.details == null && !state.isLoading) {
            viewModel.onAction(RepoDetailsAction.LoadDetails(repoOwner, repoName))
        }
    }

    val resultsFlow = viewModel.results
    LaunchedEffect(Unit) {
        resultsFlow.collect { result ->
            when (result) {
                is RepoDetailsResult.ShowError -> {
                    snackbarHostState.showSnackbar(message = result.message)
                }
            }
        }
    }

    RepoDetailsScreen(state = state, onBack = onBackPressed, snackbarHostState = snackbarHostState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoDetailsScreen(
    state: RepoDetailsUiState,
    onBack: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            state.details?.name
                                ?: stringResource(R.string.app_name),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
                HorizontalDivider()
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> LoadingContent()
                state.errorMessage != null -> ErrorContent(state.errorMessage)
                state.details != null -> DetailsContent(state.details)
            }
        }
    }
}

@Composable
private fun DetailsContent(repo: RepoUiModel) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(padding_2x),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding_2x),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(padding_2x)
            ) {
                AsyncImage(
                    model = repo.ownerAvatarUrl,
                    contentDescription = stringResource(R.string.repo_details_author),
                    modifier = Modifier
                        .size(padding_8x)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                )
                Column {
                    Text(
                        stringResource(R.string.repo_details_author),
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        repo.ownerName,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(padding_2x))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(padding_1x)
        ) {
            DetailStatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Star,
                label = stringResource(R.string.repo_details_stars),
                value = repo.stars
            )
            DetailStatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Sync,
                label = stringResource(R.string.repo_details_forks),
                value = repo.forks
            )
        }
        Spacer(Modifier.height(padding_2x))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(padding_2x)) {
                DetailRow(
                    label = stringResource(R.string.repo_details_language),
                    value = repo.language
                )

                DetailRow(
                    label = stringResource(R.string.repo_details_last_updated),
                    value = repo.updatedAt
                )

                Text(
                    stringResource(R.string.repo_details_url),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = padding_1x)
                )
                Text(
                    text = repo.repoUrl,
                    style = LocalTextStyle.current.copy(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Medium
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.clickable { uriHandler.openUri(repo.repoUrl) }
                )
            }
        }

        Spacer(Modifier.height(padding_2x))

        Text(
            stringResource(R.string.repo_details_description_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(Modifier.height(padding_1x))
        Text(
            repo.description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(Modifier.height(padding_4x))
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = padding_1x),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun DetailStatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Column(
            modifier = Modifier
                .padding(padding_1_5x)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Spacer(Modifier.height(padding_1x))
            Text(
                value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(Modifier.height(padding_2x))
        Text(stringResource(R.string.repo_details_loading), style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun ErrorContent(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding_4x),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Warning,
            contentDescription = stringResource(R.string.repo_details_error),
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(padding_6x)
        )
        Spacer(Modifier.height(padding_2x))
        Text(
            message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsContentPreview() {
    val sampleRepo = RepoUiModel(
        name = "Jetpack Compose Samples",
        ownerName = "android",
        ownerAvatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
        repoUrl = "https://github.com/android/compose-samples",
        forks = "100",
        stars = "1.5k",
        description = "A collection of samples showing off what you can do with Jetpack Compose.",
        language = "Kotlin",
        updatedAt = "Nov 19, 2023"
    )
    MaterialTheme {
        DetailsContent(repo = sampleRepo)
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    MaterialTheme {
        RepoDetailsScreen(
            state = RepoDetailsUiState(isLoading = true),
            onBack = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    MaterialTheme {
        RepoDetailsScreen(
            state = RepoDetailsUiState(errorMessage = stringResource(R.string.repo_details_loading_error)),
            onBack = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}
