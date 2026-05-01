# Infra Docente Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Crear la base de navegacion del modulo Docente (drawables, strings, nav graph, activity, pill nav, stubs de fragments) que permita compilar la app, navegar entre los 5 tabs y desbloquear los planes 2-5.

**Architecture:** Espejo exacto de `MainAdminActivity` y `nav_admin.xml`. Los 5 fragments raiz se entregan como stubs con layout placeholder (un solo `TextView` centrado) para que el grafo de navegacion compile y la pill nav sea navegable; los planes 2-5 sobreescriben esos stubs con la implementacion real. Los 9 sub-destinos del spec (CrearGrupo, GrupoDetalle, MiembrosGrupo, Bloques, TareasBloque, CrearTarea, Entregas, CalificarEntrega, Conversacion) **no** se crean en este plan: se declaran en `nav_docente.xml` apuntando a clases que aun no existen, pero gracias a la inflacion perezosa del NavController la app compila igual. Los planes 3, 4 y 5 crearan esas clases en sus paquetes esperados.

**Tech Stack:** Java 11, Hilt 2.51, ViewBinding, Navigation Component 2.8.2, Material 3 (Dark), AndroidX AppCompat, AndroidX CoordinatorLayout.

---

## Tareas

| #  | Tarea                                              | Archivos clave                                                          | Bloquea       | Tiempo |
|----|----------------------------------------------------|-------------------------------------------------------------------------|---------------|--------|
| 1  | Crear `ic_chat.xml`                                | `res/drawable/ic_chat.xml`                                              | T8            | 3 min  |
| 2  | Crear `ic_clipboard.xml`                           | `res/drawable/ic_clipboard.xml`                                         | T8            | 3 min  |
| 3  | Crear `ic_paperclip.xml`                           | `res/drawable/ic_paperclip.xml`                                         | (planes 4, 5) | 3 min  |
| 4  | Crear `ic_send.xml`                                | `res/drawable/ic_send.xml`                                              | (plan 5)      | 3 min  |
| 5  | Crear `ic_plus.xml`                                | `res/drawable/ic_plus.xml`                                              | (planes 3, 4) | 3 min  |
| 6  | Crear `ic_qr_placeholder.xml`                      | `res/drawable/ic_qr_placeholder.xml`                                    | (plan 3)      | 5 min  |
| 7  | Agregar strings de modulo Docente                  | `res/values/strings.xml`                                                | T8, T9        | 3 min  |
| 8  | Crear `activity_main_docente.xml`                  | `res/layout/activity_main_docente.xml`                                  | T10           | 5 min  |
| 9  | Crear `nav_docente.xml`                            | `res/navigation/nav_docente.xml`                                        | T10           | 5 min  |
| 10 | Crear placeholders de los 5 fragments stub         | `res/layout/fragment_docente_*_placeholder.xml` (x5)                    | T11..T15      | 5 min  |
| 11 | Crear `DocenteDashboardFragment` stub              | `ui/docente/dashboard/DocenteDashboardFragment.java`                    | T16           | 4 min  |
| 12 | Crear `DocenteGruposFragment` stub                 | `ui/docente/grupos/DocenteGruposFragment.java`                          | T16           | 4 min  |
| 13 | Crear `DocenteTareasFragment` stub                 | `ui/docente/tareas/DocenteTareasFragment.java`                          | T16           | 4 min  |
| 14 | Crear `DocenteChatFragment` stub                   | `ui/docente/chat/DocenteChatFragment.java`                              | T16           | 4 min  |
| 15 | Crear `DocentePerfilFragment` stub                 | `ui/docente/perfil/DocentePerfilFragment.java`                          | T16           | 4 min  |
| 16 | Crear `MainDocenteActivity.java`                   | `ui/docente/MainDocenteActivity.java`                                   | T17           | 5 min  |
| 17 | Registrar `MainDocenteActivity` en el manifest     | `AndroidManifest.xml`                                                   | T18           | 3 min  |
| 18 | Verificacion final del modulo (build + checks)     | -                                                                       | -             | 5 min  |

**Tiempo total estimado:** ~75 min (1h 15min).

---

### Task 1: Crear `ic_chat.xml`

Drawable vectorial 24dp para la tab Chat. Burbuja de chat redonda con tres puntos internos.

**Files:**
- Create: `app/src/main/res/drawable/ic_chat.xml`

- [ ] **Step 1: Crear el drawable**

Crear el archivo `app/src/main/res/drawable/ic_chat.xml` con el contenido siguiente:

```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M4,4h16a2,2 0,0 1,2 2v10a2,2 0,0 1,-2 2h-9.586l-3.707,3.707A1,1 0,0 1,5 21v-3H4a2,2 0,0 1,-2 -2V6a2,2 0,0 1,2 -2zM7.5,11.75a1.25,1.25 0,1 1,2.5 0a1.25,1.25 0,0 1,-2.5 0zM11.75,11.75a1.25,1.25 0,1 1,2.5 0a1.25,1.25 0,0 1,-2.5 0zM16,11.75a1.25,1.25 0,1 1,2.5 0a1.25,1.25 0,0 1,-2.5 0z"
        android:fillType="evenOdd" />
</vector>
```

**Nota:** el `fillColor` se sobrescribe via `app:tint` en el ImageView de la pill nav (mismo patron que `ic_home.xml` del proyecto, que usa `#969696` como fallback y se tinta en runtime).

- [ ] **Step 2: Verificar que compila**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -5
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 3: Commit**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
git add app/src/main/res/drawable/ic_chat.xml
git commit -m "feat(docente): add ic_chat drawable for chat tab"
```

---

### Task 2: Crear `ic_clipboard.xml`

Drawable vectorial 24dp para la tab Tareas. Portapapeles con grapa superior y tres lineas horizontales internas.

**Files:**
- Create: `app/src/main/res/drawable/ic_clipboard.xml`

- [ ] **Step 1: Crear el drawable**

Crear el archivo `app/src/main/res/drawable/ic_clipboard.xml` con el contenido siguiente:

```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M9,2h6a1,1 0,0 1,1 1v1h2a2,2 0,0 1,2 2v14a2,2 0,0 1,-2 2H6a2,2 0,0 1,-2 -2V6a2,2 0,0 1,2 -2h2V3a1,1 0,0 1,1 -1zM10,4v2h4V4h-4zM7.5,10.5a0.75,0.75 0,0 1,0.75 -0.75h7.5a0.75,0.75 0,0 1,0 1.5h-7.5a0.75,0.75 0,0 1,-0.75 -0.75zM7.5,14a0.75,0.75 0,0 1,0.75 -0.75h7.5a0.75,0.75 0,0 1,0 1.5h-7.5a0.75,0.75 0,0 1,-0.75 -0.75zM7.5,17.5a0.75,0.75 0,0 1,0.75 -0.75h4.5a0.75,0.75 0,0 1,0 1.5h-4.5a0.75,0.75 0,0 1,-0.75 -0.75z"
        android:fillType="evenOdd" />
</vector>
```

- [ ] **Step 2: Verificar que compila**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -5
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 3: Commit**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
git add app/src/main/res/drawable/ic_clipboard.xml
git commit -m "feat(docente): add ic_clipboard drawable for tareas tab"
```

---

### Task 3: Crear `ic_paperclip.xml`

Drawable vectorial 24dp para boton "adjuntar" en chat y entregas. Clip diagonal estilizado.

**Files:**
- Create: `app/src/main/res/drawable/ic_paperclip.xml`

- [ ] **Step 1: Crear el drawable**

Crear el archivo `app/src/main/res/drawable/ic_paperclip.xml` con el contenido siguiente:

```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M16.5,6.5L8.207,14.793a2.5,2.5 0,1 0,3.535 3.536l9.193,-9.193a4.5,4.5 0,0 0,-6.364 -6.364L4.379,12.964a6.5,6.5 0,1 0,9.192 9.192l8.486,-8.486a1,1 0,0 0,-1.414 -1.414l-8.486,8.486a4.5,4.5 0,0 1,-6.364 -6.364L15.964,4.186a2.5,2.5 0,0 1,3.536 3.536L10.307,16.914a0.5,0.5 0,0 1,-0.707 -0.707L17.914,7.914a1,1 0,1 0,-1.414 -1.414z"
        android:fillType="evenOdd" />
</vector>
```

- [ ] **Step 2: Verificar que compila**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -5
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 3: Commit**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
git add app/src/main/res/drawable/ic_paperclip.xml
git commit -m "feat(docente): add ic_paperclip drawable for chat attachments"
```

---

### Task 4: Crear `ic_send.xml`

Drawable vectorial 24dp para boton enviar mensaje en el chat. Flecha tipo paper-plane.

**Files:**
- Create: `app/src/main/res/drawable/ic_send.xml`

- [ ] **Step 1: Crear el drawable**

Crear el archivo `app/src/main/res/drawable/ic_send.xml` con el contenido siguiente:

```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M2.293,2.293a1,1 0,0 1,1.094 -0.217l18,8a1,1 0,0 1,0 1.828l-18,8a1,1 0,0 1,-1.378 -1.222L4.382,12L2.009,3.318a1,1 0,0 1,0.284 -1.025zM5.945,13l-1.46,5.343L17.539,12L4.485,5.657L5.945,11h6.555a1,1 0,0 1,0 2L5.945,13z"
        android:fillType="evenOdd" />
</vector>
```

- [ ] **Step 2: Verificar que compila**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -5
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 3: Commit**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
git add app/src/main/res/drawable/ic_send.xml
git commit -m "feat(docente): add ic_send drawable for chat send button"
```

---

### Task 5: Crear `ic_plus.xml`

Drawable vectorial 24dp para botones de creacion (Nuevo grupo, Nueva tarea). Signo "+" centrado.

**Files:**
- Create: `app/src/main/res/drawable/ic_plus.xml`

- [ ] **Step 1: Crear el drawable**

Crear el archivo `app/src/main/res/drawable/ic_plus.xml` con el contenido siguiente:

```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M12,3a1,1 0,0 1,1 1v7h7a1,1 0,0 1,0 2h-7v7a1,1 0,0 1,-2 0v-7H4a1,1 0,0 1,0 -2h7V4a1,1 0,0 1,1 -1z"
        android:fillType="evenOdd" />
</vector>
```

- [ ] **Step 2: Verificar que compila**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -5
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 3: Commit**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
git add app/src/main/res/drawable/ic_plus.xml
git commit -m "feat(docente): add ic_plus drawable for creation buttons"
```

---

### Task 6: Crear `ic_qr_placeholder.xml`

Drawable vectorial 200dp como placeholder visual de un QR. Composicion de tres "finder squares" en las esquinas (TL, TR, BL) + cuadritos pequenos en el centro como puntos del payload. Solo tipo imagen, no es un QR funcional.

**Files:**
- Create: `app/src/main/res/drawable/ic_qr_placeholder.xml`

- [ ] **Step 1: Crear el drawable**

Crear el archivo `app/src/main/res/drawable/ic_qr_placeholder.xml` con el contenido siguiente:

```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="200dp"
    android:height="200dp"
    android:viewportWidth="100"
    android:viewportHeight="100">

    <!-- Finder square Top-Left: marco exterior -->
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M5,5h25v25h-25z M9,9v17h17v-17z" />
    <!-- Finder square TL: cuadrado interior -->
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M13,13h9v9h-9z" />

    <!-- Finder square Top-Right: marco exterior -->
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M70,5h25v25h-25z M74,9v17h17v-17z" />
    <!-- Finder square TR: cuadrado interior -->
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M78,13h9v9h-9z" />

    <!-- Finder square Bottom-Left: marco exterior -->
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M5,70h25v25h-25z M9,74v17h17v-17z" />
    <!-- Finder square BL: cuadrado interior -->
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M13,78h9v9h-9z" />

    <!-- Patron central: cuadritos 4x4 simulando puntos del payload -->
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M40,10h4v4h-4z M50,10h4v4h-4z M60,10h4v4h-4z" />
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M40,20h4v4h-4z M55,20h4v4h-4z M65,20h4v4h-4z" />
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M10,40h4v4h-4z M40,40h4v4h-4z M50,40h4v4h-4z M60,40h4v4h-4z M75,40h4v4h-4z M85,40h4v4h-4z" />
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M15,50h4v4h-4z M35,50h4v4h-4z M45,50h4v4h-4z M55,50h4v4h-4z M70,50h4v4h-4z M80,50h4v4h-4z M90,50h4v4h-4z" />
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M10,60h4v4h-4z M30,60h4v4h-4z M40,60h4v4h-4z M55,60h4v4h-4z M65,60h4v4h-4z M75,60h4v4h-4z M85,60h4v4h-4z" />
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M40,70h4v4h-4z M50,70h4v4h-4z M65,70h4v4h-4z M80,70h4v4h-4z" />
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M40,80h4v4h-4z M55,80h4v4h-4z M65,80h4v4h-4z M75,80h4v4h-4z M90,80h4v4h-4z" />
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M40,90h4v4h-4z M50,90h4v4h-4z M60,90h4v4h-4z M75,90h4v4h-4z M85,90h4v4h-4z" />

</vector>
```

**Nota:** se usa `viewportWidth/Height=100` (canvas logico) y `width/height=200dp` (tamaño fisico). `fillColor=#FFFFFF` se tinta luego con `app:tint="@color/colorOnSurface"` desde el ImageView en `fragment_docente_crear_grupo.xml` (plan 3) y `fragment_docente_grupo_detalle.xml` (plan 3).

- [ ] **Step 2: Verificar que compila**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -5
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 3: Commit**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
git add app/src/main/res/drawable/ic_qr_placeholder.xml
git commit -m "feat(docente): add ic_qr_placeholder drawable"
```

---

### Task 7: Agregar strings del modulo Docente

Agregar 5 titulos de tab + content descriptions del pill nav docente al archivo de strings global. Solo lo necesario para el plan 1; los demas strings (subtitulos, hints, botones, labels de tipos/estados) se agregaran en los planes 2-5 cuando los layouts respectivos los necesiten.

**Files:**
- Modify: `app/src/main/res/values/strings.xml`

- [ ] **Step 1: Leer el archivo actual**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
grep -n "</resources>" app/src/main/res/values/strings.xml
```

Expected: imprime el numero de linea de la etiqueta de cierre `</resources>`. Anota ese numero (sera el lugar donde insertar antes).

- [ ] **Step 2: Insertar los strings nuevos**

Insertar este bloque **inmediatamente antes** de la linea `</resources>` (al final del archivo, dentro del nodo raiz):

```xml
    <!-- ───────────────────────── Modulo Docente ───────────────────────── -->
    <!-- Tabs de la pill nav -->
    <string name="title_docente_dashboard">Inicio</string>
    <string name="title_docente_grupos">Grupos</string>
    <string name="title_docente_tareas">Tareas</string>
    <string name="title_docente_chat">Chat</string>
    <string name="title_docente_perfil">Mi perfil</string>
    <!-- Content descriptions de los iconos de la pill nav -->
    <string name="cd_nav_docente_dashboard">Inicio</string>
    <string name="cd_nav_docente_grupos">Grupos</string>
    <string name="cd_nav_docente_tareas">Tareas</string>
    <string name="cd_nav_docente_chat">Chat</string>
    <string name="cd_nav_docente_perfil">Perfil</string>
```

- [ ] **Step 3: Verificar que el XML sigue siendo valido**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -5
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 4: Verificar el contenido**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
grep -c "title_docente_" app/src/main/res/values/strings.xml
grep -c "cd_nav_docente_" app/src/main/res/values/strings.xml
```

Expected: ambos imprimen `5`.

- [ ] **Step 5: Commit**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
git add app/src/main/res/values/strings.xml
git commit -m "feat(docente): add tab titles and nav content descriptions"
```

---

### Task 8: Crear `activity_main_docente.xml`

Layout de la activity principal del rol Docente. Espejo estructural exacto de `activity_main_admin.xml`, con:
- IDs `bottomNavDocente`, `pillNavDocente`, `navHostDocente`.
- 5 items: `itemDashboard`/`circleDashboard`/`iconDashboard`/`labelDashboard`, `itemGrupos`/..., `itemTareas`/..., `itemChat`/..., `itemPerfil`/....
- Iconos: `ic_home` (Dashboard), `ic_school` (Grupos), `ic_clipboard` (Tareas), `ic_chat` (Chat), `ic_perfil` (Perfil).
- Labels: "Inicio", "Grupos", "Tareas", "Chat", "Perfil".

**Files:**
- Create: `app/src/main/res/layout/activity_main_docente.xml`

- [ ] **Step 1: Crear el layout**

Crear el archivo `app/src/main/res/layout/activity_main_docente.xml` con el contenido siguiente:

```xml
<?xml version="1.0" encoding="utf-8"?>
<!-- Actividad principal del rol Docente: NavHost + pill bottom nav custom -->
<androidx.coordinatorlayout.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/colorBackground">

    <!-- Contenedor de fragmentos — ocupa toda la pantalla -->
    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/navHostDocente"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:defaultNavHost="true"
        app:navGraph="@navigation/nav_docente" />

    <!-- BottomNav custom (pill) -->
    <LinearLayout
        android:id="@+id/bottomNavDocente"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom"
        android:layout_marginBottom="15dp"
        android:gravity="center"
        android:orientation="vertical">

        <LinearLayout
            android:id="@+id/pillNavDocente"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="bottom|center_horizontal"
            android:background="@drawable/bg_nav_container"
            android:gravity="center_vertical"
            android:orientation="horizontal"
            android:padding="8dp">

            <!-- Dashboard -->
            <LinearLayout
                android:id="@+id/itemDashboard"
                android:layout_width="wrap_content"
                android:layout_height="56dp"
                android:clickable="true"
                android:focusable="true"
                android:gravity="center_vertical"
                android:orientation="horizontal">

                <FrameLayout
                    android:id="@+id/circleDashboard"
                    android:layout_width="56dp"
                    android:layout_height="56dp"
                    android:background="@drawable/bg_icon_circle_inactive">

                    <ImageView
                        android:id="@+id/iconDashboard"
                        android:layout_width="27dp"
                        android:layout_height="27dp"
                        android:layout_gravity="center"
                        android:src="@drawable/ic_home"
                        android:contentDescription="@string/cd_nav_docente_dashboard"
                        app:tint="@color/nav_icon_inactive" />
                </FrameLayout>

                <TextView
                    android:id="@+id/labelDashboard"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:paddingStart="7dp"
                    android:paddingEnd="11dp"
                    android:text="@string/title_docente_dashboard"
                    android:textColor="@color/nav_label"
                    android:textSize="16sp"
                    android:visibility="gone" />
            </LinearLayout>

            <Space
                android:layout_width="6dp"
                android:layout_height="1dp" />

            <!-- Grupos -->
            <LinearLayout
                android:id="@+id/itemGrupos"
                android:layout_width="wrap_content"
                android:layout_height="56dp"
                android:clickable="true"
                android:focusable="true"
                android:gravity="center_vertical"
                android:orientation="horizontal">

                <FrameLayout
                    android:id="@+id/circleGrupos"
                    android:layout_width="56dp"
                    android:layout_height="56dp"
                    android:background="@drawable/bg_icon_circle_inactive">

                    <ImageView
                        android:id="@+id/iconGrupos"
                        android:layout_width="27dp"
                        android:layout_height="27dp"
                        android:layout_gravity="center"
                        android:src="@drawable/ic_school"
                        android:contentDescription="@string/cd_nav_docente_grupos"
                        app:tint="@color/nav_icon_inactive" />
                </FrameLayout>

                <TextView
                    android:id="@+id/labelGrupos"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:paddingStart="7dp"
                    android:paddingEnd="11dp"
                    android:text="@string/title_docente_grupos"
                    android:textColor="@color/nav_label"
                    android:textSize="16sp"
                    android:visibility="gone" />
            </LinearLayout>

            <Space
                android:layout_width="6dp"
                android:layout_height="1dp" />

            <!-- Tareas -->
            <LinearLayout
                android:id="@+id/itemTareas"
                android:layout_width="wrap_content"
                android:layout_height="56dp"
                android:clickable="true"
                android:focusable="true"
                android:gravity="center_vertical"
                android:orientation="horizontal">

                <FrameLayout
                    android:id="@+id/circleTareas"
                    android:layout_width="56dp"
                    android:layout_height="56dp"
                    android:background="@drawable/bg_icon_circle_inactive">

                    <ImageView
                        android:id="@+id/iconTareas"
                        android:layout_width="27dp"
                        android:layout_height="27dp"
                        android:layout_gravity="center"
                        android:src="@drawable/ic_clipboard"
                        android:contentDescription="@string/cd_nav_docente_tareas"
                        app:tint="@color/nav_icon_inactive" />
                </FrameLayout>

                <TextView
                    android:id="@+id/labelTareas"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:paddingStart="7dp"
                    android:paddingEnd="11dp"
                    android:text="@string/title_docente_tareas"
                    android:textColor="@color/nav_label"
                    android:textSize="16sp"
                    android:visibility="gone" />
            </LinearLayout>

            <Space
                android:layout_width="6dp"
                android:layout_height="1dp" />

            <!-- Chat -->
            <LinearLayout
                android:id="@+id/itemChat"
                android:layout_width="wrap_content"
                android:layout_height="56dp"
                android:clickable="true"
                android:focusable="true"
                android:gravity="center_vertical"
                android:orientation="horizontal">

                <FrameLayout
                    android:id="@+id/circleChat"
                    android:layout_width="56dp"
                    android:layout_height="56dp"
                    android:background="@drawable/bg_icon_circle_inactive">

                    <ImageView
                        android:id="@+id/iconChat"
                        android:layout_width="27dp"
                        android:layout_height="27dp"
                        android:layout_gravity="center"
                        android:src="@drawable/ic_chat"
                        android:contentDescription="@string/cd_nav_docente_chat"
                        app:tint="@color/nav_icon_inactive" />
                </FrameLayout>

                <TextView
                    android:id="@+id/labelChat"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:paddingStart="7dp"
                    android:paddingEnd="11dp"
                    android:text="@string/title_docente_chat"
                    android:textColor="@color/nav_label"
                    android:textSize="16sp"
                    android:visibility="gone" />
            </LinearLayout>

            <Space
                android:layout_width="6dp"
                android:layout_height="1dp" />

            <!-- Perfil -->
            <LinearLayout
                android:id="@+id/itemPerfil"
                android:layout_width="wrap_content"
                android:layout_height="56dp"
                android:clickable="true"
                android:focusable="true"
                android:gravity="center_vertical"
                android:orientation="horizontal">

                <FrameLayout
                    android:id="@+id/circlePerfil"
                    android:layout_width="56dp"
                    android:layout_height="56dp"
                    android:background="@drawable/bg_icon_circle_inactive">

                    <ImageView
                        android:id="@+id/iconPerfil"
                        android:layout_width="27dp"
                        android:layout_height="27dp"
                        android:layout_gravity="center"
                        android:src="@drawable/ic_perfil"
                        android:contentDescription="@string/cd_nav_docente_perfil"
                        app:tint="@color/nav_icon_inactive" />
                </FrameLayout>

                <TextView
                    android:id="@+id/labelPerfil"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:paddingStart="7dp"
                    android:paddingEnd="11dp"
                    android:text="Perfil"
                    android:textColor="@color/nav_label"
                    android:textSize="16sp"
                    android:visibility="gone" />
            </LinearLayout>

        </LinearLayout>

    </LinearLayout>
</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

**Nota:** En este layout NO se compila aun (todavia no existe `nav_docente.xml`). La verificacion se hace en la Task 9 luego de crear el grafo.

- [ ] **Step 2: Commit**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
git add app/src/main/res/layout/activity_main_docente.xml
git commit -m "feat(docente): add activity_main_docente layout with pill nav"
```

---

### Task 9: Crear `nav_docente.xml`

Grafo de navegacion del modulo Docente. Contiene los **5 destinos raiz** + los **9 sub-destinos** con sus argumentos y todas las acciones segun §4 del spec. Las clases Java de los sub-destinos no existen aun, pero el `NavController` no las instancia hasta que se navega a ellas (inflacion perezosa); el grafo compila igual.

**Files:**
- Create: `app/src/main/res/navigation/nav_docente.xml`

- [ ] **Step 1: Crear el grafo de navegacion**

Crear el archivo `app/src/main/res/navigation/nav_docente.xml` con el contenido siguiente:

```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/nav_docente"
    app:startDestination="@id/docenteDashboardFragment">

    <!-- ───────────── Destinos raiz (tabs de la pill nav) ───────────── -->

    <!-- 0 · Dashboard -->
    <fragment
        android:id="@+id/docenteDashboardFragment"
        android:name="com.conectatec.ui.docente.dashboard.DocenteDashboardFragment"
        android:label="@string/title_docente_dashboard" />

    <!-- 1 · Grupos -->
    <fragment
        android:id="@+id/docenteGruposFragment"
        android:name="com.conectatec.ui.docente.grupos.DocenteGruposFragment"
        android:label="@string/title_docente_grupos">
        <action
            android:id="@+id/action_grupos_to_crear"
            app:destination="@id/docenteCrearGrupoFragment"
            app:enterAnim="@anim/slide_in_right"
            app:exitAnim="@anim/slide_out_left"
            app:popEnterAnim="@anim/slide_in_left"
            app:popExitAnim="@anim/slide_out_right" />
        <action
            android:id="@+id/action_grupos_to_detalle"
            app:destination="@id/docenteGrupoDetalleFragment"
            app:enterAnim="@anim/slide_in_right"
            app:exitAnim="@anim/slide_out_left"
            app:popEnterAnim="@anim/slide_in_left"
            app:popExitAnim="@anim/slide_out_right" />
    </fragment>

    <!-- 2 · Tareas -->
    <fragment
        android:id="@+id/docenteTareasFragment"
        android:name="com.conectatec.ui.docente.tareas.DocenteTareasFragment"
        android:label="@string/title_docente_tareas">
        <action
            android:id="@+id/action_tareas_to_bloques"
            app:destination="@id/docenteBloquesFragment"
            app:enterAnim="@anim/slide_in_right"
            app:exitAnim="@anim/slide_out_left"
            app:popEnterAnim="@anim/slide_in_left"
            app:popExitAnim="@anim/slide_out_right" />
    </fragment>

    <!-- 3 · Chat -->
    <fragment
        android:id="@+id/docenteChatFragment"
        android:name="com.conectatec.ui.docente.chat.DocenteChatFragment"
        android:label="@string/title_docente_chat">
        <action
            android:id="@+id/action_chat_to_conversacion"
            app:destination="@id/docenteConversacionFragment"
            app:enterAnim="@anim/slide_in_right"
            app:exitAnim="@anim/slide_out_left"
            app:popEnterAnim="@anim/slide_in_left"
            app:popExitAnim="@anim/slide_out_right" />
    </fragment>

    <!-- 4 · Perfil -->
    <fragment
        android:id="@+id/docentePerfilFragment"
        android:name="com.conectatec.ui.docente.perfil.DocentePerfilFragment"
        android:label="@string/title_docente_perfil" />

    <!-- ───────────── Sub-destinos: Grupos ───────────── -->

    <!-- Crear nuevo grupo (sin args) -->
    <fragment
        android:id="@+id/docenteCrearGrupoFragment"
        android:name="com.conectatec.ui.docente.grupos.DocenteCrearGrupoFragment"
        android:label="Nuevo grupo" />

    <!-- Detalle de grupo -->
    <fragment
        android:id="@+id/docenteGrupoDetalleFragment"
        android:name="com.conectatec.ui.docente.grupos.DocenteGrupoDetalleFragment"
        android:label="Detalle de grupo">
        <argument
            android:name="grupoId"
            app:argType="integer"
            android:defaultValue="0" />
        <action
            android:id="@+id/action_grupo_detalle_to_miembros"
            app:destination="@id/docenteMiembrosGrupoFragment"
            app:enterAnim="@anim/slide_in_right"
            app:exitAnim="@anim/slide_out_left"
            app:popEnterAnim="@anim/slide_in_left"
            app:popExitAnim="@anim/slide_out_right" />
        <action
            android:id="@+id/action_grupo_detalle_to_tareas_grupo"
            app:destination="@id/docenteBloquesFragment"
            app:enterAnim="@anim/slide_in_right"
            app:exitAnim="@anim/slide_out_left"
            app:popEnterAnim="@anim/slide_in_left"
            app:popExitAnim="@anim/slide_out_right" />
    </fragment>

    <!-- Lista completa de miembros del grupo -->
    <fragment
        android:id="@+id/docenteMiembrosGrupoFragment"
        android:name="com.conectatec.ui.docente.grupos.DocenteMiembrosGrupoFragment"
        android:label="Miembros del grupo">
        <argument
            android:name="grupoId"
            app:argType="integer"
            android:defaultValue="0" />
    </fragment>

    <!-- ───────────── Sub-destinos: Tareas ───────────── -->

    <!-- Bloques del grupo -->
    <fragment
        android:id="@+id/docenteBloquesFragment"
        android:name="com.conectatec.ui.docente.tareas.DocenteBloquesFragment"
        android:label="Bloques">
        <argument
            android:name="grupoId"
            app:argType="integer"
            android:defaultValue="0" />
        <action
            android:id="@+id/action_bloques_to_tareas_bloque"
            app:destination="@id/docenteTareasBloqueFragment"
            app:enterAnim="@anim/slide_in_right"
            app:exitAnim="@anim/slide_out_left"
            app:popEnterAnim="@anim/slide_in_left"
            app:popExitAnim="@anim/slide_out_right" />
    </fragment>

    <!-- Tareas dentro de un bloque -->
    <fragment
        android:id="@+id/docenteTareasBloqueFragment"
        android:name="com.conectatec.ui.docente.tareas.DocenteTareasBloqueFragment"
        android:label="Tareas del bloque">
        <argument
            android:name="grupoId"
            app:argType="integer"
            android:defaultValue="0" />
        <argument
            android:name="bloqueId"
            app:argType="integer"
            android:defaultValue="0" />
        <action
            android:id="@+id/action_tareas_bloque_to_crear"
            app:destination="@id/docenteCrearTareaFragment"
            app:enterAnim="@anim/slide_in_right"
            app:exitAnim="@anim/slide_out_left"
            app:popEnterAnim="@anim/slide_in_left"
            app:popExitAnim="@anim/slide_out_right" />
        <action
            android:id="@+id/action_tareas_bloque_to_entregas"
            app:destination="@id/docenteEntregasFragment"
            app:enterAnim="@anim/slide_in_right"
            app:exitAnim="@anim/slide_out_left"
            app:popEnterAnim="@anim/slide_in_left"
            app:popExitAnim="@anim/slide_out_right" />
    </fragment>

    <!-- Crear nueva tarea -->
    <fragment
        android:id="@+id/docenteCrearTareaFragment"
        android:name="com.conectatec.ui.docente.tareas.DocenteCrearTareaFragment"
        android:label="Nueva tarea">
        <argument
            android:name="grupoId"
            app:argType="integer"
            android:defaultValue="0" />
        <argument
            android:name="bloqueId"
            app:argType="integer"
            android:defaultValue="0" />
    </fragment>

    <!-- Lista de entregas de una tarea -->
    <fragment
        android:id="@+id/docenteEntregasFragment"
        android:name="com.conectatec.ui.docente.tareas.DocenteEntregasFragment"
        android:label="Entregas">
        <argument
            android:name="tareaId"
            app:argType="integer"
            android:defaultValue="0" />
        <action
            android:id="@+id/action_entregas_to_calificar"
            app:destination="@id/docenteCalificarEntregaFragment"
            app:enterAnim="@anim/slide_in_right"
            app:exitAnim="@anim/slide_out_left"
            app:popEnterAnim="@anim/slide_in_left"
            app:popExitAnim="@anim/slide_out_right" />
    </fragment>

    <!-- Calificar entrega de un alumno -->
    <fragment
        android:id="@+id/docenteCalificarEntregaFragment"
        android:name="com.conectatec.ui.docente.tareas.DocenteCalificarEntregaFragment"
        android:label="Calificar entrega">
        <argument
            android:name="tareaId"
            app:argType="integer"
            android:defaultValue="0" />
        <argument
            android:name="alumnoId"
            app:argType="integer"
            android:defaultValue="0" />
    </fragment>

    <!-- ───────────── Sub-destinos: Chat ───────────── -->

    <!-- Conversacion (sala de chat individual o grupal) -->
    <fragment
        android:id="@+id/docenteConversacionFragment"
        android:name="com.conectatec.ui.docente.chat.DocenteConversacionFragment"
        android:label="Conversacion">
        <argument
            android:name="salaId"
            app:argType="integer"
            android:defaultValue="0" />
    </fragment>

</navigation>
```

- [ ] **Step 2: Verificar que el activity layout + el nav graph compilan juntos**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -10
```

Expected: `BUILD SUCCESSFUL`. Si hay error sobre `bg_nav_container` u otros drawables, son recursos pre-existentes del Admin y deben estar disponibles. Si hay error sobre clases Java de fragments faltantes, **es esperado** que NO falle aqui: el plugin `androidx.navigation.safeargs` no esta habilitado en este proyecto, y el grafo solo guarda nombres como String — no resuelve clases en build time. Si aparece un warning tipo "class not found" durante la inflacion de runtime, es OK porque las tasks 11-15 crearan los stubs raiz, y los planes 3-5 crearan las sub-clases.

- [ ] **Step 3: Commit**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
git add app/src/main/res/navigation/nav_docente.xml
git commit -m "feat(docente): add nav_docente graph with 5 root tabs and 9 sub-destinations"
```

---

### Task 10: Crear placeholders de los 5 fragments stub

Crear los 5 layouts `fragment_docente_<modulo>_placeholder.xml`. Cada uno tiene un `FrameLayout` raiz con un `TextView` centrado mostrando el nombre del modulo. Los planes 2-5 sobreescriben estos archivos con la version real.

**Files:**
- Create: `app/src/main/res/layout/fragment_docente_dashboard_placeholder.xml`
- Create: `app/src/main/res/layout/fragment_docente_grupos_placeholder.xml`
- Create: `app/src/main/res/layout/fragment_docente_tareas_placeholder.xml`
- Create: `app/src/main/res/layout/fragment_docente_chat_placeholder.xml`
- Create: `app/src/main/res/layout/fragment_docente_perfil_placeholder.xml`

- [ ] **Step 1: Crear `fragment_docente_dashboard_placeholder.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/colorBackground">

    <TextView
        android:id="@+id/tvPlaceholderDashboard"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:text="@string/title_docente_dashboard"
        android:textColor="@color/colorOnSurface"
        android:textSize="22sp"
        android:textStyle="bold" />
</FrameLayout>
```

- [ ] **Step 2: Crear `fragment_docente_grupos_placeholder.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/colorBackground">

    <TextView
        android:id="@+id/tvPlaceholderGrupos"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:text="@string/title_docente_grupos"
        android:textColor="@color/colorOnSurface"
        android:textSize="22sp"
        android:textStyle="bold" />
</FrameLayout>
```

- [ ] **Step 3: Crear `fragment_docente_tareas_placeholder.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/colorBackground">

    <TextView
        android:id="@+id/tvPlaceholderTareas"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:text="@string/title_docente_tareas"
        android:textColor="@color/colorOnSurface"
        android:textSize="22sp"
        android:textStyle="bold" />
</FrameLayout>
```

- [ ] **Step 4: Crear `fragment_docente_chat_placeholder.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/colorBackground">

    <TextView
        android:id="@+id/tvPlaceholderChat"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:text="@string/title_docente_chat"
        android:textColor="@color/colorOnSurface"
        android:textSize="22sp"
        android:textStyle="bold" />
</FrameLayout>
```

- [ ] **Step 5: Crear `fragment_docente_perfil_placeholder.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/colorBackground">

    <TextView
        android:id="@+id/tvPlaceholderPerfil"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:text="@string/title_docente_perfil"
        android:textColor="@color/colorOnSurface"
        android:textSize="22sp"
        android:textStyle="bold" />
</FrameLayout>
```

- [ ] **Step 6: Verificar que compila**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -5
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 7: Commit**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
git add app/src/main/res/layout/fragment_docente_dashboard_placeholder.xml \
        app/src/main/res/layout/fragment_docente_grupos_placeholder.xml \
        app/src/main/res/layout/fragment_docente_tareas_placeholder.xml \
        app/src/main/res/layout/fragment_docente_chat_placeholder.xml \
        app/src/main/res/layout/fragment_docente_perfil_placeholder.xml
git commit -m "feat(docente): add placeholder layouts for the 5 root fragments"
```

---

### Task 11: Crear `DocenteDashboardFragment` stub

Stub en su paquete final (`com.conectatec.ui.docente.dashboard`) para que `nav_docente.xml` lo referencie correctamente. Sera **sobreescrito por el plan 2** (dashboard-perfil).

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/docente/dashboard/DocenteDashboardFragment.java`

- [ ] **Step 1: Crear el directorio del paquete y el archivo**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
mkdir -p app/src/main/java/com/conectatec/ui/docente/dashboard
```

- [ ] **Step 2: Crear el Fragment stub**

Crear `app/src/main/java/com/conectatec/ui/docente/dashboard/DocenteDashboardFragment.java` con el contenido siguiente:

```java
package com.conectatec.ui.docente.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.conectatec.databinding.FragmentDocenteDashboardPlaceholderBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Stub del Dashboard del Docente. Sera reemplazado por la implementacion real
 * en el plan 2 (dashboard-perfil). Solo muestra un texto centrado.
 */
@AndroidEntryPoint
public class DocenteDashboardFragment extends Fragment {

    private FragmentDocenteDashboardPlaceholderBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteDashboardPlaceholderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
```

- [ ] **Step 3: Verificar que compila**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -5
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 4: Commit**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
git add app/src/main/java/com/conectatec/ui/docente/dashboard/DocenteDashboardFragment.java
git commit -m "feat(docente): add DocenteDashboardFragment stub"
```

---

### Task 12: Crear `DocenteGruposFragment` stub

Stub en su paquete final (`com.conectatec.ui.docente.grupos`). Sera **sobreescrito por el plan 3** (grupos).

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/docente/grupos/DocenteGruposFragment.java`

- [ ] **Step 1: Crear el directorio del paquete**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
mkdir -p app/src/main/java/com/conectatec/ui/docente/grupos
```

- [ ] **Step 2: Crear el Fragment stub**

Crear `app/src/main/java/com/conectatec/ui/docente/grupos/DocenteGruposFragment.java` con el contenido siguiente:

```java
package com.conectatec.ui.docente.grupos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.conectatec.databinding.FragmentDocenteGruposPlaceholderBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Stub del modulo Grupos del Docente. Sera reemplazado por la implementacion real
 * en el plan 3 (grupos). Solo muestra un texto centrado.
 */
@AndroidEntryPoint
public class DocenteGruposFragment extends Fragment {

    private FragmentDocenteGruposPlaceholderBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteGruposPlaceholderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
```

- [ ] **Step 3: Verificar que compila**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -5
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 4: Commit**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
git add app/src/main/java/com/conectatec/ui/docente/grupos/DocenteGruposFragment.java
git commit -m "feat(docente): add DocenteGruposFragment stub"
```

---

### Task 13: Crear `DocenteTareasFragment` stub

Stub en su paquete final (`com.conectatec.ui.docente.tareas`). Sera **sobreescrito por el plan 4** (tareas).

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteTareasFragment.java`

- [ ] **Step 1: Crear el directorio del paquete**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
mkdir -p app/src/main/java/com/conectatec/ui/docente/tareas
```

- [ ] **Step 2: Crear el Fragment stub**

Crear `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteTareasFragment.java` con el contenido siguiente:

```java
package com.conectatec.ui.docente.tareas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.conectatec.databinding.FragmentDocenteTareasPlaceholderBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Stub del modulo Tareas del Docente. Sera reemplazado por la implementacion real
 * en el plan 4 (tareas). Solo muestra un texto centrado.
 */
@AndroidEntryPoint
public class DocenteTareasFragment extends Fragment {

    private FragmentDocenteTareasPlaceholderBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteTareasPlaceholderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
```

- [ ] **Step 3: Verificar que compila**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -5
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 4: Commit**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
git add app/src/main/java/com/conectatec/ui/docente/tareas/DocenteTareasFragment.java
git commit -m "feat(docente): add DocenteTareasFragment stub"
```

---

### Task 14: Crear `DocenteChatFragment` stub

Stub en su paquete final (`com.conectatec.ui.docente.chat`). Sera **sobreescrito por el plan 5** (chat).

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/docente/chat/DocenteChatFragment.java`

- [ ] **Step 1: Crear el directorio del paquete**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
mkdir -p app/src/main/java/com/conectatec/ui/docente/chat
```

- [ ] **Step 2: Crear el Fragment stub**

Crear `app/src/main/java/com/conectatec/ui/docente/chat/DocenteChatFragment.java` con el contenido siguiente:

```java
package com.conectatec.ui.docente.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.conectatec.databinding.FragmentDocenteChatPlaceholderBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Stub del modulo Chat del Docente. Sera reemplazado por la implementacion real
 * en el plan 5 (chat). Solo muestra un texto centrado.
 */
@AndroidEntryPoint
public class DocenteChatFragment extends Fragment {

    private FragmentDocenteChatPlaceholderBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteChatPlaceholderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
```

- [ ] **Step 3: Verificar que compila**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -5
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 4: Commit**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
git add app/src/main/java/com/conectatec/ui/docente/chat/DocenteChatFragment.java
git commit -m "feat(docente): add DocenteChatFragment stub"
```

---

### Task 15: Crear `DocentePerfilFragment` stub

Stub en su paquete final (`com.conectatec.ui.docente.perfil`). Sera **sobreescrito por el plan 2** (dashboard-perfil).

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/docente/perfil/DocentePerfilFragment.java`

- [ ] **Step 1: Crear el directorio del paquete**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
mkdir -p app/src/main/java/com/conectatec/ui/docente/perfil
```

- [ ] **Step 2: Crear el Fragment stub**

Crear `app/src/main/java/com/conectatec/ui/docente/perfil/DocentePerfilFragment.java` con el contenido siguiente:

```java
package com.conectatec.ui.docente.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.conectatec.databinding.FragmentDocentePerfilPlaceholderBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Stub del Perfil del Docente. Sera reemplazado por la implementacion real
 * en el plan 2 (dashboard-perfil). Solo muestra un texto centrado.
 */
@AndroidEntryPoint
public class DocentePerfilFragment extends Fragment {

    private FragmentDocentePerfilPlaceholderBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocentePerfilPlaceholderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
```

- [ ] **Step 3: Verificar que compila**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -5
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 4: Commit**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
git add app/src/main/java/com/conectatec/ui/docente/perfil/DocentePerfilFragment.java
git commit -m "feat(docente): add DocentePerfilFragment stub"
```

---

### Task 16: Crear `MainDocenteActivity.java`

Activity principal del rol Docente. Espejo Java exacto de `MainAdminActivity.java`:
- `@AndroidEntryPoint`, `extends AppCompatActivity`.
- `ActivityMainDocenteBinding`, `NavController`, `NavItem[] items`.
- `DESTINATIONS[]` con los 5 IDs raiz del nav docente.
- `setupPillNav()` con click listeners y `addOnDestinationChangedListener` que mapea sub-destinos a tab activo.
- `selectItem(int)` con `AutoTransition` 180 ms.
- `onDestroy` libera `binding = null`.

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/docente/MainDocenteActivity.java`

- [ ] **Step 1: Crear el directorio raiz del paquete docente**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
mkdir -p app/src/main/java/com/conectatec/ui/docente
```

- [ ] **Step 2: Crear la Activity**

Crear `app/src/main/java/com/conectatec/ui/docente/MainDocenteActivity.java` con el contenido siguiente:

```java
package com.conectatec.ui.docente;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.conectatec.R;
import com.conectatec.databinding.ActivityMainDocenteBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity principal del rol Docente.
 * Maneja el NavController y el pill bottom nav custom (sin Toolbar).
 * TODO: quitar el intent-filter del manifest cuando el login enrute por rol.
 */
@AndroidEntryPoint
public class MainDocenteActivity extends AppCompatActivity {

    private ActivityMainDocenteBinding binding;
    private NavController navController;
    private NavItem[] items;

    /** Destinos del nav graph en el mismo orden que el array items. */
    private static final int[] DESTINATIONS = {
            R.id.docenteDashboardFragment,
            R.id.docenteGruposFragment,
            R.id.docenteTareasFragment,
            R.id.docenteChatFragment,
            R.id.docentePerfilFragment
    };

    private static class NavItem {
        final LinearLayout container;
        final FrameLayout circle;
        final ImageView icon;
        final TextView label;

        NavItem(LinearLayout c, FrameLayout fc, ImageView i, TextView l) {
            container = c; circle = fc; icon = i; label = l;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainDocenteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navHostDocente);
        if (navHost == null) return;

        navController = navHost.getNavController();
        setupPillNav();
    }

    private void setupPillNav() {
        items = new NavItem[]{
                new NavItem(binding.itemDashboard, binding.circleDashboard,
                        binding.iconDashboard, binding.labelDashboard),
                new NavItem(binding.itemGrupos,    binding.circleGrupos,
                        binding.iconGrupos,    binding.labelGrupos),
                new NavItem(binding.itemTareas,    binding.circleTareas,
                        binding.iconTareas,    binding.labelTareas),
                new NavItem(binding.itemChat,      binding.circleChat,
                        binding.iconChat,      binding.labelChat),
                new NavItem(binding.itemPerfil,    binding.circlePerfil,
                        binding.iconPerfil,    binding.labelPerfil)
        };

        // Click → navegar al destino correspondiente
        for (int i = 0; i < items.length; i++) {
            final int destId = DESTINATIONS[i];
            items[i].container.setOnClickListener(v -> {
                if (navController.getCurrentDestination() != null
                        && navController.getCurrentDestination().getId() == destId) {
                    return; // ya estas en esa pestana, no hagas nada
                }

                NavOptions options = new NavOptions.Builder()
                        .setLaunchSingleTop(true)        // no apilar copias del mismo destino
                        .setRestoreState(true)           // restaura el estado guardado de la pestana
                        .setPopUpTo(
                                navController.getGraph().getStartDestinationId(),
                                false,                   // no incluir el start destination en el pop
                                true                     // saveState: guardar estado del back stack
                        )
                        .build();

                navController.navigate(destId, null, options);
            });
        }

        // Cambio de destino → actualizar UI del pill
        // El listener se dispara tambien con el destino inicial, asi que
        // no hace falta seleccionar manualmente la pestana al arranque.
        navController.addOnDestinationChangedListener((controller, dest, args) -> {
            int id = dest.getId();

            // Intentar encontrar el item raiz exacto
            for (int i = 0; i < DESTINATIONS.length; i++) {
                if (DESTINATIONS[i] == id) {
                    selectItem(i);
                    return;
                }
            }

            // Para sub-destinos (detalles), resaltar la pestana padre.
            // Mapeo segun §5.1 del spec:
            //   Crear/Detalle/Miembros de grupo  → 1 (Grupos)
            //   Bloques/TareasBloque/CrearTarea/Entregas/CalificarEntrega → 2 (Tareas)
            //   Conversacion → 3 (Chat)
            if (id == R.id.docenteCrearGrupoFragment
                    || id == R.id.docenteGrupoDetalleFragment
                    || id == R.id.docenteMiembrosGrupoFragment) {
                selectItem(1); // indice Grupos
            } else if (id == R.id.docenteBloquesFragment
                    || id == R.id.docenteTareasBloqueFragment
                    || id == R.id.docenteCrearTareaFragment
                    || id == R.id.docenteEntregasFragment
                    || id == R.id.docenteCalificarEntregaFragment) {
                selectItem(2); // indice Tareas
            } else if (id == R.id.docenteConversacionFragment) {
                selectItem(3); // indice Chat
            }
        });
    }

    private void selectItem(int activeIndex) {
        AutoTransition t = new AutoTransition();
        t.setDuration(180);
        TransitionManager.beginDelayedTransition(binding.bottomNavDocente, t);

        int activeIcon   = ContextCompat.getColor(this, R.color.nav_icon_active);
        int inactiveIcon = ContextCompat.getColor(this, R.color.nav_icon_inactive);

        for (int i = 0; i < items.length; i++) {
            NavItem item = items[i];
            boolean active = (i == activeIndex);

            if (active) {
                item.container.setBackgroundResource(R.drawable.bg_nav_item_active);
                item.circle.setBackgroundResource(R.drawable.bg_icon_circle_active);
                item.circle.setScaleX(0.8f);
                item.circle.setScaleY(0.8f);
                item.label.setVisibility(View.VISIBLE);
                ImageViewCompat.setImageTintList(item.icon, ColorStateList.valueOf(activeIcon));
            } else {
                item.container.setBackground(null);
                item.circle.setBackgroundResource(R.drawable.bg_icon_circle_inactive);
                item.circle.setScaleX(1f);
                item.circle.setScaleY(1f);
                item.label.setVisibility(View.GONE);
                ImageViewCompat.setImageTintList(item.icon, ColorStateList.valueOf(inactiveIcon));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
```

- [ ] **Step 3: Verificar que compila**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -10
```

Expected: `BUILD SUCCESSFUL`.

Si hay error de "cannot resolve symbol" sobre `binding.circleTareas`, `binding.iconTareas`, `binding.labelTareas` o variantes para Chat: revisar `activity_main_docente.xml` (Task 8) — los IDs deben ser exactamente `circleTareas`, `iconTareas`, `labelTareas`, `circleChat`, `iconChat`, `labelChat`.

- [ ] **Step 4: Commit**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
git add app/src/main/java/com/conectatec/ui/docente/MainDocenteActivity.java
git commit -m "feat(docente): add MainDocenteActivity with pill nav and sub-dest mapping"
```

---

### Task 17: Registrar `MainDocenteActivity` en el manifest

Agregar la entrada `<activity>` para `MainDocenteActivity` en el `AndroidManifest.xml`. Como en el Admin, NO se le pone `intent-filter` activo — se deja un comentario explicito y un bloque de intent-filter comentado para activarlo cuando el login enrute por rol.

**Files:**
- Modify: `app/src/main/AndroidManifest.xml`

- [ ] **Step 1: Modificar el manifest**

Insertar el bloque siguiente **inmediatamente despues** del bloque `<activity android:name=".ui.admin.MainAdminActivity" ...>`...`</activity>` y **antes** de `<activity android:name=".ui.auth.WaitingApprovalActivity" ...>` (es decir, dentro de `<application>`, manteniendo el orden visual del archivo):

```xml
        <!-- TODO: quitar el intent-filter de MainDocenteActivity cuando el login enrute por rol -->
        <activity
            android:name=".ui.docente.MainDocenteActivity"
            android:exported="true">
            <!--
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
            -->
        </activity>
```

El archivo despues del cambio debe verse asi (fragmento relevante):

```xml
        <!-- TODO: quitar el intent-filter de MainAdminActivity cuando el login enrute por rol -->
        <activity
            android:name=".ui.admin.MainAdminActivity"
            android:exported="true">
            <!--
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
            -->
        </activity>

        <!-- TODO: quitar el intent-filter de MainDocenteActivity cuando el login enrute por rol -->
        <activity
            android:name=".ui.docente.MainDocenteActivity"
            android:exported="true">
            <!--
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
            -->
        </activity>

        <activity
            android:name=".ui.auth.WaitingApprovalActivity"
            android:exported="false" />
```

- [ ] **Step 2: Verificar que el manifest es valido y compila**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -5
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 3: Verificar el contenido del manifest**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
grep -c "MainDocenteActivity" app/src/main/AndroidManifest.xml
```

Expected: `2` (uno en el comentario TODO, uno en `android:name`). Si imprime `>= 1`, OK para los criterios del spec.

- [ ] **Step 4: Commit**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
git add app/src/main/AndroidManifest.xml
git commit -m "feat(docente): register MainDocenteActivity in AndroidManifest"
```

---

### Task 18: Verificacion final del modulo

Comprobar que toda la infra compila, que los inventarios coinciden con lo esperado del spec, y que el manifest registro la activity. Esta task NO genera commits adicionales — solo valida.

**Files:** ninguno modificado.

- [ ] **Step 1: Build limpio**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew clean :app:assembleDebug 2>&1 | tail -10
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 2: Inventario de Java del modulo docente**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
find app/src/main/java/com/conectatec/ui/docente -type f -name "*.java" | wc -l
```

Expected: `6` (1 Activity + 5 Fragments stub).

- [ ] **Step 3: Listado nominal de archivos Java creados**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
find app/src/main/java/com/conectatec/ui/docente -type f -name "*.java" | sort
```

Expected (orden lexicografico):
```
app/src/main/java/com/conectatec/ui/docente/MainDocenteActivity.java
app/src/main/java/com/conectatec/ui/docente/chat/DocenteChatFragment.java
app/src/main/java/com/conectatec/ui/docente/dashboard/DocenteDashboardFragment.java
app/src/main/java/com/conectatec/ui/docente/grupos/DocenteGruposFragment.java
app/src/main/java/com/conectatec/ui/docente/perfil/DocentePerfilFragment.java
app/src/main/java/com/conectatec/ui/docente/tareas/DocenteTareasFragment.java
```

- [ ] **Step 4: Inventario de layouts del modulo docente**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
find app/src/main/res/layout -name "*docente*" | wc -l
```

Expected: `6` (1 activity + 5 placeholders).

- [ ] **Step 5: Inventario de drawables nuevos**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
ls app/src/main/res/drawable/ic_chat.xml \
   app/src/main/res/drawable/ic_clipboard.xml \
   app/src/main/res/drawable/ic_paperclip.xml \
   app/src/main/res/drawable/ic_send.xml \
   app/src/main/res/drawable/ic_plus.xml \
   app/src/main/res/drawable/ic_qr_placeholder.xml
```

Expected: las 6 rutas existen, sin error "No such file or directory".

- [ ] **Step 6: Verificar nav graph**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
test -f app/src/main/res/navigation/nav_docente.xml && echo "OK: nav_docente.xml existe"
grep -c "<fragment" app/src/main/res/navigation/nav_docente.xml
```

Expected: `OK: nav_docente.xml existe` y luego `14` (5 raiz + 9 sub-destinos).

- [ ] **Step 7: Verificar manifest**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
grep -c "MainDocenteActivity" app/src/main/AndroidManifest.xml
```

Expected: `>= 1` (idealmente `2`).

- [ ] **Step 8: Verificar @AndroidEntryPoint en todos los Java del modulo docente**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
grep -L "@AndroidEntryPoint" $(find app/src/main/java/com/conectatec/ui/docente -name "*.java")
```

Expected: salida vacia (todo archivo Java tiene la anotacion). Si imprime alguna ruta, abrir ese archivo y agregar `@AndroidEntryPoint` antes de la declaracion `public class`.

- [ ] **Step 9: Verificar binding=null en todos los Fragments del modulo docente**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
grep -L "binding = null" $(find app/src/main/java/com/conectatec/ui/docente -name "*Fragment.java")
```

Expected: salida vacia (todo Fragment libera el binding). Si imprime alguna ruta, agregar el override de `onDestroyView()` con `binding = null`.

- [ ] **Step 10: Build final**

```bash
cd /home/crisgoat/Desarrollo/ConectaTec && ./gradlew :app:assembleDebug 2>&1 | tail -3
```

Expected: `BUILD SUCCESSFUL`.

Si todas las verificaciones pasan, el plan 1 esta completo. Los planes 2, 3, 4 y 5 ya pueden ejecutarse — el plan 2 (dashboard-perfil) y el plan 5 (chat) pueden correr en paralelo; el plan 3 (grupos) bloquea al plan 4 (tareas) por el modelo dummy de grupos.

---

## Notas de coordinacion con los planes 2-5

- **Plan 2 (dashboard-perfil):** sobreescribe `DocenteDashboardFragment` y `DocentePerfilFragment` (tasks 11 y 15) con la implementacion real del spec §6.1 y §6.2. Tambien crea `fragment_docente_dashboard.xml` y `fragment_docente_perfil.xml` (los placeholders `*_placeholder.xml` quedan obsoletos pero NO se borran — se ignoran). Strings usadas: `subtitle_docente_dashboard`, etc. (los agrega el plan 2).
- **Plan 3 (grupos):** sobreescribe `DocenteGruposFragment` (task 12). Crea `DocenteCrearGrupoFragment`, `DocenteGrupoDetalleFragment`, `DocenteMiembrosGrupoFragment` y los adapters bajo `com.conectatec.ui.docente.grupos.adapter`. Usa `ic_plus` (task 5) y `ic_qr_placeholder` (task 6).
- **Plan 4 (tareas):** sobreescribe `DocenteTareasFragment` (task 13). Crea `DocenteBloquesFragment`, `DocenteTareasBloqueFragment`, `DocenteCrearTareaFragment`, `DocenteEntregasFragment`, `DocenteCalificarEntregaFragment` y los adapters. Usa `ic_plus` (task 5), `ic_paperclip` (task 3 — solo en CalificarEntrega para el adjunto). Depende del modelo de grupos del plan 3.
- **Plan 5 (chat):** sobreescribe `DocenteChatFragment` (task 14). Crea `DocenteConversacionFragment` y los adapters de salas/mensajes. Usa `ic_paperclip` (task 3) y `ic_send` (task 4).

**Riesgo conocido:** los layouts placeholder `fragment_docente_*_placeholder.xml` quedan en el repo despues de los planes 2-5. No se eliminan automaticamente. Si esto molesta, agregar al final de cada plan 2-5 un task opcional `git rm app/src/main/res/layout/fragment_docente_<modulo>_placeholder.xml` y un commit "chore: remove obsolete docente placeholder layout".
