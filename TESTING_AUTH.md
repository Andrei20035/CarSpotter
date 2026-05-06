# Auth flow — testing setup

Teste de unit pentru flow-ul de auth, scrise ca pentru un MVP. Fără
clean-architecture-pentru-arhitectură, fără use case-uri ad-hoc, fără
mocking de Retrofit internals.

## 1. Dependințe de test

Adaugă în `gradle/libs.versions.toml`:

```toml
[versions]
mockk = "1.13.13"
turbine = "1.2.0"
coroutines-test = "1.10.2"
json = "20240303"

[libraries]
mockk = { group = "io.mockk", name = "mockk", version.ref = "mockk" }
turbine = { group = "app.cash.turbine", name = "turbine", version.ref = "turbine" }
kotlinx-coroutines-test = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version.ref = "coroutines-test" }
json = { group = "org.json", name = "json", version.ref = "json" }
```

Și în `app/build.gradle.kts`, înlocuiește blocul `dependencies { ... testImplementation(libs.junit) }` cu:

```kotlin
testImplementation(libs.junit)
testImplementation(libs.mockk)
testImplementation(libs.turbine)
testImplementation(libs.kotlinx.coroutines.test)
testImplementation(libs.json) // pentru org.json.JSONObject din safeApiCall — Android stub-ul aruncă RuntimeException("Stub!") în unit tests
```

De ce `org.json:json` ca test dep: `safeApiCall` folosește `org.json.JSONObject`,
care în Android JAR-ul de test e doar stub. Fără dependința asta, testele de
parsing eroare crapă cu `RuntimeException: "Stub!"`.

De ce MockK și nu Mockito: stack-ul e Kotlin 100%, MockK suportă nativ
clase final, suspend functions și extension functions fără hack-uri.

## 2. Structura folderelor de test

```
app/src/test/java/com/example/carspotter/
├── MainDispatcherRule.kt                     ← rule pt. Dispatchers.Main
├── core/
│   ├── navigation/
│   │   └── StartDestinationViewModelTest.kt
│   └── network/
│       └── SafeApiCallTest.kt
├── data/
│   └── repository/
│       └── AuthRepositoryImplTest.kt
└── features/
    └── auth/
        └── AuthViewModelTest.kt
```

Mirror exact al package-urilor din `main/`, ca să găsești testul rapid.

## 3. Categorii de teste — ce și de ce

### `SafeApiCallTest`
Toate erorile de network ajung la UI prin `safeApiCall`. Dacă parsing-ul
crapă tăcut, userul vede mesaje aiurea în snackbar. Testele acoperă:
success / null body / JSON cu `error` / JSON cu `message` / JSON fără
nimic util / body gol / HTML / text scurt non-JSON / text lung / IOException
/ Exception generic. **11 teste**, fiecare prinde câte o ramură reală
din `extractErrorMessage`.

### `AuthRepositoryImplTest`
Repository-ul e thin — construiește `AuthRequest` și pasează prin
`safeApiCall`. Verificăm:
- `login` REGULAR trimite email/password și NU trimite `googleIdToken`
- `login` GOOGLE trimite `googleIdToken` și provider GOOGLE, fără email/password
- `register` propagă mesajul de eroare al serverului
- `logout` șterge auth data
- `deleteAccount` șterge prefs **doar** la success (nu la 403/500)

Punctul important: `deleteAccount` are side-effect condiționat. E ușor
să faci typo `if (result is ApiResult.Error)` și să ștergi prefs pe failure.

### `AuthViewModelTest`
Aici e zona cu cele mai multe buguri reale. Acoperim:

**Login REGULAR:**
- happy path (COMPLETED → Feed, JWT salvat)
- PROFILE_REQUIRED → ProfileCustomization (gating onboarding)
- email gol → eroare locală, fără apel la repo
- parolă greșită → mesaj server, NU se salvează JWT

**Register:**
- happy path
- parole diferite
- email format invalid
- parolă < 8 caractere

**Google login:**
- happy path
- token null → eroare, fără apel la repo (Google picker anulat)
- token blank → eroare
- **regression:** după Google login, login regular trebuie să folosească
  REGULAR și să **nu** reutilizeze `googleIdToken`-ul anterior

**Error / snackbar:**
- `onErrorShown` șterge mesajul dar păstrează `errorId`
- `errorId` crește strict la fiecare eroare → snackbar-ul poate fi
  re-afișat (Compose detectează emisii noi)
- `consumeNavigationEvent` șterge nav event
- `toggleLoginMode` curăță parolele și errorMessage

**StateFlow / Turbine:**
- un test demonstrativ care urmărește emisii prin `vm.uiState.test { }`

### `StartDestinationViewModelTest`
4 cazuri pentru gating-ul de la app start:
- onboarding nefăcut → Onboarding
- onboarding făcut, fără token → Login
- token, fără userId → ProfileCustomization
- token + userId → Feed

Pentru un MVP atât e suficient — un bug aici trimite userul pe
ecranul greșit la deschiderea aplicației.

## 4. Tooling — alegerile

| Aspect | Alegere | De ce |
|---|---|---|
| Test framework | JUnit 4 | E deja în proiect, e ce se integrează standard cu Android Studio |
| Mocking | **MockK** | Kotlin-first; `coEvery`/`coVerify` pe suspend; clase final fără bytecode magic |
| Flow assertions | **Turbine** (light) | Doar pentru un test demonstrativ; restul citesc `.value` direct |
| Coroutines | `kotlinx-coroutines-test` cu `UnconfinedTestDispatcher` | `viewModelScope.launch { }` rulează eager → fără `advanceUntilIdle()` peste tot |
| Dispatcher rule | `MainDispatcherRule` (custom, ~15 linii) | Pattern standard |

## 5. Ce NU merită testat acum pentru MVP

1. **Compose UI pixel-perfect** — fragil, lent, nu prinde buguri logice.
2. **Screenshot tests** — overkill pentru un MVP, întreținere mare.
3. **Retrofit internals** — Square deja le testează. Mock-uim API-ul, nu OkHttp.
4. **DataStore real** (UserPreferences cu adevărat persistat) — testabil
   doar instrumentat; pentru unit tests mock-uim toată clasa.
5. **Tranziții `isLoading: true → false`** — implementation detail. Testăm
   starea finală.
6. **Toate combinațiile de input** (parametrize trash) — alegem cazurile
   care reflectă buguri reale (parolă scurtă, email greșit, token blank).
7. **`resetOnboardingStatus`** — e marcat "scoate înainte de release",
   n-are sens să-l blochezi cu test.
8. **NavController** — testat instrumentat; aici verificăm doar că
   `navigationEvent` ajunge corect în state.
9. **Hilt graph** — Dagger validează la compile time. Nu e nevoie de
   `@HiltAndroidTest` pentru auth flow.
10. **Crashlytics / Firebase** — wrappers third-party, fără valoare în testat.

## 6. Cum rulezi

```bash
./gradlew :app:testDebugUnitTest
```

Sau în Android Studio: click dreapta pe folderul `test/` → Run Tests.

## 7. Refactor proposals — minim, doar dacă vrei

Codul de auth e deja test-friendly (DI prin constructor, no statics,
no Android types în logic). Singurul lucru pe care l-aș schimba dacă
vrei și mai multă rezistență: extrage validarea de email/parolă din
`AuthViewModel` într-o funcție pură (`AuthValidator.kt` sau pur și
simplu un set de top-level functions). Atunci poți avea 5 teste de
validare ultra-rapide, izolate de coroutines/state. **Nu e necesar
pentru MVP** — testele actuale acoperă deja validarea prin VM.
