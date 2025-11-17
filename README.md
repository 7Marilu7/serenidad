Serenidad – Aplicación de salud mental.

1. Descripción.

Serenidad es una aplicación móvil enfocada en el bienestar emocional. Permite al usuario registrar su estado de ánimo, practicar ejercicios de respiración, acceder a meditaciones guiadas y consultar un historial de progreso. También integra contenido educativo sobre el manejo del estrés.

2. Metodología.

El desarrollo de esta aplicación se llevó a cabo utilizando la metodología ágil Scrum, trabajando por sprints y manteniendo entregables funcionales al final de cada iteración.

3. Roles.

Product Owner: Luis Enrique Rivera Delgado – Prioriza el backlog y define los requisitos.

Scrum Master: César David González Sánchez – Facilita las ceremonias y elimina impedimentos.

Equipo de Desarrollo: Bryans Yahir Martínez Espinoza, Cruz Paz María Guadalupe – Implementa las funcionalidades del sistema.

4. Sprints.

Sprint 1 – MVP/ Módulos base.

Registro y autenticación (Firebase Auth), seguimiento de estado de ánimo, ejercicios de respiración guiadas y estructura inicial de la aplicación.

Sprint 2 – Historial, recordatorios y experiencia de usuario.

Historial del estado de ánimo (Firestore), recordatorios y notificaciones y expansión del contenido de ejercicios.

Sprint 3 –  Recursos, administración y preparación para producción 

Biblioteca de recursos educativos, panel de administración básico para gestionar contenido y documentación técnica y preparación para despliegue.

5. Tecnologías y herramientas.
   
Lenguaje: Kotlin + Jetpack Compose

IDE: Android Studio

Arquitectura: MVVM

Backend / Nube: Firebase (Auth, Firestore, Storage)

Control de versiones: Git + GitHub

Gestión de proyecto: Jira

Comunicación: Slack

Instalación y ejecución

6. Arquitectura.

El proyecto utiliza el patrón MVVM, separando la lógica de negocio, la capa de datos y la interfaz gráfica.

Incluye:

ViewModels para manejar estados.

Repositorios para comunicación con Firebase.

UI desarrollada con componentes de Compose.

7. APIs y servicios usados.

Firebase Authentication: registro e inicio de sesión.

Cloud Firestore: almacenamiento de estados de ánimo, recursos y perfiles.

Firebase Storage: manejo de archivos y contenido multimedia.

8. Licencia

Proyecto académico desarrollado como parte del módulo de desarrollo móvil. Incluye código original diseñado por el equipo.
