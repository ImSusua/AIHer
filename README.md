# AIHer - AI Companion Android App

An open-source Android application that provides an AI companion experience, featuring chat, plugin system, Mac control, and extensibility through dynamic projects.

## Architecture

### Core Components

- **Runtime System**: Bundled development environment (Flutter SDK, Android SDK, Python, Gradle, OpenJDK)
- **Plugin System**: Dynamic AI plugins loaded from `assets/ai-dynamic-project`
- **Native Bridge**: Native libraries for Java/Kotlin interop via `SecShell`
- **Accessibility Service**: `HerAccessibilityService` for screen reading and interaction

### App Package: `com.perhaps.her`

**Main Activities:**
- `MainActivity` - Entry point
- `AiTextChatActivity` - AI chat interface
- `AiSettingsActivity` - AI configuration
- `UserSettingsActivity` - User preferences
- `PlusUpgradeActivity` - Premium upgrades
- `FeatureStoreActivity` - Feature marketplace
- `AiPluginsActivity` - Plugin management
- `MacControlActivity` - Mac computer control
- `RootVmFridaActivity` - Root/VM/Frida integration
- `FullScreenWebActivity` - Web content display

**Services:**
- `HerAccessibilityService` - Accessibility for UI automation
- `HerBackgroundTaskService` - Background task processing

**Authentication:**
- `AuthGateActivity` - OAuth gate (WeChat, QQ, Alipay)
- `AuthCallbackActivity` - OAuth callback handler

### Runtime Assets

Located in `assets/runtime/`:
- `flutter-sdk/` - Flutter engine and framework
- `android-jdk/` - Android SDK (API 34)
- `gradle-7.5-bin.zip` - Gradle build tool
- `OpenJDK17U-jdk_aarch64_linux_hotspot.tgz` - Java 17 runtime
- `python-3.12.13-aarch64-linux-gnu.zip` - Python interpreter
- `proot/` - PRoot for non-root environments
- `ubuntu-base-22.04.5-base-arm64.tgz` - Ubuntu base filesystem
- `lib/` - Native libraries

### Dynamic Project System

Located in `assets/ai-dynamic-project/`:

The app can compile and run Kotlin plugins dynamically using:
- Kotlin JVM compiler
- Android SDK (`android-build/`)
- D8 dexer for DEX conversion
- AGP 7.4.2 classpath

## Technical Stack

- **Platform**: Android (minSdk 24, targetSdk 34)
- **Language**: Kotlin, Java, Flutter/Dart
- **Build**: Gradle 7.5, AGP 7.4.2
- **Runtime**: Java 17, Python 3.12, Flutter 3.x
- **Native**: ARM64/ARMv7/x86/x86_64

## Building from Source

This is a reverse-engineered analysis project. To build:
1. Set up Android SDK with NDK
2. Install Flutter SDK matching the bundled version
3. Configure Gradle and Kotlin
4. Follow the dynamic project compilation flow in `ai-dynamic-project/`

## License

See original app for proprietary components. This is an open-source analysis project.
