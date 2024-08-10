package com.example.workoutapp.screens.helpers

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.workoutapp.R
import com.example.workoutapp.screens.HomeScreenContent
import com.example.workoutapp.ui.theme.WorkoutAppTheme

// There are issues due to the scrollable. It makes it hard to define that the card should take up
// X% of the screen's height.
// @Composable
// fun MakeCardTodo(
//     title : String,
//     // description : String,
//     onClick : () -> Unit = {},
// ) {
//
//     // https://developer.android.com/develop/ui/compose/components/card
//     Card(
//         colors = CardDefaults.cardColors(
//             containerColor = MaterialTheme.colorScheme.surfaceVariant,
//         ),
//         elevation = CardDefaults.cardElevation(
//             defaultElevation = 6.dp
//         ),
//         modifier = Modifier
//             .fillMaxWidth(0.8f)
//             // .fillMaxHeight(0.25f)
//     ) {
//
//         Box(modifier = Modifier) {
//
//             Image(
//                 painter = painterResource(id = R.drawable.ic_launcher_background),
//                 contentDescription = null,
//                 contentScale = ContentScale.FillBounds,
//                 modifier = Modifier.fillMaxWidth(),
//             ) // Make image take up the whole card? Probably means that the card size needs to be fixed
//
//             Column(
//                 modifier = Modifier
//                     .fillMaxSize()
//                     .fillMaxWidth(), // Needs maxWidth else the column won't use the
//                 // cards space, and the centering in this column won't be aligned/make sense with the card's space.
//                 verticalArrangement = Arrangement.Center,
//                 horizontalAlignment = Alignment.CenterHorizontally,
//             ) {
//
//                 // https://developer.android.com/develop/ui/compose/graphics/images/loading
//                 // Image(
//                 //     painter = painterResource(id = R.drawable.ic_launcher_foreground),
//                 //     contentDescription = null,
//                 //     contentScale = ContentScale.Crop,
//                 //     modifier = Modifier.wrapContentSize(),
//                 // ) // Make image take up the whole card? Probably means that the card size needs to be fixed
//
//                 // Spacer(
//                 //     modifier = Modifier
//                 //         .height(16.dp)
//                 // ) // Weight of 1.0f means it fills the space
//
//                 Text(
//                     text = title,
//                     modifier = Modifier
//                     // textAlign = TextAlign.Center,
//                 )
//
//                 // Spacer(modifier = Modifier.height(16.dp))
//
//             }
//         }
//
//     }
// }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeCard(
    title : String,
    imageResourceID : Int,
    // description : String,
    onClick : () -> Unit = {},
) {

    // https://developer.android.com/develop/ui/compose/components/card
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.8f)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth(), // Needs maxWidth else the column won't use the
            // cards space, and the centering in this column won't be aligned/make sense with the card's space.
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // https://developer.android.com/develop/ui/compose/graphics/images/loading
            Image(
                painter = painterResource(id = imageResourceID),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(percent = 20))
                ,
                contentScale = ContentScale.Fit
            ) // Make image take up the whole card? Probably means that the card size needs to be fixed

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(16.dp))

        }

    }
}

@Preview(showBackground = true)
@Composable
fun MakeCardPreview() {
    WorkoutAppTheme {

        Column(
            modifier = Modifier
                .size(200.dp)
                .verticalScroll(rememberScrollState())
            ,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            MakeCard(
                title = "My Card",
                imageResourceID = R.drawable.ic_launcher_background,
            )

        }
    }
}
