---
name: tecconnect-android-java
description: Use when creating or editing Java Activity, Fragment, or RecyclerView Adapter files under app/src/main/java/com/conectatec/ in the TecConnect Android project. Enforces @AndroidEntryPoint, ViewBinding lifecycle, Spanish backend TODOs, NavOptions for tab navigation, and dummy-data adapter pattern with inner static model class.
---

# TecConnect — Android Java Conventions

Reglas Java para Activities, Fragments y Adapters en `com.conectatec.*`. Aplica al editar/crear cualquier `.java` bajo `app/src/main/java/com/conectatec/ui/`.

## Reglas innegociables

### 1. `@AndroidEntryPoint` siempre
Todas las Activities y Fragments del proyecto se anotan con `@dagger.hilt.android.AndroidEntryPoint`. Hilt ya está cableado a nivel `Application` (`TecConnectApp` con `@HiltAndroidApp`). Sin la anotación, la inyección rompe.

```java
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteGruposFragment extends Fragment { ... }
```

### 2. ViewBinding con liberación obligatoria

**Fragments:**
```java
private FragmentDocenteGruposBinding binding;

@Override
public View onCreateView(@NonNull LayoutInflater inflater,
                         @Nullable ViewGroup container,
                         @Nullable Bundle savedInstanceState) {
    binding = FragmentDocenteGruposBinding.inflate(inflater, container, false);
    return binding.getRoot();
}

@Override
public void onDestroyView() {
    super.onDestroyView();
    binding = null;   // ← OBLIGATORIO
}
```

**Activities:**
```java
private ActivityMainDocenteBinding binding;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainDocenteBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
}

@Override
protected void onDestroy() {
    super.onDestroy();
    binding = null;   // ← OBLIGATORIO
}
```

### 3. TODOs en español, formato fijo
Donde iría una llamada al backend, escribe:
```java
// TODO: llamar a [Servicio].[metodo]()
```
Ejemplos válidos:
- `// TODO: llamar a TareaService.crearTarea()`
- `// TODO: llamar a EntregaService.calificar() — luego NotificacionService.enviar(tipo=3)`
- `// TODO: llamar a SessionService.cerrarSesion()`

**Nunca** en inglés, **nunca** sin el verbo "llamar a".

### 4. Argumentos por Bundle (no SafeArgs)
El proyecto no usa `androidx.navigation.safeargs`. Lectura:
```java
int grupoId = getArguments() != null
        ? getArguments().getInt("grupoId", 0) : 0;
```
Escritura al navegar:
```java
Bundle args = new Bundle();
args.putInt("grupoId", g.id);
Navigation.findNavController(requireView())
        .navigate(R.id.action_grupos_to_detalle, args);
```

Los nombres de argumentos deben coincidir exactamente con el `<argument android:name="..."/>` del nav graph.

### 5. Navegación entre tabs raíz — `NavOptions` específicos
Si una card o link debe llevar a una pestaña raíz (no a un sub-destino), usa el helper que tiene `AdminDashboardFragment.navigateToTab()`:

```java
private void navigateToTab(View v, int destId) {
    NavController nav = Navigation.findNavController(v);
    NavOptions opts = new NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setRestoreState(true)
            .setPopUpTo(nav.getGraph().getStartDestinationId(), false, true)
            .build();
    nav.navigate(destId, null, opts);
}
```

`setLaunchSingleTop(true)` evita apilar copias del mismo destino. `setRestoreState(true)` mantiene el scroll/estado al volver.

### 6. Sub-destinos usan `Navigation.navigate(actionId, args)` directamente
Sin `NavOptions` (la animación viene del `<action>` del XML):
```java
Bundle args = new Bundle();
args.putInt("tareaId", tarea.id);
Navigation.findNavController(requireView())
        .navigate(R.id.action_entregas_to_calificar, args);
```

### 7. Patrón de Adapter con datos dummy

```java
public class GrupoDocenteAdapter
        extends RecyclerView.Adapter<GrupoDocenteAdapter.ViewHolder> {

    /** Modelo dummy del grupo (se reemplazará al integrar backend). */
    public static class GrupoDummyDocente {
        public final int id;
        public final String nombre;
        public final String materia;
        public final int totalAlumnos;
        public final String fechaCreacion;
        public final String codigoUnion;
        public final boolean activo;

        public GrupoDummyDocente(int id, String nombre, String materia,
                                 int totalAlumnos, String fechaCreacion,
                                 String codigoUnion, boolean activo) {
            this.id = id;
            this.nombre = nombre;
            // ...
        }
    }

    public interface OnGrupoClickListener {
        void onClick(GrupoDummyDocente grupo, int position);
    }

    private final List<GrupoDummyDocente> lista = new ArrayList<>();
    private final List<GrupoDummyDocente> listaCompleta = new ArrayList<>();
    private final OnGrupoClickListener listener;

    public GrupoDocenteAdapter(OnGrupoClickListener listener) {
        this.listener = listener;
        cargarDatosDummy();
    }

    private void cargarDatosDummy() {
        lista.clear();
        listaCompleta.clear();
        lista.add(new GrupoDummyDocente(1, "...", "...", 18, "01/02/2026", "TC-9X4P", true));
        // 2-6 entradas dummy
        listaCompleta.addAll(lista);
        notifyDataSetChanged();
    }

    // onCreateViewHolder, onBindViewHolder, getItemCount, ViewHolder estática
}
```

Reglas:
- Modelo es **clase interna estática** del adapter, sufijo `*DummyDocente`.
- Constructor del adapter recibe el listener y carga los datos dummy.
- Atributos del modelo son `public final` (inmutables, accesibles desde Fragment).
- ViewHolder estática que envuelve el ViewBinding del item.
- Nombre del listener: `OnXxxClickListener` con un solo método `onClick(model, position)`.

### 8. Imports y formato
- Usa imports explícitos, no `*`.
- Order: java, androidx, com.google, com.conectatec, dagger.
- Comentarios de cabecera de clase opcionales pero en español si existen.

### 9. Cerrar sesión y volver al login
Patrón estándar para el botón "Cerrar sesión":
```java
// TODO: llamar a SessionService.cerrarSesion()
Intent intent = new Intent(requireActivity(), LoginActivity.class);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
startActivity(intent);
requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
```

## Checklist antes de cerrar un archivo

- [ ] `@AndroidEntryPoint` presente (Activities y Fragments)
- [ ] `binding = null` en `onDestroyView()` (Fragments) o `onDestroy()` (Activities)
- [ ] TODOs de backend en español con formato `// TODO: llamar a X.y()`
- [ ] Sin uso de SafeArgs, args vía Bundle
- [ ] Tabs raíz usan `navigateToTab()` con `NavOptions`; sub-destinos usan `navigate(actionId, args)` directo
- [ ] Adapter declara `*DummyDocente` interna estática y carga datos en constructor

## Referencias en el repo

- `app/src/main/java/com/conectatec/ui/admin/MainAdminActivity.java` — pill nav + NavController.
- `app/src/main/java/com/conectatec/ui/admin/dashboard/AdminDashboardFragment.java` — `navigateToTab()` helper.
- `app/src/main/java/com/conectatec/ui/admin/grupos/AdminGruposFragment.java` — Fragment con búsqueda y filtros.
- `app/src/main/java/com/conectatec/ui/admin/grupos/adapter/GrupoAdminAdapter.java` — patrón completo de adapter con dummy.
- `app/src/main/java/com/conectatec/ui/admin/perfil/AdminPerfilFragment.java` — botón cerrar sesión.
