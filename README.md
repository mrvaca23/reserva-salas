# Sistema de Reserva de Salas de Reuniones - Centro Empresarial

## Descripción del Proyecto
Este repositorio contiene el desarrollo del sistema backend para la gestión de reservas de salas de reuniones corporativas. El núcleo de la aplicación se ha construido aplicando el paradigma de Orientación a Objetos e implementando estructuras de datos dinámicas desde cero (sin depender de las colecciones nativas de `java.util`), garantizando un manejo eficiente y a medida de la memoria.

## Arquitectura y Estructuras de Datos
El proyecto está desarrollado en **Java (JDK 17+)** y emplea las siguientes estructuras algorítmicas personalizadas:

* **Árbol Binario de Búsqueda** (`estructuras/ArbolBinarioSalas.java`): Indexa el catálogo de salas basándose en su capacidad para agilizar de forma logarítmica la búsqueda de disponibilidad por aforo y hora.
* **Lista Enlazada Genérica** (`estructuras/ListaEnlazada.java`): Administra dinámicamente el registro de asistentes, el equipamiento tecnológico requerido y el historial de cada reserva.
* **Cola Estándar FIFO** (`estructuras/Cola.java`): Gestiona de manera estricta el orden de llegada de las solicitudes de reserva que se encuentran pendientes de aprobación.

## Características Principales
El sistema opera de forma ágil interactuando exclusivamente en memoria principal (sin persistencia en disco o bases de datos) y cubre el siguiente flujo operativo:
1.  **Consultar disponibilidad:** Búsqueda optimizada dentro del árbol de salas.
2.  **Realizar reserva:** Creación y encolamiento de nuevas solicitudes.
3.  **Aprobar reserva:** Procesamiento, desencolado y asignación final de la sala.
4.  **Generar reporte:** Emisión de estadísticas de uso y nivel de ocupación.

---

## Instrucciones de Despliegue y Ejecución

El proyecto está preparado para ser compilado tanto con herramientas de automatización como de forma manual mediante el kit de desarrollo estándar.

### Despliegue del Entorno de Consola (CLI)

**Opción A: Construcción automatizada con Maven**
```bash
mvn clean package
java -jar target/reservasalas.jar
```
**Opción B: Construcción automatizada con Maven**
```bash
find src -name "*.java" > sources.txt
javac -d out -encoding UTF-8 @sources.txt
java -cp out com.centroempresarial.reservasalas.app.Main
```

## Módulo Adicional: Interfaz Gráfica (GUI)

Como valor añadido, el sistema incluye una interfaz gráfica de escritorio desarrollada bajo el framework Java Swing (app/GuiApp.java). Esta capa de presentación cuenta con navegación por pestañas y está estrictamente desacoplada; reutiliza el 100% de la lógica de negocio (GestorReservas) y las estructuras de datos de la versión de consola.

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
