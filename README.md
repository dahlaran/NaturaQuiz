# NaturaQuiz 🌿

A Jetpack Compose Android application that helps users learn about plants and species through an interactive quiz and informative catalog.

## App Preview

**Quiz**
https://github.com/dahlaran/NaturaQuiz/screenshots/NaturaQuiz-Quiz.webm

**List**
https://github.com/dahlaran/NaturaQuiz/screenshots/NaturaQuiz-List.webm


## Features

- **Interactive Plant Quiz**: Test your knowledge of plants with an engaging swipe-based quiz interface
- **Plant Catalog**: Browse through a comprehensive list of plants and species
- **Detailed Information**: View detailed information about each plant including scientific names, families, and discovery dates
- **Modern UI**: Built with Material 3 design principles and smooth animations

## Architecture

The application follows Clean Architecture principles with MVVM pattern:

```
app/
├── core/
│   ├── bus/           # Event handling
│   ├── data/          # Base data handling classes
│   ├── network/       # Network related components
│   └── presentation/  # Common UI components
├── data/              # Data layer implementation
├── di/                # Dependency injection modules
├── domain/           
│   ├── entities/      # Business models
│   ├── repository/    # Repository interfaces
│   └── usecases/      # Business logic
└── presentation/
    ├── home/          # Home screen UI
    ├── splash/        # Splash screen
    └── viewmodel/     # ViewModels
```

## Tech Stack

- **UI**: Jetpack Compose with Material 3
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt
- **Networking**: Retrofit + OkHttp
- **Image Loading**: Coil
- **Animation**: Compose Animation APIs
- **Build System**: Gradle with Version Catalog

## Setup

1. Clone the repository
2. Create a `local.properties` file in the root directory
3. Add your Trefle API key:
   ```properties
   trefle.api.key=your_api_key_here
   ```
4. Build and run the project

## Development

### Prerequisites

- Android Studio Hedgehog or later
- JDK 17
- Android SDK 34

### Building
Add your Trefle API key to the `local.properties` file and run the following command:
```bash
./gradlew build
```