package edu.temple.convoy.ui.components

import android.graphics.drawable.Icon
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.temple.convoy.R
import edu.temple.convoy.main_convoy.location_data.LocationViewModel
import edu.temple.convoy.main_convoy.screen.ConvoyScreen

@Composable
fun CustomTopAppBar(
    title: String,
    actionIcon: Int,
    navIcon: Int,
    onActionClick: () -> Unit,
    onNavClick: () -> Unit
) {
    TopAppBar(
        title = { Text(text = title) },
        actions = {
            IconButton(
                onClick = { onActionClick() }
            ) {
                Icon(
                    painter = painterResource(id = actionIcon),
                    contentDescription = null
                )
            }
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier.padding(start = 8.dp),
                onClick = { onNavClick() }
            ) {
                Icon(
                    painter = painterResource(id = navIcon),
                    contentDescription = null
                )
            }
        },

    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTopBar() {
    CustomTopAppBar(
        title = "Jaime",
        actionIcon = R.drawable.baseline_logout_24,
        navIcon = R.drawable.profile,
        onActionClick = {}) {

    }
}