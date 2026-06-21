package com.roamly.app.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roamly.app.data.SocialRepository
import com.roamly.app.ui.components.RoutePost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * What: The "brain" for the Favorites screen. Loads the signed-in user's bookmarked posts from
 *       Firestore and supports removing a bookmark. One StateFlow UI state (course Topic 06 pattern).
 * Who:  An Nguyen
 * When: Goal 7 - Final project (Jun 2026)
 */
data class FavoritesUiState(
    val isLoading: Boolean = true,
    val favorites: List<RoutePost> = emptyList(),
)

class FavoritesViewModel : ViewModel() {

    private val social = SocialRepository()

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init { refresh() }

    fun refresh() = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true)
        val favorites = social.getFavorites().getOrDefault(emptyList())
        _uiState.value = FavoritesUiState(isLoading = false, favorites = favorites)
    }

    // Removes a post from favorites and updates the list in place.
    fun removeFavorite(post: RoutePost) = viewModelScope.launch {
        social.toggleFavorite(post).onSuccess {
            _uiState.value = _uiState.value.copy(
                favorites = _uiState.value.favorites.filterNot { it.id == post.id },
            )
        }
    }
}
