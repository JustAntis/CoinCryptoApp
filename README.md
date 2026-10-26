# CoinCryptoApp

Android-приложение, которое показывает топ-50 криптовалют по объёму торгов и их курс к USD.
Данные берутся из [CryptoCompare API](https://min-api.cryptocompare.com/) и кэшируются в локальной базе, поэтому список доступен и без сети.

## Возможности

- Список топ-50 монет с ценой и временем последнего обновления
- Экран с подробной информацией о монете (цена, минимум и максимум за день, биржа последней сделки)
- Адаптивная вёрстка: в альбомной ориентации список и детали показываются рядом
- Фоновая синхронизация через WorkManager

## Стек

- Kotlin, Coroutines, LiveData, ViewModel
- Clean Architecture (`data` / `domain` / `presentation`)
- Dagger 2 (DI, в том числе для ViewModel и Worker)
- Retrofit + Gson
- Room
- WorkManager
- Picasso, Data Binding

## Запуск

1. Получите бесплатный API-ключ на [cryptocompare.com](https://www.cryptocompare.com/cryptopian/api-keys).
2. Добавьте его в файл `local.properties` в корне проекта (файл не попадает в git):

   ```properties
   CRYPTOCOMPARE_API_KEY=ваш_ключ
   ```

3. Откройте проект в Android Studio и запустите конфигурацию `app`.

Без ключа проект соберётся, но запросы к API будут отклоняться.

## Синхронизация данных

Бесплатный тариф CryptoCompare сильно ограничивает число запросов, поэтому данные обновляются редко:

- периодически раз в 24 часа (`PeriodicWorkRequest`);
- при открытии приложения, только если данные старше 12 часов.

Одна синхронизация = 2 запроса к API. Интервалы задаются в `CoinSyncWorker`.

## Структура

```
app/src/main/java/com/example/cryptoapp/
├── data/          # Retrofit, Room, мапперы, репозиторий, Worker
├── di/            # Dagger-компонент и модули
├── domain/        # сущности, интерфейс репозитория, use case'ы
└── presentation/  # Activity, Fragment, ViewModel, адаптер списка
```
