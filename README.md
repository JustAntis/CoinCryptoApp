# CoinCryptoApp

An Android app that shows the top 50 cryptocurrencies by trading volume and their prices in USD.
Data comes from the [CryptoCompare API](https://min-api.cryptocompare.com/) and is cached in a local database, so the list is available offline.

## Features

- List of the top 50 coins with price and last update time
- Coin detail screen (price, daily low and high, exchange of the last trade)
- Adaptive layout: in landscape, the list and details are shown side by side
- Background sync with WorkManager

## Tech stack

- Kotlin, Coroutines, LiveData, ViewModel
- Clean Architecture (`data` / `domain` / `presentation`)
- Dagger 2 (DI, including ViewModels and Workers)
- Retrofit + Gson
- Room
- WorkManager
- Picasso, Data Binding

## Getting started

1. Get a free API key at [cryptocompare.com](https://www.cryptocompare.com/cryptopian/api-keys).
2. Add it to `local.properties` in the project root (this file is not tracked by git):

   ```properties
   CRYPTOCOMPARE_API_KEY=your_key
   ```

3. Open the project in Android Studio and run the `app` configuration.

Without a key the project still builds, but API requests will be rejected.

## Data sync

The CryptoCompare free tier has a very low request limit, so data is refreshed rarely:

- periodically, once every 24 hours (`PeriodicWorkRequest`);
- on app launch, only if the data is older than 12 hours.

One sync = 2 API requests. The intervals are defined in `CoinSyncWorker`.

## Project structure

```
app/src/main/java/com/example/cryptoapp/
├── data/          # Retrofit, Room, mappers, repository, Worker
├── di/            # Dagger component and modules
├── domain/        # entities, repository interface, use cases
└── presentation/  # Activities, Fragment, ViewModels, list adapter
```
