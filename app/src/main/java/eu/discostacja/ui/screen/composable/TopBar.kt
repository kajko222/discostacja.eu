package eu.discostacja.ui.screen.composable

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import eu.discostacja.R

@Composable
fun TopBar() {
    val context = LocalContext.current

    Box(modifier = Modifier.height(200.dp)) {
        Image(
            painter = painterResource(R.drawable.main_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Image(
            modifier = Modifier
                .width(170.dp)
                .padding(start = 20.dp),
            painter = painterResource(R.drawable.logo),
            contentDescription = null
        )
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 6.dp)
                .padding(end = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            SocialButton(
                icon = Icons.Default.Public,
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.main_screen_web_url)))
                    context.startActivity(intent)
                }
            )
            SocialButton(
                icon = Icons.Default.People,
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.main_screen_facebook_url)))
                    context.startActivity(intent)
                }
            )
            SocialButton(
                icon = Icons.Default.Share,
                onClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.main_screen_share_text))
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, "Udostępnij przez")
                    context.startActivity(shareIntent)
                })
            SocialButton(
                icon = Icons.Default.Mail,
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:")
                            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.topbar_mail)))
                            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.topbar_mail_title))
                        }
                        context.startActivity(Intent.createChooser(intent, "Wyślij e-mail przez"))
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(context, context.getString(R.string.topbar_mail_app_not_found), Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}