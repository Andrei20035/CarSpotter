# Network Utilities

This package contains utilities for handling network operations in the CarSpotter app.

## ApiResult

`ApiResult` is a sealed class that represents the result of an API call. It has three states:
- `Success`: Contains the successful response data
- `Error`: Contains the error information
- `Loading`: Represents that the API call is in progress

## ApiResultCallAdapter

The `ApiResultCallAdapter` is a custom Retrofit CallAdapter that automatically converts Retrofit responses to `ApiResult`. This eliminates the need for manual conversion in each repository.

### How to use

1. In your API interface, use `ApiResult<T>` as the return type:

```kotlin
interface AuthApi {
    @POST("/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): ApiResult<AuthResponse>
}
```

2. In your repository, directly use the API method without any conversion:

```kotlin
class AuthRepository @Inject constructor(
    private val authApi: AuthApi
) : IAuthRepository {
    override suspend fun login(email: String, password: String): ApiResult<AuthResponse> {
        val loginRequest = LoginRequest(email, password)
        return authApi.login(loginRequest)
    }
}
```

## NetworkConnectivityInterceptor

The `NetworkConnectivityInterceptor` is an OkHttp interceptor that checks for network connectivity before making API calls. If there's no network connection, it throws a `NoConnectivityException`.

## NetworkConnectivityManager

The `NetworkConnectivityManager` provides a way to observe network connectivity changes in the app. It exposes a `StateFlow<Boolean>` that emits the current network connectivity status.

### How to use

1. Inject the `NetworkConnectivityManager` into your ViewModel:

```kotlin
class MyViewModel @Inject constructor(
    private val networkConnectivityManager: NetworkConnectivityManager
) : ViewModel() {
    val isNetworkAvailable = networkConnectivityManager.isNetworkAvailable
}
```

2. Observe the network connectivity status in your UI:

```kotlin
@Composable
fun MyScreen(viewModel: MyViewModel) {
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()
    
    if (!isNetworkAvailable) {
        // Show a network error message
    }
}
```