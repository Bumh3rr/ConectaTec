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
    ├── auth
    │   ├── LoginActivity.java
    │   ├── RegistroActivity.java
    │   ├── WaitingApprovalActivity.java
    │   └── registro
    │       ├── RegistroFormFragment.java
    │       ├── RegistroFotoFragment.java
    │       ├── RegistroVerificandoFragment.java
    │       ├── RegistroExitosoFragment.java
    │       ├── RegistroFallidoFragment.java
    │       └── RegistroViewModel.java
    └── admin
        ├── MainAdminActivity.java
        ├── dashboard
        │   └── AdminDashboardFragment.java
        ├── usuarios
        │   ├── AdminUsuariosFragment.java
        │   ├── AdminPendientesFragment.java
        │   ├── AdminUsuarioDetalleFragment.java
        │   ├── AsignarRolBottomSheet.java
        │   └── adapter
        │       ├── UsuarioAdminAdapter.java
        │       └── UsuarioPendienteAdapter.java
        ├── grupos
        │   ├── AdminGruposFragment.java
        │   ├── AdminGrupoDetalleFragment.java
        │   ├── AdminMiembrosGrupoFragment.java
        │   └── adapter
        │       ├── GrupoAdminAdapter.java
        │       └── MiembrosGrupoAdapter.java
        ├── actividades
        │   ├── AdminActividadesFragment.java
        │   ├── AdminActividadDetalleFragment.java
        │   └── adapter
        │       └── ActividadAdminAdapter.java
        ├── alumno
        │   └── AdminAlumnoDetalleFragment.java
        └── perfil
            └── AdminPerfilFragment.java
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

**Auth / Splash**
- `activity_splash.xml`
- `activity_login.xml`
- `activity_registro.xml`
- `activity_waiting_approval.xml`
- `fragment_registro_form.xml`
- `fragment_registro_foto.xml`
- `fragment_registro_verificando.xml`
- `fragment_registro_exitoso.xml`
- `fragment_registro_fallido.xml`

**Admin**
- `fragment_admin_dashboard.xml`
- `fragment_admin_usuarios.xml`
- `fragment_admin_usuario_detalle.xml`
- `fragment_admin_grupos.xml`
- `fragment_admin_grupo_detalle.xml`
- `fragment_admin_miembros_grupo.xml`
- `fragment_admin_actividades.xml`
- `fragment_admin_actividad_detalle.xml`
- `fragment_admin_perfil.xml`
- `fragment_alumno_detalle.xml`

**Items (RecyclerView)**
- `item_usuario_admin.xml`
- `item_usuario_pendiente.xml`
- `item_grupo_admin.xml`
- `item_miembro_grupo.xml`
- `item_actividad_admin.xml`
- `item_dashboard_card.xml`

**Compartidos**
- `layout_empty_state.xml`

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

## 11. Módulo Admin — Vistas y atributos

### 11.1 Actividad contenedora — `MainAdminActivity`

- Layout: `activity_main_admin.xml`
- Contiene un `NavHostFragment` con grafo `nav_admin.xml`.
- Implementa una **pill nav bar** personalizada (no usa `BottomNavigationView` estándar).
- Usa `addOnDestinationChangedListener` para sincronizar la pestaña activa.
- La navegación entre pestañas usa `NavOptions` con `setLaunchSingleTop(true)`, `setRestoreState(true)` y `setPopUpTo(startDest, false, true)` — evita duplicar destinos en el back stack.
- Pestañas raíz declaradas en `DESTINATIONS[]`: `adminDashboardFragment`, `adminUsuariosFragment`, `adminGruposFragment`, `adminActividadesFragment`, `adminPerfilFragment`.
- Sub-destinos (`adminUsuarioDetalleFragment`, `adminGrupoDetalleFragment`, `adminMiembrosGrupoFragment`, `adminActividadDetalleFragment`, `adminAlumnoDetalleFragment`) no cambian la pestaña activa.

### 11.2 Grafo de navegación — `nav_admin.xml`

| ID destino | Fragmento | Tipo |
|---|---|---|
| `adminDashboardFragment` *(start)* | `AdminDashboardFragment` | Pestaña raíz |
| `adminUsuariosFragment` | `AdminUsuariosFragment` | Pestaña raíz |
| `adminUsuarioDetalleFragment` | `AdminUsuarioDetalleFragment` | Sub-destino |
| `adminGruposFragment` | `AdminGruposFragment` | Pestaña raíz |
| `adminGrupoDetalleFragment` | `AdminGrupoDetalleFragment` | Sub-destino |
| `adminMiembrosGrupoFragment` | `AdminMiembrosGrupoFragment` | Sub-destino |
| `adminAlumnoDetalleFragment` | `AdminAlumnoDetalleFragment` | Sub-destino |
| `adminActividadesFragment` | `AdminActividadesFragment` | Pestaña raíz |
| `adminActividadDetalleFragment` | `AdminActividadDetalleFragment` | Sub-destino |
| `adminPerfilFragment` | `AdminPerfilFragment` | Pestaña raíz |

**Acciones declaradas:**

| Acción | Origen → Destino | Args pasados |
|---|---|---|
| `action_usuarios_to_detalle` | Usuarios → UsuarioDetalle | `usuarioId: int` |
| `action_usuario_detalle_to_grupos`*(eliminada)* | *(reemplazada por tab NavOptions)* | — |
| `action_grupos_to_detalle` | Grupos → GrupoDetalle | `grupoId: int` |
| `action_grupo_detalle_to_miembros` | GrupoDetalle → Miembros | `grupoId: int`, `actividadId: int` |
| `action_actividades_to_detalle` | Actividades → ActividadDetalle | `actividadId: int` |
| `action_actividad_detalle_to_miembros` | ActividadDetalle → Miembros | `grupoId: int`, `actividadId: int` |
| `action_miembros_to_alumno_detalle` | Miembros → AlumnoDetalle | `usuarioId: int` |

Todas las acciones usan animaciones `slide_in_right / slide_out_left` (avance) y `slide_in_left / slide_out_right` (retroceso).

---

### 11.3 Dashboard — `AdminDashboardFragment`

- Layout: `fragment_admin_dashboard.xml`
- **Header**: label `@string/app_name` (azul), saludo "Bienvenido Pedro", título "Panel de control", avatar 48dp con iniciales "LR" (`bg_chip_admin`).
- **Sección RESUMEN** (fecha "Hoy, 29 abr"): 4 tarjetas `MaterialCardView` en grid 2×2.

| ID | Contenido | Destino al pulsar |
|---|---|---|
| `cardUsuarios` | `tvTotalUsuarios` "24" + "↑ +2 este mes" | `adminUsuariosFragment` |
| `cardPendientes` | `tvTotalPendientes` "3" en warning + "Requieren acción" | `adminUsuariosFragment` |
| `cardGrupos` | `tvTotalGrupos` "8" + "↑ +1 este mes" | `adminGruposFragment` |
| `cardTareas` | `tvTotalTareas` "41" + "↑ +5 esta semana" | `adminActividadesFragment` |

- **Sección TRABAJOS Y TAREAS RECIENTES**: 3 ítems dentro de una `MaterialCardView`.
  - Ítem 1 — `TRABAJO`: "Proyecto Final de Software" · Ing. de Software 6A · vence 05/05/2025 · hace 1h (`bg_chip_docente`)
  - Ítem 2 — `TAREA`: "Tarea 3: Normalización BD" · Bases de Datos 4B · vence 02/05/2025 · hace 4h (`bg_chip_estudiante`)
  - Ítem 3 — `EXAMEN`: "Examen Parcial 2" · Cálculo Integral 2A · vence 30/04/2025 · hace 1d (`bg_chip_admin`)
- `tvVerTodoActividad` navega a `adminActividadesFragment` con tab NavOptions.

---

### 11.4 Usuarios — `AdminUsuariosFragment`

- Layout: `fragment_admin_usuarios.xml`
- **Header**: título "Usuarios", subtítulo "Gestión de accesos y roles", badge `tvHeaderTotalUsuarios`.
- **Búsqueda**: `tilBuscarUsuario` / `etBuscarUsuario` (TextInputLayout OutlinedBox).
- **Chips de filtro de rol** (HorizontalScrollView):
  - `chipTodos` (checked por defecto), `chipDocentes`, `chipEstudiantes`, `chipPendientes`
- **TabLayout** (`tabUsuarios`) con 2 pestañas: "Activos" / "Pendientes" → `ViewPager2` (`vpUsuarios`).
  - Pestaña Activos → `AdminUsuariosFragment` (tab host)
  - Pestaña Pendientes → `AdminPendientesFragment`
- `rvUsuarios` / `rvPendientes` — RecyclerViews con `LinearLayoutManager`.
- Estado vacío: `include layout_empty_state`.
- Adapter: `UsuarioAdminAdapter` con modelo `UsuarioDummy` (id, nombre, iniciales, correo, rol, activo, fechaRegistro).
  - Chips de rol coloreados: DOCENTE→`bg_chip_docente`, ESTUDIANTE→`bg_chip_estudiante`, ADMIN→`bg_chip_admin`, PENDIENTE→`bg_chip_pendiente`.
  - Navegación al detalle: `action_usuarios_to_detalle` con `usuarioId`.

---

### 11.5 Detalle de usuario — `AdminUsuarioDetalleFragment`

- Layout: `fragment_admin_usuario_detalle.xml`
- **Header con back**: `btnVolver` (ImageView, `ic_arrow_left`) + label `@string/app_name` + título "Detalle de usuario". Línea de acento 3dp primary.
- **Hero**: avatar 88dp (`bg_avatar_placeholder`) + `tvInicialesDetalle` centrado ("CB") + `ivAvatarDetalle` (`CircleImageView`, hidden si no hay foto).
- `tvNombreDetalle` (22sp bold), `tvCorreoDetalle` (14sp muted), `tvChipRolDetalle` (chip coloreado por rol).
- **Card INFORMACIÓN DE CUENTA**:
  - `tvFechaRegistro`, `tvEstadoCuenta` (coloreado: verde=activa, rojo=desactivada), `tvFotoVerificada`.
- **Card GRUPOS**:
  - `containerGrupos` (LinearLayout dinámico con TextViews por cada grupo).
  - `tvSinGrupos` (visible si no hay grupos).
  - `tvVerTodosGrupos` — navega a `adminGruposFragment` con tab NavOptions pasando `docenteNombre` como argumento de Bundle (activa filtro automático en Grupos).
- **Botones de acción**:
  - `btnCambiarRol` → abre `AsignarRolBottomSheet` (BottomSheetDialogFragment con chips de rol).
  - `btnToggleActivo` → alterna estado cuenta (texto y color cambian: warning=desactivar, success=activar).
  - `btnEliminarUsuario` → `MaterialAlertDialogBuilder` de confirmación.
- Datos: actualmente hardcodeados (dummy: "Carlos Bautista", DOCENTE).

---

### 11.6 Grupos — `AdminGruposFragment`

- Layout: `fragment_admin_grupos.xml`
- **Header**: título "Grupos", subtítulo "Grupos académicos activos", badge `tvHeaderTotalGrupos` (se actualiza con el conteo filtrado).
- **Búsqueda**: `tilBuscarGrupo` / `etBuscarGrupo`.
- **Chips de filtro de estado** (HorizontalScrollView):
  - `chipGruposTodos` (checked), `chipGruposActivos`, `chipGruposDesactivados`.
- **Banner de filtro por docente** (`bannerFiltroUsuario`, `visibility=gone`): aparece al navegar desde detalle de usuario. Contiene `chipFiltroDocente` con `app:closeIconEnabled="true"` para limpiar el filtro.
- `rvGrupos` + estado vacío `emptyStateGrupos`.
- **3 filtros combinados** en el Fragment: `docenteFiltro` (String|null), `activoFiltro` (Boolean|null), `queryFiltro` (String). Se aplican simultáneamente vía `adapter.setFiltros(docente, activo, query)`.
- Adapter: `GrupoAdminAdapter` con modelo `GrupoDummy` (id, nombre, materia, docente, miembros, fechaCreacion, activo).
  - Filtrado en `listaCompleta` con `setFiltros()` — combinación de los 3 criterios.
  - Badge activo/inactivo: ACTIVO→`bg_chip_estudiante`, INACTIVO→`bg_chip_pendiente`.
  - Item alpha 0.6 si inactivo.
  - Navegación al detalle: `action_grupos_to_detalle` con `grupoId`.

---

### 11.7 Detalle de grupo — `AdminGrupoDetalleFragment`

- Layout: `fragment_admin_grupo_detalle.xml`
- **Header con back**: `btnVolverGrupo` + label `@string/app_name` + "Detalle de grupo". Línea 3dp primary.
- **ScrollView** con:
  - Card **INFORMACIÓN DEL GRUPO**: `tvNombreGrupoDetalle`, `tvMateriaGrupoDetalle`, `tvDocenteGrupoDetalle`, `tvEstadoGrupoDetalle` (chip), `tvFechaCreacionDetalle`.
  - Card **MIEMBROS** (preview 3): `containerMiembrosDetalle` (LinearLayout dinámico) + `tvVerTodosMiembros` → navega vía `action_grupo_detalle_to_miembros` con `{grupoId, actividadId=0}`.
  - Card **ACTIVIDADES** (preview 3): `containerActividadesDetalle` + `tvVerTodasActividades`.
- Recibe argumento `grupoId: int` (defaultValue=0).
- Datos dummy con 6 grupos (IDs 1-6) mapeados en arrays estáticos.

---

### 11.8 Miembros del grupo — `AdminMiembrosGrupoFragment`

- Layout: `fragment_admin_miembros_grupo.xml`
- **Header con back**: `btnVolverMiembros` + label `@string/app_name` + "Miembros del grupo". Línea 3dp primary.
- `tvSubtituloMiembros` — nombre del grupo (dinámico según `grupoId`).
- `tvTotalMiembrosHeader` — badge con conteo.
- `rvMiembros` (paddingBottom=88dp) + `emptyStateMiembros`.
- Recibe args: `grupoId: int`, `actividadId: int`.
- Si `actividadId > 0` → muestra columna de estado de entrega por alumno (chip ENTREGADO/PENDIENTE).
- **Datos dummy**: array estático `MIEMBROS_POR_GRUPO[6][~8]` con IDs 1-8, 11-16, 21-26, 31-37, 41-46, 51-56. Array `ENTREGAS_POR_ACTIVIDAD[8]` = {18,22,8,0,15,12,30,0}.
- Adapter: `MiembrosGrupoAdapter` con modelo `MiembroDummy` (id, nombre, iniciales, correo, rol, `@Nullable Boolean entregado`).
  - `tvEstadoEntregaMiembro` visible solo si `entregado != null`.
  - Navegación al perfil de alumno: `action_miembros_to_alumno_detalle` con `usuarioId`.

**Item layout** `item_miembro_grupo.xml`:
| Vista | ID | Descripción |
|---|---|---|
| FrameLayout 44dp | — | Avatar con `bg_avatar_placeholder` |
| TextView | `tvInicialesMiembro` | Iniciales centradas (14sp bold) |
| TextView | `tvNombreMiembro` | Nombre (14sp bold) |
| TextView | `tvChipRolMiembro` | Chip de rol coloreado |
| TextView | `tvCorreoMiembro` | Correo (12sp muted) |
| TextView | `tvEstadoEntregaMiembro` | Estado entrega (visibility=gone por defecto) |

---

### 11.9 Perfil de alumno — `AdminAlumnoDetalleFragment`

- Layout: `fragment_alumno_detalle.xml`
- **Header con back**: `btnVolverAlumno` + label `@string/app_name` + "Perfil del alumno". Línea 3dp primary.
- **ScrollView** (paddingBottom=88dp) con:
  - **Hero centrado**: FrameLayout 96dp (`bg_avatar_placeholder`) + `tvInicialesAlumno` (32sp bold) + `CircleImageView ivFotoAlumno` (oculta). `tvNombreAlumno` (22sp bold), `tvCorreoAlumno` (muted), `tvCarreraAlumno` (muted), `tvChipRolAlumno` (`bg_chip_estudiante`).
  - Card **INFORMACIÓN ACADÉMICA**: `tvMatricula`, `tvSemestre`, `tvFechaInscripcion`, `tvEstadoCuentaAlumno` (coloreado).
  - Card **GRUPOS INSCRITOS**: `containerGruposAlumno` (LinearLayout dinámico).
  - Card **ENTREGAS RECIENTES**: `containerEntregasAlumno` (filas dinámicas: título+fecha a la izquierda, chip de estado a la derecha).
- Recibe argumento `usuarioId: int`.
- **36 entradas dummy** (`DATOS[]`) cubriendo todos los IDs de miembros posibles.
- Carreras: ISC, IRT, IS (constantes estáticas).
- Chips de estado de entrega: COMPLETADA→`bg_chip_estudiante`, VENCIDA→`bg_chip_admin`, PENDIENTE→`bg_chip_pendiente`, EN CURSO→`bg_chip_docente`.
- Vista de solo lectura — sin botones de acción admin.

---

### 11.10 Actividades — `AdminActividadesFragment`

- Layout: `fragment_admin_actividades.xml`
- **Header**: título `@string/title_admin_actividades`, subtítulo "Tareas académicas por grupo", badge `tvHeaderTotalActividades`.
- **Búsqueda**: `tilBuscarActividad` / `etBuscarActividad`.
- **Chips de filtro de estado** (5 chips):
  - `chipActividadesTodas` (checked), `chipActividadesEnCurso`, `chipActividadesPendiente`, `chipActividadesCompletada`, `chipActividadesVencida`.
- `rvActividades` + `emptyStateActividades`.
- Adapter: `ActividadAdminAdapter` con modelo `ActividadDummy` (id, titulo, tipo, grupo, docente, fechaPublicacion, fechaVence, estado, grupoId, totalAlumnos, entregadas).
  - Tipos: TRABAJO, TAREA, EXAMEN, PROYECTO.
  - Estados: EN CURSO, PENDIENTE, COMPLETADA, VENCIDA.
  - 8 registros dummy (actividadId 1-8).
  - Chip de estado coloreado según tipo/estado.
  - Navegación al detalle: `action_actividades_to_detalle` con `actividadId`.

**Item layout** `item_actividad_admin.xml`:
| Vista | ID | Descripción |
|---|---|---|
| TextView | `tvTipoActividad` | Badge tipo (TRABAJO/TAREA/etc.) |
| TextView | `tvTituloActividad` | Título (15sp bold) |
| TextView | `tvGrupoActividad` | Nombre del grupo (12sp muted) |
| TextView | `tvDocenteActividad` | Nombre del docente (12sp muted) |
| TextView | `tvFechaVenceActividad` | Fecha de vencimiento |
| TextView | `tvEstadoActividad` | Chip estado coloreado |
| TextView | `tvEntregasActividad` | "X/Y entregas" |

---

### 11.11 Detalle de actividad — `AdminActividadDetalleFragment`

- Layout: `fragment_admin_actividad_detalle.xml`
- **Header con back**: `btnVolverActividad` + label `@string/app_name` + "Detalle de actividad". Línea 3dp primary.
- **ScrollView** con 4 cards:

**Card INFORMACIÓN GENERAL:**
| Vista ID | Contenido |
|---|---|
| `tvTituloDetActiv` | Título de la actividad (20sp bold) |
| `tvChipEstadoDetActiv` | Chip estado (`bg_chip_docente` / `bg_chip_pendiente` / etc.) |
| `tvGrupoDetActiv` | Nombre del grupo (14sp muted) |
| `tvTipoDetActiv` | "Tipo: Proyecto" (12sp muted) |
| `tvDocenteDetActiv` | Nombre del docente (13sp bold) |
| `tvFechaPublicacion` | Fecha de publicación (13sp bold) |
| `tvFechaVenceDetActiv` | Fecha de vencimiento (13sp bold) |

**Card PROGRESO DE ENTREGAS:**
| Vista ID | Contenido |
|---|---|
| `progressEntregas` | `LinearProgressIndicator` (max=100, color success, track=divider) |
| `tvStatTotal` | Total de alumnos (26sp bold) |
| `tvStatEntregadas` | Cantidad entregadas (26sp, colorSuccess) |
| `tvStatPendientes` | Cantidad pendientes (26sp, colorWarning) |

**Card DESCRIPCIÓN:**
| Vista ID | Contenido |
|---|---|
| `tvDescripcionDetActiv` | Texto libre de la actividad (14sp, lineSpacing=4dp) |

**Card MIEMBROS:**
| Vista ID | Contenido |
|---|---|
| `tvVerTodosMiembrosDetActiv` | Link "Ver todos" → navega vía `action_actividad_detalle_to_miembros` con `{grupoId, actividadId}` |
| `containerMiembrosDetActiv` | LinearLayout dinámico con preview de miembros |

- Recibe argumento `actividadId: int` (defaultValue=0).
- 8 entradas dummy con `grupoId` asignado (1,2,3,4,5 según grupo).
- Inner class `DetalleActividad`: id, titulo, tipo, grupo, docente, fechaPublicacion, fechaVence, descripcion, estado, totalAlumnos, entregadas, grupoId.

---

### 11.12 Perfil del administrador — `AdminPerfilFragment`

- Layout: `fragment_admin_perfil.xml`
- **Header de marca**: barra de acento vertical 4dp (`bg_accent_bar`), label `@string/app_name` (10sp bold primary), título "Mi perfil", subtítulo con cargo.
- **Card hero de usuario**: avatar 88dp (`bg_avatar_placeholder`) + iniciales + nombre + correo + `tvChipRolPerfil` (`bg_chip_admin`).
- **Card INFORMACIÓN DE CUENTA**: matrícula/ID de empleado, fecha de registro, estado de cuenta.
- **Card CONFIGURACIÓN**: opciones de ajustes y preferencias.
- **Botón Cerrar sesión**: `btnCerrarSesion` → vuelve a `LoginActivity` con `FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK`.

---

### 11.13 Drawables añadidos para el módulo Admin

| Archivo | Descripción |
|---|---|
| `bg_accent_bar.xml` | Barra vertical 4dp con gradiente primary (decoración de headers) |
| `bg_chip_admin.xml` | Fondo oval morado `#7B1FA2` (rol Administrador) |
| `bg_chip_docente.xml` | Fondo oval azul `#1976D2` (rol Docente) |
| `bg_chip_estudiante.xml` | Fondo oval verde `#388E3C` (rol Estudiante) |
| `bg_chip_pendiente.xml` | Fondo oval naranja `#F57C00` (rol Pendiente) |
| `bg_avatar_placeholder.xml` | Oval gris oscuro `colorSurfaceVariant` (placeholder avatar) |
| `bg_icon_circle_active.xml` | Círculo activo para nav pill |
| `bg_icon_circle_inactive.xml` | Círculo inactivo para nav pill |
| `bg_nav_container.xml` | Contenedor pill nav bar |
| `bg_nav_item_active.xml` | Item activo de pill nav |
| `ic_home.xml`, `ic_users.xml`, `ic_school.xml`, `ic_markets.xml`, `ic_perfil.xml` | Íconos de la pill nav |
| `ic_scan_frame.xml` | Vector con 4 esquinas tipo escáner (registro foto) |
| `bg_scan_area.xml` | Fondo oscuro redondeado del área de escaneo |
| `bg_scan_line.xml` | Gradiente horizontal para línea animada de escaneo |

---

## 12. Estado del trabajo y siguientes pasos lógicos

Implementado:
- Flujo completo de **UI del registro** (form → foto → verificando → éxito/fallo) con step indicator en todas las vistas.
- Animación de escaneo en `RegistroFotoFragment` (scan line + corner pulse + flash de éxito).
- UI de **login** y **splash**.
- UI de **espera de aprobación**.
- Navigation graph `nav_registro` con animaciones.
- Módulo **Admin completo** (UI + navegación + datos dummy):
  - Dashboard con resumen y actividades recientes.
  - Usuarios con filtro por rol, TabLayout Activos/Pendientes, detalle con acciones.
  - Grupos con 3 filtros combinados (docente, estado, texto), filtro contextual desde detalle de usuario.
  - Detalle de grupo con preview de miembros y actividades.
  - Lista de miembros con estado de entrega por actividad.
  - Perfil de alumno (solo lectura).
  - Actividades con 5 filtros de estado.
  - Detalle de actividad con progreso de entregas.
  - Perfil del administrador.
- Pill nav bar custom con sincronización correcta para sub-destinos.
- Tema Material 3 oscuro y catálogo de strings/colores extenso.
- Hilt cableado a nivel `Application`.

Pendiente (TODOs presentes en el código):
- `SessionManager` con DataStore para guardar/leer JWT y rol.
- `LoginViewModel` y conexión con Retrofit.
- Capa de red: cliente Retrofit + OkHttp logging + módulos Hilt (`@Module`/`@InstallIn`).
- Subida de foto y observación de `LiveData` en `RegistroVerificandoFragment`.
- `WaitingApprovalActivity`: cargar nombre/correo reales desde sesión.
- Conectar datos reales del backend en todas las vistas admin (sustituir datos dummy).
- Pantallas Docente y Estudiante (pendientes de implementar).
- Escáner QR con CameraX + ML Kit (deps ya añadidas).
