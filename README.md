# BookNest 
_Plataforma de Intercambio de Libros para Universitarios_ 

**Curso**: CS 2031 Desarrollo Basado en Plataformas
**Integrantes**:  
- Espinoza Cabrera, Camila Del Rosario
- Frisancho Gálvez, Gabriel Nicolás

---

## Índice

1. [Introducción](#introducción)  
2. [Identificación del Problema o Necesidad](#identificación-del-problema-o-necesidad)  
3. [Descripción de la Solución](#descripción-de-la-solución)  
4. [Modelo de Entidades](#modelo-de-entidades)  
5. [Testing y Manejo de Errores](#testing-y-manejo-de-errores)  
6. [Medidas de Seguridad Implementadas](#medidas-de-seguridad-implementadas)  
7. [Eventos y Asincronía](#eventos-y-asincronía)  
8. [GitHub](#github)  
9. [Conclusión](#conclusión)  
10. [Apéndices](#apéndices)  
11. [Referencias](#referencias)  

---

## Introducción

**Contexto**: BookNest nace como solución al problema específico de los estudiantes de UTEC que necesitan intercambiar libros de diversos géneros, relacionado principalmente al mercado de segunda mano y comercio electrónico. La plataforma facilita el trueque de libros entre compañeros, eliminando la necesidad de transacciones monetarias y fomentando la comunidad universitaria. Surge por la falta de plataformas especializadas en el intercambio de libros con funcionalidades que atiendan a las funcionalidades específicas del contexto.

**Objetivos del Proyecto**:
- Crear un marketplace de intercambio exclusivo para estudiantes UTEC  
- Limitar el uso de enpoints mediante autenticación de usuarios.
- Permitir la gestión de usuarios, libros y transacciones con roles diferenciados de user.
- Sistema de ofertas y aceptación/rechazo de intercambios
- Catálogo con búsqueda de tags como géneros, cursos y carreras
- Notificaciones internas para gestión de intercambios


---

## Identificación del Problema o Necesidad

1. **Descripción del Problema**  
   - Alto costo de libros universitarios nuevos
   - Dificultad para encontrar ediciones específicas requeridas en cursos  
   - Falta de plataforma segura para intercambios entre estudiantes UTEC  
   - Libros usados en buen estado que ya no son utilizados por sus dueños 

2. **Justificación**  
   - En ocasiones, los libros utilizados por estudiantes solo los utilizan por un ciclo
   - Ahorro potencial por cada estudiante
   - Fomento de la economía dentro de la universidad UTEC

---

## Descripción de la Solución

1. **Funcionalidades Implementadas**  
   - Feature 1: Autentificación de usuarios
   - Feature 2: Libre comercio de transacciones entre estudiantes
   - Feature 3: Mayor información brindada sobre el libro puesto a intercambio
2. **Tecnologías Utilizadas**  
   - Lenguajes: Java
   - Frameworks: Springboot con autentificación JWT
   - Bases de datos: PostgresSQL
   - Herramientas auxiliares: Docker, Postman

3. **Flujo Principal**:  
a. Usuario A (Vendedor) registra libro con:  
   - Título, autor, edición  
   - Estado (Nuevo/Seminuevo/Subrayado)  
   - Carrera y cursos relacionados  
   - Fotos del estado actual  
b. Usuario B (Comprador) busca libros y selecciona uno para ofertar  
c. Usuario B crea transacción ofreciendo uno de sus libros en intercambio  
d. Usuario A recibe notificación y puede:  
   - Aceptar (cierra otras ofertas automáticamente)  
   - Rechazar (permite nuevas ofertas)  
e. Sistema notifica a ambos usuarios para coordinar intercambio físico  

4. **Endpoints Críticos**:
   - Auth
     
   | Método | Endpoint | Descripción |
   |--------|----------|-------------|
   | POST   | /api/auth | Registrar usuario nuevo |
   | GET    | /api/register | Registar usuario |

   - Book
     
   | Método | Endpoint | Descripción |
   |--------|----------|-------------|
   | POST   | /api/books | Registrar libro |
   | GET    | /api/books?tag=accion | Buscar por tag |
   | POST   | /api/transactions | Crear oferta de intercambio |
   | PATCH  | /api/transactions/{id} | Aceptar/rechazar oferta |

---

## Modelo de Entidades

- **Diagrama**: inserta tu imagen E-R o diagrama de clases (con Markdown: `![E-R Diagram](ruta/diagrama.png)`).  

- **Relaciones Clave**:  

   - Un Usuario puede tener múltiples Libros registrados  
   - Cada Transacción conecta exactamente 2 Libros (ofrecido y solicitado)  
   - Los Libros pueden estar asociados a múltiples tags

- **Descripción de Entidades**: por cada entidad, indica atributos clave y relaciones.  

Atributos clave:
User:
id: Identificador único
email: Correo institucional (@utec.edu.pe)
role: ADMIN o USER
dateOfRegistration: Fecha de registro
OneToMany Transaction (como comprador)
OneToMany Transaction (como vendedor)

Book:
Transaction:
status: PENDING/ACCEPTED/REJECTED
creationDate: Fecha de creación
decisionDate: Fecha de aceptación/rechazo

Review:
---

## Testing y Manejo de Errores

1. **Niveles de Testing**  
   - Pruebas Unitarias
```java
// Ejemplo prueba validación DTO transacción
@Test
void whenInvalidTransaction_thenThrowsException() {
    TransactionRequestDTO dto = new TransactionRequestDTO();
    dto.setBookId(null);
    dto.setOfferedBookId(null);
    
    Set<ConstraintViolation<TransactionRequestDTO>> violations = validator.validate(dto);
    assertEquals(2, violations.size());
}

- Pruebas de integración:
@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {
    
    @Test
    @WithMockUser(roles = "USER")
    void createTransaction_ValidData_ReturnsCreated() throws Exception {
        mockMvc.perform(post("/api/transactions")
               .contentType(MediaType.APPLICATION_JSON)
               .content(validTransactionJson))
               .andExpect(status().isCreated());
    }
}

**Casos de Prueba Específicos**:  
- Validación de correo UTEC (@utec.edu.pe)  
- Restricción: Un usuario no puede ofertar sus propios libros  
- Flujo completo:  - Registro → Publicación → Oferta → Aceptación  


2. **Resultados**  
   - Resumen de las métricas (porcentaje de cobertura, tests pasados/fallidos).  

3. **Manejo de Errores**  
   - Excepciones especializadas:
 @ExceptionHandler(TransactionException.class)
public ResponseEntity<ErrorDTO> handleTransactionException(TransactionException ex) {
    ErrorDTO error = new ErrorDTO(
        Instant.now(),
        HttpStatus.BAD_REQUEST.value(),
        ex.getMessage()
    );
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
}

Flujo de validación:
Validación de DTOs con Bean Validation
Verificación de reglas de negocio en Service
Manejo centralizado con @ControllerAdvice


---

## Medidas de Seguridad Implementadas

1. **Seguridad de Datos**  
   - Para módulos de usuario:
@PreAuthorize("hasRole('ADMIN') or #id == principal.id")
@GetMapping("/users/{id}")
public UserResponseDTO getUser(@PathVariable Long id) {
    // ...
}
   - Para transacciones:
Validación de propiedad de libros

Cierre automático de ofertas duplicadas

Auditoría de cambios de estado


2. **Prevención de Vulnerabilidades**  
   - Protección contra inyección SQL, XSS, CSRF, configuración de CORS, políticas de cabeceras.  

---

## Eventos y Asincronía

- SignInEvent
- BookReminderEvent
- TransactionEventFlow
@Async
@TransactionalEventListener
public void handleTransactionAcceptance(TransactionAcceptedEvent event) {
    notificationService.send(
        event.getBuyerId(), 
        "¡Oferta aceptada! Coordina el intercambio"
    );
    transactionLogRepository.log(
        event.getTransactionId(), 
        "STATUS_CHANGE", 
        "ACCEPTED"
    );
}

Los eventos en BookNest funcionan como **disparadores de procesos secundarios** que:
1. **Notifican cambios de estado** (ej: cuando una transacción es aceptada)
2. **Sincronizan sistemas externos** (ej: actualización de inventario)
3. **Registran auditorías** (ej: logs de actividad)
4. **Gestionan side-effects** (ej: actualizar reputación de usuarios)


---

## GitHub

1. **Project Board / Issues**  
   - Cómo organizaron épicas, issues, asignación de tareas, milestones.  
2. **GitHub Actions**  
   - Flujo de CI/CD: build, test, deploy.  

---

## Conclusión

- **Logros del Proyecto**  
- **Aprendizajes Clave**  

**Impacto Esperado**:  
- Reducción del 40% en gastos de libros por estudiante  
- Creación de comunidad de intercambio académico  
- Base para futuras integraciones con biblioteca UTEC  

**Trabajo Futuro**:   
- Puntos de intercambio oficiales en campus  
- Sistema de reputación basado en intercambios exitosos  

---

## Apéndices

- **Licencia**: MIT, Apache 2.0, GPL…  
- **Documentación Adicional**: scripts, links a manuales, etc.  

---

## Referencias

- Lista de artículos, documentación oficial, bibliografía externa.


