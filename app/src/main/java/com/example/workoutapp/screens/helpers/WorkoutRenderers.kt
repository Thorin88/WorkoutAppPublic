package com.example.workoutapp.screens.helpers

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.workoutapp.R
import com.example.workoutapp.models.TextFieldState
import com.example.workoutapp.models.workoutapi.Workout
import com.example.workoutapp.models.workoutapi.WorkoutComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderWorkoutComponent(
    workoutIdx: Int,
    componentIdx: Int,
    selected: Boolean,
    editMode: Boolean,
    onClick: () -> Unit = {},
    updateWorkoutComponent: (Int, Int, WorkoutComponent) -> Unit,
    hasWorkoutComponentChanged: (Int, Int) -> Boolean,
    workoutComponent: WorkoutComponent // Moved to the end for better readability
) {

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(0.8f)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            val horizonalArrangement = if (selected) Arrangement.Center else Arrangement.Start

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = horizonalArrangement,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Spacer(modifier = Modifier.width(8.dp))

                if (!selected) {
                    val exerciseContent = ExerciseContent(workoutComponent.exercise_name)
                    Image(
                        painter = painterResource(id = exerciseContent.exerciseIconPath.toInt()),
                        contentDescription = "Exercise Icon",
                        modifier = Modifier
                            .fillMaxWidth(0.1f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(percent = 20))
                        ,
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    text = makeNamePretty(workoutComponent.exercise_name) + if (hasWorkoutComponentChanged(workoutIdx, componentIdx)) "*" else ""
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (selected) {
                Log.d("RenderWorkoutComponent", "Selected WorkoutComponent ID: ${workoutComponent.workout_component_id}")
                val textFieldsAreReadOnly = !editMode
                val textFieldColorConfig = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )

                // Remembers don't seem to be needed? I think this is because the changes are actually
                // being made to the workout list itself, instead of this state. So this state rerunning every
                // single recomposition is actually correct in the sense that the user input is already stored, and
                // not just reflected in this state variable.
                var repFieldState = TextFieldState(workoutComponent.reps, true)
                var weightFieldState = TextFieldState(workoutComponent.weight.toString(), true)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val exerciseContent = ExerciseContent(workoutComponent.exercise_name)
                    Image(
                        painter = painterResource(id = exerciseContent.exerciseIconPath.toInt()),
                        contentDescription = "Exercise Icon",
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(percent = 20))
                        ,
                        contentScale = ContentScale.Fit
                    )
                }

                LabeledTextField(
                    label = "Reps:",
                    textFieldState = repFieldState,
                    textFieldsAreReadOnly = textFieldsAreReadOnly,
                    textFieldColorConfig = textFieldColorConfig,
                    onValueChange = { newValue ->
                        repFieldState = repFieldState.copy(text = newValue)
                        if (repFieldState.isValid) {
                            updateWorkoutComponent(workoutIdx, componentIdx, workoutComponent.copy(reps = newValue))
                        }
                    }
                )

                LabeledTextField(
                    label = "Weight (${workoutComponent.units}):",
                    textFieldState = weightFieldState,
                    textFieldsAreReadOnly = textFieldsAreReadOnly,
                    textFieldColorConfig = textFieldColorConfig,
                    isWeightField = true,
                    onValueChange = { newValue ->
                        val isValid = newValue.toFloatOrNull() != null
                        weightFieldState = weightFieldState.copy(text = newValue, isValid = isValid, message = if (isValid) "" else "Please enter a valid weight")
                        if (isValid) {
                            updateWorkoutComponent(workoutIdx, componentIdx, workoutComponent.copy(weight = newValue.toFloat()))
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun LabeledTextField(
    label: String,
    textFieldState: TextFieldState,
    textFieldsAreReadOnly: Boolean,
    textFieldColorConfig: TextFieldColors,
    isWeightField: Boolean = false,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        
        // Spacer(modifier = Modifier.width(8.dp))

        // Is just text, but cannot get them to align due to the supportingText option requiring more space
        TextField(
            value = label,
            modifier = Modifier.weight(1.0f),
            onValueChange = {},
            supportingText = {},
            colors = textFieldColorConfig,
        )

        TextField(
            value = textFieldState.text,
            onValueChange = onValueChange,
            isError = !textFieldState.isValid,
            supportingText = {
                if (!textFieldState.isValid) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = textFieldState.message
                    )
                }
            },
            maxLines = 1,
            colors = textFieldColorConfig,
            readOnly = textFieldsAreReadOnly,
            keyboardOptions = if (isWeightField) KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
            modifier = Modifier.weight(1.0f),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderWorkoutCard(
    workout : Workout<WorkoutComponent>,
    onClick : () -> Unit,
) {

    // Should probably do this per workout too
    // val prettyComponent = WorkoutComponentForUI.fromWorkoutComponent(workoutComponent = workoutComponent)

    val isAIGenerated : Boolean = workout.ai_generated

    Card(
        // colors = CardDefaults.cardColors(
        //     containerColor = if (isAIGenerated) Color.Transparent else MaterialTheme.colorScheme.surfaceVariant,
        // ),
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

        val boxModifier = if (isAIGenerated) {
            Modifier.background(
                GeminiGradient
            )
        }
        else {
            Modifier
        }

        // ALlows the background modifier to work better than when just applied to the card
        Box(
            modifier = boxModifier,
        ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth(), // Needs maxWidth else the column won't use the
            // cards space, and the centering in this column won't be aligned/make sense with the card's space.
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxWidth(), // Needs maxWidth else the column won't use the
                // cards space, and the centering in this column won't be aligned/make sense with the card's space.
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = makeNamePretty(workout.name))
            }

            Spacer(modifier = Modifier.height(8.dp))

        }

        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderExpandedWorkout(
    workout : Workout<WorkoutComponent>,
    workoutIdx: Int,
    modifier: Modifier = Modifier,
    componentOnClick : (Int) -> Unit,
    selectedComponentIdx : Int,
    editMode : Boolean,
    updateWorkoutComponent: (Int, Int, WorkoutComponent) -> Unit,
    hasWorkoutComponentChanged: (Int, Int) -> Boolean,
    // No onclicks here, but they are at the component level
) {

    LazyColumn(
        modifier = modifier,
    ) {
        items(workout.workout_components.size) { idx ->
            RenderWorkoutComponent(
                workoutComponent = workout.workout_components[idx],
                workoutIdx = workoutIdx,
                componentIdx = idx,
                selected = selectedComponentIdx == idx,
                onClick = {
                    componentOnClick(idx)
                },
                editMode = editMode,
                updateWorkoutComponent = updateWorkoutComponent,
                hasWorkoutComponentChanged = hasWorkoutComponentChanged,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

}