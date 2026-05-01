# TecConnect — Módulo Docente · Spec de diseño

**Fecha:** 2026-04-30
**Rama:** `vista-docente`
**Autor:** Claude (Superpowers — brainstorming)
**Estado:** Aprobado

---

## 1. Contexto y alcance

TecConnect (paquete `com.conectatec`, app Android nativa Java 11, minSdk 24, Material 3 Dark) ya tiene implementado el módulo Administrador completo. Este spec define las vistas del rol **Docente** siguiendo **exactamente** los mismos patrones del Admin: pill nav personalizada (no `BottomNavigationView`), `ViewBinding`, Hilt (`@AndroidEntryPoint`), animaciones `slide_in_right/slide_out_left` reutilizadas, paleta sólo por referencia (`@color/*`), datos 100 % dummy estáticos en arrays internos de cada Fragment/Adapter.

**Alcance:** 5 tabs principales (Dashboard, Grupos, Tareas, Chat, Perfil) + 9 sub-pantallas de detalle. Cero llamadas reales al backend — solo `// TODO: ...` en español marcando puntos de integración.

**Fuera de alcance:**
- Repositorios, Retrofit, Room, ViewModel, LiveData (los datos son dummy estáticos por adapter).
- Pantalla de configuración (link en Perfil con TODO).
- Funcionalidad real de adjuntar archivo (solo UI placeholder).
- Generación real de QR (solo placeholder vectorial).

**Backend asumido:** existe Spring Boot 3 con servicios `GrupoService`, `TareaService`, `EntregaService`, `MensajeService`, `AvisoService`, `NotificacionService`, `ArchivoService`, `SessionService`. Los TODOs los referencian por nombre.

---

## 2. Decisiones arquitectónicas

### 2.1 Datos dummy locales (Enfoque 1)
Cada adapter declara su propia clase interna estática `*DummyDocente` con array de datos. Sin repositorio compartido.
- **Razón:** consistencia 1:1 con el patrón del Admin (`GrupoAdminAdapter.GrupoDummy`, `ActividadAdminAdapter.ActividadDummy`, etc.).
- **Tradeoff aceptado:** algunos datos se duplican entre módulos (ej. nombres de grupos en Grupos y en el entry de Tareas). Cuando ocurra, los IDs y nombres deben coincidir exactamente entre módulos.

### 2.2 Drill-down para Tareas (Opción B)
Tareas se navega `Grupo → Bloque → Tareas del bloque`, no como lista plana ni con tabs.
- **Razón:** el spinner de grupo en CrearTarea queda implícito (heredado de args), las pantallas son más limpias y refleja el modelo mental de un docente que piensa "qué tareas tengo en el bloque 2 de Móvil 6A".

### 2.3 Pill nav idéntica al Admin
Misma lógica de `MainAdminActivity` clonada en `MainDocenteActivity`. Sub-destinos no cambian la tab activa pero resaltan la tab padre.

### 2.4 Grafo de navegación plano (no anidado)
Un solo `nav_docente.xml` con todos los destinos al mismo nivel. Mismo patrón que `nav_admin.xml`.

---

## 3. Estructura de paquetes

```
com.conectatec.ui.docente
├── MainDocenteActivity.java
├── dashboard/
│   └── DocenteDashboardFragment.java
├── perfil/
│   └── DocentePerfilFragment.java
├── grupos/
│   ├── DocenteGruposFragment.java
│   ├── DocenteCrearGrupoFragment.java
│   ├── DocenteGrupoDetalleFragment.java
│   ├── DocenteMiembrosGrupoFragment.java
│   └── adapter/
│       ├── GrupoDocenteAdapter.java
│       └── MiembroGrupoDocenteAdapter.java
├── tareas/
│   ├── DocenteTareasFragment.java
│   ├── DocenteBloquesFragment.java
│   ├── DocenteTareasBloqueFragment.java
│   ├── DocenteCrearTareaFragment.java
│   ├── DocenteEntregasFragment.java
│   ├── DocenteCalificarEntregaFragment.java
│   └── adapter/
│       ├── GrupoTareasDocenteAdapter.java
│       ├── BloqueDocenteAdapter.java
│       ├── TareaDocenteAdapter.java
│       └── EntregaDocenteAdapter.java
└── chat/
    ├── DocenteChatFragment.java
    ├── DocenteConversacionFragment.java
    └── adapter/
        ├── SalaDocenteAdapter.java
        └── MensajeDocenteAdapter.java
```

Total: **23 archivos Java** (1 Activity + 14 Fragments + 8 Adapters), todos los Fragments y la Activity con `@AndroidEntryPoint` y liberación de binding (`binding = null` en `onDestroyView` para Fragments, `onDestroy` para Activity).

---

## 4. Grafo de navegación (`res/navigation/nav_docente.xml`)

### 4.1 Destinos raíz (5 tabs del pill nav)

| Orden | ID | Fragmento |
|---|---|---|
| 0 (start) | `docenteDashboardFragment` | `DocenteDashboardFragment` |
| 1 | `docenteGruposFragment` | `DocenteGruposFragment` |
| 2 | `docenteTareasFragment` | `DocenteTareasFragment` |
| 3 | `docenteChatFragment` | `DocenteChatFragment` |
| 4 | `docentePerfilFragment` | `DocentePerfilFragment` |

### 4.2 Sub-destinos

| ID | Args | Tab padre |
|---|---|---|
| `docenteCrearGrupoFragment` | — | Grupos |
| `docenteGrupoDetalleFragment` | `grupoId: int` | Grupos |
| `docenteMiembrosGrupoFragment` | `grupoId: int` | Grupos |
| `docenteBloquesFragment` | `grupoId: int` | Tareas |
| `docenteTareasBloqueFragment` | `grupoId: int`, `bloqueId: int` | Tareas |
| `docenteCrearTareaFragment` | `grupoId: int`, `bloqueId: int` | Tareas |
| `docenteEntregasFragment` | `tareaId: int` | Tareas |
| `docenteCalificarEntregaFragment` | `tareaId: int`, `alumnoId: int` | Tareas |
| `docenteConversacionFragment` | `salaId: int` | Chat |

### 4.3 Acciones

Todas las acciones usan animaciones existentes:
- **Avance:** `enterAnim=slide_in_right`, `exitAnim=slide_out_left`
- **Retroceso:** `popEnterAnim=slide_in_left`, `popExitAnim=slide_out_right`

| Acción | Origen → Destino |
|---|---|
| `action_grupos_to_crear` | Grupos → CrearGrupo |
| `action_grupos_to_detalle` | Grupos → GrupoDetalle |
| `action_grupo_detalle_to_miembros` | GrupoDetalle → MiembrosGrupo |
| `action_grupo_detalle_to_tareas_grupo` | GrupoDetalle → BloquesFragment (con `grupoId`) |
| `action_tareas_to_bloques` | Tareas → Bloques |
| `action_bloques_to_tareas_bloque` | Bloques → TareasBloque |
| `action_tareas_bloque_to_crear` | TareasBloque → CrearTarea |
| `action_tareas_bloque_to_entregas` | TareasBloque → Entregas |
| `action_entregas_to_calificar` | Entregas → CalificarEntrega |
| `action_chat_to_conversacion` | Chat → Conversacion |

---

## 5. `MainDocenteActivity` y pill nav

### 5.1 Java
Espejo exacto de `MainAdminActivity.java`:
- `@AndroidEntryPoint`, `extends AppCompatActivity`.
- `ActivityMainDocenteBinding binding`, `NavController navController`.
- `DESTINATIONS[]` con los 5 destinos raíz.
- `setupPillNav()` con array de `NavItem` y click listeners idénticos.
- `addOnDestinationChangedListener` con mapeo de sub-destinos a índice de tab:
  - `docenteCrearGrupoFragment`, `docenteGrupoDetalleFragment`, `docenteMiembrosGrupoFragment` → 1 (Grupos)
  - `docenteBloquesFragment`, `docenteTareasBloqueFragment`, `docenteCrearTareaFragment`, `docenteEntregasFragment`, `docenteCalificarEntregaFragment` → 2 (Tareas)
  - `docenteConversacionFragment` → 3 (Chat)
- `selectItem(int)` con `AutoTransition` 180 ms.
- `onDestroy() { super; binding = null; }`.

### 5.2 Layout `activity_main_docente.xml`
Espejo exacto de `activity_main_admin.xml`:
- `CoordinatorLayout` con `bg=@color/colorBackground`.
- `FragmentContainerView id=navHostDocente` con `app:navGraph="@navigation/nav_docente"`.
- `LinearLayout id=bottomNavDocente` (margin bottom 15dp) que contiene `LinearLayout id=pillNavDocente` con `bg=@drawable/bg_nav_container` y 5 items de pill (cada uno con `circle*` 56dp + `icon*` 27dp + `label*` 16sp).
- IDs por tab: `itemDashboard`/`circleDashboard`/`iconDashboard`/`labelDashboard`, `itemGrupos`/..., `itemTareas`/..., `itemChat`/..., `itemPerfil`/...
- Iconos:
  - Dashboard → `@drawable/ic_home` (existente)
  - Grupos → `@drawable/ic_school` (existente)
  - Tareas → `@drawable/ic_clipboard` (**nuevo**)
  - Chat → `@drawable/ic_chat` (**nuevo**)
  - Perfil → `@drawable/ic_perfil` (existente)

### 5.3 Manifest
Agregar dentro de `<application>`:
```xml
<activity
    android:name=".ui.docente.MainDocenteActivity"
    android:exported="true">
    <!-- TODO: quitar el intent-filter cuando el login enrute por rol -->
</activity>
```
No se le añade `intent-filter` por ahora (el splash sigue como launcher).

---

## 6. Pantallas — especificación detallada

### 6.1 `DocenteDashboardFragment`

**Layout:** `fragment_docente_dashboard.xml`

**Estructura:**
- Header: barra acento 4dp `bg_accent_bar` + label "TecConnect" 10sp primary + título "Inicio" 26sp bold + saludo "Hola, Prof. Carlos" 12sp muted + avatar 48dp `bg_chip_docente` con iniciales "CB" 16sp bold blanco.
- Sección RESUMEN (subtítulo "Hoy, [fecha]" en muted): 2 cards en `LinearLayout horizontal weight=1` cada una.
  - `cardGruposActivosDocente` — `tvTotalGruposDashboardDocente` "3" 28sp bold + "Grupos activos" 11sp muted. Tap → tab Grupos vía NavOptions.
  - `cardAlumnosTotalesDocente` — `tvTotalAlumnos` "47" + "Alumnos totales". Tap → tab Grupos.
- Sección TAREAS RECIENTES (`MaterialCardView` 12dp corner): título "Tareas recientes" 12sp muted letter-spacing 0.08 + divisor + 3 ítems dummy (chip tipo + título + grupo + fecha vence + tiempo relativo). `tvVerTodasTareasDashboard` 12sp bold primary → tab Tareas.
- Sección AVISOS DEL DÍA (`MaterialCardView`): 3 ítems dummy (título + grupo + tiempo relativo).

**Datos dummy (arrays estáticos en el Fragment):**
- 3 tareas: "Proyecto Final POO" (PROYECTO, Prog. Móvil 6A, vence 03/05/2026), "Examen parcial 2" (EXAMEN, BD 4B, 02/05/2026), "Tarea 5: Integrales" (TAREA, Cálculo 2A, 30/04/2026).
- 3 avisos: "Cambio de horario", "Material adicional subido", "Recordatorio examen".

**Java:** `@AndroidEntryPoint`, ViewBinding, listeners en `setupListeners()` que navegan a tabs vía `navigateToTab(view, destId)` (mismo helper que `AdminDashboardFragment`).

### 6.2 `DocentePerfilFragment`

**Layout:** `fragment_docente_perfil.xml` — espejo de `fragment_admin_perfil.xml`.

**Estructura:**
- Header de marca (acento 4dp + "TecConnect" + "Mi perfil" 22sp bold + "Docente" muted).
- Card hero: avatar 88dp `bg_avatar_placeholder` con iniciales "CB" 32sp + `tvNombrePerfilDocente` "Prof. Carlos Bautista" 22sp bold + `tvCorreoPerfilDocente` 14sp muted + `tvChipRolPerfilDocente` con `bg_chip_docente` "DOCENTE".
- Card INFORMACIÓN DE CUENTA: ID empleado, fecha de registro, departamento ("Sistemas Computacionales"), estado de cuenta (Activa, color success).
- Card CONFIGURACIÓN: 3 filas (Notificaciones / Tema / Idioma) con flecha derecha. Comentario `// TODO: implementar pantalla de configuración`.
- Botón `btnCerrarSesionDocente` outlined error → vuelve a `LoginActivity` con `FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK`. Comentario `// TODO: llamar a SessionService.cerrarSesion()`.

### 6.3 Módulo Grupos

#### 6.3.1 `DocenteGruposFragment` — `fragment_docente_grupos.xml`
- Header con título "Grupos" 26sp bold + "Tus grupos académicos" + badge `tvHeaderTotalGruposDocente` (color `colorChipDocente`).
- Botón `btnCrearGrupo` (icon `ic_plus` + texto "Nuevo grupo") debajo del header → `action_grupos_to_crear`.
- `tilBuscarGrupoDocente` / `etBuscarGrupoDocente` (TextInputLayout OutlinedBox).
- `rvGruposDocente` (paddingBottom 88dp) + `<include layout="@layout/layout_empty_state" id="emptyStateGruposDocente"/>`.

**`GrupoDocenteAdapter` con clase `GrupoDummyDocente`:**
```java
public final int id;
public final String nombre;
public final String materia;
public final int totalAlumnos;
public final String fechaCreacion;
public final String codigoUnion;  // formato "TC-XXXX"
public final boolean activo;
```

**Dataset (3 grupos):**
| id | nombre | materia | alumnos | fecha | código |
|---|---|---|---|---|---|
| 1 | Programación Móvil 6A | Programación Móvil | 18 | 01/02/2026 | TC-9X4P |
| 2 | Bases de Datos 4B | Bases de Datos | 16 | 15/01/2026 | TC-K3M2 |
| 3 | Cálculo Integral 2A | Cálculo Integral | 13 | 20/01/2026 | TC-7Z8R |

**Item layout `item_grupo_docente.xml`:** MaterialCardView 12dp corner, sin elevación, surface bg. Contiene: nombre 16sp bold, materia 12sp muted, badge alumnos (icon `ic_users` + número), fecha 11sp muted.

#### 6.3.2 `DocenteCrearGrupoFragment` — `fragment_docente_crear_grupo.xml`
- Header back ("Nuevo grupo").
- Form con:
  - `etNombreGrupoNuevo` (TextInputLayout OutlinedBox)
  - `etMateriaGrupoNuevo`
  - `etDescripcionGrupoNuevo` (multi-línea, opcional)
- Botón `btnCrearGrupoNuevo` Material primary 52dp → muestra layout de éxito (`LinearLayout id=layoutQrExito` inicialmente `gone`).
- Layout de éxito: `ImageView` con `ic_qr_placeholder` 200dp + `tvCodigoUnion` "TC-XXXX" 24sp bold (generado con `Random` al pulsar Crear) + texto "Comparte este código con tus alumnos" + botón "Listo" → `popBackStack()`.
- Comentario en `onCrearClicked()`: `// TODO: llamar a GrupoService.crearGrupo() para obtener QR real y token desde backend`.

#### 6.3.3 `DocenteGrupoDetalleFragment` — `fragment_docente_grupo_detalle.xml`
Espejo estructural de `fragment_admin_grupo_detalle.xml`.
- Header back + línea acento 3dp primary.
- ScrollView con 4 cards:
  - **INFORMACIÓN**: nombre 20sp bold, materia, código de unión, fecha creación, total alumnos. Botón pequeño "Ver QR" (muestra/oculta sección con QR placeholder).
  - **MIEMBROS** (preview 5): `containerMiembrosGrupoDocente` LinearLayout dinámico + `tvVerTodosMiembrosDocente` → `action_grupo_detalle_to_miembros`.
  - **AVISOS** (preview 3): `containerAvisosGrupoDocente` dinámico + botón `btnPublicarAviso`. Comentario `// TODO: llamar a AvisoService.publicar()`.
  - **TAREAS PUBLICADAS**: `tvTotalTareasGrupoDocente` "8 tareas" + botón `btnVerTareasGrupo` → `action_grupo_detalle_to_tareas_grupo` con `grupoId`.
- Recibe `grupoId: int`. Datos dummy hardcodeados con `switch(grupoId)`.

#### 6.3.4 `DocenteMiembrosGrupoFragment` — `fragment_docente_miembros_grupo.xml`
- Header back + subtítulo dinámico "Miembros — [grupo]" + badge total.
- `rvMiembrosGrupoDocente` paddingBottom 88dp + empty state.
- **`MiembroGrupoDocenteAdapter` con `MiembroDummyDocente { id, nombre, iniciales, correo, matricula }`** — sin estado de entrega (es la lista plana).
- 6 alumnos dummy por grupo, con datos coherentes:
  - Grupo 1 (IDs 101-106): "Ana López", "Bruno García", "Carla Méndez", "Diego Ruiz", "Elena Torres", "Fernando Vega".
  - Grupo 2 (IDs 201-206): nombres distintos.
  - Grupo 3 (IDs 301-306): nombres distintos.
- Item layout `item_miembro_grupo_docente.xml`: avatar 44dp `bg_avatar_placeholder` + iniciales bold + nombre + correo + matrícula.

### 6.4 Módulo Tareas

#### 6.4.1 `DocenteTareasFragment` — `fragment_docente_tareas.xml`
- Header "Tareas" 26sp bold + "Selecciona un grupo" + badge total grupos.
- `rvTareasGrupos` con cards de los 3 grupos del docente.
- **Reutiliza `GrupoDocenteAdapter` declarado en otro paquete:** Decisión de aislamiento — declaramos un adapter local `GrupoTareasDocenteAdapter` con clase interna `GrupoDummyTareas` idéntica al modelo de Grupos, pero sin `codigoUnion` (no se necesita aquí). Mismos IDs, mismos nombres.
- Tap → `action_tareas_to_bloques` con `grupoId`.

#### 6.4.2 `DocenteBloquesFragment` — `fragment_docente_bloques.xml`
- Header back ("Bloques — [nombre del grupo]") con `grupoId` resolviendo el nombre.
- `rvBloques` con 3 cards apiladas.
- **`BloqueDocenteAdapter` con `BloqueDummyDocente { id, numero, nombre, totalTareas }`:**
  - Bloque 1 (id=1, numero=1, "Bloque 1", totalTareas=3)
  - Bloque 2 (id=2, numero=2, "Bloque 2", totalTareas=3)
  - Bloque 3 (id=3, numero=3, "Bloque 3", totalTareas=2)
- Card de bloque: número grande "1" 36sp bold + "Bloque 1" 16sp + "3 tareas" 12sp muted + flecha derecha.
- Tap → `action_bloques_to_tareas_bloque` con `grupoId`+`bloqueId`.

#### 6.4.3 `DocenteTareasBloqueFragment` — `fragment_docente_tareas_bloque.xml`
- Header back ("Bloque N — [grupo]").
- Botón `btnCrearTareaBloque` (icon `ic_plus` + "Nueva tarea") → `action_tareas_bloque_to_crear` con `grupoId`+`bloqueId`.
- HorizontalScrollView de chips de tipo: `chipTipoTodas` (checked), `chipTipoTarea`, `chipTipoTrabajo`, `chipTipoExamen`, `chipTipoProyecto`.
- HorizontalScrollView de chips de estado: `chipEstadoTodas` (checked), `chipEstadoEnCurso`, `chipEstadoVencida`, `chipEstadoCompletada`.
- `rvTareasBloque` + `emptyStateTareasBloque`.

**`TareaDocenteAdapter` con `TareaDummyDocente`:**
```java
public final int id;
public final String titulo;
public final String tipo;        // "TAREA" | "TRABAJO" | "EXAMEN" | "PROYECTO"
public final String estado;      // "EN_CURSO" | "VENCIDA" | "COMPLETADA"
public final int grupoId;
public final int bloqueId;
public final String fechaPublicacion;
public final String fechaVence;
public final int totalAlumnos;
public final int entregadas;
```

**Dataset (8 tareas distribuidas):**
- Grupo 1 / Bloque 1: id=1 "Práctica 1: Layouts" TAREA EN_CURSO; id=2 "Examen Parcial 1" EXAMEN COMPLETADA.
- Grupo 1 / Bloque 2: id=3 "Proyecto Final" PROYECTO EN_CURSO.
- Grupo 1 / Bloque 3: id=4 "Trabajo de investigación" TRABAJO EN_CURSO.
- Grupo 2 / Bloque 1: id=5 "Tarea 1: Modelo ER" TAREA COMPLETADA.
- Grupo 2 / Bloque 2: id=6 "Examen parcial 2" EXAMEN EN_CURSO.
- Grupo 3 / Bloque 1: id=7 "Tarea 5: Integrales" TAREA VENCIDA.
- Grupo 3 / Bloque 2: id=8 "Tarea 6: Series" TAREA EN_CURSO.

**Item layout `item_tarea_docente.xml`:** chip tipo (drawable según tipo, ver §7.1), título 15sp bold, fecha vence 12sp muted, "X/Y entregas" 12sp, chip estado.

#### 6.4.4 `DocenteCrearTareaFragment` — `fragment_docente_crear_tarea.xml`
- Header back ("Nueva tarea").
- ScrollView con form:
  - `etTituloTareaNueva` (TextInputLayout)
  - `etDescripcionTareaNueva` (multi-línea)
  - `spinnerTipoTareaNueva` con TAREA/TRABAJO/EXAMEN/PROYECTO
  - `etFechaLimiteTareaNueva` (read-only, abre `DatePickerDialog` al click)
  - `etCalificacionMaxTareaNueva` (numeric, default "100")
- Botón `btnPublicarTarea` 52dp primary → Snackbar "Tarea creada" + `popBackStack()`.
- Comentario en handler: `// TODO: llamar a TareaService.crearTarea() — luego NotificacionService.enviar(tipo=1)`.

#### 6.4.5 `DocenteEntregasFragment` — `fragment_docente_entregas.xml`
- Header back ("Entregas — [título tarea]").
- Card resumen: `LinearProgressIndicator` (progreso entregadas/total), 3 stats: Total, Entregadas (success), Pendientes (warning).
- HorizontalScrollView chips: `chipEntregasTodas` (checked), `chipEntregasPendientes`, `chipEntregasEntregadas`, `chipEntregasCalificadas`, `chipEntregasSinEntregar`.
- `rvEntregas` + empty state.

**`EntregaDocenteAdapter` con `EntregaDummyDocente`:**
```java
public final int alumnoId;
public final String alumnoNombre;
public final String alumnoIniciales;
public final int estado;      // 0=borrador, 1=entregada, 2=calificada, 3=sin_entregar
public final String fechaEntrega;     // null si sin_entregar
public final Integer calificacion;    // null si no calificada
```

**Dataset por tarea:** 6 entregas dummy (mezcla de los 4 estados, IDs de alumnos del grupo de la tarea).

**Item layout `item_entrega_docente.xml`:** avatar 40dp con iniciales + nombre 14sp bold + estado (chip coloreado, ver §7.2) + calificación si aplica (ej. "85/100" 12sp bold success).

Tap → `action_entregas_to_calificar` con `tareaId`+`alumnoId`.

#### 6.4.6 `DocenteCalificarEntregaFragment` — `fragment_docente_calificar_entrega.xml`
- Header back ("Calificar — [Nombre alumno]").
- ScrollView con:
  - **Card alumno:** avatar 64dp + nombre 18sp bold + correo + chip estado entrega.
  - **Card ENTREGA:** fecha de entrega, archivo adjunto simulado (TextView "documento.pdf" + icon `ic_paperclip` clickable — placeholder), `tvComentariosAlumno` (texto dummy multi-línea).
  - **Card CALIFICACIÓN:** `etCalificacionEntrega` numeric 0–100 (TextInputLayout) + `etRetroalimentacionEntrega` multi-línea.
- Botón `btnGuardarCalificacion` 52dp → Snackbar "Calificación guardada" + `popBackStack()`.
- Si la entrega tiene `estado=2` (ya calificada): pre-llenar `etCalificacionEntrega` y cambiar texto del botón a "Actualizar calificación".
- Comentario: `// TODO: llamar a EntregaService.calificar() — luego NotificacionService.enviar(tipo=3)`.

### 6.5 Módulo Chat

#### 6.5.1 `DocenteChatFragment` — `fragment_docente_chat.xml`
- Header "Chat" 26sp bold + "Conversaciones" + badge total no leídos (`bg_badge_unread`).
- HorizontalScrollView chips: `chipChatTodos` (checked), `chipChatGrupos`, `chipChatPrivados`.
- `rvSalasChat` paddingBottom 88dp + empty state.

**`SalaDocenteAdapter` con `SalaDummyDocente`:**
```java
public final int id;
public final String nombre;
public final String tipo;          // "GRUPO" | "PRIVADO"
public final String avatarIniciales;
public final String ultimoMensaje;
public final String fechaUltimo;   // ej. "10:32" o "Ayer"
public final int noLeidos;
```

**Dataset (5 salas):**
- Grupales: id=1 "Programación Móvil 6A" (3 no leídos), id=2 "Bases de Datos 4B" (0), id=3 "Cálculo Integral 2A" (1).
- Privados: id=4 "Ana López" (2), id=5 "Diego Ruiz" (0).

**Item layout `item_sala_chat_docente.xml`:** avatar 48dp `bg_avatar_placeholder` con iniciales + nombre 15sp bold + último mensaje 12sp muted (ellipsize end, maxLines=1) + hora 11sp muted (alineada derecha) + badge `bg_badge_unread` con número (visible si `noLeidos > 0`).

Tap → `action_chat_to_conversacion` con `salaId`.

#### 6.5.2 `DocenteConversacionFragment` — `fragment_docente_conversacion.xml`
- Header back con nombre de sala + "X miembros" si grupal (resuelto desde `salaId` con `switch`).
- `rvMensajesChat` con `LinearLayoutManager.setStackFromEnd(true)`.
- Barra inferior fija (`LinearLayout horizontal` ancho match_parent, alturas 56dp, fondo `colorSurface`):
  - `btnAdjuntarChat` 36dp `ic_paperclip` — `// TODO: llamar a ArchivoService.adjuntar()`.
  - `etMensajeChat` (TextInputEditText, multi-línea, ancho weight=1, `bg_input`).
  - `btnEnviarChat` 36dp `ic_send` con tint primary.

**`MensajeDocenteAdapter` con `MensajeDummyDocente` y dos viewTypes:**
```java
public final int id;
public final String texto;
public final String hora;
public final boolean esMio;       // true → viewType ENVIADO
public final String autorNombre;
public final String autorIniciales;
public final boolean tieneAdjunto;
```
- viewType 0 = `item_mensaje_recibido.xml` (avatar 32dp + bubble `bg_message_received` izquierda).
- viewType 1 = `item_mensaje_enviado.xml` (bubble `bg_message_sent` derecha).

**Dataset por sala:** 8 mensajes dummy hardcodeados (mezcla, secuencia coherente).

Botón enviar:
1. Lee `etMensajeChat.getText()`. Si vacío, no hace nada.
2. Crea `MensajeDummyDocente` con `esMio=true`, hora actual.
3. Llama `adapter.agregarMensaje(msg)` y `rvMensajesChat.smoothScrollToPosition(adapter.getItemCount() - 1)`.
4. Limpia el `EditText`.
5. Comentario: `// TODO: llamar a MensajeService.enviar()`.

---

## 7. Recursos

### 7.1 Drawables nuevos (mínimos)

| Archivo | Tipo | Descripción |
|---|---|---|
| `ic_chat.xml` | vector 24dp | Icono burbuja para tab Chat |
| `ic_clipboard.xml` | vector 24dp | Icono portafolio/lista para tab Tareas |
| `ic_paperclip.xml` | vector 24dp | Icono adjuntar (chat + entregas) |
| `ic_send.xml` | vector 24dp | Icono enviar mensaje |
| `ic_plus.xml` | vector 24dp | Icono "+" para botones de creación |
| `ic_qr_placeholder.xml` | vector 200dp | Patrón geométrico estilizado de QR (placeholder) |

**Chips de tipo de tarea:** se reutilizan los drawables existentes de chips de rol mapeando:
- TAREA → `bg_chip_docente` (azul)
- TRABAJO → `bg_chip_pendiente` (naranja)
- EXAMEN → `bg_chip_admin` (morado)
- PROYECTO → `bg_chip_estudiante` (verde)

### 7.2 Chips de estado de entrega
Se reutilizan también:
- ENTREGADA / CALIFICADA → `bg_chip_estudiante` (verde)
- PENDIENTE / BORRADOR → `bg_chip_pendiente` (naranja)
- SIN ENTREGAR → `bg_chip_admin` (morado-rojizo)
- EN CURSO → `bg_chip_docente` (azul)

### 7.3 Strings nuevos en `strings.xml`
- `title_docente_dashboard`, `title_docente_grupos`, `title_docente_tareas`, `title_docente_chat`, `title_docente_perfil`
- `subtitle_docente_dashboard`, `subtitle_docente_grupos`, `subtitle_docente_tareas`, `subtitle_docente_chat`
- `btn_crear_grupo`, `btn_crear_tarea`, `btn_publicar_tarea`, `btn_calificar`, `btn_actualizar_calificacion`, `btn_ver_qr`, `btn_publicar_aviso`, `btn_ver_tareas_grupo`
- `label_codigo_union`, `label_compartir_codigo`, `label_total_alumnos`, `label_calificacion_max`, `label_retroalimentacion`, `label_archivo_adjunto`
- `label_tipo_tarea`, `label_tipo_trabajo`, `label_tipo_examen`, `label_tipo_proyecto`
- `label_estado_borrador`, `label_estado_entregada`, `label_estado_calificada`, `label_estado_sin_entregar`, `label_estado_en_curso`, `label_estado_vencida`, `label_estado_completada`
- Hint inputs: `hint_buscar_grupo_docente`, `hint_titulo_tarea`, `hint_descripcion_tarea`, `hint_fecha_limite`, `hint_calificacion`, `hint_mensaje_chat`

### 7.4 Animaciones
**Cero archivos nuevos.** Reutilización de `slide_in_right`, `slide_out_left`, `slide_in_left`, `slide_out_right`, `fade_in`, `fade_out`.

### 7.5 Colores
**Cero archivos nuevos.** Toda la paleta de `colors.xml` actual cubre el módulo.

---

## 8. TODOs en español a sembrar (puntos de coordinación con backend)

| Servicio backend | Ubicación |
|---|---|
| `GrupoService.crearGrupo()` | `DocenteCrearGrupoFragment.onCrearClicked()` |
| `TareaService.crearTarea()` + `NotificacionService.enviar(tipo=1)` | `DocenteCrearTareaFragment.onPublicarClicked()` |
| `EntregaService.calificar()` + `NotificacionService.enviar(tipo=3)` | `DocenteCalificarEntregaFragment.onGuardarClicked()` |
| `MensajeService.enviar()` | `DocenteConversacionFragment.onSendClicked()` |
| `AvisoService.publicar()` | `DocenteGrupoDetalleFragment.onPublicarAvisoClicked()` |
| `SessionService.cerrarSesion()` | `DocentePerfilFragment.onCerrarSesionClicked()` |
| `ArchivoService.adjuntar()` | `DocenteConversacionFragment.onAdjuntarClicked()` |

Adicional en `MainDocenteActivity`: `// TODO: quitar el intent-filter del manifest cuando el login enrute por rol`.

---

## 9. Plan de ejecución (5 planes independientes)

| # | Plan | Bloquea | Bloqueado por | Contenido |
|---|---|---|---|---|
| 1 | `infra` | 2,3,4,5 | — | `nav_docente.xml`, `MainDocenteActivity`, `activity_main_docente.xml`, manifest, drawables nav (`ic_chat`, `ic_clipboard`, `ic_plus`, `ic_paperclip`, `ic_send`, `ic_qr_placeholder`), strings de tabs |
| 2 | `dashboard-perfil` | — | 1 | Dashboard + Perfil (paralelizable con 3 y 5) |
| 3 | `grupos` | 4 | 1 | DocenteGrupos + DocenteCrearGrupo + DocenteGrupoDetalle + DocenteMiembrosGrupo + adapters |
| 4 | `tareas` | — | 1, 3 | DocenteTareas → Bloques → TareasBloque → CrearTarea → Entregas → CalificarEntrega + 3 adapters. Requiere modelo de grupos del plan 3. |
| 5 | `chat` | — | 1 | DocenteChat + DocenteConversacion + 2 adapters (paralelizable con 2, 3, 4) |

**Cada plan se guarda en `docs/superpowers/plans/2026-04-30-<modulo>-plan.md`** y debe poder ejecutarse independientemente y producir software testeable por sí solo (cuando aplique — el plan de tareas requiere completar grupos antes).

---

## 10. Skills de proyecto (a crear antes de los planes)

Vía `superpowers:writing-skills` se crean en `.claude/skills/`:

### `tecconnect-android-layouts`
**Trigger:** Úsame cuando estés creando o editando archivos XML de layout Android para TecConnect.

**Reglas:**
- Cero `#RRGGBB` literal en layouts — siempre `@color/*` y `@drawable/*`.
- Pill nav personalizada — nunca `BottomNavigationView`.
- Header con barra de acento 4dp + label "TecConnect" 10sp + título de pantalla.
- `MaterialCardView` con `cornerRadius=12dp`, `elevation=0dp`, `strokeWidth=0dp`, `cardBackgroundColor=@color/colorSurface`.
- Chips como `TextView` con drawable de fondo (`bg_chip_*`), no componente `Material Chip`.
- `<include layout="@layout/layout_empty_state"/>` para estados vacíos.
- Avatares: `FrameLayout` + `bg_avatar_placeholder` + `TextView` con iniciales en bold.

### `tecconnect-android-java`
**Trigger:** Úsame cuando estés creando o editando archivos Java de Fragment o Activity para TecConnect Android.

**Reglas:**
- `@AndroidEntryPoint` obligatorio en Activities y Fragments.
- ViewBinding liberado: `binding = null` en `onDestroyView` (Fragments) y `onDestroy` (Activities).
- TODOs en español para backend, formato `// TODO: llamar a [Servicio].[metodo]()`.
- Argumentos via `getArguments().getInt("id", 0)` o constantes (no SafeArgs por ahora).
- `NavOptions` con `setLaunchSingleTop(true)` y `setRestoreState(true)` para navegación entre tabs.
- Adapters: clase interna estática `*DummyDocente`, interfaz `OnItemClickListener`, dataset cargado en constructor.

---

## 11. Verificación final (antes del commit)

```bash
# 1. Inventario
find app/src/main/java/com/conectatec/ui/docente -name "*.java" | wc -l   # esperado ~17
find app/src/main/res/layout -name "*docente*" -o -name "*tarea*" \
  -o -name "*entrega*" -o -name "*chat*" -o -name "*bloque*" -o -name "*conversacion*" -o -name "*sala*" | wc -l

# 2. Cero hex en layouts del módulo
grep -rn "#[0-9A-Fa-f]\{6\}" app/src/main/res/layout/ \
  --include="*docente*" --include="*tarea*" --include="*entrega*" \
  --include="*chat*" --include="*bloque*" --include="*conversacion*" --include="*sala*"
# (debe devolver vacío)

# 3. Cero BottomNavigationView
grep -rn "BottomNavigationView" app/src/main/java/com/conectatec/ui/docente/
# (debe devolver vacío)

# 4. @AndroidEntryPoint en todos
grep -rL "@AndroidEntryPoint" app/src/main/java/com/conectatec/ui/docente/**/*Fragment.java
grep -rL "@AndroidEntryPoint" app/src/main/java/com/conectatec/ui/docente/**/*Activity.java
# (ambos deben devolver vacío)

# 5. ViewBinding liberado
grep -rL "binding = null" app/src/main/java/com/conectatec/ui/docente/**/*Fragment.java
# (debe devolver vacío)

# 6. TODOs en español
grep -rn "TODO: llamar" app/src/main/java/com/conectatec/ui/docente/ | wc -l
# (debe ser >= 7)

# 7. Manifest registrado
grep -c "MainDocenteActivity" app/src/main/AndroidManifest.xml
# (debe ser >= 1)
```

---

## 12. Resumen ejecutivo

- **23 archivos Java** (1 Activity + 14 Fragments + 8 Adapters; nota: dashboard y perfil sin adapter dedicado).
- **23 layouts XML** (1 activity + 14 fragments + 8 items + reutilización del `layout_empty_state` existente).
- **1 grafo de navegación** (`nav_docente.xml`).
- **6 drawables nuevos** (todos vectoriales; cero PNG).
- **0 colores nuevos**, **0 animaciones nuevas**.
- **5 planes de ejecución** independientes con dependencias documentadas.
- **2 skills de proyecto** que se activarán automáticamente al editar layouts y archivos Java del módulo.
- **7 TODOs de backend** marcados en español.
- **Datos:** 100 % dummy estático, 3 grupos × 3 bloques × 8 tareas × 6 entregas × 5 salas × 8 mensajes.
