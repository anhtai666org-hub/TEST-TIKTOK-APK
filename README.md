# TikTok TV Hybrid — experimental Android TV client

This is an independent experimental project, not an official TikTok app.

## Current build

- Opens TikTok's public web experience directly instead of asking the user to pick a local video.
- TV-oriented landscape layout with large controls and Android TV launcher support.
- WebView JavaScript/DOM storage enabled for the site; cookie support enabled.
- Remote-control basics: back navigation, reload, return to For You, and a play/pause attempt for the visible HTML video.
- GitHub Actions builds a debug APK artifact.

## Important limitations

This is a **web-based interim client**, not a native rebuild of TikTok TV 12.3.4.1. TikTok may block or limit sign-in, playback, or navigation inside Android WebView; behaviour depends on TikTok's website and the device's WebView version. The private/mobile API integration, native account system, upload, LIVE, camera, likes/comments/follows, and messages are **not implemented**. No authentication checks are bypassed and no official API compatibility is claimed.

The supplied mobile and TV APKs are different signed applications; their code/resources cannot safely be merged by simply copying files. Native integration requires reverse-engineering the relevant components, resolving dependencies and testing on the target TV box.

## Build

Open this repository in Android Studio, or run `./gradlew assembleDebug` in an Android SDK environment. GitHub Actions builds a debug APK and uploads it as an artifact.
