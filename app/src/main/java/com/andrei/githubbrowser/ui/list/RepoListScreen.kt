package com.andrei.githubbrowser.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.andrei.githubbrowser.R
import com.andrei.githubbrowser.domain.list.RepoListAction
import com.andrei.githubbrowser.domain.list.RepoListState
import com.andrei.githubbrowser.ui.base.padding_0_5x
import com.andrei.githubbrowser.ui.base.padding_1x
import com.andrei.githubbrowser.ui.base.padding_2x
import com.andrei.githubbrowser.ui.base.padding_3x
import com.andrei.githubbrowser.ui.base.padding_4x
import com.andrei.githubbrowser.ui.base.padding_6x
import com.andrei.githubbrowser.ui.model.RepoUiModel

@Composable
fun ReposListScreenContainer() {
    val viewModel: ReposListViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    RepoListScreen(
        state = state,
        onRepoSelected = { viewModel.onAction(RepoListAction.RepoSelected(it)) },
        onRefresh = { viewModel.onAction(RepoListAction.Refresh) },
        onSearchLanguageChanged = { viewModel.onAction(RepoListAction.SearchLanguageChanged(it)) },
        onLoadMore = { viewModel.onAction(RepoListAction.LoadMore) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoListScreen(
    state: RepoListState,
    onRepoSelected: (RepoUiModel) -> Unit,
    onRefresh: () -> Unit,
    onSearchLanguageChanged: (String) -> Unit,
    onLoadMore: () -> Unit
) {
    var searchQuery by remember(state.searchQuery) { mutableStateOf(state.searchQuery) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val gridState = rememberLazyGridState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val totalItemsCount = gridState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            lastVisibleItemIndex >= (totalItemsCount - 4) &&
                    !state.isLoadingMore &&
                    !state.isEndReached &&
                    state.repos.isNotEmpty()
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(stringResource(R.string.repo_list_title)) },
                    scrollBehavior = scrollBehavior
                )
                HorizontalDivider()
            }
        }
    ) { paddingValues ->

        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        onSearchLanguageChanged(it.trim())
                    },
                    label = { Text(stringResource(R.string.repo_list_search_label)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(padding_1x)
                )

                Spacer(Modifier.height(padding_1x))
                Box(modifier = Modifier.fillMaxSize()) {
                    when {
                        state.repos.isEmpty() && state.isRefreshing -> { LoadingContent() }
                        state.repos.isEmpty() && state.searchQuery.isNotEmpty() && !state.isLoadingMore -> {
                            EmptyState(searchQuery = state.searchQuery)
                        }
                        state.errorMessage != null -> { ErrorState(message = state.errorMessage) }
                        else -> {
                            LazyVerticalGrid(
                                state = gridState,
                                columns = GridCells.Fixed(2),
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(padding_1x),
                                verticalArrangement = Arrangement.spacedBy(padding_1x),
                                horizontalArrangement = Arrangement.spacedBy(padding_1x)
                            ) {
                                items(state.repos, key = { it.name }) { repo ->
                                    RepoListItemShort(repo = repo, onClick = { onRepoSelected(repo) })
                                }

                                if (state.isLoadingMore) {
                                    item(span = { GridItemSpan(maxLineSpan) }) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(padding_2x),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(modifier = Modifier.size(padding_3x))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorState(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(padding_4x)) {
            Text(
                text = stringResource(R.string.repo_list_error_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(Modifier.height(padding_1x))
            Text(message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun EmptyState(searchQuery: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(padding_4x)) {
            Text(stringResource(R.string.repo_list_empty_title), style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(padding_1x))
            Text("'$searchQuery'", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(padding_1x))
            Text(stringResource(R.string.repo_list_empty_subtitle), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun LoadingContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(modifier = Modifier.size(padding_6x))
            Spacer(Modifier.height(padding_2x))
            Text(stringResource(R.string.repo_list_loading), style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun RepoListItemShort(repo: RepoUiModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = padding_0_5x)
    ) {
        Column(Modifier.padding(padding_1x)) {
            val avatarPlaceholder = rememberVectorPainter(Icons.Default.Person)
            AsyncImage(
                model = repo.ownerAvatarUrl,
                contentDescription = stringResource(R.string.repo_list_owner_avatar_content_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(padding_6x)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape),
                placeholder = avatarPlaceholder,
                error = avatarPlaceholder,
                fallback = avatarPlaceholder
            )
            Spacer(modifier = Modifier.height(padding_1x))
            Text(
                text = repo.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(padding_1x))
            Text(
                text = repo.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private val sampleRepo = RepoUiModel(
    name = "ComposeSample",
    ownerName = "androiddev",
    ownerAvatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
    repoUrl = "https://github.com/android/compose-samples",
    forks = "120",
    stars = "4700",
    description = "Sample projects to showcase Jetpack Compose features.",
    language = "Kotlin",
    updatedAt = "2025-11-19T10:00:00Z"
)

@Preview(showBackground = true)
@Composable
fun RepoListItemShortPreview() {
    MaterialTheme {
        RepoListItemShort(repo = sampleRepo, onClick = {})
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 720)
@Composable
fun RepoListScreenPreview() {
    MaterialTheme {
        RepoListScreen(
            state = RepoListState(
                repos = List(8) { sampleRepo.copy(name = "ComposeSample $it") },
                isRefreshing = false,
                isLoadingMore = true
            ),
            onRepoSelected = {},
            onRefresh = {},
            onSearchLanguageChanged = {},
            onLoadMore = {}
        )
    }
}
