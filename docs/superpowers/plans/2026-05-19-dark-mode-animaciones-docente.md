# Modo Oscuro/Claro + Animaciones Módulo Docente — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Agregar soporte DayNight con toggle en Ajustes, y aplicar EntradaAnimator + ScrollRevealAnimator a todos los fragments del módulo Docente.

**Architecture:** El sistema de temas usa `Theme.Material3.DayNight.NoActionBar` con dos paletas de colores (`values/colors.xml` para claro, `values-night/colors.xml` para oscuro). `ThemePreferenceManager` persiste la preferencia en SharedPreferences y delega a `AppCompatDelegate`. El `AjustesFragment` es un fragment compartido navegable desde ambos perfiles (Admin y Docente). El sistema de animaciones (`EntradaAnimator`, `ScrollRevealAnimator`) se replica en los 12 fragments del módulo Docente que aún no lo tienen.

**Tech Stack:** Java 11, Material3 DayNight, AppCompatDelegate, SharedPreferences, Navigation Component, Hilt, ViewBinding

---

### Task 1: Crear values-night/colors.xml con la paleta oscura

**Files:**
- Create: `app/src/main/res/values-night/colors.xml`

- [ ] **Verificar que el directorio values-night exista**

```bash
ls app/src/main/res/values-night/
```
Si no existe: `mkdir -p app/src/main/res/values-night/`

- [ ] **Crear `app/src/main/res/values-night/colors.xml`** con los valores oscuros actuales (movidos desde values/colors.xml):

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>

    <!-- Fondos (oscuro) -->
    <color name="colorBackground">#262625</color>
    <color name="colorSurface">#1E1E1E</color>
    <color name="colorSurfaceVariant">#2C2C2C</color>

    <!-- Texto (oscuro) -->
    <color name="colorOnSurface">#FFFFFF</color>
    <color name="colorOnSurfaceVariant">#AAAAAA</color>

    <!-- Divisores (oscuro) -->
    <color name="colorDivider">#2C2C2C</color>
    <color name="colorBorder">#333333</color>

    <!-- Chat (oscuro) -->
    <color name="colorMensajeEnviado">#2C2C2C</color>
    <color name="colorMensajeRecibido">#1E1E1E</color>

    <!-- Navegación (oscuro) -->
    <color name="nav_screen_bg">#0A0A0A</color>
    <color name="nav_container_bg">#1A1A1A</color>
    <color name="nav_item_active_bg">#2A2A2A</color>
    <color name="nav_item_border_bg">#2F2F2F</color>
    <color name="nav_circle_inactive">#2A2A2A</color>
    <color name="nav_circle_active">#1565C0</color>
    <color name="nav_icon_inactive">#B0B0B0</color>
    <color name="nav_icon_active">#FFFFFF</color>
    <color name="nav_label">#F5F5F5</color>

</resources>
```

- [ ] **Build de verificación**

```bash
./gradlew assembleDebug 2>&1 | tail -5
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Commit**

```bash
git add app/src/main/res/values-night/colors.xml
git commit -m "feat: agregar paleta de colores oscura en values-night"
```

---

### Task 2: Actualizar values/colors.xml con paleta clara

**Files:**
- Modify: `app/src/main/res/values/colors.xml`

- [ ] **Reemplazar el contenido completo de `app/src/main/res/values/colors.xml`** con la paleta clara. Los colores compartidos (primarios, estado, rol) permanecen; los semánticos (fondo, superficie, texto, nav, chat, divisores) toman valores claros:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>

    <!-- Primario (igual en ambos temas) -->
    <color name="colorPrimary">#1976D2</color>
    <color name="colorPrimaryDark">#1565C0</color>
    <color name="colorPrimaryLight">#42A5F5</color>

    <!-- Fondos (claro) -->
    <color name="colorBackground">#F2F4F8</color>
    <color name="colorSurface">#FFFFFF</color>
    <color name="colorSurfaceVariant">#EAECF0</color>

    <!-- Texto (claro) -->
    <color name="colorOnSurface">#1A1A1A</color>
    <color name="colorOnSurfaceVariant">#5A5A6A</color>
    <color name="colorOnPrimary">#FFFFFF</color>

    <!-- Estado (igual en ambos temas) -->
    <color name="colorError">#CF6679</color>
    <color name="colorSuccess">#4CAF50</color>
    <color name="colorWarning">#F57C00</color>

    <!-- Chips de rol (igual en ambos temas) -->
    <color name="colorChipDocente">#1976D2</color>
    <color name="colorChipEstudiante">#388E3C</color>
    <color name="colorChipPendiente">#F57C00</color>
    <color name="colorChipAdmin">#7B1FA2</color>

    <!-- Chat (claro) -->
    <color name="colorMensajeEnviado">#E3EAF5</color>
    <color name="colorMensajeRecibido">#FFFFFF</color>

    <!-- Tareas (igual en ambos temas) -->
    <color name="colorTareaUrgente">#1976D2</color>
    <color name="colorCalificacion">#4CAF50</color>

    <!-- Divisores (claro) -->
    <color name="colorDivider">#D0D4DE</color>
    <color name="colorBorder">#C8CCD8</color>

    <!-- Básicos -->
    <color name="white">#FFFFFF</color>
    <color name="black">#000000</color>

    <!-- Navegación (claro) -->
    <color name="nav_screen_bg">#F2F4F8</color>
    <color name="nav_container_bg">#FFFFFF</color>
    <color name="nav_item_active_bg">#EEF2FA</color>
    <color name="nav_item_border_bg">#E0E4EE</color>
    <color name="nav_circle_inactive">#EEF2FA</color>
    <color name="nav_circle_active">#1565C0</color>
    <color name="nav_icon_inactive">#8A8A9A</color>
    <color name="nav_icon_active">#FFFFFF</color>
    <color name="nav_label">#1A1A1A</color>

    <!-- Dashboard Activity (modo claro) -->
    <color name="colorDashBg">#F4F6FA</color>
    <color name="colorDashCard">#FFFFFF</color>
    <color name="colorDashPrimary">#6C63FF</color>
    <color name="colorDashText">#1A1A2E</color>
    <color name="colorDashTextSecondary">#6B7280</color>
    <color name="colorDashTextTertiary">#9CA3AF</color>
    <color name="colorDashGreen">#10B981</color>
    <color name="colorDashAmber">#F59E0B</color>
    <color name="colorDashRed">#EF4444</color>
    <color name="colorDashBlue">#3B82F6</color>
    <color name="colorDashViolet">#8B5CF6</color>
    <color name="colorDashDivider">#E5E7EB</color>

</resources>
```

- [ ] **Build de verificación**

```bash
./gradlew assembleDebug 2>&1 | tail -5
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Commit**

```bash
git add app/src/main/res/values/colors.xml
git commit -m "feat: paleta clara como default en values/colors.xml"
```

---

### Task 3: Actualizar themes.xml a DayNight

**Files:**
- Modify: `app/src/main/res/values/themes.xml`
- Modify: `app/src/main/res/values-night/themes.xml`

- [ ] **En `app/src/main/res/values/themes.xml`**, cambiar el parent del tema principal y `windowLightStatusBar`. Reemplazar solo las dos líneas afectadas:

Cambiar:
```xml
<style name="Theme.TecConnect" parent="Theme.Material3.Dark.NoActionBar">
```
Por:
```xml
<style name="Theme.TecConnect" parent="Theme.Material3.DayNight.NoActionBar">
```

Y cambiar:
```xml
<item name="android:windowLightStatusBar">false</item>
```
Por:
```xml
<item name="android:windowLightStatusBar">true</item>
```

- [ ] **Reemplazar el contenido completo de `app/src/main/res/values-night/themes.xml`** (actualmente es copia idéntica del light) con solo la override del status bar:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>

    <!-- Override nocturno: status bar oscuro -->
    <style name="Theme.TecConnect" parent="Theme.Material3.DayNight.NoActionBar">
        <item name="android:windowLightStatusBar">false</item>
    </style>

</resources>
```

- [ ] **Build de verificación**

```bash
./gradlew assembleDebug 2>&1 | tail -5
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Commit**

```bash
git add app/src/main/res/values/themes.xml app/src/main/res/values-night/themes.xml
git commit -m "feat: cambiar tema a DayNight con status bar adaptativo"
```

---

### Task 4: Crear ThemePreferenceManager

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/common/ThemePreferenceManager.java`

- [ ] **Crear `app/src/main/java/com/conectatec/ui/common/ThemePreferenceManager.java`**:

```java
package com.conectatec.ui.common;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public final class ThemePreferenceManager {

    private static final String PREFS_NAME = "tecconnect_prefs";
    private static final String KEY_THEME  = "app_theme_mode";

    private ThemePreferenceManager() {}

    public static void applyTheme(Context context) {
        int mode = prefs(context).getInt(KEY_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    public static void setThemeMode(Context context, int mode) {
        prefs(context).edit().putInt(KEY_THEME, mode).apply();
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    public static int getCurrentMode(Context context) {
        return prefs(context).getInt(KEY_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
}
```

- [ ] **Build de verificación**

```bash
./gradlew assembleDebug 2>&1 | tail -5
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Commit**

```bash
git add app/src/main/java/com/conectatec/ui/common/ThemePreferenceManager.java
git commit -m "feat: agregar ThemePreferenceManager para persistir modo de tema"
```

---

### Task 5: Aplicar applyTheme en TecConnectApp

**Files:**
- Modify: `app/src/main/java/com/conectatec/TecConnectApp.java`

- [ ] **Modificar `TecConnectApp.java`** para aplicar el tema al inicio de la app (antes de que se cree cualquier Activity):

```java
package com.conectatec;

import android.app.Application;
import com.conectatec.ui.common.ThemePreferenceManager;
import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class TecConnectApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ThemePreferenceManager.applyTheme(this);
    }
}
```

- [ ] **Build de verificación**

```bash
./gradlew assembleDebug 2>&1 | tail -5
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Prueba manual:** Instalar el APK, activar modo oscuro en el sistema Android y abrir la app. Verificar que la UI refleja el tema oscuro. Desactivar modo oscuro del sistema: la UI debe cambiar a modo claro.

- [ ] **Commit**

```bash
git add app/src/main/java/com/conectatec/TecConnectApp.java
git commit -m "feat: aplicar tema DayNight desde Application.onCreate"
```

---

### Task 6: Crear fragment_ajustes.xml

**Files:**
- Create: `app/src/main/res/layout/fragment_ajustes.xml`

- [ ] **Crear `app/src/main/res/layout/fragment_ajustes.xml`**:

```xml
<?xml version="1.0" encoding="utf-8"?>
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
        android:paddingBottom="40dp">

        <!-- Header -->
        <LinearLayout
            android:id="@+id/headerAjustes"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:gravity="center_vertical"
            android:paddingHorizontal="8dp"
            android:paddingTop="48dp"
            android:paddingBottom="16dp">

            <ImageButton
                android:id="@+id/btnBackAjustes"
                android:layout_width="48dp"
                android:layout_height="48dp"
                android:background="?attr/selectableItemBackgroundBorderless"
                android:src="@drawable/ic_arrow_left"
                android:contentDescription="Volver"
                app:tint="@color/colorOnSurface" />

            <TextView
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:layout_marginStart="4dp"
                android:text="Ajustes"
                android:textColor="@color/colorOnSurface"
                android:textSize="22sp"
                android:textStyle="bold"
                android:fontFamily="sans-serif-medium" />

        </LinearLayout>

        <!-- ═══ Sección Apariencia ═══ -->
        <TextView
            android:id="@+id/tvSeccionApariencia"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginHorizontal="20dp"
            android:layout_marginBottom="8dp"
            android:text="APARIENCIA"
            android:textColor="@color/colorPrimary"
            android:textSize="11sp"
            android:letterSpacing="0.1"
            android:textStyle="bold" />

        <com.google.android.material.card.MaterialCardView
            android:id="@+id/cardTema"
            style="@style/Widget.TecConnect.Card"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginHorizontal="16dp">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:paddingHorizontal="20dp"
                android:paddingVertical="16dp">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Tema de la aplicación"
                    android:textColor="@color/colorOnSurface"
                    android:textSize="14sp"
                    android:textStyle="bold"
                    android:layout_marginBottom="8dp" />

                <RadioGroup
                    android:id="@+id/radioGroupTema"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="vertical">

                    <RadioButton
                        android:id="@+id/radioSistema"
                        android:layout_width="match_parent"
                        android:layout_height="48dp"
                        android:text="Sistema (automático)"
                        android:textColor="@color/colorOnSurface"
                        android:textSize="14sp"
                        android:buttonTint="@color/colorPrimary" />

                    <RadioButton
                        android:id="@+id/radioClaro"
                        android:layout_width="match_parent"
                        android:layout_height="48dp"
                        android:text="Claro"
                        android:textColor="@color/colorOnSurface"
                        android:textSize="14sp"
                        android:buttonTint="@color/colorPrimary" />

                    <RadioButton
                        android:id="@+id/radioOscuro"
                        android:layout_width="match_parent"
                        android:layout_height="48dp"
                        android:text="Oscuro"
                        android:textColor="@color/colorOnSurface"
                        android:textSize="14sp"
                        android:buttonTint="@color/colorPrimary" />

                </RadioGroup>
            </LinearLayout>
        </com.google.android.material.card.MaterialCardView>

        <!-- ═══ Sección Notificaciones ═══ -->
        <TextView
            android:id="@+id/tvSeccionNotificaciones"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginHorizontal="20dp"
            android:layout_marginTop="24dp"
            android:layout_marginBottom="8dp"
            android:text="NOTIFICACIONES"
            android:textColor="@color/colorPrimary"
            android:textSize="11sp"
            android:letterSpacing="0.1"
            android:textStyle="bold" />

        <com.google.android.material.card.MaterialCardView
            android:id="@+id/cardNotificaciones"
            style="@style/Widget.TecConnect.Card"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginHorizontal="16dp">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="56dp"
                android:orientation="horizontal"
                android:gravity="center_vertical"
                android:paddingHorizontal="20dp">

                <TextView
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:text="Notificaciones"
                    android:textColor="@color/colorOnSurface"
                    android:textSize="14sp" />

                <com.google.android.material.materialswitch.MaterialSwitch
                    android:id="@+id/switchNotificaciones"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:checked="true"
                    android:enabled="false" />

            </LinearLayout>
        </com.google.android.material.card.MaterialCardView>

        <!-- ═══ Sección General ═══ -->
        <TextView
            android:id="@+id/tvSeccionGeneral"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginHorizontal="20dp"
            android:layout_marginTop="24dp"
            android:layout_marginBottom="8dp"
            android:text="GENERAL"
            android:textColor="@color/colorPrimary"
            android:textSize="11sp"
            android:letterSpacing="0.1"
            android:textStyle="bold" />

        <com.google.android.material.card.MaterialCardView
            android:id="@+id/cardGeneral"
            style="@style/Widget.TecConnect.Card"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginHorizontal="16dp">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="56dp"
                android:orientation="horizontal"
                android:gravity="center_vertical"
                android:paddingHorizontal="20dp">

                <TextView
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:text="Idioma"
                    android:textColor="@color/colorOnSurface"
                    android:textSize="14sp" />

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Español"
                    android:textColor="@color/colorOnSurfaceVariant"
                    android:textSize="14sp" />

            </LinearLayout>
        </com.google.android.material.card.MaterialCardView>

    </LinearLayout>
</ScrollView>
```

- [ ] **Build de verificación**

```bash
./gradlew assembleDebug 2>&1 | tail -5
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Commit**

```bash
git add app/src/main/res/layout/fragment_ajustes.xml
git commit -m "feat: layout del fragment de ajustes"
```

---

### Task 7: Crear AjustesFragment.java

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/common/ajustes/AjustesFragment.java`

- [ ] **Crear el directorio y el archivo**:

```bash
mkdir -p app/src/main/java/com/conectatec/ui/common/ajustes
```

- [ ] **Crear `app/src/main/java/com/conectatec/ui/common/ajustes/AjustesFragment.java`**:

```java
package com.conectatec.ui.common.ajustes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.databinding.FragmentAjustesBinding;
import com.conectatec.ui.common.EntradaAnimator;
import com.conectatec.ui.common.ThemePreferenceManager;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AjustesFragment extends Fragment {

    private FragmentAjustesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAjustesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        marcarModoActual();
        setupListeners();
        EntradaAnimator.animar(
                binding.headerAjustes,
                binding.cardTema,
                binding.cardNotificaciones,
                binding.cardGeneral
        );
    }

    private void marcarModoActual() {
        int modo = ThemePreferenceManager.getCurrentMode(requireContext());
        if (modo == AppCompatDelegate.MODE_NIGHT_NO) {
            binding.radioClaro.setChecked(true);
        } else if (modo == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.radioOscuro.setChecked(true);
        } else {
            binding.radioSistema.setChecked(true);
        }
    }

    private void setupListeners() {
        binding.btnBackAjustes.setOnClickListener(v ->
                Navigation.findNavController(requireView()).navigateUp());

        binding.radioGroupTema.setOnCheckedChangeListener((group, checkedId) -> {
            int modo;
            if (checkedId == R.id.radioClaro) {
                modo = AppCompatDelegate.MODE_NIGHT_NO;
            } else if (checkedId == R.id.radioOscuro) {
                modo = AppCompatDelegate.MODE_NIGHT_YES;
            } else {
                modo = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
            }
            ThemePreferenceManager.setThemeMode(requireContext(), modo);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
```

- [ ] **Build de verificación**

```bash
./gradlew assembleDebug 2>&1 | tail -5
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Commit**

```bash
git add app/src/main/java/com/conectatec/ui/common/ajustes/AjustesFragment.java
git commit -m "feat: AjustesFragment con selector de tema claro/oscuro/sistema"
```

---

### Task 8: Agregar destino Ajustes en ambos nav graphs

**Files:**
- Modify: `app/src/main/res/navigation/nav_admin.xml`
- Modify: `app/src/main/res/navigation/nav_docente.xml`

- [ ] **En `nav_admin.xml`**: justo antes de `</navigation>`, agregar el destino y la acción dentro del fragment de perfil.

Reemplazar:
```xml
    <!-- Perfil -->
    <fragment
        android:id="@+id/adminPerfilFragment"
        android:name="com.conectatec.ui.admin.perfil.AdminPerfilFragment"
        android:label="@string/title_admin_perfil" />

</navigation>
```

Por:
```xml
    <!-- Perfil -->
    <fragment
        android:id="@+id/adminPerfilFragment"
        android:name="com.conectatec.ui.admin.perfil.AdminPerfilFragment"
        android:label="@string/title_admin_perfil">
        <action
            android:id="@+id/action_admin_perfil_to_ajustes"
            app:destination="@id/ajustesFragment" />
    </fragment>

    <!-- Ajustes -->
    <fragment
        android:id="@+id/ajustesFragment"
        android:name="com.conectatec.ui.common.ajustes.AjustesFragment"
        android:label="Ajustes" />

</navigation>
```

- [ ] **En `nav_docente.xml`**: Reemplazar el bloque del perfil:

Reemplazar:
```xml
    <!-- 4 · Perfil -->
    <fragment
        android:id="@+id/docentePerfilFragment"
        android:name="com.conectatec.ui.docente.perfil.DocentePerfilFragment"
        android:label="@string/title_docente_perfil" />
```

Por:
```xml
    <!-- 4 · Perfil -->
    <fragment
        android:id="@+id/docentePerfilFragment"
        android:name="com.conectatec.ui.docente.perfil.DocentePerfilFragment"
        android:label="@string/title_docente_perfil">
        <action
            android:id="@+id/action_docente_perfil_to_ajustes"
            app:destination="@id/ajustesFragment" />
    </fragment>

    <!-- Ajustes -->
    <fragment
        android:id="@+id/ajustesFragment"
        android:name="com.conectatec.ui.common.ajustes.AjustesFragment"
        android:label="Ajustes" />
```

- [ ] **Build de verificación**

```bash
./gradlew assembleDebug 2>&1 | tail -5
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Commit**

```bash
git add app/src/main/res/navigation/nav_admin.xml app/src/main/res/navigation/nav_docente.xml
git commit -m "feat: agregar destino AjustesFragment en nav_admin y nav_docente"
```

---

### Task 9: Conectar AdminPerfilFragment a Ajustes

**Files:**
- Modify: `app/src/main/res/layout/fragment_admin_perfil.xml`
- Modify: `app/src/main/java/com/conectatec/ui/admin/perfil/AdminPerfilFragment.java`

- [ ] **En `fragment_admin_perfil.xml`**, agregar una fila de Ajustes antes del botón de cerrar sesión. Reemplazar:

```xml
        <!-- Espaciador -->
        <Space
            android:layout_width="match_parent"
            android:layout_height="8dp" />

        <!-- Botón cerrar sesión -->
```

Por:
```xml
        <!-- Fila Ajustes -->
        <com.google.android.material.card.MaterialCardView
            android:id="@+id/cardAjustesAdmin"
            style="@style/Widget.TecConnect.Card"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="12dp">

            <LinearLayout
                android:id="@+id/rowAjustesAdmin"
                android:layout_width="match_parent"
                android:layout_height="56dp"
                android:orientation="horizontal"
                android:gravity="center_vertical"
                android:paddingHorizontal="20dp"
                android:background="?attr/selectableItemBackground">

                <TextView
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:text="Ajustes"
                    android:textColor="@color/colorOnSurface"
                    android:textSize="14sp" />

                <ImageView
                    android:layout_width="20dp"
                    android:layout_height="20dp"
                    android:src="@drawable/ic_arrow_left"
                    android:rotation="180"
                    app:tint="@color/colorOnSurfaceVariant"
                    android:contentDescription="Ir a Ajustes" />

            </LinearLayout>
        </com.google.android.material.card.MaterialCardView>

        <!-- Espaciador -->
        <Space
            android:layout_width="match_parent"
            android:layout_height="8dp" />

        <!-- Botón cerrar sesión -->
```

- [ ] **En `AdminPerfilFragment.java`**, agregar import de Navigation y el listener de Ajustes. Reemplazar el método `onViewCreated`:

```java
import androidx.navigation.Navigation;
import com.conectatec.R;
import com.conectatec.ui.common.EntradaAnimator;
```

Reemplazar `onViewCreated`:
```java
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: cargar desde SessionManager cuando esté implementado.
        binding.rowAjustesAdmin.setOnClickListener(v ->
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_admin_perfil_to_ajustes));
        binding.btnLogoutAdmin.setOnClickListener(v -> confirmarCerrarSesion());
        EntradaAnimator.animar(binding.cardAjustesAdmin, binding.btnLogoutAdmin);
    }
```

- [ ] **Build de verificación**

```bash
./gradlew assembleDebug 2>&1 | tail -5
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Prueba manual:** Abrir la app como Admin → tab Perfil → tocar "Ajustes" → debe navegar a AjustesFragment. Cambiar el tema → la UI debe cambiar de inmediato.

- [ ] **Commit**

```bash
git add app/src/main/res/layout/fragment_admin_perfil.xml \
        app/src/main/java/com/conectatec/ui/admin/perfil/AdminPerfilFragment.java
git commit -m "feat: conectar perfil Admin a AjustesFragment"
```

---

### Task 10: Conectar DocentePerfilFragment a Ajustes

**Files:**
- Modify: `app/src/main/java/com/conectatec/ui/docente/perfil/DocentePerfilFragment.java`

- [ ] **En `DocentePerfilFragment.java`**, agregar import de Navigation y R, y conectar `rowTemaDocente` a Ajustes.

Agregar imports:
```java
import androidx.navigation.Navigation;
import com.conectatec.R;
```

En `setupListeners()`, reemplazar la línea del `rowTemaDocente`:
```java
        binding.rowTemaDocente.setOnClickListener(v ->
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_docente_perfil_to_ajustes));
```

El archivo completo resultante:
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
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.databinding.FragmentDocentePerfilBinding;
import com.conectatec.ui.common.EntradaAnimator;
import com.conectatec.ui.auth.LoginActivity;

import dagger.hilt.android.AndroidEntryPoint;

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
        EntradaAnimator.animar(
            binding.cardHeroPerfilDocente,
            binding.cardInfoCuentaDocente,
            binding.cardConfigDocente,
            binding.btnCerrarSesionDocente
        );
    }

    private void setupListeners() {
        binding.rowNotificacionesDocente.setOnClickListener(v -> { /* placeholder */ });
        binding.rowTemaDocente.setOnClickListener(v ->
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_docente_perfil_to_ajustes));
        binding.rowIdiomaDocente.setOnClickListener(v -> { /* placeholder */ });
        binding.btnCerrarSesionDocente.setOnClickListener(v -> cerrarSesion());
    }

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

- [ ] **Build de verificación**

```bash
./gradlew assembleDebug 2>&1 | tail -5
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Prueba manual:** Abrir como Docente → tab Perfil → tocar fila "Tema" → debe abrir AjustesFragment.

- [ ] **Commit**

```bash
git add app/src/main/java/com/conectatec/ui/docente/perfil/DocentePerfilFragment.java
git commit -m "feat: conectar fila Tema del perfil Docente a AjustesFragment"
```

---

### Task 11: EntradaAnimator en 5 fragments Docente — Dashboard y Grupos

**Files:**
- Modify: `app/src/main/java/com/conectatec/ui/docente/dashboard/DocenteDashboardFragment.java`
- Modify: `app/src/main/java/com/conectatec/ui/docente/grupos/DocenteGruposFragment.java`
- Modify: `app/src/main/java/com/conectatec/ui/docente/grupos/DocenteGrupoDetalleFragment.java`
- Modify: `app/src/main/java/com/conectatec/ui/docente/grupos/DocenteMiembrosGrupoFragment.java`
- Modify: `app/src/main/java/com/conectatec/ui/docente/grupos/DocenteCrearGrupoFragment.java`

Para cada fragment, el patrón es el mismo: agregar import de `EntradaAnimator` y llamarlo al final de `onViewCreated` con las vistas principales.

- [ ] **`DocenteDashboardFragment.java`** — al final de `onViewCreated` agregar:

```java
import com.conectatec.ui.common.EntradaAnimator;
// ...
EntradaAnimator.animar(
    binding.cardBienvenidaDocente,
    binding.layoutKpisDocente,
    binding.cardTareasRecientesDashboard,
    binding.cardActividadHoyDashboard
);
```

- [ ] **`DocenteGruposFragment.java`** — al final de `onViewCreated` agregar:

```java
import com.conectatec.ui.common.EntradaAnimator;
// ...
EntradaAnimator.animar(
    binding.tvHeaderTotalGruposDocente,
    binding.etBuscarGrupoDocente,
    binding.rvGruposDocente
);
```

- [ ] **`DocenteGrupoDetalleFragment.java`** — al final de `onViewCreated` agregar:

```java
import com.conectatec.ui.common.EntradaAnimator;
// ...
EntradaAnimator.animar(
    binding.cardHeroGrupo,
    binding.cardAccionesRapidas,
    binding.cardMiembrosGrupo,
    binding.cardAvisosGrupo
);
```

- [ ] **`DocenteMiembrosGrupoFragment.java`** — al final de `onViewCreated` agregar:

```java
import com.conectatec.ui.common.EntradaAnimator;
// ...
EntradaAnimator.animar(
    binding.tvSubtituloMiembrosDocente,
    binding.rvMiembrosGrupoDocente
);
```

- [ ] **`DocenteCrearGrupoFragment.java`** — al final de `onViewCreated` agregar:

```java
import com.conectatec.ui.common.EntradaAnimator;
// ...
EntradaAnimator.animar(
    binding.cardPreviewGrupo,
    binding.containerFormGrupoNuevo,
    binding.btnCrearGrupoNuevo
);
```

- [ ] **Build de verificación**

```bash
./gradlew assembleDebug 2>&1 | tail -5
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Commit**

```bash
git add app/src/main/java/com/conectatec/ui/docente/dashboard/DocenteDashboardFragment.java \
        app/src/main/java/com/conectatec/ui/docente/grupos/DocenteGruposFragment.java \
        app/src/main/java/com/conectatec/ui/docente/grupos/DocenteGrupoDetalleFragment.java \
        app/src/main/java/com/conectatec/ui/docente/grupos/DocenteMiembrosGrupoFragment.java \
        app/src/main/java/com/conectatec/ui/docente/grupos/DocenteCrearGrupoFragment.java
git commit -m "feat: EntradaAnimator en fragments Dashboard y Grupos del Docente"
```

---

### Task 12: EntradaAnimator en 7 fragments Docente — Tareas y Chat

**Files:**
- Modify: `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteTareasFragment.java`
- Modify: `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteBloquesFragment.java`
- Modify: `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteTareasBloqueFragment.java`
- Modify: `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteCrearTareaFragment.java`
- Modify: `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteEntregasFragment.java`
- Modify: `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteCalificarEntregaFragment.java`
- Modify: `app/src/main/java/com/conectatec/ui/docente/chat/DocenteChatFragment.java`

- [ ] **`DocenteTareasFragment.java`** — agregar import y llamada:

```java
import com.conectatec.ui.common.EntradaAnimator;
// al final de onViewCreated:
EntradaAnimator.animar(
    binding.tvHeaderTotalGruposTareas,
    binding.rvTareasGrupos
);
```

- [ ] **`DocenteBloquesFragment.java`** — agregar import y llamada:

```java
import com.conectatec.ui.common.EntradaAnimator;
// al final de onViewCreated:
EntradaAnimator.animar(
    binding.tvTituloBloques,
    binding.tvSubtituloBloques,
    binding.rvBloques
);
```

- [ ] **`DocenteTareasBloqueFragment.java`** — agregar import y llamada:

```java
import com.conectatec.ui.common.EntradaAnimator;
// al final de onViewCreated:
EntradaAnimator.animar(
    binding.tvTituloTareasBloque,
    binding.chipGroupTipo,
    binding.chipGroupEstado,
    binding.rvTareasBloque
);
```

- [ ] **`DocenteCrearTareaFragment.java`** — agregar import y llamada:

```java
import com.conectatec.ui.common.EntradaAnimator;
// al final de onViewCreated:
EntradaAnimator.animar(
    binding.cardPreviewTarea,
    binding.etTituloTareaNueva,
    binding.etDescripcionTareaNueva,
    binding.rowFechaLimiteTarea,
    binding.btnPublicarTarea
);
```

- [ ] **`DocenteEntregasFragment.java`** — agregar import y llamada:

```java
import com.conectatec.ui.common.EntradaAnimator;
// al final de onViewCreated:
EntradaAnimator.animar(
    binding.progressEntregasResumen,
    binding.chipGroupEntregas,
    binding.rvEntregas
);
```

- [ ] **`DocenteCalificarEntregaFragment.java`** — agregar import y llamada:

```java
import com.conectatec.ui.common.EntradaAnimator;
// al final de onViewCreated:
EntradaAnimator.animar(
    binding.cardAlumnoCalif,
    binding.containerArchivoCalif,
    binding.cardCalificacionEntrega,
    binding.btnGuardarCalificacion
);
```

- [ ] **`DocenteChatFragment.java`** — agregar import y llamada:

```java
import com.conectatec.ui.common.EntradaAnimator;
// al final de onViewCreated:
EntradaAnimator.animar(
    binding.headerChatDocente,
    binding.scrollChipsChat,
    binding.rvSalasChat
);
```

- [ ] **Build de verificación**

```bash
./gradlew assembleDebug 2>&1 | tail -5
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Commit**

```bash
git add app/src/main/java/com/conectatec/ui/docente/tareas/DocenteTareasFragment.java \
        app/src/main/java/com/conectatec/ui/docente/tareas/DocenteBloquesFragment.java \
        app/src/main/java/com/conectatec/ui/docente/tareas/DocenteTareasBloqueFragment.java \
        app/src/main/java/com/conectatec/ui/docente/tareas/DocenteCrearTareaFragment.java \
        app/src/main/java/com/conectatec/ui/docente/tareas/DocenteEntregasFragment.java \
        app/src/main/java/com/conectatec/ui/docente/tareas/DocenteCalificarEntregaFragment.java \
        app/src/main/java/com/conectatec/ui/docente/chat/DocenteChatFragment.java
git commit -m "feat: EntradaAnimator en fragments Tareas y Chat del Docente"
```

---

### Task 13: ScrollRevealAnimator en 7 fragments Docente

**Files:**
- Modify: `app/src/main/java/com/conectatec/ui/docente/grupos/DocenteGruposFragment.java`
- Modify: `app/src/main/java/com/conectatec/ui/docente/grupos/DocenteMiembrosGrupoFragment.java`
- Modify: `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteTareasFragment.java`
- Modify: `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteBloquesFragment.java`
- Modify: `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteTareasBloqueFragment.java`
- Modify: `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteEntregasFragment.java`
- Modify: `app/src/main/java/com/conectatec/ui/docente/chat/DocenteChatFragment.java`

El patrón para cada fragment: agregar el import, declarar el campo, instanciar el `ScrollRevealAnimator` **después** de `adapter.submitList()` o de asignar el adapter al RecyclerView, y llamar `triggerInicial()` después de cargar datos.

- [ ] **`DocenteGruposFragment.java`** — agregar import y campo:

```java
import com.conectatec.ui.common.ScrollRevealAnimator;
// campo en la clase:
private ScrollRevealAnimator scrollReveal;
```

En `onViewCreated`, después de asignar el adapter a `rvGruposDocente`:
```java
scrollReveal = new ScrollRevealAnimator(binding.rvGruposDocente);
scrollReveal.triggerInicial();
```

- [ ] **`DocenteMiembrosGrupoFragment.java`** — agregar import y campo:

```java
import com.conectatec.ui.common.ScrollRevealAnimator;
private ScrollRevealAnimator scrollReveal;
```

Después de asignar el adapter a `rvMiembrosGrupoDocente`:
```java
scrollReveal = new ScrollRevealAnimator(binding.rvMiembrosGrupoDocente);
scrollReveal.triggerInicial();
```

- [ ] **`DocenteTareasFragment.java`** — agregar import y campo:

```java
import com.conectatec.ui.common.ScrollRevealAnimator;
private ScrollRevealAnimator scrollReveal;
```

Después de asignar el adapter a `rvTareasGrupos`:
```java
scrollReveal = new ScrollRevealAnimator(binding.rvTareasGrupos);
scrollReveal.triggerInicial();
```

- [ ] **`DocenteBloquesFragment.java`** — agregar import y campo:

```java
import com.conectatec.ui.common.ScrollRevealAnimator;
private ScrollRevealAnimator scrollReveal;
```

Después de asignar el adapter a `rvBloques`:
```java
scrollReveal = new ScrollRevealAnimator(binding.rvBloques);
scrollReveal.triggerInicial();
```

- [ ] **`DocenteTareasBloqueFragment.java`** — agregar import y campo:

```java
import com.conectatec.ui.common.ScrollRevealAnimator;
private ScrollRevealAnimator scrollReveal;
```

Después de asignar el adapter a `rvTareasBloque`:
```java
scrollReveal = new ScrollRevealAnimator(binding.rvTareasBloque);
scrollReveal.triggerInicial();
```

Si el fragment llama a un método de filtro que recarga el adapter (p.ej. `aplicarFiltros()`), agregar también `scrollReveal.triggerInicial()` al final de ese método.

- [ ] **`DocenteEntregasFragment.java`** — agregar import y campo:

```java
import com.conectatec.ui.common.ScrollRevealAnimator;
private ScrollRevealAnimator scrollReveal;
```

Después de asignar el adapter a `rvEntregas`:
```java
scrollReveal = new ScrollRevealAnimator(binding.rvEntregas);
scrollReveal.triggerInicial();
```

- [ ] **`DocenteChatFragment.java`** — agregar import y campo:

```java
import com.conectatec.ui.common.ScrollRevealAnimator;
private ScrollRevealAnimator scrollReveal;
```

Después de asignar el adapter a `rvSalasChat`:
```java
scrollReveal = new ScrollRevealAnimator(binding.rvSalasChat);
scrollReveal.triggerInicial();
```

- [ ] **Build de verificación**

```bash
./gradlew assembleDebug 2>&1 | tail -5
```
Esperado: `BUILD SUCCESSFUL`

- [ ] **Prueba manual:** Navegar por el módulo Docente con una lista larga (Grupos, Tareas, Chat). Al hacer scroll, los ítems que aparecen desde abajo deben tener el efecto de fade + scale.

- [ ] **Commit**

```bash
git add app/src/main/java/com/conectatec/ui/docente/grupos/DocenteGruposFragment.java \
        app/src/main/java/com/conectatec/ui/docente/grupos/DocenteMiembrosGrupoFragment.java \
        app/src/main/java/com/conectatec/ui/docente/tareas/DocenteTareasFragment.java \
        app/src/main/java/com/conectatec/ui/docente/tareas/DocenteBloquesFragment.java \
        app/src/main/java/com/conectatec/ui/docente/tareas/DocenteTareasBloqueFragment.java \
        app/src/main/java/com/conectatec/ui/docente/tareas/DocenteEntregasFragment.java \
        app/src/main/java/com/conectatec/ui/docente/chat/DocenteChatFragment.java
git commit -m "feat: ScrollRevealAnimator en 7 fragments del módulo Docente"
```
