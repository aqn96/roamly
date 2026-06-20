/**
 * What: A reusable outlined "Sign in with <provider>" button (Apple/Google/Facebook) for the auth
 *       screens. Decorative in this build — only email/password auth is wired.
 * Who:  An Nguyen
 * When: Goal 7 — Final project (Jun 2026)
 */
package com.roamly.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roamly.app.ui.theme.RoamlyTheme

@Composable
fun SocialSignInButton(
    provider: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Text("Sign in with $provider")
    }
}

@Preview(showBackground = true)
@Composable
private fun SocialSignInButtonPreview() {
    RoamlyTheme {
        SocialSignInButton(provider = "Google")
    }
}
