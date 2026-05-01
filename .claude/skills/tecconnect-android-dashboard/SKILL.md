# SKILL: tecconnect-android-dashboard

## Propósito
Guía de implementación para los dashboards de TecConnect (Admin y Docente).
Toda pantalla de dashboard DEBE seguir estas reglas antes de escribir código.

## Librería de gráficas
- Usar **MPAndroidChart** (com.github.PhilJay:MPAndroidChart:v3.1.0)
- Verificar si ya está en build.gradle antes de agregarla
- Si no está: agregar a settings.gradle el repositorio de JitPack y al build.gradle del módulo app la dependencia
- Tipos usados: PieChart (donut), HorizontalBarChart, LineChart

## Paleta de colores — dashboard
Definir en res/values/colors.xml (agregar si no existen, NO sobrescribir los existentes):
  db_background     #0D1117
  db_surface        #161B22
  db_border         #21262D
  db_blue           #58A6FF
  db_green          #3FB950
  db_purple         #D2A8FF
  db_orange         #F0883E
  db_red            #F78166
  db_text_primary   #E6EDF3
  db_text_secondary #C9D1D9
  db_text_muted     #7D8590
  db_dim            #484F58

## Estilos de cards (res/values/styles.xml)
  DashboardCard → background=@color/db_surface, cornerRadius=12dp, stroke=1dp @color/db_border
  KpiCard       → hereda DashboardCard, padding=12dp
  ChartCard     → hereda DashboardCard, padding=14dp

## Convenciones de layout
- Root: ScrollView > LinearLayout vertical, background @color/db_background, padding 12dp
- KPIs: GridLayout 2 columnas, gap 8dp
- Cada sección de gráfica: MaterialCardView con estilo ChartCard
- Section title: TextView 9sp, color @color/db_text_muted, allCaps=true, letterSpacing=0.08
- Valores KPI: TextView 26sp, fontWeight=medium
- Leyenda: fila horizontal con View 9dp×9dp (cornerRadius 3dp) + label + valor

## Regla de oro
NUNCA eliminar lógica existente. Solo se modifican o crean:
  - Archivos XML de layout
  - Código de binding/charts en el Fragment/Activity correspondiente
  - Resources (colors, styles, strings) — solo agregando, no sobreescribiendo
