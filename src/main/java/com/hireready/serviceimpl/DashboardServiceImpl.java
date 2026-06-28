package com.hireready.serviceimpl;

import com.hireready.dtos.*;
import com.hireready.entities.*;
import com.hireready.enums.AuthorityRole;
import com.hireready.repositories.*;
import com.hireready.services.DashboardService;
import com.hireready.services.FillerWordService;
import com.hireready.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ApplicantRepository applicantRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    SimulationRepository simulationRepository;

    @Autowired
    FillerWordRepository fillerWordRepository;

    @Autowired
    SimulationReportRepository simulationReportRepository;

    @Autowired
    QuestionBankRepository questionBankRepository;

    @Autowired
    UserService userService;

    // US22
    @Override
    public ApplicantDashboardResponseDTO getApplicantMetrics(Long applicantUserId) {
        Applicant applicant = applicantRepository.findByUserId(applicantUserId);
        Long applicantId = applicant.getId();

        long total = simulationRepository.countByApplicant_Id(applicantId);
        long completed = simulationRepository.countByApplicant_IdAndStatus(
                applicantId, com.hireready.enums.SimulationStatus.COMPLETED);

        if (completed == 0) {
            return new ApplicantDashboardResponseDTO(
                    total, 0L, null, null, null, null, null,
                    new ArrayList<>(), new ArrayList<>(),
                    false,
                    "Aún no tienes simulaciones completadas. Completa una para ver tus métricas."
            );
        }

        List<SimulationReport> reports = simulationReportRepository
                .findBySimulation_Applicant_IdOrderBySimulation_CompletedAtAsc(applicantId);

        int sumOverall = 0, sumRel = 0, sumCla = 0, sumStr = 0, best = 0;
        List<ScoreTimePointDTO> overTime = new ArrayList<>();
        for (SimulationReport r : reports) {
            sumOverall += r.getOverallScore();
            sumRel += r.getAvgRelevance();
            sumCla += r.getAvgClarity();
            sumStr += r.getAvgStructure();
            if (r.getOverallScore() > best) best = r.getOverallScore();
            Simulation s = r.getSimulation();
            overTime.add(new ScoreTimePointDTO(
                    s.getCompletedAt(),
                    r.getOverallScore(),
                    s.getQuestionBank() != null ? s.getQuestionBank().getName() : null
            ));
        }
        int n = reports.size();

        // para el top muletillas
        List<FillerWord> fillers = fillerWordRepository.findBySimulation_Applicant_Id(applicantId);
        Map<String, Integer> agg = new HashMap<>();
        for (FillerWord fw : fillers) agg.merge(fw.getWord(), fw.getCount(), Integer::sum);

        List<FillerWordResponseDTO> topFillers = agg.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(5) // top 5
                .map(e -> new FillerWordResponseDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        return new ApplicantDashboardResponseDTO(
                total, completed,
                sumOverall / n, best,
                sumRel / n, sumCla / n, sumStr / n,
                overTime, topFillers,
                true, null
        );
    }

    // US23
    @Override
    public CompanyDashboardResponseDTO getCompanyMetrics(Long companyUserId) {
        Company company = companyRepository.findByUserId(companyUserId);
        Long companyId = company.getId();

        List<QuestionBank> banks = questionBankRepository.findByCompany_Id(companyId);
        List<Simulation> sims = simulationRepository.findByQuestionBank_Company_Id(companyId);

        long totalBanks = banks.size();
        long totalSims  = sims.size();

        if (totalSims == 0) {
            List<String> unused = new ArrayList<>();
            for (QuestionBank b : banks) unused.add(b.getName());
            return new CompanyDashboardResponseDTO(
                    totalBanks, 0L,
                    new ArrayList<>(), new ArrayList<>(), unused,
                    null, null, null, null,
                    new ArrayList<>(),
                    new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                    false,
                    "Aún no hay simulaciones asociadas a tus bancos. No hay datos disponibles."
            );
        }

        // sims X banco
        Map<String, Long> perBank = new LinkedHashMap<>();
        for (QuestionBank b : banks) perBank.put(b.getName(), 0L);
        for (Simulation s : sims) {
            if (s.getQuestionBank() != null) {
                perBank.merge(s.getQuestionBank().getName(), 1L, Long::sum);
            }
        }
        List<CountByLabelDTO> simsPerBank = perBank.entrySet().stream()
                .map(e -> new CountByLabelDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        List<CountByLabelDTO> topUsed = simsPerBank.stream()
                .filter(c -> c.getCount() > 0)
                .sorted((a, b) -> Long.compare(b.getCount(), a.getCount()))
                .limit(5)
                .collect(Collectors.toList());

        List<String> unused = simsPerBank.stream()
                .filter(c -> c.getCount() == 0)
                .map(CountByLabelDTO::getLabel)
                .collect(Collectors.toList());

        // mettricas globales (considera sims completadas)
        int sumOverall = 0, sumRel = 0, sumCla = 0, sumStr = 0, n = 0;
        for (Simulation s : sims) {
            if (s.getStatus() == com.hireready.enums.SimulationStatus.COMPLETED
                    && s.getSimulationReport() != null) {
                SimulationReport r = s.getSimulationReport();
                sumOverall += r.getOverallScore();
                sumRel     += r.getAvgRelevance();
                sumCla     += r.getAvgClarity();
                sumStr     += r.getAvgStructure();
                n++;
            }
        }
        Integer avgOverall = n == 0 ? null : sumOverall / n;
        Integer avgRel = n == 0 ? null : sumRel / n;
        Integer avgCla = n == 0 ? null : sumCla / n;
        Integer avgStr  = n == 0 ? null : sumStr / n;

        // preguntas menor score
        Map<Long, int[]> qStats = new HashMap<>();
        Map<Long, Question> qIndex = new HashMap<>();
        for (Simulation s : sims) {
            if (s.getResponses() == null) continue;
            for (Response resp : s.getResponses()) {
                ResponseAnalysis a = resp.getResponseAnalysis();
                if (a == null || resp.getQuestion() == null) continue;
                int score = (a.getRelevanceScore() + a.getClarityScore() + a.getStructureScore()) / 3;
                qStats.computeIfAbsent(resp.getQuestion().getId(), k -> new int[]{0, 0});
                qStats.get(resp.getQuestion().getId())[0] += score;
                qStats.get(resp.getQuestion().getId())[1] += 1;
                qIndex.put(resp.getQuestion().getId(), resp.getQuestion());
            }
        }
        List<QuestionScoreDTO> lowest = qStats.entrySet().stream()
                .map(e -> {
                    Question q = qIndex.get(e.getKey());
                    int avg = e.getValue()[0] / Math.max(1, e.getValue()[1]);
                    return new QuestionScoreDTO(
                            q.getId(),
                            q.getContent(),
                            q.getQuestionBank() != null ? q.getQuestionBank().getName() : null,
                            avg,
                            e.getValue()[1]);
                })
                .sorted(Comparator.comparingInt(QuestionScoreDTO::getAverageScore))
                .limit(5)
                .collect(Collectors.toList());

        // distribuciones de postulantes >> cada applicant cuenta una vez aunque tenga varias sims
        Set<Long> seenApplicants = new HashSet<>();
        Map<String, Long> byCareer = new HashMap<>();
        Map<String, Long> byLevel = new HashMap<>();
        Map<String, Long> byUni = new HashMap<>();
        for (Simulation s : sims) {
            Applicant a = s.getApplicant();
            if (a == null || !seenApplicants.add(a.getId())) continue;
            if (a.getCareer() != null && a.getCareer().getName() != null) {
                byCareer.merge(a.getCareer().getName(), 1L, Long::sum);
            }
            if (a.getLevelStudy() != null) byLevel.merge(a.getLevelStudy(), 1L, Long::sum);
            if (a.getUniversity() != null && !a.getUniversity().isBlank()) {
                byUni.merge(a.getUniversity(), 1L, Long::sum);
            }
        }
        List<CountByLabelDTO> byCareerList = mapToSortedList(byCareer);
        List<CountByLabelDTO> byLevelList = mapToSortedList(byLevel);
        List<CountByLabelDTO> topUnis = byUni.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(5)
                .map(e -> new CountByLabelDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        return new CompanyDashboardResponseDTO(
                totalBanks, totalSims,
                simsPerBank, topUsed, unused,
                avgOverall, avgRel, avgCla, avgStr,
                lowest,
                byCareerList, byLevelList, topUnis,
                true, null
        );
    }

    private List<CountByLabelDTO> mapToSortedList(Map<String, Long> m) {
        return m.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .map(e -> new CountByLabelDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    // US24
    @Override
    public DashboardResponseDTO getMetrics(Long adminUserId) {

        long totalUsers = userRepository.count();
        long totalApplicants = applicantRepository.count();
        long totalCompanies = companyRepository.count();
        long totalSimulations = simulationRepository.count();

        List<CountByLabelDTO> byCareer = applicantRepository.countByCareer();
        List<CountByLabelDTO> byLevelStudy = applicantRepository.countByLevelStudy();
        List<CountByLabelDTO> banksByCompany = questionBankRepository.countByCompany();

        // simulaciones por mes
        List<Simulation> sims = simulationRepository.findByStartedAtIsNotNull();
        Map<String, Long> bucket = new TreeMap<>();
        for (Simulation s : sims) {
            String period = YearMonth.from(s.getStartedAt()).toString(); // YYYY-MM
            bucket.merge(period, 1L, Long::sum);
        }
        List<TimeBucketDTO> simulationsOverTime = bucket.entrySet().stream()
                .map(e -> new TimeBucketDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        Double avgScore = simulationReportRepository.averageOverallScore();

        // cuando faltan datos, como es un estado del sistema no lanza excepcion
        boolean hasEnoughData = totalUsers > 0 && totalSimulations > 0;
        String message = hasEnoughData ? null : "Aún no hay datos suficientes para mostrar métricas significativas.";

        return new DashboardResponseDTO(
                totalUsers, totalApplicants, totalCompanies, totalSimulations,
                simulationsOverTime, byCareer, byLevelStudy, banksByCompany,
                avgScore, hasEnoughData, message
        );
    }
}
