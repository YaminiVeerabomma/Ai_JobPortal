package com.example.service;

import com.example.DTO.JobPostDTO;
import com.example.Enum.JobType;
import com.example.Enum.RequiredExperience;
import com.example.entity.JobPost;
import com.example.util.JobScore;
import com.example.entity.Student;
import com.example.exception.JobPostNotFoundException;
import com.example.repository.JobPostRepository;
import com.example.repository.StudentRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobPostService {

    private static final Logger log = LoggerFactory.getLogger(JobPostService.class);

    @Autowired
    private JobPostRepository jobPostRepository;
    
    @Autowired
    private StudentRepository studentRepository;



    // ---------------- POST JOB ----------------
    public JobPostDTO postJob(JobPostDTO dto) {
        log.info("📌 postJob called for jobTitle='{}', postedBy='{}'", dto.getJobTitle(), dto.getPostedByEmail());

        if (dto.getJobTitle() == null || dto.getJobTitle().isBlank()) {
            log.error("❌ Job title cannot be null or empty");
            throw new IllegalArgumentException("Job title cannot be null or empty");
        }

        JobPost jobPost = new JobPost();
        jobPost.setJobTitle(dto.getJobTitle());
        jobPost.setJobType(dto.getJobType());
        jobPost.setJobLocation(dto.getJobLocation());
        jobPost.setJobDescription(dto.getJobDescription());
        jobPost.setCompanyName(dto.getCompanyName());
        jobPost.setPostedByEmail(dto.getPostedByEmail());
        jobPost.setPostedDate(dto.getPostedDate());
        jobPost.setSalaryMin(dto.getSalaryMin());
        jobPost.setSalaryMax(dto.getSalaryMax());
        jobPost.setEducation(dto.getEducation());
        jobPost.setSkills(dto.getSkills());
        jobPost.setRequiredExperience(dto.getRequiredExperience());
        jobPost.setActive(dto.isActive());
        jobPost.setNumberOfVacancies(dto.getNumberOfVacancies());

        JobPost saved = jobPostRepository.save(jobPost);
        log.info("✅ Job posted successfully with id={} and title='{}'", saved.getId(), saved.getJobTitle());

        return mapToDTO(saved);
    }

    // ---------------- UPDATE JOB ----------------
    public JobPostDTO updateJobPost(Long id, JobPostDTO dto) {
        log.info("✏️ updateJobPost called for id={}", id);

        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("❌ Job post not found with id={}", id);
                    return new JobPostNotFoundException("Job post not found with id: " + id);
                });

        jobPost.setJobTitle(dto.getJobTitle());
        jobPost.setJobType(dto.getJobType());
        jobPost.setJobLocation(dto.getJobLocation());
        jobPost.setJobDescription(dto.getJobDescription());
        jobPost.setCompanyName(dto.getCompanyName());
        jobPost.setPostedByEmail(dto.getPostedByEmail());
        jobPost.setPostedDate(dto.getPostedDate());
        jobPost.setSalaryMin(dto.getSalaryMin());
        jobPost.setSalaryMax(dto.getSalaryMax());
        jobPost.setEducation(dto.getEducation());
        jobPost.setSkills(dto.getSkills());
        jobPost.setRequiredExperience(dto.getRequiredExperience());
        jobPost.setActive(dto.isActive());
        jobPost.setNumberOfVacancies(dto.getNumberOfVacancies());

        JobPost updated = jobPostRepository.save(jobPost);
        log.info("✅ Job updated successfully for id={} with title='{}'", updated.getId(), updated.getJobTitle());

        return mapToDTO(updated);
    }

    // ---------------- DELETE JOB ----------------
    public void deleteJob(Long id) {
        log.info("🗑️ deleteJob called for id={}", id);

        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("❌ Job post not found with id={}", id);
                    return new JobPostNotFoundException("Job post not found with id: " + id);
                });

        jobPostRepository.delete(jobPost);
        log.info("✅ Job deleted successfully with id={}", id);
    }

    // ---------------- GET JOBS BY EMAIL ----------------
    public List<JobPostDTO> getByPostedByEmail(String email) {
        log.info("📬 getByPostedByEmail called for email={}", email);

        List<JobPostDTO> jobs = jobPostRepository.findByPostedByEmail(email)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        if (jobs.isEmpty()) {
            log.warn("⚠️ No job posts found for email={}", email);
            throw new JobPostNotFoundException("No job posts found for email: " + email);
        }

        log.info("✅ {} job(s) found for email={}", jobs.size(), email);
        return jobs;
    }

    // ---------------- GET JOBS BY TITLE ----------------
    public List<JobPostDTO> getByJobTitle(String jobTitle) {
        log.info("🔍 getByJobTitle called for jobTitle='{}'", jobTitle);

        List<JobPostDTO> jobs = jobPostRepository.findByJobTitle(jobTitle)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        if (jobs.isEmpty()) {
            log.warn("⚠️ No job posts found with title='{}'", jobTitle);
            throw new JobPostNotFoundException("No job posts found with title: " + jobTitle);
        }

        log.info("✅ {} job(s) found with title='{}'", jobs.size(), jobTitle);
        return jobs;
    }

    // ---------------- GET JOBS BY TYPE ----------------
    public List<JobPostDTO> getByJobType(JobType jobType) {
        log.info("🔎 getByJobType called for jobType={}", jobType);

        List<JobPostDTO> jobs = jobPostRepository.findByJobType(jobType)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        if (jobs.isEmpty()) {
            log.warn("⚠️ No job posts found with type={}", jobType);
            throw new JobPostNotFoundException("No job posts found with type: " + jobType);
        }

        log.info("✅ {} job(s) found with type={}", jobs.size(), jobType);
        return jobs;
    }

    // ---------------- GET JOBS BY COMPANY ----------------
    public List<JobPostDTO> getByCompanyName(String companyName) {
        log.info("🏢 getByCompanyName called for company='{}'", companyName);

        List<JobPostDTO> jobs = jobPostRepository.findByCompanyName(companyName)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        if (jobs.isEmpty()) {
            log.warn("⚠️ No job posts found for company='{}'", companyName);
            throw new JobPostNotFoundException("No job posts found for company: " + companyName);
        }

        log.info("✅ {} job(s) found for company='{}'", jobs.size(), companyName);
        return jobs;
    }

    // ---------------- GET ACTIVE JOBS ----------------
    public List<JobPostDTO> getActiveJobs() {
        log.info("⚡ getActiveJobs called");

        List<JobPostDTO> jobs = jobPostRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        if (jobs.isEmpty()) {
            log.warn("⚠️ No active job posts available");
            throw new JobPostNotFoundException("No active job posts available");
        }

        log.info("✅ {} active job(s) found", jobs.size());
        return jobs;
    }

    // ---------------- GET JOBS BY LOCATION ----------------
    public List<JobPostDTO> getByJobLocation(String jobLocation) {
        log.info("📍 getByJobLocation called for location='{}'", jobLocation);

        List<JobPostDTO> jobs = jobPostRepository.findByJobLocation(jobLocation)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        if (jobs.isEmpty()) {
            log.warn("⚠️ No job posts found in location='{}'", jobLocation);
            throw new JobPostNotFoundException("No job posts found in location: " + jobLocation);
        }

        log.info("✅ {} job(s) found in location='{}'", jobs.size(), jobLocation);
        return jobs;
    }

    // ---------------- GET JOBS BY EXPERIENCE ----------------
    public List<JobPostDTO> getByRequiredExperience(RequiredExperience requiredExperience) {
        log.info("🧰 getByRequiredExperience called for experience={}", requiredExperience);

        List<JobPostDTO> jobs = jobPostRepository.findByRequiredExperience(requiredExperience)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        if (jobs.isEmpty()) {
            log.warn("⚠️ No job posts found with required experience={}", requiredExperience);
            throw new JobPostNotFoundException("No job posts found with required experience: " + requiredExperience);
        }

        log.info("✅ {} job(s) found with required experience={}", jobs.size(), requiredExperience);
        return jobs;
    }
    public List<JobPostDTO> getTopMatchingJobs(Long userId) {

        // 1️⃣ Get student
        Student student = studentRepository.findById(userId)
                .orElseThrow(new java.util.function.Supplier<RuntimeException>() {
                    @Override
                    public RuntimeException get() {
                        return new RuntimeException("Student not found");
                    }
                });

        // 2️⃣ Clean student skills
        Set<String> studentSkills = new java.util.HashSet<String>();
        for (String skill : student.getSkills()) {
            if (skill != null) {
                studentSkills.add(skill.trim().toLowerCase());
            }
        }

        // 3️⃣ Get all jobs
        List<JobPost> jobs = jobPostRepository.findAll();

        List<JobScore> scoredJobs = new ArrayList<JobScore>();

        // 4️⃣ Compare jobs
        for (JobPost job : jobs) {

            String jobSkillsStr = job.getSkills();

            if (jobSkillsStr == null || jobSkillsStr.trim().isEmpty()) {
                continue;
            }

            // Convert job skills to set
            Set<String> jobSkills = new java.util.HashSet<String>();
            String[] skillsArray = jobSkillsStr.split(",");

            for (int i = 0; i < skillsArray.length; i++) {
                jobSkills.add(skillsArray[i].trim().toLowerCase());
            }

            int matchCount = 0;

            // Match skills
            for (String skill : studentSkills) {
                if (jobSkills.contains(skill)) {
                    matchCount++;
                }
            }

            // Debug log
            System.out.println("Job: " + job.getJobTitle() + " | Score: " + matchCount);

            if (matchCount > 0) {
                scoredJobs.add(new JobScore(job, matchCount));
            }
        }

        // 5️⃣ Sort manually (Java 7 style)
        java.util.Collections.sort(scoredJobs, new java.util.Comparator<JobScore>() {
            @Override
            public int compare(JobScore a, JobScore b) {
                return b.getScore() - a.getScore(); // descending
            }
        });

        // 6️⃣ Convert to DTO (top 5)
        List<JobPostDTO> result = new ArrayList<JobPostDTO>();

        int limit = Math.min(5, scoredJobs.size());

        for (int i = 0; i < limit; i++) {
            result.add(mapToDTO(scoredJobs.get(i).getJob()));
        }

        return result;
    }
    // ---------------- MAP ENTITY TO DTO ----------------
    private JobPostDTO mapToDTO(JobPost job) {
        log.debug("🔧 mapToDTO called for jobId={}", job.getId());

        return new JobPostDTO(
                job.getId(),
                job.getJobTitle(),
                job.getJobType(),
                job.getJobLocation(),
                job.getJobDescription(),
                job.getCompanyName(),
                job.getPostedByEmail(),
                job.getPostedDate(),
                job.getRequiredExperience(),
                job.getSalaryMin(),
                job.getSalaryMax(),
                job.getEducation(),
                job.getSkills(),
                job.isActive(),
                job.getNumberOfVacancies()
        );
    }
}
