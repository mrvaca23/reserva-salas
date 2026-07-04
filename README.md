# Sistema de Reserva de Salas de Reuniones — Centro Empresarial

Proyecto final de Estructuras de Datos Dinámicas (Java, JDK 17+).

## Estructuras de datos implementadas (desde cero, sin java.util.LinkedList/Queue)
- **Árbol Binario de Búsqueda** (`estructuras/ArbolBinarioSalas.java`): indexa las salas por capacidad para buscar disponibilidad por aforo y hora.
- **Lista Enlazada genérica** (`estructuras/ListaEnlazada.java`): almacena asistentes, equipos requeridos y el historial de reservas.
- **Cola (FIFO) genérica** (`estructuras/Cola.java`): gestiona las solicitudes de reserva pendientes de aprobación.

## Cómo compilar y ejecutar

### Con Maven
```
mvn clean package
java -jar target/reservasalas.jar
```

### Sin Maven (javac directo)
```
find src -name "*.java" > sources.txt
javac -d out -encoding UTF-8 @sources.txt
java -cp out com.centroempresarial.reservasalas.app.Main
```

## Funcionalidades
1. Consultar disponibilidad (búsqueda en árbol de salas y horarios)
2. Realizar reserva (encola solicitud)
3. Aprobar reserva (desencola y asigna sala)
4. Generar reporte de uso (estadísticas de ocupación)

Todos los datos se mantienen en memoria (no se usan bases de datos ni archivos en disco).

## Interfaz Gráfica (Java Swing) — Puntos Extra

Se agregó `app/GuiApp.java`, una interfaz gráfica de escritorio (Swing) que
usa exactamente la misma lógica de negocio y las mismas estructuras de
datos (`GestorReservas`, árbol, lista enlazada y cola) que la versión de
consola. Tiene 4 pestañas, una por cada funcionalidad.

### Ejecutar la GUI

Con Maven:
```
mvn clean package
java -cp target/reservasalas.jar com.centroempresarial.reservasalas.app.GuiApp
```

Sin Maven:
```
find src -name "*.java" > sources.txt
javac -d out -encoding UTF-8 @sources.txt
java -cp out com.centroempresarial.reservasalas.app.GuiApp
```

### Ejecutar la versión de consola (sigue disponible)
```
java -cp out com.centroempresarial.reservasalas.app.Main
```
