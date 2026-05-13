# Wholesale Manager — Android App

## Project Overview
Android application for managing wholesale trading operations. Built with Jetpack Compose, Room, Clean Architecture, and MVVM + StateFlow.

## Architecture
- **Clean Architecture**: data / domain / presentation layers
- **MVVM** with StateFlow for reactive UI
- **Room** local database with soft delete
- **Jetpack Compose** + **Material 3** UI
- Manual DI via `AppModule` (ServiceLocator pattern)

## Package: `com.wholesale.manager`

## Modules

### Data Layer
- `data/local/entity/` — Room entities (BatchEntity, PurchasedRawEntity, OrderEntity, ExpenseEntity)
- `data/local/dao/` — DAO interfaces
- `data/local/AppDatabase.kt` — Room database
- `data/repository/` — Repository implementations

### Domain Layer
- `domain/model/` — Domain models (Batch, PurchasedRaw, Order, Expense)
- `domain/repository/` — Repository interfaces
- `domain/usecase/` — Use cases per entity and operation

### Presentation Layer
- `presentation/theme/` — Material 3 theme
- `presentation/common/` — Reusable UI components (SearchBar, FilterChipRow, AppTextField, AppDropdown, SwipeToDeleteBackground)
- `presentation/main/` — MainScreen, BottomNavItem, 4 tabs

## Tabs
1. **Закупки** (Purchases) — raw material purchases with supplier info
2. **Партии** (Batches) — production batches with cost tracking
3. **Расходы** (Expenses) — operational expenses by type
4. **Заказы** (Orders) — customer orders with delivery tracking

## Build
```
./gradlew :app:assembleDebug
```

## User Preferences
- Language: Russian UI labels
- Architecture: Clean Architecture + MVVM + StateFlow
- Database: Room with soft delete (isDeleted flag)
- UI: Material 3, Jetpack Compose
