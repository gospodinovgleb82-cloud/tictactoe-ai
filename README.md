TicTacToe AI 🎮
Мобильное приложение для Android — классические «Крестики-нолики» с непобедимым искусственным интеллектом на основе алгоритма Minimax + Alpha-Beta Pruning.

Описание проекта
TicTacToe AI — Android-приложение, в котором игрок сражается против AI. На уровне сложности «Сложный» AI перебирает все возможные ходы и никогда не проигрывает. Поддерживаются три размера поля и три уровня сложности, а прогресс-бар победных шансов обновляется в реальном времени после каждого хода.

Технологический стек

Язык: Kotlin
Платформа: Android (min API 26)
UI: Android Views, GridLayout, ConstraintLayout
AI: Алгоритм Minimax + Alpha-Beta отсечение
Асинхронность: Kotlin Coroutines (Dispatchers.IO → Main)
Тестирование: JUnit 4
VCS: Git / GitHub


Ключевые функции

Три размера поля: 3×3, 4×4, 5×5
Три уровня сложности AI: Лёгкий, Средний, Сложный
Win Probability Bar: прогресс-бар победных шансов в реальном времени
Native Performance: тяжёлые вычисления AI выполняются в фоновом потоке через Dispatchers.IO, UI не зависает
Статистика сессии: счётчик побед игрока, AI и ничьих
Комментарии AI: приложение показывает, сколько позиций перебрал AI при выборе хода
Тёмная тема с анимациями победы и подсветкой выигрышных ячеек


Архитектура
Проект построен по паттерну MVC:
┌─────────────────────────────────────┐
│              View Layer             │
│  MainActivity / GameActivity /      │
│  SettingsActivity + XML layouts     │
└──────────────┬──────────────────────┘
               │ events / UI updates
┌──────────────▼──────────────────────┐
│           GameController            │
│  Coroutines: IO → Main              │
│  Связывает UI с логикой и AI        │
└──────┬───────────────────┬──────────┘
       │                   │
┌──────▼──────┐     ┌──────▼──────────┐
│  GameRules  │     │   MinimaxAI     │
│  GameState  │     │  ScoreEvaluator │
│  Player     │     │  Alpha-Beta     │
└─────────────┘     └─────────────────┘

Состав команды
УчастникРольЗона ответственностиГлебProject Lead / AI DeveloperАлгоритм Minimax, Alpha-Beta, архитектура, code reviewЕвгенийFrontend / UI DeveloperВсе экраны, анимации, тёмная тема, release APKСтасLogic & Algorithm DeveloperGameState, GameRules, GameController, CoroutinesАлексQA / DocsТест-кейсы, unit-тесты, README, документация

Структура репозитория
tictactoe-ai/
├── app/src/main/
│   ├── java/.../
│   │   ├── MainActivity.kt
│   │   ├── GameActivity.kt
│   │   ├── SettingsActivity.kt
│   │   ├── GameController.kt
│   │   ├── GameState.kt
│   │   ├── GameRules.kt
│   │   ├── Player.kt
│   │   ├── MinimaxAI.kt
│   │   └── ScoreEvaluator.kt
│   └── res/
│       ├── layout/
│       │   ├── activity_main.xml
│       │   ├── activity_game.xml
│       │   └── activity_settings.xml
│       └── values/
│           ├── colors.xml
│           └── styles.xml
└── app/src/test/
    └── GameTest.kt

Сборка и запуск
Требования

Android Studio Hedgehog или новее
Android SDK 26+
Kotlin 1.9+

Debug-сборка
bash./gradlew :app:assembleDebug
Release APK
bash./gradlew :app:assembleRelease
Запуск тестов
bash./gradlew :app:test

GitHub
🔗 https://github.com/gospodinovgleb82-cloud/tictactoe-ai
