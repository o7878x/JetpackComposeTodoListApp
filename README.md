# Jetpack Compose Todo List App

A simple todo list app built with Jetpack Compose, demonstrating modern Android development practices.

## Tech Stack & Key Dependencies

| Category | Library | Version | Purpose |
|----------|---------|---------|---------|
| **UI** | Jetpack Compose (BOM) | 2024.09.00 | Declarative UI framework |
| **UI** | Material 3 | via BOM | Material Design 3 components |
| **DI** | Koin | 4.2.1 | Dependency injection with Koin Annotations |
| **DI** | Koin Compose ViewModel | 4.2.1 | ViewModel injection for Compose |
| **DI** | Koin Compiler Plugin | 1.0.0 | Koin annotation processing |
| **Database** | Room | 2.8.4 | Local database (with KSP) |
| **KSP** | Google KSP | 2.3.9 | Symbol processing for Room & Koin |
| **Lifecycle** | Lifecycle ViewModel Compose | 2.6.1 | ViewModel integration for Compose |
| **Activity** | Activity Compose | 1.8.0 | Compose entry point |
| **Build** | AGP | 8.12.2 | Android Gradle Plugin |
| **Language** | Kotlin | 2.3.20 | Primary language |

## Architecture

- **MVVM** pattern with ViewModel + Repository
- **Koin** for dependency injection (annotated modules)
- **Room** for local data persistence with DAO abstraction
- **Kotlin Coroutines** for async operations# yolo
