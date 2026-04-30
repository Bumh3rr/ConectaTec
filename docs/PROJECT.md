# ConectaTec / TecConnect — Documentación del Proyecto

## 1. Resumen general

**ConectaTec** (nombre comercial **TecConnect**) es una aplicación Android nativa orientada a una **plataforma académica** que conecta tres roles dentro de un Tec: **Docente**, **Estudiante** y **Administrador**, más un estado intermedio **Pendiente** mientras la cuenta espera ser aprobada.

La aplicación está pensada para gestionar:

- Autenticación vía **JWT** contra un backend Spring Boot.
- Registro multi-paso con foto de perfil verificada (probablemente vía Google Cloud Vision en backend).
- Gestión de **grupos**, **tareas**, **calificaciones**, **mensajes** y **usuarios** (roles inferidos por strings y colores ya definidos).
- Escaneo de **códigos QR** para unirse a grupos.
- Notificaciones push.

Estado actual del repositorio: rama `feat/implement-vistas-auth`. Está implementado el **flujo de autenticación / registro** (UI + navegación + ViewModel del flujo de registro). El resto (tareas, grupos, mensajes, perfil, admin) aún no tiene clases Java pero sí cuenta con strings, colores y estilos preparados.

---

## 2. Tecnologías base

| Capa | Tecnología |
|---|---|
| Lenguaje principal | **Java** (con interoperabilidad con Kotlin a nivel de dependencias) |
| Plataforma | **Android nativo** |
| `compileSdk` / `targetSdk` | **36** |
| `minSdk` | **24** |
| `applicationId` | `com.conectatec` |
| `namespace` | `com.conectatec` |
| Java target | **Java 11** (`sourceCompatibility` y `targetCompatibility`) |
| Build system | **Gradle Kotlin DSL** (`build.gradle.kts`) + **Version Catalog** (`libs.versions.toml`) |
| Android Gradle Plugin | `9.0.1` |
| Theme | **Material 3 Dark** (`Theme.Material3.Dark.NoActionBar`) |
| View system | **XML + ViewBinding** (`buildFeatures.viewBinding = true`) |
| `BuildConfig` | habilitado, expone `BASE_URL` por buildType |

### BuildTypes

- **debug**: `BASE_URL = http://10.0.2.2:8080/` (localhost desde el emulador), `isDebuggable = true`.
- **release**: `BASE_URL = https://tu-servidor.com/` (placeholder), ProGuard listo pero `isMinifyEnabled = false`.

---

## 3. Dependencias declaradas

Definidas en `gradle/libs.versions.toml` y consumidas vía alias en `app/build.gradle.kts`.

### 3.1 AndroidX Core / UI
- `androidx.appcompat:appcompat` — `1.7.1`
- `androidx.activity:activity` — `1.10.1`
- `androidx.fragment:fragment` — `1.8.6`
- `androidx.constraintlayout:constraintlayout` — `2.2.1`
- `androidx.recyclerview:recyclerview` — `1.3.2`
- `androidx.viewpager2:viewpager2` — `1.1.0`
- `androidx.swiperefreshlayout:swiperefreshlayout` — `1.1.0`

### 3.2 Material Design
- `com.google.android.material:material` — `1.13.0` (Material 3)

### 3.3 Navigation Component
- `androidx.navigation:navigation-fragment` — `2.8.2`
- `androidx.navigation:navigation-ui` — `2.8.2`

### 3.4 Lifecycle (MVVM)
- `androidx.lifecycle:lifecycle-viewmodel` — `2.8.7`
- `androidx.lifecycle:lifecycle-livedata` — `2.8.7`
- `androidx.lifecycle:lifecycle-runtime` — `2.8.7`

### 3.5 Networking (consumo del backend Spring Boot)
- `com.squareup.retrofit2:retrofit` — `2.11.0`
- `com.squareup.retrofit2:converter-gson` — `2.11.0`
- `com.squareup.okhttp3:okhttp` — `4.12.0`
- `com.squareup.okhttp3:logging-interceptor` — `4.12.0`
- `com.google.code.gson:gson` — `2.11.0`

### 3.6 Inyección de dependencias
- `com.google.dagger:hilt-android` — `2.59`
- `com.google.dagger:hilt-android-compiler` — `2.59` (vía `annotationProcessor`)
- Plugin Gradle: `com.google.dagger.hilt.android`

### 3.7 Concurrencia
- `org.jetbrains.kotlinx:kotlinx-coroutines-android` — `1.8.1`
- `org.jetbrains.kotlinx:kotlinx-coroutines-core` — `1.8.1`

### 3.8 Imágenes
- `com.github.bumptech.glide:glide` — `4.16.0`
- `com.github.bumptech.glide:compiler` — `4.16.0` (vía `annotationProcessor`)
- `de.hdodenhof:circleimageview` — `3.1.0`

### 3.9 Almacenamiento seguro / preferencias
- `androidx.datastore:datastore-preferences` — `1.1.1` (declarado para guardar el JWT)
- `androidx.security:security-crypto` — `1.1.0-alpha06` (declarado en catalog como alternativa, aún no consumido)

### 3.10 Cámara y QR
- `androidx.camera:camera-core` — `1.4.2`
- `androidx.camera:camera-camera2` — `1.4.2`
- `androidx.camera:camera-lifecycle` — `1.4.2`
- `androidx.camera:camera-view` — `1.4.2`
- `com.google.mlkit:barcode-scanning` — `17.3.0`

### 3.11 Animaciones
- `com.airbnb.android:lottie` — `3.4.0` (usado en pantalla de éxito con `confeti.json`)

### 3.12 Testing
- `junit:junit` — `4.13.2`
- `androidx.test.ext:junit` — `1.3.0`
- `androidx.test.espresso:espresso-core` — `3.7.0`

---

## 4. Permisos Android (`AndroidManifest.xml`)

- `INTERNET`
- `ACCESS_NETWORK_STATE`
- `CAMERA` (foto de perfil + escaneo QR)
- `READ_EXTERNAL_STORAGE` (hasta SDK 32 – galería en versiones antiguas)
- `READ_MEDIA_IMAGES` (Android 13+)
- `POST_NOTIFICATIONS` (Android 13+)
- Feature requerida: `android.hardware.camera`

---

## 5. Arquitectura

Patrón aplicado: **MVVM** + **Single-Activity por flujo** + **Navigation Component** + **Hilt**.

### 5.1 Application
- `com.conectatec.TecConnectApp` — anotada con `@HiltAndroidApp`. Punto de entrada del grafo de inyección.

### 5.2 Estructura de paquetes (estado actual)

```
com.conectatec
├── TecConnectApp.java
└── ui
    ├── splash
    │   └── SplashActivity.java
    └── auth
        ├── LoginActivity.java
        ├── RegistroActivity.java
        ├── WaitingApprovalActivity.java
        └── registro
            ├── RegistroFormFragment.java
            ├── RegistroFotoFragment.java
            ├── RegistroVerificandoFragment.java
            ├── RegistroExitosoFragment.java
            ├── RegistroFallidoFragment.java
            └── RegistroViewModel.java
```

> Nota: aún no existen paquetes `data/`, `domain/`, `di/` ni clases de Retrofit, repositorios o módulos Hilt — están planeados pero no implementados (varios `TODO` lo indican).

---

## 6. Flujo de pantallas implementado

### 6.1 SplashActivity
- Layout: `activity_splash.xml`
- Muestra splash 2 s y redirige a `LoginActivity`.
- Anotada `@AndroidEntryPoint`.
- TODO declarado: validar `SessionManager` (JWT) y enrutar al Main del rol o a `WaitingApprovalActivity` si rol = `PENDIENTE`.

### 6.2 LoginActivity
- Layout: `activity_login.xml`
- Inputs: `etCorreo`, `etContrasena`.
- Maneja insets de IME / system bars.
- Botón **Iniciar sesión** (`btnLogin`) con `TODO: viewModel.login(correo, contrasena)` (aún no conectado).
- Link `tvIrRegistro` → `RegistroActivity`.

### 6.3 RegistroActivity (host del flujo de registro)
- Layout: `activity_registro.xml`
- Contiene un `NavHostFragment` (`navHostRegistro`) con grafo `nav_registro.xml`.
- Anotada `@AndroidEntryPoint`.
- Expone `irAEspera()` para que el fragment de éxito navegue a `WaitingApprovalActivity`.

### 6.4 Flujo de registro (multi-paso, fragments + ViewModel compartido)

Grafo en `res/navigation/nav_registro.xml`:

| ID | Fragmento | Rol |
|---|---|---|
| `fragmentRegistroForm` (start) | `RegistroFormFragment` | Paso 1 — datos personales |
| `fragmentRegistroFoto` | `RegistroFotoFragment` | Paso 2 — foto de perfil |
| `fragmentRegistroVerificando` | `RegistroVerificandoFragment` | Paso 3 — loading |
| `fragmentRegistroExitoso` | `RegistroExitosoFragment` | Paso 4a — éxito |
| `fragmentRegistroFallido` | `RegistroFallidoFragment` | Paso 4b — fallo |

Acciones:
- `action_form_to_foto` (slide right)
- `action_foto_to_verificando` (slide right)
- `action_verificando_to_exitoso` (fade + popUpTo form inclusivo)
- `action_verificando_to_fallido` (fade)
- `action_fallido_to_form` (slide left + popUpTo form inclusivo)

Animaciones declaradas en `res/anim/`: `fade_in`, `fade_out`, `slide_in_left`, `slide_in_right`, `slide_out_left`, `slide_out_right`.

#### 6.4.1 RegistroFormFragment
- Layout: `fragment_registro_form.xml`
- Campos: `etNombreCompleto`, `etCorreo`, `etContrasena`, `etConfirmarContrasena` (envueltos en `TextInputLayout`).
- Validación local implementada (`validarCampos()`): nombre obligatorio, correo válido (`Patterns.EMAIL_ADDRESS`), contraseña ≥ 8 chars, confirmación que coincida.
- Actualmente la validación está comentada y el botón siempre navega al paso 2.
- `tvIrLogin` → `requireActivity().finish()` para volver al login.

#### 6.4.2 RegistroFotoFragment
- Layout: `fragment_registro_foto.xml`
- Soporta dos `ActivityResultLauncher`:
  - `GetContent("image/*")` — galería.
  - `TakePicture()` — cámara, con `Uri` generada por `FileProvider` (autoridad `${packageName}.fileprovider`, ver `xml/file_paths.xml`).
- Vista previa con `CircleImageView` (`ivFotoPreview`).
- Persiste la URI en `RegistroViewModel.setFotoUri(...)` antes de navegar.
- Habilita el botón **Continuar** una vez seleccionada la foto.

#### 6.4.3 RegistroVerificandoFragment
- Layout: `fragment_registro_verificando.xml` con tres pasos animados (`progressPaso1/2/3` + `tvPaso1/2/3`).
- Provisional: tras 1 s navega a `RegistroExitosoFragment`.
- TODO declarado: observar `LiveData` del ViewModel cuando se conecte al backend (subir foto, llamar a Cloud Vision, decidir éxito/fallo).

#### 6.4.4 RegistroExitosoFragment
- Layout: `fragment_registro_exitoso.xml`
- Muestra animación Lottie `R.raw.confeti` con `LottieAnimationView`, además de un `TranslateAnimation` de Android.
- Imprime el resumen `viewModel.getNombre()` y `viewModel.getCorreo()`.
- Botón Continuar invoca `((RegistroActivity) requireActivity()).irAEspera()`.

#### 6.4.5 RegistroFallidoFragment
- Layout: `fragment_registro_fallido.xml`
- Constantes con motivos posibles:
  - `ERROR_CORREO_DUPLICADO`
  - `ERROR_FOTO_INVALIDA`
  - `ERROR_RED`
  - `ERROR_GENERICO`
- Acciones: **Intentar de nuevo** (vuelve al paso 1) y **Volver al login** (cierra la `RegistroActivity`).

#### 6.4.6 RegistroViewModel
- Extiende `androidx.lifecycle.ViewModel` (estado en memoria, sin LiveData aún).
- Estado expuesto:
  - `String nombre`
  - `String correo`
  - `String contrasena`
  - `Uri fotoUri`
- Compartido entre fragments con `new ViewModelProvider(requireActivity()).get(RegistroViewModel.class)`.

### 6.5 WaitingApprovalActivity
- Layout: `activity_waiting_approval.xml`
- Muestra pantalla intermedia cuando el usuario ya tiene cuenta pero rol = `PENDIENTE`.
- Renderiza `tvNombre` y `tvCorreo` (TODO: leer de `SessionManager`).
- Botón **Cerrar sesión** vuelve a `LoginActivity` con flags `FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK`.
- Anotada `@AndroidEntryPoint`.

---

## 7. Recursos UI

### 7.1 Layouts (`res/layout/`)
- `activity_splash.xml`
- `activity_login.xml`
- `activity_registro.xml`
- `activity_waiting_approval.xml`
- `fragment_registro_form.xml`
- `fragment_registro_foto.xml`
- `fragment_registro_verificando.xml`
- `fragment_registro_exitoso.xml`
- `fragment_registro_fallido.xml`

### 7.2 Drawables
Backgrounds custom (XML): `bg_message_sent`, `bg_message_received`, `bg_upload_zone`, `bg_avatar`, `bg_circle_success`, `bg_circle_error`, `bg_input`, `bg_dot_error`, `bg_badge_unread`, `bg_card`, `bg_button_primary`, `bg_chip_pendiente`. Iconos: `ic_info`, `ic_launcher_*`. Color selector para bottom nav: `color/selector_bottom_nav.xml`.

### 7.3 Animaciones
`anim/fade_in`, `fade_out`, `slide_in_left`, `slide_in_right`, `slide_out_left`, `slide_out_right`.

### 7.4 Recursos raw / xml
- `raw/confeti.json` — animación Lottie usada en éxito.
- `xml/file_paths.xml` — autoridades de FileProvider (foto de perfil).
- `xml/backup_rules.xml`, `xml/data_extraction_rules.xml` — reglas de respaldo de Android 12+.

### 7.5 Estilos (`values/themes.xml`)
- `Theme.TecConnect` (Material3 Dark) — colores primarios, surface, error y system bars.
- `Theme.TecConnect.Splash` — splash sin parpadeo blanco.
- `Widget.TecConnect.Toolbar`
- `Widget.TecConnect.Button` (corner radius 8dp)
- `Widget.TecConnect.Button.Outlined`
- `Widget.TecConnect.Card` (corner radius 12dp, elevación 0)
- `Widget.TecConnect.TextInputLayout` (Outlined, surface variant)
- `Widget.TecConnect.FAB`

### 7.6 Paleta (`values/colors.xml`)
- Primario: `#1976D2` / dark `#1565C0` / light `#42A5F5`
- Fondo: `#262625` / Surface: `#1E1E1E` / SurfaceVariant: `#2C2C2C`
- Texto: blanco / `#AAAAAA`
- Estado: error `#CF6679`, success `#4CAF50`, warning `#F57C00`
- Chips por rol: Docente `#1976D2`, Estudiante `#388E3C`, Pendiente `#F57C00`, Admin `#7B1FA2`
- Variant night en `values-night/themes.xml`

---

## 8. Strings y dominio inferido

A partir de `res/values/strings.xml` se infiere el alcance funcional (no implementado en código aún):

- **Roles**: Docente, Estudiante, Administrador, Pendiente.
- **Tareas**: publicar, entregar, calificar; estados (pendiente, vence hoy, entregada, calificada, sin entregar).
- **Grupos**: crear, unirse (vía QR), administración, lista de alumnos.
- **Mensajes**: chat enviado/recibido, badge de no leídos, mensaje eliminado.
- **Perfil**: foto, cambiar foto, cerrar sesión.
- **Administración**: gestión de usuarios y grupos, filtros por rol.
- **Verificación de foto**: rechazo/aprobación (mensajes ya redactados).

---

## 9. Convenciones detectadas

- Todas las Activities anotadas con `@AndroidEntryPoint` (Hilt listo).
- ViewBinding en cada Activity/Fragment con liberación en `onDestroy` / `onDestroyView`.
- ViewModel compartido a nivel Activity para el flujo de registro.
- Navegación entre activities con `overridePendingTransition(android.R.anim.fade_in, fade_out)`.
- Logs de comentarios en español, con `TODO:` marcando las integraciones pendientes (SessionManager, ViewModel del login, llamadas al backend, Cloud Vision).
- Tema oscuro forzado (`Theme.Material3.Dark.NoActionBar`).
- Configuración de `LocalProperties`, `gradle.properties`: `android.useAndroidX=true`, `android.nonTransitiveRClass=true`.

---

## 10. Backend esperado (inferido del código)

- API REST en `http://10.0.2.2:8080/` (debug) — Spring Boot.
- Autenticación con **JWT** persistido en **DataStore Preferences** (planeado, aún no implementado).
- Endpoint(s) de registro que recibirían: nombre, correo, contraseña y foto, devolviendo:
  - éxito → token + rol (`PENDIENTE` al inicio).
  - fallo → motivo (correo duplicado, foto inválida, error de red, genérico).
- Verificación de rostro probablemente vía **Google Cloud Vision** del lado del servidor (referenciado en comentarios del fragmento de verificación).

---

## 11. Estado del trabajo y siguientes pasos lógicos

Implementado:
- Flujo completo de **UI** del registro (form → foto → verificando → éxito/fallo).
- UI de **login** y **splash**.
- UI de **espera de aprobación**.
- Navigation graph `nav_registro` con animaciones.
- Tema Material 3 oscuro y catálogo de strings/colores extenso.
- Hilt cableado a nivel `Application`.

Pendiente (TODOs presentes en el código):
- `SessionManager` con DataStore para guardar/leer JWT y rol.
- `LoginViewModel` y conexión con Retrofit.
- Capa de red: cliente Retrofit + OkHttp logging + módulos Hilt (`@Module`/`@InstallIn`).
- Subida de foto y observación de `LiveData` en `RegistroVerificandoFragment`.
- `WaitingApprovalActivity`: cargar nombre/correo reales desde sesión.
- Pantallas de Tareas, Grupos, Mensajes, Perfil y Admin (strings ya redactados).
- Escáner QR con CameraX + ML Kit (deps ya añadidas).
