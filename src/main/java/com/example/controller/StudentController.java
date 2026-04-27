package com.example.controller;

import com.example.DTO.JobPostDTO;
import com.example.DTO.StudentDTO;
import com.example.Enum.ExperienceLevel;
import com.example.Enum.Gender;
import com.example.Enum.NoticePeriod;
import com.example.Enum.PreferredJobLocations;
import com.example.service.JobPostService;
import com.example.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Student API", description = "CRUD and filters for Students")
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private JobPostService jobPostService;

    // ----------------- Update -----------------
    @Operation(summary = "Update student", description = "Update existing student details")
    @ApiResponse(responseCode = "200", description = "Student updated successfully")
    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @RequestBody StudentDTO studentDTO) {
        StudentDTO updated = studentService.updateStudent(id, studentDTO);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // ----------------- Delete -----------------
    @Operation(summary = "Delete student", description = "Delete student by ID")
    @ApiResponse(responseCode = "204", description = "Student deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    // ----------------- Get By ID -----------------
    @Operation(summary = "Get student by ID", description = "Retrieve student by ID")
    @ApiResponse(responseCode = "200", description = "Student found")
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
    	 return ResponseEntity.ok(studentService.getStudentById(id));
    }
 

    // ----------------- Get All -----------------
    @Operation(summary = "Get all students", description = "Retrieve all students")
    @ApiResponse(responseCode = "200", description = "List of students returned")
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    // ----------------- Filters -----------------
    @Operation(summary = "Get students by gender")
    @GetMapping("/gender/{gender}")
    public ResponseEntity<List<StudentDTO>> getStudentsByGender(@PathVariable Gender gender) {
        return ResponseEntity.ok(studentService.getStudentsByGender(gender));
    }

    @Operation(summary = "Get students by experience level")
    @GetMapping("/experience/{experienceLevel}")
    public ResponseEntity<List<StudentDTO>> getStudentsByExperienceLevel(@PathVariable ExperienceLevel experienceLevel) {
        return ResponseEntity.ok(studentService.getStudentsByExperienceLevel(experienceLevel));
    }

    @Operation(summary = "Get students by graduation year")
    @GetMapping("/graduation/{year}")
    public ResponseEntity<List<StudentDTO>> getStudentsByGraduationYear(@PathVariable Integer year) {
        return ResponseEntity.ok(studentService.getStudentsByGraduationYear(year));
    }

    @Operation(summary = "Get students by skill (keyword search)")
    @GetMapping("/skill/{skill}")
    public ResponseEntity<List<StudentDTO>> getStudentsBySkill(@PathVariable String skill) {
        return ResponseEntity.ok(studentService.getStudentsBySkill(skill));
    }

    @Operation(summary = "Get students by notice period")
    @GetMapping("/notice/{noticePeriod}")
    public ResponseEntity<List<StudentDTO>> getStudentsByNoticePeriod(@PathVariable NoticePeriod noticePeriod) {
        return ResponseEntity.ok(studentService.getStudentsByNoticePeriod(noticePeriod));
    }

    @Operation(summary = "Get students by preferred job location")
    @GetMapping("/location/{location}")
    public ResponseEntity<List<StudentDTO>> getStudentsByPreferredLocation(@PathVariable PreferredJobLocations location) {
        return ResponseEntity.ok(studentService.getStudentsByPreferredLocation(location));
    }
    @GetMapping("/{userId}/ai-jobs")
    public List<JobPostDTO> getAiJobs(@PathVariable Long userId) {
        return jobPostService.getTopMatchingJobs(userId);
    }
}