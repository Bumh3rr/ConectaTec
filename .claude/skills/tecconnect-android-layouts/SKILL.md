---
name: tecconnect-android-layouts
description: Use when creating or editing Android XML layout files (res/layout/*.xml) in the TecConnect (com.conectatec) project — applies to fragments, activities, items and includes. Enforces dark theme references-only palette, custom pill nav (no BottomNavigationView), MaterialCardView styling, chip-as-TextView pattern, and shared empty state include.
---

# TecConnect — Android Layout Conventions

Reglas de layout XML para los módulos UI del proyecto **ConectaTec** (`com.conectatec`). Aplica al editar/crear cualquier archivo bajo `app/src/main/res/layout/`.

## Reglas innegociables

### 1. Paleta solo por referencia
- **Cero literales hex** (`#RRGGBB`, `#RRGGBBAA`) en cualquier atributo.
- Siempre `@color/colorBackground`, `@color/colorSurface`, `@color/colorOnSurface`, `@color/colorOnSurfaceVariant`, `@color/colorPrimary`, `@color/colorChipDocente`, etc.
- Backgrounds custom siempre vía `@drawable/*` existente (`bg_chip_docente`, `bg_chip_estudiante`, `bg_chip_pendiente`, `bg_chip_admin`, `bg_avatar_placeholder`, `bg_card`, `bg_input`, `bg_message_sent`, `bg_message_received`).

### 2. Nav personalizada — nunca BottomNavigationView
- El proyecto usa una pill nav custom (LinearLayout horizontal con items 56dp, círculo + ícono + label oculto).
- Si necesitas nav, replica la estructura de `activity_main_admin.xml`: `bottomNavAdmin` → `pillNavAdmin` (`bg_nav_container`) → 5 ítems con `circle*` (FrameLayout 56dp + `bg_icon_circle_inactive`), `icon*` (ImageView 27dp con `app:tint`), `label*` (TextView visibility=gone por defecto).
- **Nunca** `<com.google.android.material.bottomnavigation.BottomNavigationView>` ni `<menu>` para tabs.

### 3. Header de pantalla raíz
Patrón fijo:
```xml
<LinearLayout orientation="horizontal" gravity="center_vertical"
              background="@color/colorSurface"
              paddingTop="48dp" paddingBottom="20dp" paddingHorizontal="20dp">
    <View width="4dp" height="36dp"
          background="@drawable/bg_accent_bar" marginEnd="14dp" />
    <LinearLayout orientation="vertical" weight="1">
        <TextView text="Título" textColor="@color/colorOnSurface"
                  textSize="26sp" textStyle="bold" />
        <TextView text="Subtítulo" textColor="@color/colorOnSurfaceVariant"
                  textSize="12sp" letterSpacing="0.03" />
    </LinearLayout>
    <!-- opcional: badge contador -->
</LinearLayout>
```

### 4. Header de sub-pantalla (back)
Mismo padding superior + ImageView `ic_arrow_left` 36dp + label "TecConnect" 9sp primary letter-spacing 0.18 + título 20sp bold + línea acento 3dp `colorPrimary`.

### 5. MaterialCardView siempre con esta firma
```xml
<com.google.android.material.card.MaterialCardView
    app:cardBackgroundColor="@color/colorSurface"
    app:cardCornerRadius="12dp"
    app:cardElevation="0dp"
    app:strokeWidth="0dp">
```
Padding interno típico: 20dp. Margin entre cards: 12dp.

### 6. Chips como TextView (no Material Chip)
Para chips de rol o estado en items y headers de detalle:
```xml
<TextView
    android:text="DOCENTE"
    android:textColor="@color/white"
    android:textSize="10sp"
    android:textStyle="bold"
    android:letterSpacing="0.05"
    android:paddingHorizontal="10dp"
    android:paddingVertical="3dp"
    android:background="@drawable/bg_chip_docente" />
```
- Los `bg_chip_*` son ovalados con color sólido. Mapeos:
  - DOCENTE / EN_CURSO / TIPO_TAREA → `bg_chip_docente` (azul)
  - ESTUDIANTE / ENTREGADA / CALIFICADA / TIPO_PROYECTO → `bg_chip_estudiante` (verde)
  - PENDIENTE / TIPO_TRABAJO / BORRADOR → `bg_chip_pendiente` (naranja)
  - ADMIN / VENCIDA / SIN_ENTREGAR / TIPO_EXAMEN → `bg_chip_admin` (morado)

**Excepción:** chips de filtro en `HorizontalScrollView` (que sí cambian estado checked) usan `com.google.android.material.chip.Chip style="@style/Widget.Material3.Chip.Filter"`. Eso ya está establecido en `fragment_admin_grupos.xml` para `chipGruposTodos/Activos/Desactivados`.

### 7. Avatares con iniciales
```xml
<FrameLayout
    android:layout_width="44dp"
    android:layout_height="44dp"
    android:background="@drawable/bg_avatar_placeholder">
    <TextView
        android:layout_gravity="center"
        android:text="CB"
        android:textColor="@color/colorOnSurface"
        android:textSize="14sp"
        android:textStyle="bold" />
</FrameLayout>
```
Tallas estándar: 40dp (entregas), 44dp (lista de miembros), 48dp (dashboard, salas chat), 64dp (calificar entrega), 88dp (perfil hero).

### 8. Empty state compartido
```xml
<include
    android:id="@+id/emptyStateXxx"
    layout="@layout/layout_empty_state"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:visibility="gone" />
```
Nunca dupliques layouts de "no hay resultados" — usa el include existente.

### 9. Padding inferior en RecyclerViews que coexisten con la pill nav
`android:paddingBottom="88dp"` y `android:clipToPadding="false"` para que la última fila no quede tapada por la pill nav (88dp = nav 56 + margin 15 + colchón).

### 10. Animaciones reutilizadas
**Cero archivos nuevos en `res/anim/`.** Las acciones del nav graph deben usar:
- Avance: `slide_in_right` / `slide_out_left`
- Retroceso: `slide_in_left` / `slide_out_right`
- Fade: `fade_in` / `fade_out` (para transiciones modales o splash)

## Checklist antes de cerrar un archivo

- [ ] `grep "#[0-9A-Fa-f]\{6\}" archivo.xml` → vacío
- [ ] No contiene `BottomNavigationView`
- [ ] MaterialCardView con `cornerRadius=12dp elevation=0dp strokeWidth=0dp`
- [ ] Empty state vía `<include layout="@layout/layout_empty_state"/>` si el layout tiene lista
- [ ] RecyclerView con `paddingBottom=88dp` si es pantalla raíz con pill nav
- [ ] Chips de rol/estado son `TextView` con `bg_chip_*`, no `<Chip>`

## Referencias en el repo
- `app/src/main/res/layout/activity_main_admin.xml` — pill nav completa.
- `app/src/main/res/layout/fragment_admin_grupos.xml` — header raíz + búsqueda + chips filtro.
- `app/src/main/res/layout/fragment_admin_grupo_detalle.xml` — header back + cards + botones.
- `app/src/main/res/layout/item_grupo_admin.xml` — item con chip de estado.
- `app/src/main/res/values/colors.xml` — paleta completa.
