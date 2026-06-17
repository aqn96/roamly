package com.roamly.app.data

/**
 * What: A comment on a post (Firestore "posts/{postId}/comments/{id}"). All fields default so
 *       Firestore can deserialize via toObject(). Enables multi-user conversation on routes.
 * Who:  An Nguyen
 * When: Goal 7 — Final project (Jun 2026)
 */
data class Comment(
    val id: String = "",
    val authorUid: String = "",
    val username: String = "",
    val text: String = "",
    val createdAt: Long = 0L,
    val likeCount: Int = 0,
)
