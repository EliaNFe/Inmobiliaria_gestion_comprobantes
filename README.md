📑 - Gestión Inmobiliaria Lily Cirigliano

Es una solución integral diseñada para optimizar la gestión de contratos, clientes y la emisión de comprobantes para la inmobiliaria Lily Cirigliano. El sistema ofrece interfaces diferenciadas para administradores y empleados, enfocándose en la automatización de procesos críticos como tasaciones y recibos de alquiler.



✨ Características Principales

🏛️ Módulo de Administración (Admin)

Dashboard Ejecutivo: Visualización en tiempo real de contratos por vencer y actualizaciones de monto pendientes.



Gestión de Clientes: Control total sobre la base de datos de Propietarios e Inquilinos.



Generador de Tasaciones: Creación de minutas de tasación profesionales exportables en formato PDF.



Control de Contratos: Alta, edición e inactivación de contratos de alquiler.



👥 Módulo de Operaciones (Empleado)

Interfaz Simplificada: Acceso rápido a las tareas diarias de cobranza.



Emisión de Recibos: Sistema interactivo para cargar consumos (Luz/Gas) y generar recibos de pago en PDF al instante.



Historial de Comprobantes: Consulta rápida de recibos emitidos anteriormente para su reimpresión.



🛠️ Tecnologías Utilizadas

Backend: Java 21 con Spring Boot 3.



Persistencia: Spring Data JPA con H2 Database.



Nota: Se utiliza H2 para garantizar una portabilidad total y un inicio rápido sin necesidad de instalar servidores externos.



Frontend: Thymeleaf con Tailwind CSS.



🚀 Instalación y Configuración

Clonar el repositorio:



Bash

git clone https://github.com/tu-usuario/inmodoc-gestion.git

Configurar la base de datos:

Modifica el archivo src/main/resources/application.properties con tus credenciales locales:



Properties

spring.datasource.url=jdbc:mysql://localhost:3306/inmodoc\_db

spring.datasource.username=root

spring.datasource.password=tu\_password

Ejecutar la aplicación:



Bash

mvn spring-boot:run



📂 Estructura del Proyecto

src/main/java: Contiene la lógica de negocio dividida en capas (Controller, Service, Model, Repository).



src/main/resources/templates: Vistas de Thymeleaf separadas por módulos.



src/main/resources/static: Recursos estáticos (Imágenes, Scripts, Estilos).



📄 Licencia

Este proyecto fue desarrollado de forma privada para Gestión Inmobiliaria Lily Cirigliano. Todos los derechos reservados.

