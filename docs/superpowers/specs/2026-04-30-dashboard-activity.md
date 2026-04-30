# Dashboard de Actividad — TecConnect Android

## Contexto
Pantalla de inicio diferenciada por rol (ADMINISTRADOR / DOCENTE) inspirada en Power BI / Microsoft Teams. Reemplaza cualquier Activity de inicio genérica. El JWT ya está guardado en SharedPreferences con key "jwt_token". Los skills del proyecto están en .claude/skills/tecconnect-android-layouts/SKILL.md y .claude/skills/tecconnect-android-java/SKILL.md — léelos antes de generar cualquier archivo.

## Objetivo
Implementar una pantalla de dashboard visualmente rica con:
- KPI cards en scroll horizontal por rol
- Gráfica principal con MPAndroidChart
- Lista de actividad reciente con RecyclerView
- Datos mock en ViewModel (sin Retrofit todavía)
- Animación de entrada escalonada en las cards

## Archivos a crear

### Layouts (res/layout/)
- activity_dashboard.xml
  - Raíz: NestedScrollView, fondo #F4F6FA
  - Header: avatar circular (Glide, 40dp) + nombre + rol + campana notificaciones
  - Sección KPIs: título "Resumen de actividad" + HorizontalScrollView con LinearLayout horizontal
  - Sección gráfica: MaterialCardView elevation 4dp con Chart (ver rol)
  - Sección actividad: título + RecyclerView vertical

- item_kpi_card.xml
  - MaterialCardView 160dp x 110dp, cornerRadius 14dp, cardBackgroundColor #FFFFFF
  - View strip 4dp ancho en borde izquierdo (color dinámico desde Java)
  - ImageView 22dp (ícono tintado) + TextView id=kpiValue 26sp bold + TextView id=kpiLabel 11sp #6B7280

- item_actividad_reciente.xml
  - ConstraintLayout, ancho match_parent, padding 12dp
  - Avatar circular 40dp color sólido con inicial (TextView centrado)
  - titulo 14sp bold, descripcion 12sp #6B7280 maxLines=1 ellipsis
  - timestamp 11sp #9CA3AF alineado derecha-arriba
  - Divider 0.5dp al fondo

### Java (com.conectatec)

- model/ActividadItem.java
  - Campos: String titulo, descripcion, timestamp; int tipoIcono, colorAvatar
  - Constantes: TIPO_USUARIO=1, TIPO_TAREA=2, TIPO_ENTREGA=3, TIPO_GRUPO=4, TIPO_MENSAJE=5, TIPO_AVISO=6

- model/DashboardStats.java
  - int usuariosTotales, docentesActivos, gruposActivos, tareasPublicadas, entregasPendientes, mensajesHoy
  - int[] usuariosPorRol (docentes/estudiantes/pendientes)
  - int[] entregasPorDia (últimos 7 días)

- model/DocenteStats.java
  - int misGrupos, tareasActivas, entregasHoy, pendientesCalificar, alumnosTotales
  - float promedioGeneral
  - int[] entregasPorDia (últimos 7 días)

- ui/dashboard/DashboardViewModel.java
  - Extiende ViewModel
  - MutableLiveData para adminStats, docenteStats, actividadReciente
  - cargarDatosAdmin(String token): datos mock realistas para ADMINISTRADOR
  - cargarDatosDocente(String token): datos mock realistas para DOCENTE

- ui/dashboard/ActividadAdapter.java
  - RecyclerView.Adapter con ViewHolder para item_actividad_reciente
  - onBindViewHolder: texto, GradientDrawable para avatar circular coloreado, inicial del título
  - Lista de ActividadItem

- ui/dashboard/DashboardActivity.java
  - Extiende AppCompatActivity, usa ViewBinding
  - onCreate: decodifica JWT (Base64 nativo Android, sin librerías externas), extrae claim "rol"
  - Según rol llama setupAdminDashboard() o setupDocenteDashboard()

  setupAdminDashboard() — 6 KPI cards:
    1. Usuarios Totales     | ic_people      | #6C63FF
    2. Docentes Activos     | ic_school      | #10B981
    3. Grupos Activos       | ic_group       | #F59E0B
    4. Tareas Publicadas    | ic_assignment  | #EF4444
    5. Pend. de Revisar     | ic_pending     | #3B82F6
    6. Mensajes Hoy         | ic_chat        | #8B5CF6
  Gráfica: BarChart "Usuarios por rol" (barras DOCENTE/ESTUDIANTE/PENDIENTE)
           animateY(1000), sin descripción nativa, sin leyenda nativa

  setupDocenteDashboard() — 6 KPI cards:
    1. Mis Grupos           | ic_class       | #6C63FF
    2. Tareas Activas       | ic_assignment  | #10B981
    3. Entregas Hoy         | ic_inbox       | #F59E0B
    4. Pend. Calificar      | ic_grading     | #EF4444
    5. Alumnos Totales      | ic_people      | #3B82F6
    6. Promedio General     | ic_bar_chart   | #8B5CF6
  Gráfica: LineChart "Entregas últimos 7 días" (cubicBezier, relleno area alpha 50)
           Eje X: días semana abreviados, animateY(800)

  Animaciones KPI cards: ObjectAnimator fade-in + slide-up escalonado, 80ms de delay entre cards
  Animación RecyclerView: DefaultItemAnimator

  Registra DashboardActivity en AndroidManifest.xml

## Dependencias (agregar si no están)
En build.gradle (Module: app):
  implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
  implementation 'com.google.android.material:material:1.11.0'
En settings.gradle (o build.gradle Project):
  maven { url 'https://jitpack.io' }

## Restricciones
- ViewBinding habilitado (ya activo en el proyecto)
- SDK mínimo API 26
- Solo modo claro por ahora
- Sin llamadas Retrofit todavía — todos los datos son mock en el ViewModel
- Los íconos son Vector Assets de Material Design (Android Studio > New > Vector Asset > Clip Art)
- Paleta: fondo #F4F6FA, cards #FFFFFF, primario #6C63FF, texto #1A1A2E, secundario #6B7280
