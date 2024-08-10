### Setup

Internet perm in manifest

For local HTTP APIs, enable clear text traffic

Made first screen

Nav controller requires a gradle dependency

Made associated VM

Setup Dagger
- Dependencies and language for gradle can change, so full the documentation on doing this part
- **If there are old/unhelpful dagger errors, run `./gradlew clean build` in the terminal for fuller error logs**
- The kotlin version of the build will change which version of daggerhilt is usable. Eg this build was kotlin 1.9 and dagger 2.48
- Also need dep for HiltViewModel, which is in addition to the base DaggerHilt
- app/appName file
- Name of app in manifest, which makes the new app/appName file
- @AndroidEntryPoint in MainActivity
- @HiltViewModel on ViewModel and inject constructor
- Add extra dependency for the function hiltViewModel specifically

Navigation
- a package for this logic
- A class for screens, defining the possible options and their routes
- A class defining the available subgraphs, and their routes and start screens
- A class that builds the navigation class of the navController that will be passed around the
  application. Defines subgraphs and any one-screen routes.
- The controller needs to be built using this code, this is done by calling this code in the starting
  point of the app (main activity).

Login Screen
- base screen and graph
- TextField objects to prep for errors
- LoginScreen and ViewModel, slowly connecting the two together
- Login Usecase
- Needs repository for the associated API
- Deps for retrofit and retrofit gson
- Retrofit needs models for the params and response contents of the endpoints

Session Managers

Room
- Deps (use online doc, may have to lower version in told about API issues, or if strange room errors,
  may need to up the version to ensure compatibility)
- Requires the DB instance, DAOs, Tables

Repository
- Needs usecases
- usecases need room and managers via daggerhilt
- Repository has to specifically indicate when 401's are thrown, to indicate that the user needs
  to be asked to log in again

Usecases
- DaggerHilt teaches each usecase as a singleton, so can be injected into each constructor of dependant objects very easily

DaggerHilt
- Now that all the pieces are here, the dependency instantiation needs to be defined for DaggerHilt.
- di package, with module files. These files define
- Recall that daggerhilt can inject dependencies while providing dependencies
- Provides is used when a dependency needs something that also needs this object
- Typically a module per package can be a good way to define what's in each module file

- This requires daggerhilt to know about the repository
- Also need the authenticator and interceptor objects, which then require room
- Connect Login button to the API

### TopBar

Allows up to 3 icons (navigation icon + 3 icons). If more required, need to use a popup menu for one of the
elements.

https://www.youtube.com/watch?v=EqCvUETekjk&t=522s

Nice styling examples: https://developer.android.com/jetpack/compose/components/app-bars

Can change behaviour of the scrolling on the page. Eg if it's always at top, or just at the top of
the list of objects on the page.

Add side bar for menu icon, and right settings page for settings icon.

https://developer.android.com/develop/ui/views/components/appbar

### TODO

#### Logging Out

Current State: Done at a basic level. To be more advanced, the backend needs to provide an endpoint
for logging users out.

Next Steps: The logout and logging flow needs to be considered more carefully.

~~Logout Usecase~~

Currently uses Boolean return results, but with API integration this will probably be upgraded to
a object.

Logins should create entries in the API. Logout should then attempt to call the API.
- No internet connection?
- Auth needed to logout?

Storing an clearing tokens should involve persistent storage on the device, for faster login upon return.

Can prevent users from clicking back when on the logout page transition?

#### Logging

Current State: Backend has a new table for logging actions. Login attempts are logged. In making this change,
I realised that logging logouts was not that useful, because the backend doesn't really track logged in users
at all. Which then led to the point of, it probably should, else it is impossible to kick users out of the app
without their token expiring.

Next Steps: Clarify the typical way that this is tracked in a database, as there are different options and I am
not certain which is the usual practice.

When kicked out, users need to be logged out in the backend.

How to force users to login again? Tokens are kept only by the app, do I need to store them in the backend
too?

#### Recommendation

Have the page prevent empty messages being sent

Can have the AI able to gage user expertise, or use an assumed profile if nothing is available. i.e. how did you find that workout? Feed workouts with previous workout's difficulty rating.

#### Displaying Workouts

Add workout names to the API. Needs to be added to create workouts endpoint too.

~~On API side, sort query by date.~~ Untested

~~Workout is a List<WorkoutComponent>~~

Convert things to use UI versions of models where needed

Add '+' symbol to the MyWorkoutsPage that deletes the top of the stack and goes to the add workout page

If the current display workout page is setup to read from the API once per lifecycle, make sure it is
removed from the stack whenever it is moved away from, like if there was ever a feature that added workouts
in a following screen.

Actually revisiting the page, even if already on the stack, seems to count as a new lifecycle. Which makes sense,
the VM just gives the impression that the composable on the stack is the same one, but actually it's a new instance.

List needs to be scrollable, either whole view or scrollable within a fixed area.

Selectable components

Components need to be ordered by position

#### Editing Components

Tick click will attempt a write to backend

Misalignment between Text and TextField composable when TextField has to make space for supportText.
Not sure how to deal with this currently.

#### General

ScreenWithApiUsage should call the logout functions, at least the non-api versions.

Make all buttons inactive as needed

Test cloning code via Unit tests

Make a search bar for user workouts, based on workout names

Export workout screen, to get a csv version of workout progress

Limit username and password text box width (and other boxes). Currently expands when filled.

For large boxes of text, eg AI recommendation input, use multiple lines

Dark theme

### Bugs

Unexpected end of stream occasionally when using Retrofit. https://stackoverflow.com/questions/32183990/java-net-protocolexception-unexpected-end-of-stream
- Seems like an emulator specific bug, UI handles it fine at least, but good to confirm it's not an issue I'm causing

~~"Couldn't find cached token, trying database" from getCurrentAccessToken is displaying a lot.~~

Edit and tick only makes sense to apply once a workout has been clicked? Unless there is good "no work needed" checks

Tracking workout doesn't update state. With a set of the value each time working, but needs to be decoupled from the editable version. It seems
that the current setup is changing an observed state variable when edits are made to it. At the render component level, this update doesn't
actually occur. So it isn't renderComponent being recomposed due to state changes it sees, but actually the one watching the var workouts.

Button presses here are wrong too

### Notes

Pressing Esc (back) on the last screen in the stack doesn't close the app, it minimises it. Coroutines
therefore won't be cancelled unless the app is swiped off.

Padding is for space around an element, Spacers are for space between things in rows/columns.

[] is syntactic sugar for get(), hence can be defined for a class via `operator fun get` with 1 or more
parameters.