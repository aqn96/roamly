package com.roamly.app.ui.screens.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roamly.app.data.Comment
import com.roamly.app.data.ContentRepository
import com.roamly.app.data.SocialRepository
import com.roamly.app.ui.components.RoutePost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * What: The "brain" for the Post Detail screen. Loads a post and its comments from Firestore,
 *       tracks like + follow state for the current user, and handles posting comments, liking, and
 *       following the author - the multi-user interaction layer. One StateFlow UI state
 *       (course Topic 06 pattern).
 * Who:  An Nguyen
 * When: Goal 7 - Final project (Jun 2026)
 */
data class PostDetailUiState(
    val isLoading: Boolean = true,
    val post: RoutePost? = null,
    val comments: List<Comment> = emptyList(),
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val isFollowingAuthor: Boolean = false,
)

class PostDetailViewModel : ViewModel() {

    private val content = ContentRepository()
    private val social = SocialRepository()

    private val _uiState = MutableStateFlow(PostDetailUiState())
    val uiState: StateFlow<PostDetailUiState> = _uiState.asStateFlow()

    private var postId: String = ""

    fun load(postId: String) {
        this.postId = postId
        viewModelScope.launch {
            val post = content.getPost(postId).getOrNull()
            val comments = content.getComments(postId).getOrDefault(emptyList())
            val liked = content.isLiked(postId).getOrDefault(false)
            val saved = social.getFavoriteIds().getOrDefault(emptySet()).contains(postId)
            val following = post?.authorUid?.let { social.isFollowing(it).getOrDefault(false) } ?: false
            _uiState.value = PostDetailUiState(
                isLoading = false,
                post = post,
                comments = comments,
                isLiked = liked,
                isSaved = saved,
                isFollowingAuthor = following,
            )
        }
    }

    fun toggleLike() = viewModelScope.launch {
        content.toggleLike(postId).onSuccess { liked ->
            val post = _uiState.value.post?.let {
                it.copy(likeCount = (it.likeCount + if (liked) 1 else -1).coerceAtLeast(0))
            }
            _uiState.value = _uiState.value.copy(isLiked = liked, post = post)
        }
    }

    fun addComment(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            content.addComment(postId, text).onSuccess { load(postId) }
        }
    }

    fun toggleSave() = viewModelScope.launch {
        val post = _uiState.value.post ?: return@launch
        social.toggleFavorite(post).onSuccess { saved ->
            _uiState.value = _uiState.value.copy(isSaved = saved)
        }
    }

    fun toggleFollow() = viewModelScope.launch {
        val authorUid = _uiState.value.post?.authorUid ?: return@launch
        social.toggleFollow(authorUid).onSuccess { following ->
            _uiState.value = _uiState.value.copy(isFollowingAuthor = following)
        }
    }
}
