# Spec: Login con Google — TecConnect Android

**Fecha:** 2026-05-18  
**Enfoque:** Credential Manager API (androidx.credentials) — sin Firebase  
**Alcance:** UI completa + llamada al backend, endpoint en desarrollo paralelo

---

## Objetivo

Reemplazar el selector de rol provisional (MODO DEMO) en `LoginActivity` con un flujo real de autenticación vía Google Sign-In. El usuario selecciona su cuenta Google institucional, el app obtiene un `idToken`, lo envía al backend Spring Boot, y recibe JWT + rol para rutear a la pantalla correcta.

---

## Prerequisito de Google Cloud (no es código)

Antes de que el botón funcione, el desarrollador debe:

1. Crear un proyecto en [Google Cloud Console](https://console.cloud.google.com)
2. Habilitar la API de **Google Identity**
3. Crear un **OAuth 2.0 Web Client ID** → guardar como `google_web_client_id` en `res/values/strings.xml`
4. Crear un **OAuth 2.0 Android Client ID** con el SHA-1 del debug keystore y package name `com.conectatec`

Obtener SHA-1 del debug keystore:
```bash
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

---

## 1. Dependencias nuevas

### `gradle/libs.versions.toml`

```toml
# versions
credentials            = "1.3.0"
google-identity        = "1.1.1"

# libraries
credentials            = { group = "androidx.credentials", name = "credentials",                          version.ref = "credentials" }
credentials-gms        = { group = "androidx.credentials", name = "credentials-play-services-auth",       version.ref = "credentials" }
google-identity        = { group = "com.google.android.libraries.identity.googleid", name = "googleid",   version.ref = "google-identity" }
```

### `app/build.gradle.kts`

```kotlin
implementation(libs.credentials)
implementation(libs.credentials.gms)
implementation(libs.google.identity)
```

---

## 2. Constantes de autenticación

**Archivo:** `app/src/main/java/com/conectatec/ui/auth/AuthConstants.java`

```java
package com.conectatec.ui.auth;

public final class AuthConstants {
    /** Dominio institucional permitido. Cambiar según la institución. */
    public static final String DOMINIO_INSTITUCIONAL = "@tudominio.edu.mx";

    private AuthConstants() {}
}
```

---

## 3. Capa de datos

### `data/model/LoginResponse.java`

POJO inmutable (campos `public final`) que el backend devuelve:

```java
public class LoginResponse {
    public final String jwt;
    public final String rol;    // "ADMIN" | "DOCENTE" | "ESTUDIANTE" | "PENDIENTE"
    public final String nombre;

    public LoginResponse(String jwt, String rol, String nombre) {
        this.jwt    = jwt;
        this.rol    = rol;
        this.nombre = nombre;
    }
}
```

### `data/network/AuthService.java`

```java
public interface AuthService {
    @POST("api/auth/google")
    Call<LoginResponse> loginConGoogle(@Body Map<String, String> body);
}
```

Body enviado: `{"idToken": "<token de Google>"}`.

### `data/repository/AuthRepository.java`

```java
public interface AuthRepository {
    /** @throws Exception si el backend rechaza el token o hay error de red */
    LoginResponse loginConGoogle(String idToken) throws Exception;
}
```

### `data/repository/AuthRepositoryImpl.java`

Implementación con Retrofit. Llama `authService.loginConGoogle(Map.of("idToken", idToken)).execute()`. Si el código HTTP no es exitoso (401, 403, 5xx), lanza `IOException` con el mensaje del backend. Simula latencia con `Thread.sleep(300)` hasta que el endpoint esté disponible.

```java
// TODO: llamar a AuthService.loginConGoogle()
// Por ahora devuelve LoginResponse dummy para desarrollo paralelo:
// return new LoginResponse("jwt-dummy", "DOCENTE", "Nombre Dummy");
```

### `data/SessionManager.java`

Wrapper sobre DataStore (ya en el proyecto). Opera de forma sincrónica desde repositorios usando `runBlocking`.

```java
public class SessionManager {
    private static final String KEY_JWT    = "jwt";
    private static final String KEY_ROL    = "rol";
    private static final String KEY_NOMBRE = "nombre";

    // Inyectado vía Hilt
    private final DataStore<Preferences> dataStore;

    public void guardarSesion(String jwt, String rol, String nombre) { ... }
    public String getJwt()    { ... }  // null si no hay sesión
    public String getRol()    { ... }  // null si no hay sesión
    public void cerrarSesion() { ... }
}
```

---

## 4. Inyección de dependencias

### `di/AppModule.java` (nuevo)

`@Module @InstallIn(SingletonComponent.class)` — provee `DataStore<Preferences>` y `SessionManager` como `@Singleton`.

### `di/NetworkModule.java` (nuevo)

`@Module @InstallIn(SingletonComponent.class)` — provee `Retrofit` con `BASE_URL = BuildConfig.BASE_URL` (`http://10.0.2.2:8080/` en debug, mapeado en `app/build.gradle.kts`) y `AuthService` como `@Singleton`. Conversor: `GsonConverterFactory` (Gson ya es dependencia del proyecto).

### `di/RepositoryModule.java` (modificar)

Añadir `@Binds @Singleton` para `AuthRepository → AuthRepositoryImpl`.

---

## 5. `GoogleAuthHelper`

**Archivo:** `app/src/main/java/com/conectatec/ui/auth/GoogleAuthHelper.java`

Encapsula la interacción con Credential Manager. `LoginActivity` llama `obtenerCredencial()` desde un background thread.

```java
public class GoogleAuthHelper {

    /** Resultado de la autenticación con Google. */
    public static class GoogleCredencial {
        public final String idToken;
        public final String email;
        public GoogleCredencial(String idToken, String email) { ... }
    }

    public GoogleAuthHelper(Context context, String webClientId) { ... }

    /**
     * Lanza el selector nativo de cuentas de Google y retorna el idToken + email.
     * DEBE llamarse desde un background thread (bloquea).
     *
     * @throws GetCredentialCancellationException si el usuario cancela
     * @throws GetCredentialException para cualquier otro error de Credential Manager
     */
    public GoogleCredencial obtenerCredencial(Activity activity) throws GetCredentialException {
        GetGoogleIdOption option = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(option)
                .build();

        // Bloqueante — llamar desde executor
        GetCredentialResponse response = credentialManager.getCredential(activity, request);
        Credential credential = response.getCredential();
        GoogleIdTokenCredential googleCred = GoogleIdTokenCredential.createFrom(credential.getData());
        return new GoogleCredencial(googleCred.getIdToken(), googleCred.getId());
    }
}
```

---

## 6. `LoginViewModel`

**Archivo:** `app/src/main/java/com/conectatec/ui/auth/LoginViewModel.java`

`@HiltViewModel`. Inyecta `AuthRepository` y `SessionManager`.

```java
// LiveData expuesto:
public LiveData<UiState<String>> getState()  // String = rol

public void loginConGoogle(String idToken, String email) {
    // 1. Validar dominio institucional (cliente-side, early rejection)
    if (!email.endsWith(AuthConstants.DOMINIO_INSTITUCIONAL)) {
        state.setValue(new UiState.Error<>("Solo se permiten cuentas " + AuthConstants.DOMINIO_INSTITUCIONAL));
        return;
    }
    // 2. Llamar backend en background
    state.setValue(UiState.Loading.get());
    executor.execute(() -> {
        try {
            LoginResponse resp = authRepository.loginConGoogle(idToken);
            sessionManager.guardarSesion(resp.jwt, resp.rol, resp.nombre);
            state.postValue(new UiState.Success<>(resp.rol));
        } catch (Exception e) {
            state.postValue(new UiState.Error<>(e.getMessage()));
        }
    });
}
```

---

## 7. `LoginActivity` — cambios

### Layout (`activity_login.xml`)

Añadir debajo del bloque correo/contraseña, antes de los botones de rol DEMO:

1. Divisor visual: `TextView` con texto `"— o —"`, color `@color/colorOnSurfaceVariant`
2. Botón Google: `MaterialButton` con id `btnLoginGoogle`, texto `"Continuar con Google"`, icono `@drawable/ic_google` (nuevo drawable), fondo y borde consistentes con el tema oscuro

Los botones DEMO (`btnLogin`, `btnLoginDocente`) se mantienen visualmente como están — son provisionales y se eliminarán cuando el login real esté completo.

### `LoginActivity.java`

```java
// Campos de clase:
private GoogleAuthHelper googleHelper;
private LoginViewModel viewModel;
private final ExecutorService executor = Executors.newSingleThreadExecutor();

// En onCreate:
googleHelper = new GoogleAuthHelper(this, getString(R.string.google_web_client_id));
viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
observeViewModel();

// Listener:
binding.btnLoginGoogle.setOnClickListener(v -> {
    binding.btnLoginGoogle.setEnabled(false);
    executor.execute(() -> {
        try {
            GoogleAuthHelper.GoogleCredencial cred = googleHelper.obtenerCredencial(this);
            runOnUiThread(() -> viewModel.loginConGoogle(cred.idToken, cred.email));
        } catch (GetCredentialCancellationException e) {
            runOnUiThread(() -> binding.btnLoginGoogle.setEnabled(true));
        } catch (GetCredentialException e) {
            runOnUiThread(() -> {
                Snackbar.make(binding.getRoot(), "Error al iniciar sesión con Google", Snackbar.LENGTH_LONG).show();
                binding.btnLoginGoogle.setEnabled(true);
            });
        }
    });
});

// Observer:
private void observeViewModel() {
    viewModel.getState().observe(this, state -> {
        binding.progressBarLogin.setVisibility(state instanceof UiState.Loading ? View.VISIBLE : View.GONE);
        binding.btnLoginGoogle.setEnabled(!(state instanceof UiState.Loading));

        if (state instanceof UiState.Success) {
            String rol = ((UiState.Success<String>) state).data;
            rutearPorRol(rol);
        } else if (state instanceof UiState.Error) {
            Snackbar.make(binding.getRoot(), ((UiState.Error<?>) state).mensaje, Snackbar.LENGTH_LONG).show();
        }
    });
}

private void rutearPorRol(String rol) {
    Intent intent;
    switch (rol) {
        case "ADMIN":     intent = new Intent(this, MainAdminActivity.class);     break;
        case "DOCENTE":   intent = new Intent(this, MainDocenteActivity.class);   break;
        case "PENDIENTE": intent = new Intent(this, WaitingApprovalActivity.class); break;
        default:          // ESTUDIANTE — TODO: módulo estudiante
                          return;
    }
    startActivity(intent);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    finish();
}
```

---

## 8. Drawable nuevo

**`res/drawable/ic_google.xml`** — ícono vectorial del logo "G" de Google en sus colores estándar (rojo `#EA4335`, azul `#4285F4`, amarillo `#FBBC05`, verde `#34A853`). El botón `btnLoginGoogle` tiene fondo `@color/colorSurface` (oscuro) con borde `@color/colorBorder`, para que el logo multicolor contraste correctamente.

---

## Archivos modificados / creados

| Acción | Archivo |
|---|---|
| Modificar | `gradle/libs.versions.toml` |
| Modificar | `app/build.gradle.kts` |
| Crear | `ui/auth/AuthConstants.java` |
| Crear | `ui/auth/GoogleAuthHelper.java` |
| Crear | `ui/auth/LoginViewModel.java` |
| Crear | `data/model/LoginResponse.java` |
| Crear | `data/network/AuthService.java` |
| Crear | `data/repository/AuthRepository.java` |
| Crear | `data/repository/AuthRepositoryImpl.java` |
| Crear | `data/SessionManager.java` |
| Crear | `di/AppModule.java` |
| Crear | `di/NetworkModule.java` |
| Modificar | `di/RepositoryModule.java` |
| Modificar | `ui/auth/LoginActivity.java` |
| Modificar | `res/layout/activity_login.xml` |
| Modificar | `res/values/strings.xml` |
| Crear | `res/drawable/ic_google.xml` |

---

## Fuera de alcance

- Logout (cerrar sesión): `SessionManager.cerrarSesion()` está disponible pero no se conecta a UI en este sprint
- Módulo Estudiante: el caso `default` del switch de rol deja un return vacío con comentario TODO
- `SplashActivity`: verificar token existente al arranque (requiere `SessionManager` ya implementado — se puede hacer en un sprint posterior)
- Retrofit genérico para otros repositorios: `NetworkModule` provee solo `AuthService` por ahora
