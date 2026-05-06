package com.hireready;

import com.hireready.entities.*;
import com.hireready.enums.AuthorityRole;
import com.hireready.enums.SimulationStatus;
import com.hireready.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class HireReadyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HireReadyApplication.class, args);
    }

    @Bean
    public CommandLineRunner startConfiguration(
            AuthorityRepository authorityRepository,
            UserRepository userRepository,
            CareerRepository careerRepository,
            ApplicantRepository applicantRepository,
            CompanyRepository companyRepository,
            QuestionBankRepository questionBankRepository,
            QuestionRepository questionRepository
    ) {
        return args -> {
            // 1. Crear Autoridades (Roles)
            // Según tu Authority.java, usa el enum AuthorityRole
            Authority authApplicant = authorityRepository.save(new Authority(null, AuthorityRole.APPLICANT, null));
            Authority authCompany = authorityRepository.save(new Authority(null, AuthorityRole.COMPANY, null));
            Authority authAdmin = authorityRepository.save(new Authority(null, AuthorityRole.ADMIN, null));
            System.out.println("---- Autoridades creadas ----");

            // 2. Crear Usuarios (Se necesitan para Applicant y Company)
            User userAurora = new User(null, "aurora.portalanza@upc.edu.pe", "pass123", true, authApplicant, null, null);
            userAurora = userRepository.save(userAurora);

            User userTech = new User(null, "hr@techsolutions.com", "techpass", true, authCompany, null, null);
            userTech = userRepository.save(userTech);

            // 3. Crear Carreras
            Career sistemas = careerRepository.save(new Career(null, "Ingeniería de Sistemas", null, null));
            careerRepository.save(new Career(null, "Psicología", null, null));

            // 4. Crear el Postulante (Aurora Portalanza)
            Applicant aurora = new Applicant(
                    null,
                    "Aurora Portalanza",
                    LocalDate.of(2005, 5, 15),
                    "Universitario",
                    "UPC",
                    userAurora,
                    sistemas,
                    null
            );
            applicantRepository.save(aurora);

            // 5. Crear una Empresa
            Company techCorp = companyRepository.save(new Company(null, "TechCorp Solutions", "Líder en IA", userTech, null));

            // 6. Crear Banco de Preguntas
            QuestionBank bank = new QuestionBank();
            bank.setName("Prueba Técnica Java");
            bank.setDescription("Evaluación de algoritmos y Spring Boot");
            bank.setJobPosition("Junior Developer");
            bank.setLevel("Básico");
            bank.setCompany(techCorp);
            bank = questionBankRepository.save(bank);

            // 7. Crear Preguntas asociadas al banco
            // El campo es 'content' según tu entidad
            questionRepository.save(new Question(null, "¿Qué es la Inyección de Dependencias?", 1, bank, null));
            questionRepository.save(new Question(null, "¿Para qué sirve @Autowired?", 2, bank, null));

            // --- Mostrar datos en consola para verificar ---
            System.out.println("\n---- Datos de Prueba Cargados en BD ----");
            System.out.println("Postulante Guardado: " + aurora.getName() + " de la carrera " + aurora.getCareer().getName());

            List<Question> questionList = questionRepository.findAll();
            System.out.println("Lista de Preguntas Disponibles:");
            for(Question q : questionList) {
                System.out.println("- [" + q.getOrderIndex() + "] " + q.getContent());
            }
        };
    }
}