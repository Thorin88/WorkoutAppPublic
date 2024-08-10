# Workout Tracking App

My first attempt at building an Android application.

This project was focussed on learning the basics of Android development, and following best practices from start.

This version of the code has been prepared for [Google's Gemini Competition](https://ai.google.dev/competition). This means that an AI powered element has been introduced into the application. The code is a little bit rough around the edges in places, due to this competition's deadline, but this will be smoothed out in following updates.

The app is supported by a backend API that I built [here](https://github.com/Thorin88/WorkoutAppAPIPublic). This API must be running in order to use the app.

## Gemini Usage

Gemini is used to allow users to get customised workout plans, with grounding in the user's previously completed workout routines.

Gemini isn't called by the Android application directly, this is done by the backend API, which is using LangChain, which is then calling Gemini via the Google Cloud Python SDK. The app makes a request to an endpoint that the backend API offers, which invokes the calls to Gemini.

3 key concepts are utilised to get the most out of Gemini:

1) **Prompt Engineering:** Telling Gemini what its task is, how to make use of the tools it has available to it, and how to format its responses so that the app's backend API can understand the workouts it is requesting.
2) **RAG:** Gemini is provided grounding in two ways. First, the excerise names and the creation request format that the backend API recognises. Second, the latest workouts completed by the user making the request. As an extra challenge for Gemini, it is actually left down to Gemini to call the RAG functions, via a Function Calling setup.
3) **Function Calling:** Once it has come up with a workout, Gemini makes creation requests to the app's backend API using tools/functions that invoke such a call.

## Summary of Features

Users can make workout routines with the assistance of Gemini.

Users can track their progress through the workout routines that they make.

Gemini is aware of the workouts that a user has completed, so can give tailored workout recommendations.