# APK reconnaissance notes (2026-10-14)

These notes are based on ZIP-level inspection of the two user-provided APKs. They are not a decompilation or a claim that the apps have been merged.

## Confirmed structure

| Property | TikTok TV 12.3.4.1 | TikTok Mobile 47.1.4 |
|---|---:|---:|
| APK size | 47,161,335 bytes | 70,263,764 bytes |
| ZIP entries | 3,208 | 23,167 |
| DEX files | 10 | 15 |
| Native `.so` files | 87 | 0 |
| `res/layout/tv_*.xml` layouts | 142 | 0 |
| `resources.arsc` | present | present |

The TV APK contains a substantial native TV UI resource set, including layouts for the main container, side navigation, vertical video, landscape video detail, search, login, profile, comments and interaction bars. That is the correct starting point for preserving the TV experience; replacing it with a generic WebView or local player does not satisfy the goal.

## Engineering implications

1. Do not attempt a blind resource/DEX merge. The APKs have different package versions, resource tables, dependency sets and signing identities.
2. First decode the TV APK and identify its launcher Activity, navigation graph, feed/player components, networking stack and API models.
3. Then compare mobile networking/feed implementations and determine whether any components are reusable without copying incompatible resources or bypassing authentication.
4. Preserve the TV app's own layouts and remote-control navigation. Implement features incrementally and test on the actual Android TV box.
5. Keep the WebView build labelled as an interim experiment only. It is not the native hybrid target and may be limited by TikTok's website.

## Next required tools/tests

- Decode binary AndroidManifest/resources and decompile DEX for both APKs.
- Confirm application IDs, launcher activities, target SDKs, permissions, and native ABI requirements.
- Build and install a test APK on the target TV box, then validate feed playback, sign-in, D-pad navigation and app stability.

No private API compatibility, successful sign-in, or native integration is claimed at this stage.
