package com.roamly.app.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roamly.app.data.AuthRepository
import com.roamly.app.data.RoamlyUser
import com.roamly.app.data.SocialRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * What: The "brain" for the Profile screen. Loads either the signed-in user's own profile or
 *       another traveler's (by uid) from Firestore, tracks follow state, and loads suggested
 *       users to follow. Handles following the viewed user and suggested users (multi-user).
 *       One StateFlow UI state (course Topic 06 pattern).
 * Who:  An Nguyen
 * When: Goal 7 - Final project (Jun 2026)
 */
data class ProfileUiState(
    val isLoading: Boolean = true,
    val profile: RoamlyUser? = null,
    val isOwnProfile: Boolean = true,
    val isFollowing: Boolean = false,
    val suggested: List<RoamlyUser> = emptyList(),
    val followedUserIds: Set<String> = emptySet(),
)

class ProfileViewModel : ViewModel() {

    private val authRepo = AuthRepository()
    private val social = SocialRepository()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private var viewedUid: String = ""

    fun load(userId: String) {
        viewModelScope.launch {
            val own = userId.isBlank() || userId == authRepo.currentUid
            viewedUid = if (own) (authRepo.currentUid ?: "") else userId
            val profile = if (own) authRepo.loadCurrentProfile().getOrNull()
            else authRepo.loadProfile(userId).getOrNull()
            val following = if (!own) social.isFollowing(userId).getOrDefault(false) else false
            val suggested = social.getSuggestedUsers().getOrDefault(emptyList())
            _uiState.value = ProfileUiState(
                isLoading = false,
                profile = profile,
                isOwnProfile = own,
                isFollowing = following,
                suggested = suggested,
            )
        }
    }

    // Follows/unfollows the profile currently being viewed (only meaningful on another's profile).
    fun toggleFollow() = viewModelScope.launch {
        if (viewedUid.isBlank()) return@launch
        social.toggleFollow(viewedUid).onSuccess { following ->
            _uiState.value = _uiState.value.copy(isFollowing = following)
        }
    }

    // Follows a user from the "Travelers to Follow" suggestions.
    fun followSuggested(userId: String) = viewModelScope.launch {
        social.toggleFollow(userId).onSuccess { following ->
            val ids = _uiState.value.followedUserIds.toMutableSet()
            if (following) ids.add(userId) else ids.remove(userId)
            _uiState.value = _uiState.value.copy(followedUserIds = ids)
        }
    }

    // Signs the user out of Firebase; the screen then navigates back to Login.
    fun signOut() = authRepo.signOut()
}
