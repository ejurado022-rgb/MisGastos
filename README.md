# 💰 Mis Gastos

Aplicación Android para registrar y controlar gastos personales. Permite agregar gastos por categoría, filtrarlos, ver el detalle de cada uno y consultar un resumen con estadísticas.

Es mi proyecto integrador del curso de desarrollo Android: reúne en una sola app los temas que trabajé en los talleres.

## Funcionalidades

- **Pantalla de bienvenida** con animación Lottie.
- **Lista de gastos** en un RecyclerView, del más reciente al más antiguo.
- **Encabezado animado** con MotionLayout: la tarjeta del total se contrae al deslizar la lista.
- **Filtro por categoría** con Spinner; el total se recalcula según el filtro.
- **Formulario para agregar gastos** con validación: descripción obligatoria, monto numérico mayor que cero y categoría seleccionada.
- **Detalle del gasto** que recibe los datos por Intent y permite eliminarlo.
- **Resumen** con total, promedio, gasto mayor, porcentaje del total y una barra por categoría.
- **Datos guardados** en el teléfono (SharedPreferences + JSON), así que no se pierden al cerrar la app.
- **Modo oscuro** y diseño edge-to-edge.

## Temas aplicados

| Tema | Dónde se usa |
|---|---|
| EditText, Button con `android:onClick`, Toast, `try/catch` | `AgregarGastoActivity`: botones **Guardar** y **Limpiar**, validación del monto |
| Spinner + `ArrayAdapter` con diseño propio | Filtro de la pantalla principal, categoría del formulario y resumen (`item_spinner.xml`) |
| Operaciones matemáticas | `CalculadoraGastos`: suma, promedio, mayor y porcentaje, con control de división entre cero |
| MotionLayout + MotionScene (`OnSwipe`) | Encabezado de `MainActivity` (`res/xml/activity_main_scene.xml`) |
| Animaciones Lottie (play / pause) | Splash, lista vacía (se pausa o reanuda al tocarla) y confirmación al guardar |
| Intents y envío de datos entre Activities | `putExtra` de `MainActivity` a `DetalleGastoActivity`; resultado de vuelta con `ActivityResultLauncher` |
| RecyclerView, Adapter y ViewHolder | `GastoAdapter` con clic en cada elemento |
| Pruebas unitarias (JUnit) | `CalculadoraGastosTest` |

## Estructura

```
app/src/main/java/com/ernestojurado/misgastos/
├── SplashActivity.java
├── MainActivity.java
├── AgregarGastoActivity.java
├── DetalleGastoActivity.java
├── ResumenActivity.java
├── model/Gasto.java
├── data/GastoRepository.java
├── ui/GastoAdapter.java
└── util/
    ├── CalculadoraGastos.java
    ├── Categorias.java
    └── Formato.java
```

## Tecnologías

- Java 11
- Vistas XML con Material Design 3
- Android Gradle Plugin 8.9.1, con catálogo de versiones (`libs.versions.toml`)
- Compatible desde Android 7.0 (API 24); compilado con API 35
- Librerías: AppCompat, Material Components, ConstraintLayout/MotionLayout, RecyclerView, Lottie

## Capturas

| Inicio | Nuevo gasto | Detalle | Resumen |
|---|---|---|---|
| _(captura)_ | _(captura)_ | _(captura)_ | _(captura)_ |

## Cómo ejecutarlo

1. Clona el repositorio:
   ```bash
   git clone https://github.com/<tu-usuario>/MisGastos.git
   ```
2. Ábrelo en Android Studio y espera a que termine la sincronización de Gradle.
3. Ejecuta la app en un emulador o en un dispositivo físico.

Para correr las pruebas unitarias:

```bash
./gradlew testDebugUnitTest
```

## Autor

**Ernesto Jurado**

## Licencia

Distribuido bajo la licencia MIT. Consulta el archivo [LICENSE](LICENSE).
