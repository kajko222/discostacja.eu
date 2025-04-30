package eu.discostacja.ui.screen.main.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer


@Composable
fun StationShimmer() {

    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shimmer(shimmer),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
        )
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .weight(0.5f)
                        .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                )
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .weight(0.5f)
                        .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                )
            }
        }
    }
}
