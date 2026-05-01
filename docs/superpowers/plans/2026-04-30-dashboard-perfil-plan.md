# Dashboard + Perfil Docente Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implementar las pantallas Dashboard (resumen + tareas recientes + avisos) y Perfil (datos del docente + configuración + cerrar sesión) del módulo Docente, reemplazando los stubs creados por el Plan 1.

**Architecture:** Espejo estructural de `AdminDashboardFragment` y `AdminPerfilFragment`. Datos dummy hardcodeados en arrays estáticos en cada Fragment. Sin adapter dedicado (ítems de tareas y avisos se renderizan de forma estática en el XML). Botón cerrar sesión replica el patrón estándar `Intent → LoginActivity` con flags `NEW_TASK | CLEAR_TASK`.

**Tech Stack:** Java 11, Hilt (`@AndroidEntryPoint`), ViewBinding, Material 3 Dark, Navigation Component, minSdk 24.

**Depende de:** Plan 1 (infra) completo — `MainDocenteActivity`, `nav_docente.xml`, stubs `DocenteDashboardFragment` y `DocentePerfilFragment` registrados en el grafo, drawables de pill nav, `LoginActivity` accesible.

**Skills auto-aplicables durante la ejecución:**
- `tecconnect-android-layouts` — al editar XML de layout.
- `tecconnect-android-java` — al editar Java de Fragments.

---

## File Structure

| Archivo | Acción | Responsabilidad |
|---|---|---|
| `app/src/main/res/values/strings.xml` | Modificar | Agregar 11 strings nuevos (sección Docente Dashboard + Perfil) |
| `app/src/main/res/layout/fragment_docente_dashboard.xml` | Crear | Layout del dashboard: header + 2 cards resumen + card tareas recientes + card avisos |
| `app/src/main/java/com/conectatec/ui/docente/dashboard/DocenteDashboardFragment.java` | Reemplazar (era stub del Plan 1) | Fragment con ViewBinding y listeners hacia tabs Grupos/Tareas |
| `app/src/main/res/layout/fragment_docente_perfil.xml` | Crear | Layout del perfil: header de marca + card hero + info de cuenta + configuración + botón logout |
| `app/src/main/java/com/conectatec/ui/docente/perfil/DocentePerfilFragment.java` | Reemplazar (era stub del Plan 1) | Fragment con ViewBinding y handler de cerrar sesión |

---

## Task Index

| # | Tarea | Tiempo estimado |
|---|---|---|
| 1 | Strings nuevos en `strings.xml` | 8 min |
| 2 | Layout `fragment_docente_dashboard.xml` | 25 min |
| 3 | Implementación `DocenteDashboardFragment.java` | 15 min |
| 4 | Verificación dashboard (build + grep) | 5 min |
| 5 | Layout `fragment_docente_perfil.xml` | 25 min |
| 6 | Implementación `DocentePerfilFragment.java` | 12 min |
| 7 | Verificación perfil (build + grep) | 5 min |
| 8 | Verificación final integrada | 8 min |

**Total estimado:** ~1h 45min

---

## Task 1: Strings nuevos para Dashboard y Perfil

**Files:**
- Modify: `app/src/main/res/values/strings.xml` (agregar al final, antes de `</resources>`)

- [ ] **Step 1: Verificar strings existentes que se reutilizarán**

Run:
```bash
grep -nE "btn_cerrar_sesion|label_info_cuenta|label_fecha_registro|cd_foto_perfil|label_docente|app_name" /home/crisgoat/Desarrollo/ConectaTec/app/src/main/res/values/strings.xml
```
Expected: las 6 keys deben aparecer (ya existen, no se duplican).

- [ ] **Step 2: Agregar bloque de strings nuevos**

Inserta el siguiente bloque inmediatamente antes de la etiqueta de cierre `</resources>` en `app/src/main/res/values/strings.xml`:

```xml
    <!-- ════════════════════════════════════════════════════════════
         Docente — Dashboard + Perfil (Plan 2)
         ════════════════════════════════════════════════════════════ -->
    <string name="subtitle_docente_dashboard">Hoy, 30 abr</string>
    <string name="label_grupos_activos">Grupos activos</string>
    <string name="label_alumnos_totales">Alumnos totales</string>
    <string name="label_tareas_recientes">Tareas recientes</string>
    <string name="label_avisos_del_dia">Avisos del día</string>
    <string name="btn_ver_todas_tareas">Ver todas</string>
    <string name="label_id_empleado">ID Empleado</string>
    <string name="label_departamento">Departamento</string>
    <string name="label_notificaciones">Notificaciones</string>
    <string name="label_tema">Tema</string>
    <string name="label_idioma">Idioma</string>
```

> **Nota:** `btn_cerrar_sesion`, `label_info_cuenta`, `label_fecha_registro`, `cd_foto_perfil`, `label_docente` y `app_name` ya existen — **no los redeclares** (provocan error de compilación por recurso duplicado).

- [ ] **Step 3: Compilar para validar el XML**

Run:
```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -5
```
Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 4: Commit**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
git add app/src/main/res/values/strings.xml
git commit -m "feat(strings): agregar strings para dashboard y perfil docente"
```

---

## Task 2: Layout `fragment_docente_dashboard.xml`

**Files:**
- Create: `app/src/main/res/layout/fragment_docente_dashboard.xml`

- [ ] **Step 1: Crear el layout completo**

Crea el archivo `app/src/main/res/layout/fragment_docente_dashboard.xml` con el siguiente contenido **exacto**:

```xml
<?xml version="1.0" encoding="utf-8"?>
<!-- Dashboard del Docente — Plan 2 -->
<ScrollView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/colorBackground"
    android:fillViewport="true">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:paddingBottom="100dp">

        <!-- ══ HEADER ══════════════════════════════════════════════ -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:gravity="center_vertical"
            android:background="@color/colorSurface"
            android:paddingTop="48dp"
            android:paddingBottom="20dp"
            android:paddingHorizontal="20dp">

            <View
                android:layout_width="4dp"
                android:layout_height="48dp"
                android:background="@drawable/bg_accent_bar"
                android:layout_marginEnd="14dp" />

            <LinearLayout
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:orientation="vertical">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="@string/app_name"
                    android:textColor="@color/colorPrimary"
                    android:textSize="10sp"
                    android:textStyle="bold"
                    android:letterSpacing="0.18"
                    android:layout_marginBottom="2dp" />

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Inicio"
                    android:textColor="@color/colorOnSurface"
                    android:textSize="26sp"
                    android:textStyle="bold"
                    android:fontFamily="sans-serif-medium" />

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="2dp"
                    android:text="Hola, Prof. Carlos"
                    android:textColor="@color/colorOnSurfaceVariant"
                    android:textSize="12sp"
                    android:letterSpacing="0.03" />

            </LinearLayout>

            <!-- Avatar Docente -->
            <FrameLayout
                android:layout_width="48dp"
                android:layout_height="48dp"
                android:layout_marginStart="16dp"
                android:background="@drawable/bg_chip_docente">

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:gravity="center"
                    android:text="CB"
                    android:textColor="@color/white"
                    android:textSize="16sp"
                    android:textStyle="bold" />

            </FrameLayout>

        </LinearLayout>

        <!-- Línea de acento -->
        <View
            android:layout_width="match_parent"
            android:layout_height="3dp"
            android:background="@color/colorPrimary" />

        <!-- ══ RESUMEN ════════════════════════════════════════════ -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:paddingHorizontal="16dp"
            android:paddingTop="20dp">

            <!-- Sección label -->
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:gravity="center_vertical"
                android:layout_marginBottom="14dp">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="RESUMEN"
                    android:textColor="@color/colorOnSurfaceVariant"
                    android:textSize="11sp"
                    android:textStyle="bold"
                    android:letterSpacing="0.10" />

                <View
                    android:layout_width="0dp"
                    android:layout_height="1dp"
                    android:layout_weight="1" />

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="@string/subtitle_docente_dashboard"
                    android:textColor="@color/colorOnSurfaceVariant"
                    android:textSize="11sp" />

            </LinearLayout>

            <!-- Fila: Grupos activos + Alumnos totales -->
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal">

                <!-- Card Grupos activos -->
                <com.google.android.material.card.MaterialCardView
                    android:id="@+id/cardGruposActivosDocente"
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:layout_marginEnd="5dp"
                    android:clickable="true"
                    android:focusable="true"
                    app:cardBackgroundColor="@color/colorSurface"
                    app:cardCornerRadius="12dp"
                    app:cardElevation="0dp"
                    app:strokeWidth="0dp">

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:orientation="vertical"
                        android:padding="16dp">

                        <LinearLayout
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:orientation="horizontal"
                            android:gravity="center_vertical"
                            android:layout_marginBottom="10dp">

                            <TextView
                                android:layout_width="0dp"
                                android:layout_height="wrap_content"
                                android:layout_weight="1"
                                android:text="@string/label_grupos_activos"
                                android:textColor="@color/colorOnSurfaceVariant"
                                android:textSize="11sp"
                                android:letterSpacing="0.03" />

                            <FrameLayout
                                android:layout_width="30dp"
                                android:layout_height="30dp"
                                android:background="@drawable/bg_chip_docente">

                                <ImageView
                                    android:layout_width="15dp"
                                    android:layout_height="15dp"
                                    android:layout_gravity="center"
                                    android:src="@android:drawable/ic_menu_share"
                                    android:contentDescription="@null"
                                    android:scaleType="fitCenter"
                                    app:tint="@color/white" />

                            </FrameLayout>

                        </LinearLayout>

                        <TextView
                            android:id="@+id/tvTotalGruposDashboardDocente"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="3"
                            android:textColor="@color/colorOnSurface"
                            android:textSize="28sp"
                            android:textStyle="bold"
                            android:fontFamily="sans-serif-medium" />

                    </LinearLayout>

                </com.google.android.material.card.MaterialCardView>

                <!-- Card Alumnos totales -->
                <com.google.android.material.card.MaterialCardView
                    android:id="@+id/cardAlumnosTotales"
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:layout_marginStart="5dp"
                    android:clickable="true"
                    android:focusable="true"
                    app:cardBackgroundColor="@color/colorSurface"
                    app:cardCornerRadius="12dp"
                    app:cardElevation="0dp"
                    app:strokeWidth="0dp">

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:orientation="vertical"
                        android:padding="16dp">

                        <LinearLayout
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:orientation="horizontal"
                            android:gravity="center_vertical"
                            android:layout_marginBottom="10dp">

                            <TextView
                                android:layout_width="0dp"
                                android:layout_height="wrap_content"
                                android:layout_weight="1"
                                android:text="@string/label_alumnos_totales"
                                android:textColor="@color/colorOnSurfaceVariant"
                                android:textSize="11sp"
                                android:letterSpacing="0.03" />

                            <FrameLayout
                                android:layout_width="30dp"
                                android:layout_height="30dp"
                                android:background="@drawable/bg_chip_estudiante">

                                <ImageView
                                    android:layout_width="15dp"
                                    android:layout_height="15dp"
                                    android:layout_gravity="center"
                                    android:src="@android:drawable/ic_menu_manage"
                                    android:contentDescription="@null"
                                    android:scaleType="fitCenter"
                                    app:tint="@color/white" />

                            </FrameLayout>

                        </LinearLayout>

                        <TextView
                            android:id="@+id/tvTotalAlumnos"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="47"
                            android:textColor="@color/colorOnSurface"
                            android:textSize="28sp"
                            android:textStyle="bold"
                            android:fontFamily="sans-serif-medium" />

                    </LinearLayout>

                </com.google.android.material.card.MaterialCardView>

            </LinearLayout>

        </LinearLayout>

        <!-- ══ TAREAS RECIENTES ════════════════════════════════════ -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:paddingHorizontal="16dp"
            android:paddingTop="28dp">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:gravity="center_vertical"
                android:layout_marginBottom="14dp">

                <TextView
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:text="@string/label_tareas_recientes"
                    android:textColor="@color/colorOnSurfaceVariant"
                    android:textSize="12sp"
                    android:textStyle="bold"
                    android:letterSpacing="0.08" />

                <TextView
                    android:id="@+id/tvVerTodasTareasDashboard"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="@string/btn_ver_todas_tareas"
                    android:textColor="@color/colorPrimary"
                    android:textSize="12sp"
                    android:textStyle="bold"
                    android:clickable="true"
                    android:focusable="true" />

            </LinearLayout>

            <com.google.android.material.card.MaterialCardView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:cardBackgroundColor="@color/colorSurface"
                app:cardCornerRadius="12dp"
                app:cardElevation="0dp"
                app:strokeWidth="0dp">

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="vertical"
                    android:paddingVertical="4dp">

                    <!-- Tarea 1: PROYECTO -->
                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:orientation="vertical"
                        android:padding="16dp">

                        <LinearLayout
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:orientation="horizontal"
                            android:gravity="center_vertical"
                            android:layout_marginBottom="4dp">

                            <TextView
                                android:layout_width="wrap_content"
                                android:layout_height="wrap_content"
                                android:text="PROYECTO"
                                android:textColor="@color/white"
                                android:textSize="9sp"
                                android:textStyle="bold"
                                android:letterSpacing="0.08"
                                android:paddingHorizontal="8dp"
                                android:paddingVertical="2dp"
                                android:background="@drawable/bg_chip_estudiante"
                                android:layout_marginEnd="8dp" />

                            <TextView
                                android:layout_width="0dp"
                                android:layout_height="wrap_content"
                                android:layout_weight="1"
                                android:text="hace 2h"
                                android:textColor="@color/colorOnSurfaceVariant"
                                android:textSize="10sp" />

                        </LinearLayout>

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="Proyecto Final POO"
                            android:textColor="@color/colorOnSurface"
                            android:textSize="14sp"
                            android:textStyle="bold" />

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="2dp"
                            android:text="Prog. Móvil 6A · vence 03/05/2026"
                            android:textColor="@color/colorOnSurfaceVariant"
                            android:textSize="12sp" />

                    </LinearLayout>

                    <View
                        android:layout_width="match_parent"
                        android:layout_height="1dp"
                        android:background="@color/colorDivider"
                        android:layout_marginHorizontal="16dp" />

                    <!-- Tarea 2: EXAMEN -->
                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:orientation="vertical"
                        android:padding="16dp">

                        <LinearLayout
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:orientation="horizontal"
                            android:gravity="center_vertical"
                            android:layout_marginBottom="4dp">

                            <TextView
                                android:layout_width="wrap_content"
                                android:layout_height="wrap_content"
                                android:text="EXAMEN"
                                android:textColor="@color/white"
                                android:textSize="9sp"
                                android:textStyle="bold"
                                android:letterSpacing="0.08"
                                android:paddingHorizontal="8dp"
                                android:paddingVertical="2dp"
                                android:background="@drawable/bg_chip_admin"
                                android:layout_marginEnd="8dp" />

                            <TextView
                                android:layout_width="0dp"
                                android:layout_height="wrap_content"
                                android:layout_weight="1"
                                android:text="hace 5h"
                                android:textColor="@color/colorOnSurfaceVariant"
                                android:textSize="10sp" />

                        </LinearLayout>

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="Examen parcial 2"
                            android:textColor="@color/colorOnSurface"
                            android:textSize="14sp"
                            android:textStyle="bold" />

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="2dp"
                            android:text="BD 4B · vence 02/05/2026"
                            android:textColor="@color/colorOnSurfaceVariant"
                            android:textSize="12sp" />

                    </LinearLayout>

                    <View
                        android:layout_width="match_parent"
                        android:layout_height="1dp"
                        android:background="@color/colorDivider"
                        android:layout_marginHorizontal="16dp" />

                    <!-- Tarea 3: TAREA -->
                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:orientation="vertical"
                        android:padding="16dp">

                        <LinearLayout
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:orientation="horizontal"
                            android:gravity="center_vertical"
                            android:layout_marginBottom="4dp">

                            <TextView
                                android:layout_width="wrap_content"
                                android:layout_height="wrap_content"
                                android:text="TAREA"
                                android:textColor="@color/white"
                                android:textSize="9sp"
                                android:textStyle="bold"
                                android:letterSpacing="0.08"
                                android:paddingHorizontal="8dp"
                                android:paddingVertical="2dp"
                                android:background="@drawable/bg_chip_docente"
                                android:layout_marginEnd="8dp" />

                            <TextView
                                android:layout_width="0dp"
                                android:layout_height="wrap_content"
                                android:layout_weight="1"
                                android:text="ayer"
                                android:textColor="@color/colorOnSurfaceVariant"
                                android:textSize="10sp" />

                        </LinearLayout>

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="Tarea 5: Integrales"
                            android:textColor="@color/colorOnSurface"
                            android:textSize="14sp"
                            android:textStyle="bold" />

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="2dp"
                            android:text="Cálculo 2A · vence 30/04/2026"
                            android:textColor="@color/colorOnSurfaceVariant"
                            android:textSize="12sp" />

                    </LinearLayout>

                </LinearLayout>

            </com.google.android.material.card.MaterialCardView>

        </LinearLayout>

        <!-- ══ AVISOS DEL DÍA ══════════════════════════════════════ -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:paddingHorizontal="16dp"
            android:paddingTop="24dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/label_avisos_del_dia"
                android:textColor="@color/colorOnSurfaceVariant"
                android:textSize="12sp"
                android:textStyle="bold"
                android:letterSpacing="0.08"
                android:layout_marginBottom="14dp" />

            <com.google.android.material.card.MaterialCardView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:cardBackgroundColor="@color/colorSurface"
                app:cardCornerRadius="12dp"
                app:cardElevation="0dp"
                app:strokeWidth="0dp">

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="vertical"
                    android:paddingVertical="4dp">

                    <!-- Aviso 1 -->
                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:orientation="vertical"
                        android:padding="16dp">

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="Cambio de horario"
                            android:textColor="@color/colorOnSurface"
                            android:textSize="14sp"
                            android:textStyle="bold" />

                        <LinearLayout
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:orientation="horizontal"
                            android:layout_marginTop="2dp">

                            <TextView
                                android:layout_width="0dp"
                                android:layout_height="wrap_content"
                                android:layout_weight="1"
                                android:text="Prog. Móvil 6A"
                                android:textColor="@color/colorOnSurfaceVariant"
                                android:textSize="12sp" />

                            <TextView
                                android:layout_width="wrap_content"
                                android:layout_height="wrap_content"
                                android:text="hace 30 min"
                                android:textColor="@color/colorOnSurfaceVariant"
                                android:textSize="11sp" />

                        </LinearLayout>

                    </LinearLayout>

                    <View
                        android:layout_width="match_parent"
                        android:layout_height="1dp"
                        android:background="@color/colorDivider"
                        android:layout_marginHorizontal="16dp" />

                    <!-- Aviso 2 -->
                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:orientation="vertical"
                        android:padding="16dp">

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="Material adicional subido"
                            android:textColor="@color/colorOnSurface"
                            android:textSize="14sp"
                            android:textStyle="bold" />

                        <LinearLayout
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:orientation="horizontal"
                            android:layout_marginTop="2dp">

                            <TextView
                                android:layout_width="0dp"
                                android:layout_height="wrap_content"
                                android:layout_weight="1"
                                android:text="BD 4B"
                                android:textColor="@color/colorOnSurfaceVariant"
                                android:textSize="12sp" />

                            <TextView
                                android:layout_width="wrap_content"
                                android:layout_height="wrap_content"
                                android:text="hace 2h"
                                android:textColor="@color/colorOnSurfaceVariant"
                                android:textSize="11sp" />

                        </LinearLayout>

                    </LinearLayout>

                    <View
                        android:layout_width="match_parent"
                        android:layout_height="1dp"
                        android:background="@color/colorDivider"
                        android:layout_marginHorizontal="16dp" />

                    <!-- Aviso 3 -->
                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:orientation="vertical"
                        android:padding="16dp">

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="Recordatorio examen"
                            android:textColor="@color/colorOnSurface"
                            android:textSize="14sp"
                            android:textStyle="bold" />

                        <LinearLayout
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:orientation="horizontal"
                            android:layout_marginTop="2dp">

                            <TextView
                                android:layout_width="0dp"
                                android:layout_height="wrap_content"
                                android:layout_weight="1"
                                android:text="Cálculo 2A"
                                android:textColor="@color/colorOnSurfaceVariant"
                                android:textSize="12sp" />

                            <TextView
                                android:layout_width="wrap_content"
                                android:layout_height="wrap_content"
                                android:text="hace 4h"
                                android:textColor="@color/colorOnSurfaceVariant"
                                android:textSize="11sp" />

                        </LinearLayout>

                    </LinearLayout>

                </LinearLayout>

            </com.google.android.material.card.MaterialCardView>

        </LinearLayout>

    </LinearLayout>

</ScrollView>
```

- [ ] **Step 2: Compilar para validar el layout**

Run:
```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -5
```
Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 3: Verificar cero hex literales en el layout**

Run:
```bash
grep -n "#[0-9A-Fa-f]\{6\}" /home/crisgoat/Desarrollo/ConectaTec/app/src/main/res/layout/fragment_docente_dashboard.xml
```
Expected: salida vacía.

- [ ] **Step 4: Commit**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
git add app/src/main/res/layout/fragment_docente_dashboard.xml
git commit -m "feat(docente): layout fragment_docente_dashboard.xml con resumen, tareas y avisos"
```

---

## Task 3: Implementación `DocenteDashboardFragment.java`

**Files:**
- Modify (reemplaza el stub creado en Plan 1): `app/src/main/java/com/conectatec/ui/docente/dashboard/DocenteDashboardFragment.java`

- [ ] **Step 1: Verificar que el stub existe (creado por Plan 1)**

Run:
```bash
ls /home/crisgoat/Desarrollo/ConectaTec/app/src/main/java/com/conectatec/ui/docente/dashboard/DocenteDashboardFragment.java
```
Expected: el archivo existe (stub del Plan 1). Si no existe, **deten el plan** y completa el Plan 1 antes.

- [ ] **Step 2: Reemplazar el contenido completo del Fragment**

Sustituye **todo** el contenido de `app/src/main/java/com/conectatec/ui/docente/dashboard/DocenteDashboardFragment.java` por:

```java
package com.conectatec.ui.docente.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.databinding.FragmentDocenteDashboardBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Dashboard del Docente.
 *
 * Muestra:
 *  - Resumen con 2 cards (Grupos activos, Alumnos totales).
 *  - Card de "Tareas recientes" (3 entradas dummy hardcodeadas en el XML).
 *  - Card de "Avisos del día" (3 entradas dummy hardcodeadas en el XML).
 *
 * Datos dummy en arrays estáticos abajo (referencia para integración futura con backend).
 *
 * Las dos cards de resumen navegan a la pestaña Grupos. El "Ver todas" de tareas
 * navega a la pestaña Tareas. Ambas usan {@link #navigateToTab(View, int)} para
 * mantener el estado de la pill nav consistente con {@code AdminDashboardFragment}.
 */
@AndroidEntryPoint
public class DocenteDashboardFragment extends Fragment {

    private FragmentDocenteDashboardBinding binding;

    /** Tareas recientes mostradas en el dashboard (dummy estático). */
    private static final String[][] TAREAS_RECIENTES = {
            // {tipo, titulo, grupo, fechaVence, tiempoRelativo}
            {"PROYECTO", "Proyecto Final POO", "Prog. Móvil 6A", "03/05/2026", "hace 2h"},
            {"EXAMEN",   "Examen parcial 2",   "BD 4B",          "02/05/2026", "hace 5h"},
            {"TAREA",    "Tarea 5: Integrales", "Cálculo 2A",    "30/04/2026", "ayer"}
    };

    /** Avisos del día mostrados en el dashboard (dummy estático). */
    private static final String[][] AVISOS_DEL_DIA = {
            // {titulo, grupo, tiempoRelativo}
            {"Cambio de horario",            "Prog. Móvil 6A", "hace 30 min"},
            {"Material adicional subido",    "BD 4B",          "hace 2h"},
            {"Recordatorio examen",          "Cálculo 2A",     "hace 4h"}
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupListeners();
    }

    private void setupListeners() {
        // Ambas cards de resumen llevan a la pestaña Grupos.
        binding.cardGruposActivosDocente.setOnClickListener(v ->
                navigateToTab(v, R.id.docenteGruposFragment));

        binding.cardAlumnosTotales.setOnClickListener(v ->
                navigateToTab(v, R.id.docenteGruposFragment));

        // "Ver todas" en tareas recientes → tab Tareas.
        binding.tvVerTodasTareasDashboard.setOnClickListener(v ->
                navigateToTab(v, R.id.docenteTareasFragment));
    }

    /**
     * Navega a una pestaña raíz con el mismo comportamiento que el pill nav,
     * preservando estado y evitando apilar copias del destino.
     */
    private void navigateToTab(View v, int destId) {
        NavController nav = Navigation.findNavController(v);
        NavOptions opts = new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(true)
                .setPopUpTo(nav.getGraph().getStartDestinationId(), false, true)
                .build();
        nav.navigate(destId, null, opts);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
```

> **Nota sobre IDs de destino:** los IDs `R.id.docenteGruposFragment` y `R.id.docenteTareasFragment` son los destinos raíz declarados por Plan 1 en `nav_docente.xml`. Si el Plan 1 los nombró distinto, ajusta solo esas dos referencias.

- [ ] **Step 3: Compilar**

Run:
```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -5
```
Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 4: Commit**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
git add app/src/main/java/com/conectatec/ui/docente/dashboard/DocenteDashboardFragment.java
git commit -m "feat(docente): implementar DocenteDashboardFragment con datos dummy y navegación a tabs"
```

---

## Task 4: Verificación dashboard

**Files:** ninguno modificado.

- [ ] **Step 1: Build limpio**

Run:
```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -5
```
Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 2: Cero hex en el layout del dashboard**

Run:
```bash
grep -n "#[0-9A-Fa-f]\{6\}" /home/crisgoat/Desarrollo/ConectaTec/app/src/main/res/layout/fragment_docente_dashboard.xml
```
Expected: salida vacía.

- [ ] **Step 3: `@AndroidEntryPoint` y liberación de binding**

Run:
```bash
grep -n "@AndroidEntryPoint" /home/crisgoat/Desarrollo/ConectaTec/app/src/main/java/com/conectatec/ui/docente/dashboard/DocenteDashboardFragment.java
grep -n "binding = null" /home/crisgoat/Desarrollo/ConectaTec/app/src/main/java/com/conectatec/ui/docente/dashboard/DocenteDashboardFragment.java
```
Expected: ambas grep devuelven una línea cada una.

- [ ] **Step 4: IDs de binding referenciados existen en el XML**

Run:
```bash
for id in cardGruposActivosDocente cardAlumnosTotales tvVerTodasTareasDashboard tvTotalGruposDashboardDocente tvTotalAlumnos; do
  echo -n "$id: ";
  grep -c "@+id/$id" /home/crisgoat/Desarrollo/ConectaTec/app/src/main/res/layout/fragment_docente_dashboard.xml;
done
```
Expected: cada ID con conteo `1`.

- [ ] **Step 5: BottomNavigationView no se introdujo**

Run:
```bash
grep -rn "BottomNavigationView" /home/crisgoat/Desarrollo/ConectaTec/app/src/main/java/com/conectatec/ui/docente/dashboard/ /home/crisgoat/Desarrollo/ConectaTec/app/src/main/res/layout/fragment_docente_dashboard.xml
```
Expected: salida vacía.

- [ ] **Step 6: No commit (verificación pura — sin cambios pendientes).**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec && git status --short
```
Expected: working tree clean.

---

## Task 5: Layout `fragment_docente_perfil.xml`

**Files:**
- Create: `app/src/main/res/layout/fragment_docente_perfil.xml`

- [ ] **Step 1: Crear el layout completo**

Crea el archivo `app/src/main/res/layout/fragment_docente_perfil.xml` con el siguiente contenido **exacto**:

```xml
<?xml version="1.0" encoding="utf-8"?>
<!-- Perfil del Docente — Plan 2 -->
<ScrollView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/colorBackground"
    android:fillViewport="true">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:paddingHorizontal="20dp"
        android:paddingTop="0dp"
        android:paddingBottom="100dp">

        <!-- ══ HEADER MARCA ══════════════════════════════════════════ -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:gravity="center_vertical"
            android:paddingTop="48dp"
            android:paddingBottom="20dp">

            <View
                android:layout_width="4dp"
                android:layout_height="48dp"
                android:background="@drawable/bg_accent_bar"
                android:layout_marginEnd="14dp" />

            <LinearLayout
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:orientation="vertical">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="@string/app_name"
                    android:textColor="@color/colorPrimary"
                    android:textSize="10sp"
                    android:textStyle="bold"
                    android:letterSpacing="0.18"
                    android:layout_marginBottom="4dp" />

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Mi perfil"
                    android:textColor="@color/colorOnSurface"
                    android:textSize="22sp"
                    android:textStyle="bold"
                    android:fontFamily="sans-serif-medium" />

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="2dp"
                    android:text="Docente"
                    android:textColor="@color/colorOnSurfaceVariant"
                    android:textSize="12sp"
                    android:letterSpacing="0.03" />

            </LinearLayout>

        </LinearLayout>

        <!-- ══ CARD HERO ════════════════════════════════════════════ -->
        <com.google.android.material.card.MaterialCardView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="20dp"
            app:cardBackgroundColor="@color/colorSurface"
            app:cardCornerRadius="12dp"
            app:cardElevation="0dp"
            app:strokeWidth="0dp">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:gravity="center"
                android:padding="24dp">

                <FrameLayout
                    android:layout_width="88dp"
                    android:layout_height="88dp"
                    android:background="@drawable/bg_chip_docente">

                    <TextView
                        android:id="@+id/tvInicialesPerfilDocente"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:gravity="center"
                        android:text="CB"
                        android:textColor="@color/white"
                        android:textSize="32sp"
                        android:textStyle="bold" />

                </FrameLayout>

                <TextView
                    android:id="@+id/tvNombrePerfilDocente"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="14dp"
                    android:text="Prof. Carlos Bautista"
                    android:textColor="@color/colorOnSurface"
                    android:textSize="22sp"
                    android:textStyle="bold"
                    android:fontFamily="sans-serif-medium"
                    android:gravity="center" />

                <TextView
                    android:id="@+id/tvCorreoPerfilDocente"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="4dp"
                    android:text="carlos.bautista@tec.mx"
                    android:textColor="@color/colorOnSurfaceVariant"
                    android:textSize="14sp"
                    android:gravity="center" />

                <TextView
                    android:id="@+id/tvChipRolPerfilDocente"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    android:text="@string/label_docente"
                    android:textColor="@color/white"
                    android:textSize="11sp"
                    android:textStyle="bold"
                    android:letterSpacing="0.05"
                    android:paddingHorizontal="14dp"
                    android:paddingVertical="5dp"
                    android:background="@drawable/bg_chip_docente" />

            </LinearLayout>

        </com.google.android.material.card.MaterialCardView>

        <!-- ══ CARD INFORMACIÓN DE CUENTA ═══════════════════════════ -->
        <com.google.android.material.card.MaterialCardView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="16dp"
            app:cardBackgroundColor="@color/colorSurface"
            app:cardCornerRadius="12dp"
            app:cardElevation="0dp"
            app:strokeWidth="0dp">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:padding="20dp">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="@string/label_info_cuenta"
                    android:textColor="@color/colorOnSurfaceVariant"
                    android:textSize="12sp"
                    android:textStyle="bold"
                    android:letterSpacing="0.08"
                    android:layout_marginBottom="14dp" />

                <View
                    android:layout_width="match_parent"
                    android:layout_height="1dp"
                    android:background="@color/colorDivider"
                    android:layout_marginBottom="14dp" />

                <!-- ID Empleado -->
                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:gravity="center_vertical"
                    android:layout_marginBottom="12dp">

                    <TextView
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        android:layout_weight="1"
                        android:text="@string/label_id_empleado"
                        android:textColor="@color/colorOnSurfaceVariant"
                        android:textSize="13sp" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="EMP-2018-CB"
                        android:textColor="@color/colorOnSurface"
                        android:textSize="13sp"
                        android:textStyle="bold" />

                </LinearLayout>

                <!-- Fecha de registro -->
                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:gravity="center_vertical"
                    android:layout_marginBottom="12dp">

                    <TextView
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        android:layout_weight="1"
                        android:text="@string/label_fecha_registro"
                        android:textColor="@color/colorOnSurfaceVariant"
                        android:textSize="13sp" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="01/02/2018"
                        android:textColor="@color/colorOnSurface"
                        android:textSize="13sp"
                        android:textStyle="bold" />

                </LinearLayout>

                <!-- Departamento -->
                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:gravity="center_vertical"
                    android:layout_marginBottom="12dp">

                    <TextView
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        android:layout_weight="1"
                        android:text="@string/label_departamento"
                        android:textColor="@color/colorOnSurfaceVariant"
                        android:textSize="13sp" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Sistemas Computacionales"
                        android:textColor="@color/colorOnSurface"
                        android:textSize="13sp"
                        android:textStyle="bold" />

                </LinearLayout>

                <!-- Estado de cuenta -->
                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:gravity="center_vertical">

                    <TextView
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        android:layout_weight="1"
                        android:text="Estado"
                        android:textColor="@color/colorOnSurfaceVariant"
                        android:textSize="13sp" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Activa"
                        android:textColor="@color/colorSuccess"
                        android:textSize="13sp"
                        android:textStyle="bold" />

                </LinearLayout>

            </LinearLayout>

        </com.google.android.material.card.MaterialCardView>

        <!-- ══ CARD CONFIGURACIÓN ══════════════════════════════════ -->
        <com.google.android.material.card.MaterialCardView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="20dp"
            app:cardBackgroundColor="@color/colorSurface"
            app:cardCornerRadius="12dp"
            app:cardElevation="0dp"
            app:strokeWidth="0dp">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:paddingVertical="6dp">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginHorizontal="20dp"
                    android:layout_marginTop="14dp"
                    android:layout_marginBottom="6dp"
                    android:text="CONFIGURACIÓN"
                    android:textColor="@color/colorOnSurfaceVariant"
                    android:textSize="12sp"
                    android:textStyle="bold"
                    android:letterSpacing="0.08" />

                <!-- Notificaciones -->
                <LinearLayout
                    android:id="@+id/rowNotificacionesDocente"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:gravity="center_vertical"
                    android:padding="20dp"
                    android:background="?attr/selectableItemBackground"
                    android:clickable="true"
                    android:focusable="true">

                    <TextView
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        android:layout_weight="1"
                        android:text="@string/label_notificaciones"
                        android:textColor="@color/colorOnSurface"
                        android:textSize="14sp" />

                    <ImageView
                        android:layout_width="20dp"
                        android:layout_height="20dp"
                        android:src="@android:drawable/ic_media_play"
                        android:contentDescription="@null"
                        app:tint="@color/colorOnSurfaceVariant" />

                </LinearLayout>

                <View
                    android:layout_width="match_parent"
                    android:layout_height="1dp"
                    android:background="@color/colorDivider"
                    android:layout_marginHorizontal="20dp" />

                <!-- Tema -->
                <LinearLayout
                    android:id="@+id/rowTemaDocente"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:gravity="center_vertical"
                    android:padding="20dp"
                    android:background="?attr/selectableItemBackground"
                    android:clickable="true"
                    android:focusable="true">

                    <TextView
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        android:layout_weight="1"
                        android:text="@string/label_tema"
                        android:textColor="@color/colorOnSurface"
                        android:textSize="14sp" />

                    <ImageView
                        android:layout_width="20dp"
                        android:layout_height="20dp"
                        android:src="@android:drawable/ic_media_play"
                        android:contentDescription="@null"
                        app:tint="@color/colorOnSurfaceVariant" />

                </LinearLayout>

                <View
                    android:layout_width="match_parent"
                    android:layout_height="1dp"
                    android:background="@color/colorDivider"
                    android:layout_marginHorizontal="20dp" />

                <!-- Idioma -->
                <LinearLayout
                    android:id="@+id/rowIdiomaDocente"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:gravity="center_vertical"
                    android:padding="20dp"
                    android:background="?attr/selectableItemBackground"
                    android:clickable="true"
                    android:focusable="true">

                    <TextView
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        android:layout_weight="1"
                        android:text="@string/label_idioma"
                        android:textColor="@color/colorOnSurface"
                        android:textSize="14sp" />

                    <ImageView
                        android:layout_width="20dp"
                        android:layout_height="20dp"
                        android:src="@android:drawable/ic_media_play"
                        android:contentDescription="@null"
                        app:tint="@color/colorOnSurfaceVariant" />

                </LinearLayout>

            </LinearLayout>

        </com.google.android.material.card.MaterialCardView>

        <!-- ══ BOTÓN CERRAR SESIÓN ══════════════════════════════════ -->
        <com.google.android.material.button.MaterialButton
            android:id="@+id/btnCerrarSesionDocente"
            style="@style/Widget.Material3.Button.OutlinedButton"
            android:layout_width="match_parent"
            android:layout_height="52dp"
            android:layout_marginTop="8dp"
            android:text="@string/btn_cerrar_sesion"
            android:textSize="15sp"
            android:textColor="@color/colorError"
            android:letterSpacing="0.05"
            app:strokeColor="@color/colorError"
            app:cornerRadius="10dp"
            android:stateListAnimator="@null" />

    </LinearLayout>

</ScrollView>
```

- [ ] **Step 2: Compilar para validar el layout**

Run:
```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -5
```
Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 3: Verificar cero hex literales**

Run:
```bash
grep -n "#[0-9A-Fa-f]\{6\}" /home/crisgoat/Desarrollo/ConectaTec/app/src/main/res/layout/fragment_docente_perfil.xml
```
Expected: salida vacía.

- [ ] **Step 4: Commit**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
git add app/src/main/res/layout/fragment_docente_perfil.xml
git commit -m "feat(docente): layout fragment_docente_perfil.xml con hero, info de cuenta y configuración"
```

---

## Task 6: Implementación `DocentePerfilFragment.java`

**Files:**
- Modify (reemplaza el stub creado en Plan 1): `app/src/main/java/com/conectatec/ui/docente/perfil/DocentePerfilFragment.java`

- [ ] **Step 1: Verificar que el stub y `LoginActivity` existen**

Run:
```bash
ls /home/crisgoat/Desarrollo/ConectaTec/app/src/main/java/com/conectatec/ui/docente/perfil/DocentePerfilFragment.java
find /home/crisgoat/Desarrollo/ConectaTec/app/src/main/java -name "LoginActivity.java"
```
Expected: el stub existe y `LoginActivity.java` aparece en algún paquete bajo `com.conectatec.ui.auth` (lo creó la rama `feat/implement-vistas-auth`). Si la ruta no es `com.conectatec.ui.auth.LoginActivity`, ajusta el import en el Step 2.

- [ ] **Step 2: Reemplazar el contenido completo del Fragment**

Sustituye **todo** el contenido de `app/src/main/java/com/conectatec/ui/docente/perfil/DocentePerfilFragment.java` por:

```java
package com.conectatec.ui.docente.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.conectatec.databinding.FragmentDocentePerfilBinding;
import com.conectatec.ui.auth.LoginActivity;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Perfil del Docente.
 *
 * Muestra:
 *  - Header de marca "TecConnect / Mi perfil / Docente".
 *  - Card hero con avatar de iniciales, nombre, correo y chip de rol.
 *  - Card "Información de cuenta" con ID empleado, fecha de registro,
 *    departamento y estado.
 *  - Card "Configuración" con 3 filas (Notificaciones / Tema / Idioma)
 *    como placeholders — sin pantalla destino aún.
 *  - Botón outlined "Cerrar sesión" que vuelve a {@link LoginActivity}.
 *
 * Datos hardcodeados en el XML (dummy estático).
 */
@AndroidEntryPoint
public class DocentePerfilFragment extends Fragment {

    private FragmentDocentePerfilBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocentePerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupListeners();
    }

    private void setupListeners() {
        // Filas de configuración: aún no existe pantalla destino.
        // TODO: implementar pantalla de configuración
        binding.rowNotificacionesDocente.setOnClickListener(v -> { /* placeholder */ });
        binding.rowTemaDocente.setOnClickListener(v -> { /* placeholder */ });
        binding.rowIdiomaDocente.setOnClickListener(v -> { /* placeholder */ });

        binding.btnCerrarSesionDocente.setOnClickListener(v -> cerrarSesion());
    }

    /**
     * Vuelve al Login limpiando el back stack. Usa el patrón estándar del proyecto:
     * flags NEW_TASK + CLEAR_TASK + animación fade.
     */
    private void cerrarSesion() {
        // TODO: llamar a SessionService.cerrarSesion()
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().overridePendingTransition(
                android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
```

- [ ] **Step 3: Compilar**

Run:
```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -5
```
Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 4: Commit**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
git add app/src/main/java/com/conectatec/ui/docente/perfil/DocentePerfilFragment.java
git commit -m "feat(docente): implementar DocentePerfilFragment con cierre de sesión a LoginActivity"
```

---

## Task 7: Verificación perfil

**Files:** ninguno modificado.

- [ ] **Step 1: Build limpio**

Run:
```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -5
```
Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 2: Cero hex en el layout del perfil**

Run:
```bash
grep -n "#[0-9A-Fa-f]\{6\}" /home/crisgoat/Desarrollo/ConectaTec/app/src/main/res/layout/fragment_docente_perfil.xml
```
Expected: salida vacía.

- [ ] **Step 3: `@AndroidEntryPoint`, `binding = null` y TODO en español**

Run:
```bash
grep -nE "@AndroidEntryPoint|binding = null|TODO: llamar a SessionService" /home/crisgoat/Desarrollo/ConectaTec/app/src/main/java/com/conectatec/ui/docente/perfil/DocentePerfilFragment.java
```
Expected: 3 líneas (una por cada patrón).

- [ ] **Step 4: IDs de binding referenciados existen en el XML**

Run:
```bash
for id in btnCerrarSesionDocente rowNotificacionesDocente rowTemaDocente rowIdiomaDocente tvInicialesPerfilDocente tvNombrePerfilDocente tvCorreoPerfilDocente tvChipRolPerfilDocente; do
  echo -n "$id: ";
  grep -c "@+id/$id" /home/crisgoat/Desarrollo/ConectaTec/app/src/main/res/layout/fragment_docente_perfil.xml;
done
```
Expected: cada ID con conteo `1`.

- [ ] **Step 5: BottomNavigationView no se introdujo**

Run:
```bash
grep -rn "BottomNavigationView" /home/crisgoat/Desarrollo/ConectaTec/app/src/main/java/com/conectatec/ui/docente/perfil/ /home/crisgoat/Desarrollo/ConectaTec/app/src/main/res/layout/fragment_docente_perfil.xml
```
Expected: salida vacía.

- [ ] **Step 6: No commit (verificación pura — sin cambios pendientes).**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec && git status --short
```
Expected: working tree clean.

---

## Task 8: Verificación final integrada

**Files:** ninguno modificado.

- [ ] **Step 1: Build completo limpio**

Run:
```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -5
```
Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 2: Cero hex literales en ninguno de los dos layouts del Plan 2**

Run:
```bash
grep -rn "#[0-9A-Fa-f]\{6\}" \
  /home/crisgoat/Desarrollo/ConectaTec/app/src/main/res/layout/fragment_docente_dashboard.xml \
  /home/crisgoat/Desarrollo/ConectaTec/app/src/main/res/layout/fragment_docente_perfil.xml
```
Expected: salida vacía.

- [ ] **Step 3: Cero `BottomNavigationView` en módulo docente (dashboard + perfil)**

Run:
```bash
grep -rn "BottomNavigationView" \
  /home/crisgoat/Desarrollo/ConectaTec/app/src/main/java/com/conectatec/ui/docente/dashboard/ \
  /home/crisgoat/Desarrollo/ConectaTec/app/src/main/java/com/conectatec/ui/docente/perfil/ \
  /home/crisgoat/Desarrollo/ConectaTec/app/src/main/res/layout/fragment_docente_dashboard.xml \
  /home/crisgoat/Desarrollo/ConectaTec/app/src/main/res/layout/fragment_docente_perfil.xml
```
Expected: salida vacía.

- [ ] **Step 4: `@AndroidEntryPoint` en ambos Fragments**

Run:
```bash
grep -L "@AndroidEntryPoint" \
  /home/crisgoat/Desarrollo/ConectaTec/app/src/main/java/com/conectatec/ui/docente/dashboard/DocenteDashboardFragment.java \
  /home/crisgoat/Desarrollo/ConectaTec/app/src/main/java/com/conectatec/ui/docente/perfil/DocentePerfilFragment.java
```
Expected: salida vacía (los dos archivos tienen la anotación).

- [ ] **Step 5: `binding = null` en `onDestroyView` de ambos Fragments**

Run:
```bash
grep -L "binding = null" \
  /home/crisgoat/Desarrollo/ConectaTec/app/src/main/java/com/conectatec/ui/docente/dashboard/DocenteDashboardFragment.java \
  /home/crisgoat/Desarrollo/ConectaTec/app/src/main/java/com/conectatec/ui/docente/perfil/DocentePerfilFragment.java
```
Expected: salida vacía.

- [ ] **Step 6: TODO en español presente en `DocentePerfilFragment`**

Run:
```bash
grep -n "TODO: llamar a SessionService.cerrarSesion()" \
  /home/crisgoat/Desarrollo/ConectaTec/app/src/main/java/com/conectatec/ui/docente/perfil/DocentePerfilFragment.java
```
Expected: una línea.

- [ ] **Step 7: Strings nuevos del Plan 2 declarados**

Run:
```bash
for s in subtitle_docente_dashboard label_grupos_activos label_alumnos_totales label_tareas_recientes label_avisos_del_dia btn_ver_todas_tareas label_id_empleado label_departamento label_notificaciones label_tema label_idioma; do
  echo -n "$s: ";
  grep -c "name=\"$s\"" /home/crisgoat/Desarrollo/ConectaTec/app/src/main/res/values/strings.xml;
done
```
Expected: cada string con conteo `1`.

- [ ] **Step 8: Working tree limpio (todos los commits ya hechos)**

Run:
```bash
cd /home/crisgoat/Desarrollo/ConectaTec && git status --short && git log --oneline -8
```
Expected:
- `git status --short` → vacío.
- `git log --oneline -8` → muestra al menos 4 commits del Plan 2:
  - `feat(strings): agregar strings para dashboard y perfil docente`
  - `feat(docente): layout fragment_docente_dashboard.xml ...`
  - `feat(docente): implementar DocenteDashboardFragment ...`
  - `feat(docente): layout fragment_docente_perfil.xml ...`
  - `feat(docente): implementar DocentePerfilFragment ...`

---

## Resumen ejecutivo

- **Tareas:** 8 (1 strings + 2 layouts + 2 implementaciones Java + 3 verificaciones).
- **Archivos creados:** 2 layouts XML.
- **Archivos modificados:** 1 strings.xml + 2 Fragments Java (reemplazo de stubs).
- **Commits:** 5 (`feat(strings)` + 4× `feat(docente)`).
- **Cero recursos nuevos** fuera de strings (sin colores, sin drawables, sin animaciones, sin Java extra — el helper `navigateToTab` es local al Fragment).
- **TODOs sembrados:** 1 (`SessionService.cerrarSesion()`) + 1 implícito (`pantalla de configuración`).
- **Cobertura del spec:** §6.1 (Dashboard) y §6.2 (Perfil), 100 %.
