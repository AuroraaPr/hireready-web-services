package com.hireready;

import com.hireready.entities.*;
import com.hireready.enums.AuthorityRole;
import com.hireready.enums.SimulationStatus;
import com.hireready.repositories.*;
import com.hireready.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableAsync
public class HireReadyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HireReadyApplication.class, args);
    }
    @Bean
    @Profile("dev")
    public CommandLineRunner seed(
            AuthorityRepository authorityRepository,
            CareerRepository careerRepository,
            ApplicantRepository applicantRepository,
            CompanyRepository companyRepository,
            QuestionBankRepository questionBankRepository,
            QuestionRepository questionRepository,
            QuestionBankCareerRepository questionBankCareerRepository,
            SimulationRepository simulationRepository,
            ResponseRepository responseRepository,
            ResponseAnalysisRepository responseAnalysisRepository,
            FillerWordRepository fillerWordRepository,
            SimulationReportRepository simulationReportRepository,
            UserService userService
    ) {
        return args -> {
            if (authorityRepository.count() > 0) {
                System.out.println("Seed omitido: ya existen datos en BD");
                return;
            }

            try {
                // =================== 1. AUTHORITIES ===================
                Authority adminAuth     = authorityRepository.save(new Authority(null, AuthorityRole.ADMIN, null));
                Authority companyAuth   = authorityRepository.save(new Authority(null, AuthorityRole.COMPANY, null));
                Authority applicantAuth = authorityRepository.save(new Authority(null, AuthorityRole.APPLICANT, null));
                System.out.println("   Authorities: ADMIN, COMPANY, APPLICANT");

                // =================== 2. CARRERAS ===================
                Career software  = careerRepository.save(new Career(null, "Ingeniería de Software", null, null));
                Career compSci   = careerRepository.save(new Career(null, "Ciencias de la Computación", null, null));
                Career sistemas  = careerRepository.save(new Career(null, "Ingeniería de Sistemas", null, null));
                Career industrial= careerRepository.save(new Career(null, "Ingeniería Industrial", null, null));
                Career admin     = careerRepository.save(new Career(null, "Administración de Empresas", null, null));
                System.out.println("   5 carreras creadas");

                // =================== 3. ADMIN ===================
                userService.add(new User(null, "admin@hireready.pe", "admin123", true, adminAuth, null, null));
                System.out.println("   Admin: admin@hireready.pe / admin123");

                // =================== 4. POSTULANTES ===================
                // Juan — postulante con simulaciones completas (para US-15, US-22 con datos ricos)
                User userJuan = userService.add(new User(null, "juan@upc.edu.pe", "juan123", true, applicantAuth, null, null));
                Applicant juan = applicantRepository.save(new Applicant(
                        null, "Juan Pablo Quispe",
                        LocalDate.of(2002, 5, 15),
                        "Pregrado (ciclos 7 al 10)", "UPC",
                        userJuan, software, null));

                // María — postulante con simulación IN_PROGRESS (para US-10, US-11)
                User userMaria = userService.add(new User(null, "maria@pucp.edu.pe", "maria123", true, applicantAuth, null, null));
                Applicant maria = applicantRepository.save(new Applicant(
                        null, "María García",
                        LocalDate.of(2000, 8, 20),
                        "Egresado", "PUCP",
                        userMaria, compSci, null));

                // Luis — postulante SIN simulaciones (para probar mensaje "sin datos" en US-22)
                User userLuis = userService.add(new User(null, "luis@upc.edu.pe", "luis123", true, applicantAuth, null, null));
                Applicant luis = applicantRepository.save(new Applicant(
                        null, "Luis Vargas",
                        LocalDate.of(2003, 3, 10),
                        "Bootcamp", null,  // sin universidad — para probar campo opcional
                        userLuis, sistemas, null));

                System.out.println("   3 postulantes: juan@upc.edu.pe, maria@pucp.edu.pe, luis@upc.edu.pe (pass: nombre+123)");

                // =================== 5. EMPRESAS ===================
                User userTech = userService.add(new User(null, "hr@techcorp.com", "techcorp123", true, companyAuth, null, null));
                Company techCorp = companyRepository.save(new Company(
                        null, "TechCorp Solutions",
                        "Líder en desarrollo de software empresarial y soluciones cloud en Latinoamérica.",
                        userTech, null));

                User userInnovate = userService.add(new User(null, "talent@innovatelab.com", "innovate123", true, companyAuth, null, null));
                Company innovate = companyRepository.save(new Company(
                        null, "InnovateLab",
                        "Startup de analítica de datos e inteligencia artificial aplicada.",
                        userInnovate, null));

                System.out.println("   2 empresas: hr@techcorp.com / techcorp123 — talent@innovatelab.com / innovate123");

                // =================== 6. BANCOS DE PREGUNTAS ===================

                // B1: TechCorp — Junior Java Developer
                QuestionBank b1 = questionBankRepository.save(new QuestionBank(
                        null, "Prueba Junior Java Developer",
                        "Evalúa conocimientos básicos de Java, Spring Boot y APIs REST.",
                        "Backend Developer", "Junior", techCorp, null, null, null));
                questionRepository.save(new Question(null, "¿Qué es la inyección de dependencias y por qué se usa?", 1, b1, null));
                questionRepository.save(new Question(null, "Explica la diferencia entre @Component, @Service y @Repository.", 2, b1, null));
                questionRepository.save(new Question(null, "¿Qué es REST y cuáles son sus principios?", 3, b1, null));
                questionRepository.save(new Question(null, "Describe el ciclo de vida de un objeto en una transacción JPA.", 4, b1, null));
                questionBankCareerRepository.save(new QuestionBankCareer(null, software, b1));
                questionBankCareerRepository.save(new QuestionBankCareer(null, compSci, b1));

                // B2: TechCorp — Semi Senior Backend
                QuestionBank b2 = questionBankRepository.save(new QuestionBank(
                        null, "Entrevista Semi Senior Backend",
                        "Evaluación técnica para desarrolladores con 3+ años de experiencia.",
                        "Senior Backend Engineer", "Semi Senior", techCorp, null, null, null));
                questionRepository.save(new Question(null, "Describe una arquitectura de microservicios que hayas diseñado.", 1, b2, null));
                questionRepository.save(new Question(null, "¿Cómo manejarías un cuello de botella en una API de alto tráfico?", 2, b2, null));
                questionRepository.save(new Question(null, "Explica la estrategia de caching que aplicarías en un e-commerce.", 3, b2, null));
                questionRepository.save(new Question(null, "¿Cómo aseguras la consistencia entre microservicios?", 4, b2, null));
                questionBankCareerRepository.save(new QuestionBankCareer(null, software, b2));

                // B3: TechCorp — Frontend Practitioner
                QuestionBank b3 = questionBankRepository.save(new QuestionBank(
                        null, "Práctica Frontend con React",
                        "Conocimientos básicos de HTML, CSS, JavaScript y React para practicantes.",
                        "Frontend Practitioner", "Practicante", techCorp, null, null, null));
                questionRepository.save(new Question(null, "¿Qué es el Virtual DOM y por qué React lo usa?", 1, b3, null));
                questionRepository.save(new Question(null, "Explica la diferencia entre useState y useEffect.", 2, b3, null));
                questionRepository.save(new Question(null, "¿Cuándo usarías Context API vs Redux?", 3, b3, null));
                questionBankCareerRepository.save(new QuestionBankCareer(null, software, b3));
                questionBankCareerRepository.save(new QuestionBankCareer(null, compSci, b3));

                // B4: InnovateLab — Data Analyst Junior
                QuestionBank b4 = questionBankRepository.save(new QuestionBank(
                        null, "Análisis de Datos Junior",
                        "Evaluación de fundamentos de SQL, estadística y herramientas de BI.",
                        "Data Analyst", "Junior", innovate, null, null, null));
                questionRepository.save(new Question(null, "Diferencia entre INNER JOIN y LEFT JOIN, con un ejemplo.", 1, b4, null));
                questionRepository.save(new Question(null, "¿Qué es una window function en SQL?", 2, b4, null));
                questionRepository.save(new Question(null, "Explica cuándo aplicarías un A/B test.", 3, b4, null));
                questionRepository.save(new Question(null, "¿Cómo limpiarías un dataset con valores nulos y outliers?", 4, b4, null));
                questionBankCareerRepository.save(new QuestionBankCareer(null, software, b4));
                questionBankCareerRepository.save(new QuestionBankCareer(null, compSci, b4));
                questionBankCareerRepository.save(new QuestionBankCareer(null, industrial, b4));

                System.out.println("   4 bancos creados (3 TechCorp + 1 InnovateLab) con preguntas y carreras vinculadas");

                // =================== 7. SIMULACIONES ===================
                LocalDateTime now = LocalDateTime.now();

                // ---- S1: Juan completó la simulación de B1 (Junior Java) hace 1 semana ----
                Simulation s1 = simulationRepository.save(new Simulation(
                        null,
                        now.minusDays(7).minusHours(2),
                        now.minusDays(7).minusHours(1).minusMinutes(30),
                        SimulationStatus.COMPLETED,
                        juan, b1, null, null, null));

                List<Question> b1Questions = questionRepository.findByQuestionBank_IdOrderByOrderIndexAsc(b1.getId());
                String[] s1Transcriptions = {
                        "Bueno, la inyección de dependencias es un patrón donde, este, el framework se encarga de proveer las dependencias que un objeto necesita en lugar de que el objeto las cree por sí mismo. Por ejemplo en Spring usamos @Autowired para que el contenedor inyecte la instancia automáticamente. Esto facilita las pruebas unitarias y reduce el acoplamiento.",
                        "Las tres anotaciones son tipos de @Component pero con semántica diferente. @Service se usa para clases de lógica de negocio, @Repository para clases de acceso a datos y @Component es el genérico. Spring las trata casi igual pero la separación ayuda a la legibilidad del proyecto.",
                        "REST es un estilo de arquitectura, este, para servicios web. Sus principios principales son: usar HTTP, ser stateless, identificar recursos con URIs, y usar verbos HTTP como GET, POST, PUT, DELETE. También está la idea de HATEOAS aunque no siempre se aplica.",
                        "En JPA el ciclo es: New cuando se crea con new, Managed cuando se persiste y JPA lo está rastreando, Detached cuando ya no está en el contexto de persistencia, y Removed cuando se marca para eliminar. La transacción confirma todos los cambios al hacer commit."
                };
                int[][] s1Scores = {{82, 70, 78}, {88, 85, 80}, {75, 65, 70}, {72, 60, 68}};
                for (int i = 0; i < b1Questions.size(); i++) {
                    Response r = responseRepository.save(new Response(
                            null, s1Transcriptions[i], 75 + i * 5, s1, b1Questions.get(i), null));
                    responseAnalysisRepository.save(new ResponseAnalysis(
                            null, s1Scores[i][0], s1Scores[i][1], s1Scores[i][2],
                            "Respuesta bien estructurada con buen uso de ejemplos concretos.", r));
                }
                fillerWordRepository.save(new FillerWord(null, "este", 4, s1));
                fillerWordRepository.save(new FillerWord(null, "bueno", 2, s1));
                simulationReportRepository.save(new SimulationReport(null, 79, 70, 74, 74, 138, s1));

                // ---- S2: Juan completó B4 (Data Analyst) hace 3 días — para que tenga 2 puntos en su scoreOverTime ----
                Simulation s2 = simulationRepository.save(new Simulation(
                        null,
                        now.minusDays(3).minusHours(1),
                        now.minusDays(3).minusMinutes(10),
                        SimulationStatus.COMPLETED,
                        juan, b4, null, null, null));

                List<Question> b4Questions = questionRepository.findByQuestionBank_IdOrderByOrderIndexAsc(b4.getId());
                String[] s2Transcriptions = {
                        "INNER JOIN devuelve solo las filas que tienen coincidencia en ambas tablas mientras que LEFT JOIN devuelve todas las filas de la tabla izquierda más las coincidencias de la derecha, con nulos donde no hay match. Por ejemplo si tengo usuarios y pedidos, LEFT JOIN me trae todos los usuarios aunque no tengan pedidos.",
                        "Las window functions permiten hacer cálculos sobre un conjunto de filas relacionadas sin agrupar. Ejemplos típicos son ROW_NUMBER, RANK, y SUM con OVER. Son muy útiles para calcular rankings o promedios móviles sin perder el detalle de cada fila.",
                        "Un A/B test sirve para comparar dos versiones de algo, este, típicamente dos variantes de una UI o de un feature. Lo aplicas cuando quieres tomar una decisión basada en datos y no en opinión. Hay que tener cuidado con el tamaño de muestra y el tiempo del experimento.",
                        "Para nulos primero analizo si son nulos por error o significan algo. Después decido entre eliminar las filas, imputar con la media o mediana, o usar un modelo más sofisticado. Para outliers visualizo con boxplots y decido caso por caso si los conservo o los recorto."
                };
                int[][] s2Scores = {{85, 78, 82}, {90, 88, 85}, {72, 60, 70}, {88, 82, 80}};
                for (int i = 0; i < b4Questions.size(); i++) {
                    Response r = responseRepository.save(new Response(
                            null, s2Transcriptions[i], 80 + i * 3, s2, b4Questions.get(i), null));
                    responseAnalysisRepository.save(new ResponseAnalysis(
                            null, s2Scores[i][0], s2Scores[i][1], s2Scores[i][2],
                            "Buen dominio del tema con ejemplos relevantes.", r));
                }
                fillerWordRepository.save(new FillerWord(null, "este", 2, s2));
                simulationReportRepository.save(new SimulationReport(null, 83, 77, 79, 79, 145, s2));

                // ---- S3: Juan abandonó B3 (Frontend) hace 5 días ----
                simulationRepository.save(new Simulation(
                        null,
                        now.minusDays(5).minusHours(3),
                        now.minusDays(5).minusHours(2).minusMinutes(45),
                        SimulationStatus.ABANDONED,
                        juan, b3, null, null, null));

                // ---- S4: María tiene una simulación IN_PROGRESS en B2 (Semi Senior Backend), respondió 2 de 4 ----
                Simulation s4 = simulationRepository.save(new Simulation(
                        null, now.minusHours(1), null,
                        SimulationStatus.IN_PROGRESS,
                        maria, b2, null, null, null));
                List<Question> b2Questions = questionRepository.findByQuestionBank_IdOrderByOrderIndexAsc(b2.getId());
                responseRepository.save(new Response(
                        null, "Diseñé una arquitectura de microservicios para una plataforma de e-commerce. Separamos los dominios en servicios de catálogo, carrito, órdenes y pagos. Usamos un API Gateway para enrutar peticiones y RabbitMQ para comunicación asíncrona entre servicios.",
                        95, s4, b2Questions.get(0), null));
                responseRepository.save(new Response(
                        null, "Para un cuello de botella primero hago profiling con herramientas como JProfiler o New Relic. Después identifico si es la base de datos, la CPU o la red. Generalmente la solución pasa por agregar índices, cachear con Redis, o escalar horizontalmente con réplicas.",
                        88, s4, b2Questions.get(1), null));

                System.out.println("   Simulaciones: 2 COMPLETED de Juan, 1 ABANDONED de Juan, 1 IN_PROGRESS de María (Luis sin simulaciones)");
                System.out.println("=================================================================");
                System.out.println("   Seed completo. Credenciales para Postman:");
                System.out.println("   ADMIN     → admin@hireready.pe / admin123");
                System.out.println("   APPLICANT → juan@upc.edu.pe / juan123       (con datos ricos)");
                System.out.println("   APPLICANT → maria@pucp.edu.pe / maria123    (con simulación activa)");
                System.out.println("   APPLICANT → luis@upc.edu.pe / luis123       (sin simulaciones)");
                System.out.println("   COMPANY   → hr@techcorp.com / techcorp123   (3 bancos)");
                System.out.println("   COMPANY   → talent@innovatelab.com / innovate123 (1 banco)");
                System.out.println("=================================================================");

            } catch (Exception e) {
                System.err.println("ERROR EN SEED:");
                e.printStackTrace();
            }
        };
    }
}