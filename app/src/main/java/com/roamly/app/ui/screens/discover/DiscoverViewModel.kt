package com.roamly.app.ui.screens.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roamly.app.data.ContentRepository
import com.roamly.app.data.SocialRepository
import com.roamly.app.ui.components.RoutePost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * What: The "brain" for the Discover feed. Loads every traveler's posts from Firestore (the
 *       multi-user feed), tracks which the current user has liked/saved, and handles like + save
 *       actions. Exposes one StateFlow UI state (course Topic 06 pattern). Supports simple
 *       client-side filtering for the For You / Nearby / Trending / Following chips.
 * Who:  An Nguyen
 * When: Goal 7 - Final project (Jun 2026)
 */
data class DiscoverUiState(
    val isLoading: Boolean = true,
    val posts: List<RoutePost> = emptyList(),
    val likedPostIds: Set<String> = emptySet(),
    val savedPostIds: Set<String> = emptySet(),
)

class DiscoverViewModel : ViewModel() {

    private val content = ContentRepository()
    private val social = SocialRepository()

    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    init { refresh() }

    fun refresh() = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true)
        val posts = content.getFeed().getOrDefault(emptyList())
        val saved = social.getFavoriteIds().getOrDefault(emptySet())
        _uiState.value = DiscoverUiState(
            isLoading = false,
            posts = posts,
            likedPostIds = _uiState.value.likedPostIds,
            savedPostIds = saved,
        )
    }

    fun toggleLike(post: RoutePost) = viewModelScope.launch {
        content.toggleLike(post.id).onSuccess { liked ->
            val ids = _uiState.value.likedPostIds.toMutableSet()
            if (liked) ids.add(post.id) else ids.remove(post.id)
            // Reflect the like count locally without a full refetch.
            val posts = _uiState.value.posts.map {
                if (it.id == post.id) it.copy(likeCount = (it.likeCount + if (liked) 1 else -1).coerceAtLeast(0)) else it
            }
            _uiState.value = _uiState.value.copy(posts = posts, likedPostIds = ids)
        }
    }

    fun toggleSave(post: RoutePost) = viewModelScope.launch {
        social.toggleFavorite(post).onSuccess { saved ->
            val ids = _uiState.value.savedPostIds.toMutableSet()
            if (saved) ids.add(post.id) else ids.remove(post.id)
            _uiState.value = _uiState.value.copy(savedPostIds = ids)
        }
    }
}
