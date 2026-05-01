# TecConnect — Android App

Plataforma académica Android para gestión de grupos, tareas y comunicación entre docentes y estudiantes.

## Stack técnico

- **Lenguaje:** Java 11
- **Min SDK:** 24 | **Target SDK:** 36
- **Package:** `com.conectatec`
- **DI:** Hilt (`@AndroidEntryPoint` en Activities y Fragments)
- **UI:** ViewBinding (nunca `findViewById`)
- **Nav:** Navigation Component (`nav_admin.xml`, `nav_docente.xml`, `nav_registro.xml`)
- **Build:** Gradle KTS (`app/build.gradle.kts`)

---

## Módulo Admin (`ui/admin/`)

Entry: `MainAdminActivity` → `nav_admin.xml`
5 tabs raíz: Dashboard, Grupos, Actividades, Usuarios, Perfil

### Activities

#### `MainAdminActivity`
**Función:** Contenedor principal del rol Admin. Gestiona la navegación entre las 5 tabs con pill nav custom y preservación de estado.
**Atributos:**
- `binding` (ActivityMainAdminBinding)
- `navController` (NavController)
- `items[]` (LinearLayout[5]) — referencias a los ítems del pill nav
- `DESTINATIONS[]` (int[5]) — IDs de destino: dashboard, usuarios, grupos, actividades, perfil
**Navegación:** Pill nav → 5 destinos raíz con `NavOptions` (singleTop + restoreState + popUpTo)
**Layout:** `activity_main_admin.xml` — CoordinatorLayout con `FragmentContainerView` y pill nav horizontal en la base

---

### Fragments — Tab Dashboard

#### `AdminDashboardFragment`
**Función:** Pantalla de inicio del admin con resumen estadístico del sistema. Muestra 4 tarjetas de KPIs y accesos directos a secciones.
**Atributos:**
- `binding` (FragmentAdminDashboardBinding)
**UI:** Header con avatar + saludo, 4 `MaterialCardView` (Usuarios, Pendientes, Grupos, Tareas), sección de actividades recientes (3 ítems estáticos)
**Navegación:** Click en tarjeta Usuarios → `adminUsuariosFragment` | Grupos → `adminGruposFragment` | Tareas → `adminActividadesFragment`
**Layout:** `fragment_admin_dashboard.xml`

---

### Fragments — Tab Grupos

#### `AdminGruposFragment`
**Función:** Lista todos los grupos del sistema con filtros múltiples y búsqueda de texto.
**Atributos:**
- `binding` (FragmentAdminGruposBinding)
- `adapter` (GrupoAdminAdapter)
- `docenteFiltro` (String) — nombre del docente activo como filtro
- `activoFiltro` (Boolean) — null=todos, true=activos, false=desactivados
- `queryFiltro` (String) — texto de búsqueda actual
**UI:** Header con badge total, banner de filtro activo por docente (con X), chips (Todos/Activos/Desactivados), campo de búsqueda, `RecyclerView`, empty state
**Navegación:** Click en grupo → `action_grupos_to_detalle` (arg: `grupoId`)
**Layout:** `fragment_admin_grupos.xml`

#### `AdminGrupoDetalleFragment`
**Función:** Detalle completo de un grupo con vista previa de miembros y avisos, y acciones de administración.
**Atributos:**
- `binding` (FragmentAdminGrupoDetalleBinding)
- `grupoId` (int) — recibido por args
- `grupoActivo` (boolean) — estado toggle del grupo
**UI:** Info del grupo (nombre, materia, docente, fecha creación, total tareas), chip ACTIVO/INACTIVO, preview dinámico de 4 miembros y 2 avisos, botones Desactivar/Eliminar con `MaterialAlertDialog`
**Navegación:** "Ver todos miembros" → `action_grupo_detalle_to_miembros` (arg: `grupoId`)
**Layout:** `fragment_admin_grupo_detalle.xml`

#### `AdminMiembrosGrupoFragment`
**Función:** Lista completa de miembros de un grupo, con soporte opcional para mostrar estado de entrega por actividad.
**Atributos:**
- `binding` (FragmentAdminMiembrosGrupoBinding)
- `adapter` (MiembrosGrupoAdapter)
- `MIEMBROS_POR_GRUPO[][]` — dataset estático: 6 grupos × 6–8 miembros
- `NOMBRES_GRUPO[]` — nombres de grupos para el subtítulo
- `ENTREGAS_POR_ACTIVIDAD[]` — booleans de entrega por alumno (null si no aplica)
**UI:** Header con nombre del grupo y conteo de miembros, `RecyclerView`, empty state
**Navegación:** Click en miembro → `action_miembros_to_alumno_detalle` (arg: `usuarioId`)
**Layout:** `fragment_admin_miembros_grupo.xml`

---

### Fragments — Tab Actividades

#### `AdminActividadesFragment`
**Función:** Lista todas las actividades/tareas del sistema con filtros por estado y búsqueda.
**Atributos:**
- `binding` (FragmentAdminActividadesBinding)
- `adapter` (ActividadAdminAdapter)
- `filtroEstadoActivo` (String) — estado seleccionado en chips
**UI:** Header con badge total, chips de filtro (Todas/EnCurso/Pendiente/Completada/Vencida) en `HorizontalScrollView`, campo de búsqueda, `RecyclerView`, empty state
**Navegación:** Click en actividad → `action_actividades_to_detalle` (arg: `actividadId`)
**Layout:** `fragment_admin_actividades.xml`

#### `AdminActividadDetalleFragment`
**Función:** Detalle de una actividad mostrando info general, estadísticas de entrega y vista previa de miembros.
**Atributos:**
- `binding` (FragmentAdminActividadDetalleBinding)
- `actividadId` (int) — recibido por args
- `grupoId` (int) — derivado del dataset
- `DATOS[]` (DetalleActividad[8]) — dataset estático de actividades detalladas
- Clase interna: `DetalleActividad` (id, titulo, grupo, grupoId, tipo, docente, fechaVencimiento, estado, descripcion, entregasRecibidas, totalMiembros)
**UI:** Título, grupo, tipo, docente, fecha vencimiento, chip de estado, barra de progreso con entregadas/pendientes/total, descripción, container con preview de 4 miembros
**Navegación:** "Ver todos miembros" → `action_actividad_detalle_to_miembros` (args: `grupoId`, `actividadId`)
**Layout:** `fragment_admin_actividad_detalle.xml`

---

### Fragments — Tab Usuarios

#### `AdminUsuariosFragment`
**Función:** Lista todos los usuarios del sistema con tabs Todos/Pendientes, filtros por rol y búsqueda.
**Atributos:**
- `binding` (FragmentAdminUsuariosBinding)
- `adapter` (UsuarioAdminAdapter)
- `listaCompleta` (List) — copia sin filtrar para resetear
**UI:** Header con badge, `TabLayout` (Todos / Pendientes), chips de rol (Todos/Admin/Docente/Estudiante), campo de búsqueda, `RecyclerView`, empty state
**Navegación:** Click en usuario → `action_usuarios_to_detalle` | Tab "Pendientes" → muestra `AdminPendientesFragment` inline o navega al destino pendientes
**Layout:** `fragment_admin_usuarios.xml`

#### `AdminUsuarioDetalleFragment`
**Función:** Perfil detallado de un usuario con acciones de administración (cambiar rol, toggle de cuenta, eliminar).
**Atributos:**
- `binding` (FragmentAdminUsuarioDetalleBinding)
- `NOMBRE_DUMMY`, `CORREO_DUMMY`, `ROL_DUMMY` (String) — datos estáticos del usuario
- `cuentaActiva` (boolean) — estado toggle de la cuenta
**UI:** Avatar con iniciales, nombre, correo, chip de rol, tarjeta info (fecha registro, estado cuenta, foto verificada), container dinámico de grupos asignados, botones (Cambiar Rol → `AsignarRolBottomSheet`, Toggle Cuenta, Eliminar)
**Navegación:** "Ver grupos" → `adminGruposFragment` (arg: `docenteNombre`) | `AsignarRolBottomSheet` modal inline
**Layout:** `fragment_admin_usuario_detalle.xml`

#### `AdminPendientesFragment`
**Función:** Lista exclusiva de usuarios con rol PENDIENTE para asignarles rol de Docente o Estudiante.
**Atributos:**
- `binding` (FragmentAdminPendientesBinding)
- `adapter` (UsuarioPendienteAdapter)
**UI:** `RecyclerView` con ítems que tienen dos botones (Asignar Docente / Asignar Estudiante), layout alternativo `layoutSinPendientes` cuando la lista está vacía
**Navegación:** Sin navegación — acciones muestran `Snackbar` inline
**Layout:** `fragment_admin_pendientes.xml`

---

### Fragments — Tab Perfil

#### `AdminPerfilFragment`
**Función:** Perfil del admin con opción de cerrar sesión.
**Atributos:**
- `binding` (FragmentAdminPerfilBinding)
**UI:** Avatar con iniciales, nombre, correo, chip rol, tarjeta info de cuenta (email, estado, verificación), botón Cerrar Sesión con `MaterialAlertDialog`
**Navegación:** Confirmar logout → `LoginActivity` (flags: NEW_TASK + CLEAR_TASK)
**Layout:** `fragment_admin_perfil.xml`

---

### Fragments — Sub-destinos Admin

#### `AdminAlumnoDetalleFragment`
**Función:** Detalle académico de un alumno mostrando datos personales, grupos inscritos y estado de entregas.
**Atributos:**
- `binding` (FragmentAlumnoDetalleBinding)
- `DATOS[]` (DetalleAlumno[56]) — dataset estático de alumnos distribuidos en 6 grupos
- Clase interna: `DetalleAlumno` (id, nombre, iniciales, correo, matricula, semestre, carrera, fechaInscripcion, grupoId, estadosCuentas, entregas[])
**UI:** Avatar con iniciales, nombre, correo, matrícula, semestre, carrera, fecha inscripción, estado cuenta, container dinámico de grupos y container de entregas (título, fecha, chip de estado)
**Navegación:** Sin navegación (hoja final del drill-down)
**Layout:** `fragment_alumno_detalle.xml`

---

### Adapters Admin

#### `ActividadAdminAdapter`
**Función:** Adapter con filtrado dual (estado + texto) para la lista de actividades.
**Modelo interno:** `ActividadDummy` (id, titulo, grupo, docente, fechaVencimiento, estado, entregasRecibidas, totalMiembros)
**Dataset:** 8 actividades estáticas cargadas en constructor
**Item layout:** `item_actividad_admin.xml` — barra de acento lateral coloreada por estado, título, grupo, docente, fecha, conteo de entregas, chip de estado

#### `GrupoAdminAdapter`
**Función:** Adapter con filtrado triple (docente + activo/inactivo + texto) para la lista de grupos.
**Modelo interno:** `GrupoDummy` (id, nombre, materia, docente, miembros, fechaCreacion, activo)
**Dataset:** 6 grupos estáticos
**Item layout:** `item_grupo_admin.xml` — nombre, materia, docente, miembros, fecha, chip ACTIVO/INACTIVO, opacidad reducida si inactivo

#### `MiembrosGrupoAdapter`
**Función:** Adapter para miembros de un grupo con estado de entrega opcional.
**Modelo interno:** `MiembroDummy` (id, nombre, iniciales, correo, rol, entregado: Boolean)
**Item layout:** `item_miembro_grupo.xml` — avatar iniciales, nombre, correo, chip rol, badge de entrega (visible solo si `entregado != null`)

#### `UsuarioAdminAdapter`
**Función:** Adapter para lista de usuarios con chip de rol y menú contextual.
**Modelo interno:** `UsuarioDummy` (nombre, correo, rol, activo)
**Dataset:** 7 usuarios estáticos
**Item layout:** `item_usuario_admin.xml` — avatar iniciales, nombre, correo, chip rol, badge desactivado, botón overflow (popup menu)

#### `UsuarioPendienteAdapter`
**Función:** Adapter especializado para usuarios en estado PENDIENTE con acciones de asignación de rol.
**Modelo interno:** Reutiliza `UsuarioAdminAdapter.UsuarioDummy`
**Dataset:** 3 usuarios pendientes estáticos
**Item layout:** `item_usuario_pendiente.xml` — avatar, nombre, correo, botones "Asignar Docente" / "Asignar Estudiante"

#### `AsignarRolBottomSheet`
**Función:** Modal bottom sheet para cambiar el rol de un usuario.
**Atributos:** `rolSeleccionado` (String), `listener` (OnRolAsignadoListener)
**UI:** `RadioGroup` con 4 opciones (PENDIENTE, ADMIN, DOCENTE, ESTUDIANTE), botón confirmar
**Layout:** `bottom_sheet_asignar_rol.xml`

---

## Módulo Docente (`ui/docente/`)

Entry: `MainDocenteActivity` → `nav_docente.xml`
5 tabs raíz: Dashboard, Grupos, Tareas, Chat, Perfil

### Activities

#### `MainDocenteActivity`
**Función:** Contenedor principal del rol Docente. Gestiona navegación entre 5 tabs y mapeo de sub-destinos al tab correspondiente.
**Atributos:**
- `binding` (ActivityMainDocenteBinding)
- `navController` (NavController)
- `items[]` (LinearLayout[5]) — ítems del pill nav
- `DESTINATIONS[]` (int[5]) — tabs raíz: dashboard, grupos, tareas, chat, perfil
- Mapa de sub-destinos: CrearGrupo/GrupoDetalle/MiembrosGrupo → tab Grupos | Bloques/TareasBloque/CrearTarea/Entregas/CalificarEntrega → tab Tareas | Conversacion → tab Chat
**Layout:** `activity_main_docente.xml`

---

### Fragments — Tab Dashboard

#### `DocenteDashboardFragment`
**Función:** Pantalla de inicio del docente con resumen de actividad reciente y avisos del día.
**Atributos:**
- `binding` (FragmentDocenteDashboardBinding)
- `TAREAS_RECIENTES[][]` — dataset estático de tareas recientes (título, grupo, estado)
- `AVISOS_DEL_DIA[][]` — dataset estático de avisos (título, grupo)
**UI:** Header con avatar + saludo, 2 tarjetas de stat (Grupos activos, Alumnos totales), sección de tareas recientes (3 ítems), sección de avisos del día (3 ítems)
**Navegación:** Acceso directo → `docenteGruposFragment` | `docenteTareasFragment`
**Layout:** `fragment_docente_dashboard.xml`

---

### Fragments — Tab Grupos

#### `DocenteGruposFragment`
**Función:** Lista los grupos del docente con búsqueda y acceso a crear nuevo grupo.
**Atributos:**
- `binding` (FragmentDocenteGruposBinding)
- `adapter` (GrupoDocenteAdapter) — cargado con `GrupoDocenteAdapter.cargarDatosDummy()`
**UI:** Header con badge de total, botón "Crear Grupo", campo de búsqueda, `RecyclerView`, empty state
**Navegación:** Click en grupo → `action_grupos_to_detalle` (arg: `grupoId`) | Botón crear → `action_grupos_to_crear`
**Layout:** `fragment_docente_grupos.xml`

#### `DocenteCrearGrupoFragment`
**Función:** Formulario para crear un nuevo grupo generando código de unión aleatorio.
**Atributos:**
- `binding` (FragmentDocenteCrearGrupoBinding)
- `CHARSET` (String) — caracteres alfanuméricos para generar código de 4 chars
**UI:** Campos `etNombreGrupoNuevo` y `etMateriaGrupoNuevo`, botón "Crear", layout de éxito `layoutQrExito` (código generado + placeholder QR)
**Validación:** nombre y materia no vacíos
**Navegación:** Éxito → `navigateUp()`
**Layout:** `fragment_docente_crear_grupo.xml`

#### `DocenteGrupoDetalleFragment`
**Función:** Detalle de un grupo del docente con preview de miembros, avisos, código QR y accesos a secciones.
**Atributos:**
- `binding` (FragmentDocenteGrupoDetalleBinding)
- `grupoId` (int) — recibido por args
- `MIEMBROS_PREVIEW[][]` — 3 grupos × preview de miembros
- `AVISOS_PREVIEW[][]` — 3 grupos × preview de avisos
- `TOTAL_TAREAS[]` — conteo de tareas por grupo
**UI:** Nombre, materia, código de unión, fecha creación, total alumnos, total tareas, container dinámico de 3 miembros preview, container dinámico de 2 avisos preview, toggle QR (`layoutQrDetalleDocente`), botones (Ver Miembros, Ver Tareas, Publicar Aviso)
**Navegación:** Ver Miembros → `action_grupo_detalle_to_miembros` | Ver Tareas → `action_grupo_detalle_to_tareas_grupo` (ambos con arg `grupoId`)
**Layout:** `fragment_docente_grupo_detalle.xml`

#### `DocenteMiembrosGrupoFragment`
**Función:** Lista completa de alumnos de un grupo del docente, incluyendo matrícula.
**Atributos:**
- `binding` (FragmentDocenteMiembrosGrupoBinding)
- `adapter` (MiembroGrupoDocenteAdapter)
- `MIEMBROS_POR_GRUPO[][]` — 3 grupos × 6 miembros con matrícula
- `NOMBRES_GRUPO[]` — nombres para el subtítulo del header
**UI:** Header con nombre del grupo (`tvSubtituloMiembrosDocente`) y badge de conteo (`tvBadgeMiembrosDocente`), `RecyclerView`, empty state
**Navegación:** Solo `navigateUp()`
**Layout:** `fragment_docente_miembros_grupo.xml`

---

### Fragments — Tab Tareas (drill-down)

Flujo: `DocenteTareasFragment` → `DocenteBloquesFragment` → `DocenteTareasBloqueFragment` → `DocenteEntregasFragment` → `DocenteCalificarEntregaFragment`
Bifurcación: `DocenteTareasBloqueFragment` → `DocenteCrearTareaFragment`

#### `DocenteTareasFragment`
**Función:** Punto de entrada al módulo de tareas. Lista los grupos del docente para elegir cuál gestionar.
**Atributos:**
- `binding` (FragmentDocenteTareasBinding)
- `adapter` (GrupoTareasDocenteAdapter)
**UI:** Header con badge de total grupos (`tvHeaderTotalGruposTareas`), `RecyclerView`, empty state
**Navegación:** Click en grupo → `action_tareas_to_bloques` (arg: `grupoId`)
**Layout:** `fragment_docente_tareas.xml`

#### `DocenteBloquesFragment`
**Función:** Muestra los bloques de tareas de un grupo. Cada bloque agrupa tareas temáticamente.
**Atributos:**
- `binding` (FragmentDocenteBloquesBinding)
- `adapter` (BloqueDocenteAdapter)
- `grupoId` (int) — recibido por args
**UI:** Subtítulo con nombre del grupo (`tvSubtituloBloques`), `RecyclerView` con los bloques, empty state
**Navegación:** Click en bloque → `action_bloques_to_tareas_bloque` (args: `grupoId`, `bloqueId`)
**Layout:** `fragment_docente_bloques.xml`

#### `DocenteTareasBloqueFragment`
**Función:** Lista las tareas de un bloque con filtros por tipo (TAREA/TRABAJO/EXAMEN/PROYECTO) y estado (EN_CURSO/VENCIDA/COMPLETADA).
**Atributos:**
- `binding` (FragmentDocenteTareasBloqueBinding)
- `adapter` (TareaDocenteAdapter)
- `grupoId` (int), `bloqueId` (int) — recibidos por args
- `filtroTipo` (String) — tipo seleccionado en chips
- `filtroEstado` (String) — estado seleccionado en chips
**UI:** Subtítulo con nombre del bloque, `HorizontalScrollView` de chips tipo (Todas/Tarea/Trabajo/Examen/Proyecto), `HorizontalScrollView` de chips estado (Todas/EnCurso/Vencida/Completada), `RecyclerView`, botón flotante "Crear Tarea", empty state
**Navegación:** Click en tarea → `action_tareas_bloque_to_entregas` (arg: `tareaId`) | Botón crear → `action_tareas_bloque_to_crear` (args: `grupoId`, `bloqueId`)
**Layout:** `fragment_docente_tareas_bloque.xml`

#### `DocenteCrearTareaFragment`
**Función:** Formulario para publicar una nueva tarea en un bloque específico.
**Atributos:**
- `binding` (FragmentDocenteCrearTareaBinding)
- `grupoId` (int), `bloqueId` (int) — recibidos por args
**UI:** Campos `etTituloTareaNueva`, `spinnerTipoTareaNueva` (TAREA/TRABAJO/EXAMEN/PROYECTO), `etDescripcionTareaNueva`, `etFechaLimiteTareaNueva` (date picker), botón "Publicar"
**Validación:** título y fecha no vacíos
**Navegación:** Éxito → `popBackStack()`
**Layout:** `fragment_docente_crear_tarea.xml`

#### `DocenteEntregasFragment`
**Función:** Lista las entregas de una tarea con estadísticas de progreso y filtros por estado.
**Atributos:**
- `binding` (FragmentDocenteEntregasBinding)
- `adapter` (EntregaDocenteAdapter)
- `tareaId` (int) — recibido por args
**UI:** Subtítulo con nombre de la tarea, sección de stats (total, entregadas, pendientes + `ProgressBar`), chips filtro (Todas/Pendientes/Entregadas/Calificadas/SinEntregar), `RecyclerView`, empty state
**Navegación:** Click en entrega → `action_entregas_to_calificar` (args: `tareaId`, `alumnoId`)
**Layout:** `fragment_docente_entregas.xml`

#### `DocenteCalificarEntregaFragment`
**Función:** Interfaz de calificación de la entrega de un alumno mostrando el archivo entregado y campo de nota.
**Atributos:**
- `binding` (FragmentDocenteCalificarEntregaBinding)
- `tareaId` (int), `alumnoId` (int) — recibidos por args
- `yaCalificada` (boolean) — si ya tiene calificación, precarga el campo
**UI:** Avatar con iniciales del alumno, nombre, correo, fecha de entrega, chip de estado, `containerArchivoCalif` (visible solo si hay entrega), campo `etCalificacionEntrega` (0–100), botón "Guardar Calificación"
**Validación:** nota entre 0 y 100
**Navegación:** Guardar → `popBackStack()`
**Layout:** `fragment_docente_calificar_entrega.xml`

---

### Fragments — Tab Chat

#### `DocenteChatFragment`
**Función:** Lista de salas de chat (grupos y privadas) con badge de no leídos y filtro por tipo.
**Atributos:**
- `binding` (FragmentDocenteChatBinding)
- `adapter` (SalaDocenteAdapter)
**UI:** Header con badge total de mensajes no leídos (`tvBadgeTotalNoLeidos`), chips filtro (Todos/Grupos/Privados), `RecyclerView`, empty state
**Navegación:** Click en sala → `action_chat_to_conversacion` (arg: `salaId`)
**Layout:** `fragment_docente_chat.xml`

#### `DocenteConversacionFragment`
**Función:** Vista de conversación de una sala de chat con mensajes enviados y recibidos.
**Atributos:**
- `binding` (FragmentDocenteConversacionBinding)
- `adapter` (MensajeDocenteAdapter)
- `salaId` (int) — recibido por args
**UI:** Toolbar con nombre de la sala (`tvNombreSalaConversacion`) y subtítulo opcional, `RecyclerView` con `LinearLayoutManager(stackFromEnd=true)`, área de entrada con botón adjuntar (`btnAdjuntarChat`), campo `etMensajeChat`, botón enviar (`btnEnviarChat`)
**Navegación:** Back → `popBackStack()`
**Layout:** `fragment_docente_conversacion.xml`

---

### Fragments — Tab Perfil

#### `DocentePerfilFragment`
**Función:** Perfil del docente con información personal, académica y ajustes de cuenta.
**Atributos:**
- `binding` (FragmentDocentePerfilBinding)
**UI:** Header con avatar + iniciales, tarjeta de info personal (nombre, correo, chip DOCENTE), tarjeta de info académica (ID empleado, fecha registro, departamento, estado), tarjeta de ajustes (3 filas: Notificaciones, Tema, Idioma — placeholders), botón Cerrar Sesión con `MaterialAlertDialog`
**Navegación:** Confirmar logout → `LoginActivity` (flags: NEW_TASK + CLEAR_TASK)
**Layout:** `fragment_docente_perfil.xml`

---

### Adapters Docente

#### `GrupoDocenteAdapter`
**Función:** Adapter filtrable por nombre/materia para la lista de grupos del docente.
**Modelo interno:** `GrupoDummyDocente` (id, nombre, materia, totalAlumnos, fechaCreacion, codigoUnion, activo)
**Dataset:** `cargarDatosDummy()` — 3 grupos hardcoded (coordinados con `GrupoTareasDocenteAdapter`)
**Item layout:** `item_grupo_docente.xml` — nombre, materia, fecha creación, badge de alumnos

#### `MiembroGrupoDocenteAdapter`
**Función:** Adapter simple para lista de miembros con matrícula.
**Modelo interno:** `MiembroDummyDocente` (id, nombre, iniciales, correo, matricula)
**Item layout:** `item_miembro_grupo_docente.xml` — avatar iniciales, nombre, correo, matrícula

#### `GrupoTareasDocenteAdapter`
**Función:** Adapter para grupos visto desde el módulo de tareas. Reutiliza el mismo item layout que `GrupoDocenteAdapter`.
**Modelo interno:** `GrupoDummyTareas` (id, nombre, materia, totalAlumnos, fechaCreacion, activo)
**Dataset:** 3 grupos hardcoded — IDs, nombres y totales deben coincidir exactamente con `GrupoDocenteAdapter`
**Item layout:** `item_grupo_docente.xml`

#### `BloqueDocenteAdapter`
**Función:** Adapter para bloques de tareas de un grupo.
**Modelo interno:** `BloqueDummyDocente` (id, numero, nombre, totalTareas)
**Dataset:** `cargarParaGrupo(grupoId)` — 1 a 3 bloques por grupo con conteos variables
**Item layout:** `item_bloque_docente.xml` — número, nombre y conteo de tareas

#### `TareaDocenteAdapter`
**Función:** Adapter con filtrado dual (tipo + estado) para lista de tareas de un bloque.
**Constantes:** `TIPO_*` (TAREA, TRABAJO, EXAMEN, PROYECTO) | `EST_*` (EN_CURSO, VENCIDA, COMPLETADA)
**Modelo interno:** `TareaDummyDocente` (id, titulo, tipo, estado, grupoId, bloqueId, fechaPublicacion, fechaVence, totalAlumnos, entregadas)
**Dataset:** `DATASET` — 8 tareas estáticas distribuidas en grupoId 1–3 y bloqueId 1–3
**Helpers:** `buscarPorId(id)` estático
**Item layout:** `item_tarea_docente.xml` — título, fecha vencimiento, conteo entregas, chip tipo (coloreado), chip estado (coloreado)

#### `EntregaDocenteAdapter`
**Función:** Adapter con filtrado por estado para entregas de una tarea, con calificación opcional.
**Constantes:** `ESTADO_*` (BORRADOR, ENTREGADA, CALIFICADA, SIN_ENTREGAR)
**Modelo interno:** `EntregaDummyDocente` (alumnoId, alumnoNombre, alumnoIniciales, estado, fechaEntrega, calificacion: Integer)
**Dataset:** `cargarParaTarea(tareaId)` — genera alumnos a partir de arrays estáticos por grupoId derivado de la tarea
**Helpers:** `obtenerListaActual(adapter)`, `conteoEntregadas()`, `conteoPendientes()`
**Item layout:** `item_entrega_docente.xml` — avatar iniciales, nombre, fecha entrega, chip estado, calificación (oculta si null)

#### `SalaDocenteAdapter`
**Función:** Adapter filtrable por tipo (GRUPO/PRIVADO) para salas de chat con badge de no leídos.
**Modelo interno:** `SalaDummyDocente` (id, nombre, tipo, avatarIniciales, ultimoMensaje, fechaUltimo, noLeidos)
**Dataset:** 5 salas (3 GRUPO + 2 PRIVADO)
**Item layout:** `item_sala_chat_docente.xml` — avatar iniciales, nombre, último mensaje, hora, badge no leídos (oculto si 0)
**Nota:** Usa `findViewById` manual (sin ViewBinding en el adapter)

#### `MensajeDocenteAdapter`
**Función:** Adapter de dos ViewHolders (enviado/recibido) para la conversación de chat.
**ViewTypes:** `VIEW_TYPE_RECIBIDO` (0), `VIEW_TYPE_ENVIADO` (1)
**Modelo interno:** `MensajeDummyDocente` (id, texto, hora, esMio, autorNombre, autorIniciales, tieneAdjunto)
**Dataset:** `cargarParaSala(salaId)` — 8 mensajes por sala con mezcla de enviados/recibidos
**Item layouts:** `item_mensaje_enviado.xml` | `item_mensaje_recibido.xml`
**Nota:** Usa `findViewById` manual (sin ViewBinding en el adapter)

---

## Dataset crítico — coordinación entre módulos

Los 3 grupos del docente deben coincidir exactamente en `id`, `nombre`, `materia`, `totalAlumnos` y `fechaCreacion` entre `GrupoDocenteAdapter` (Plan 3) y `GrupoTareasDocenteAdapter` (Plan 4):

| id | nombre | materia | totalAlumnos | fechaCreacion |
|----|--------|---------|-------------|--------------|
| 1 | Programación Móvil 6A | Programación Móvil | 18 | 01/02/2026 |
| 2 | Bases de Datos 4B | Bases de Datos | 16 | 15/01/2026 |
| 3 | Cálculo Integral 2A | Cálculo Integral | 13 | 20/01/2026 |

`TareaDocenteAdapter` contiene 8 tareas dummy estáticas en `DATASET` (grupoId 1–3, bloqueId 1–3).

---

## Flujo de autenticación (`ui/auth/`)

- Entry: `SplashActivity` (LAUNCHER) → `LoginActivity` → por rol
- `LoginActivity` tiene **selector provisional de rol** (MODO DEMO): botón "Admin" → `MainAdminActivity`, botón "Docente" → `MainDocenteActivity`
- Registro: `RegistroActivity` con nav graph propio (`nav_registro.xml`) y 5 fragments de flujo
- **TODO:** integrar `LoginViewModel` con JWT y enrutar por rol automáticamente (eliminar selector provisional)

---

## Convenciones de layout XML

Ver skill `tecconnect-android-layouts` para reglas completas. Resumen:

- **Cero hex literals** — solo `@color/colorXxx` y `@drawable/bg_*`
- **No `BottomNavigationView`** — pill nav custom (`LinearLayout` horizontal, items 56dp)
- **MaterialCardView:** `cornerRadius=12dp`, `elevation=0dp`, `strokeWidth=0dp`
- **Chips de estado:** `TextView` con `@drawable/bg_chip_*` (docente=azul, estudiante=verde, pendiente=naranja, admin=morado)
- **Empty state:** `<include layout="@layout/layout_empty_state"/>` — nunca duplicar
- **RecyclerView con pill nav:** `paddingBottom="88dp"` + `clipToPadding="false"`
- **Chips de filtro** (checked state): `com.google.android.material.chip.Chip style="@style/Widget.Material3.Chip.Filter"` dentro de `HorizontalScrollView`

## Convenciones Java

Ver skill `tecconnect-android-java` para reglas completas. Resumen:

- `@AndroidEntryPoint` en toda Activity y Fragment
- ViewBinding: inflar en `onCreateView`, nulificar en `onDestroyView` (`binding = null`)
- Datos dummy: clase interna estática `*DummyXxx` dentro del adapter, método `cargarDatosDummy()` o similar
- TODOs de backend en español: `// TODO: llamar a XxxService.metodo()`
- Navegación con args: `Navigation.findNavController(requireView()).navigate(R.id.action_xxx, args)`
- NavOptions solo en tabs raíz (no en sub-destinos)

## Recursos importantes

- `app/src/main/res/values/colors.xml` — paleta completa
- `app/src/main/res/values/strings.xml` — todos los strings (organizados por módulo con comentarios)
- `app/src/main/res/drawable/` — `bg_chip_*`, `bg_avatar_placeholder`, `bg_card`, `bg_input`, `bg_accent_bar`, `bg_nav_container`, `ic_*`
- `app/src/main/res/navigation/nav_docente.xml` — todas las acciones del módulo docente declaradas
- `docs/superpowers/plans/` — planes de implementación originales (referencia histórica)
- `docs/superpowers/specs/2026-04-30-docente-module-design.md` — spec completa del módulo docente
