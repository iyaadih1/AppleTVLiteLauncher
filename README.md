# Apple TV Lite Launcher
Lightweight Android TV HOME launcher prototype (minSdk 26, API 35 build).

## Build
Use Android SDK platform 35, build-tools 35.0.0, JDK 17 and Gradle 8.9:
`gradle assembleDebug`

A GitHub Actions workflow is included. Push this project to a GitHub repository, run the **Build Android APK** workflow, and download the `AppleTVLite-debug` artifact.

## Safe test
Install the APK without changing the default launcher:
`adb install -r app-debug.apk`
Open Apple TV Lite from the TV app list and test D-pad focus, app shortcuts and Settings. Only after testing, select it as Home in TV settings. Keep the original Google TV launcher enabled.
