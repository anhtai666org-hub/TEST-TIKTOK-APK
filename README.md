# TikTok TV Hybrid — Android source

Experimental Android TV + phone-friendly video player scaffold. This is an independent project, not an official TikTok app.

## Features in this starter
- Dark TV-first interface with D-pad focus support
- Pick and play a local video using Android's document picker
- Android TV / Leanback launcher support
- GitHub Actions workflow to build a debug APK

## Build
Open this repository in Android Studio, or run `./gradlew assembleDebug` in an Android SDK environment. GitHub Actions builds the debug APK and uploads it as an artifact.

## Not implemented yet
TikTok sign-in, online feed, upload, LIVE, camera integration, likes/comments/follows, and official API integration. These require separate implementation and any necessary platform authorization. This project does not bypass authentication or claim to be an official TikTok client.
