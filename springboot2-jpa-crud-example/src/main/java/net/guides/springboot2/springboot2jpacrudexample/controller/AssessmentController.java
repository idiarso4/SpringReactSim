package net.guides.springboot2.springboot2jpacrudexample.controller;

import net.guides.springboot2.springboot2jpacrudexample.model.Assessment;
import net.guides.springboot2.springboot2jpacrudexample.model.ClassRoom;
import net.guides.springboot2.springboot2jpacrudexample.model.Student;
import net.guides.springboot2.springboot2jpacrudexample.repository.AssessmentRepository;
import net.guides.springboot2.springboot2jpacrudexample.repository.ClassRoomRepository;
import net.guides.springboot2.springboot2jpacrudexample.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/assessments")
public class AssessmentController {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping
    public String listAssessments(Model model) {
        model.addAttribute("assessments", assessmentRepository.findAll());
        model.addAttribute("classrooms", classRoomRepository.findAll());
        return "assessment/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("assessment", new Assessment());
        model.addAttribute("classrooms", classRoomRepository.findAll());
        model.addAttribute("assessmentTypes", Assessment.AssessmentType.values());
        model.addAttribute("assessmentCategories", Assessment.AssessmentCategory.values());
        return "assessment/create";
    }

    @PostMapping("/create")
    public String createAssessment(@ModelAttribute Assessment assessment) {
        assessment.setScore(assessment.getScore() != null ? assessment.getScore() : 0);
        assessmentRepository.save(assessment);
        return "redirect:/assessments";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Assessment assessment = assessmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Assessment not found"));
        model.addAttribute("assessment", assessment);
        model.addAttribute("classrooms", classRoomRepository.findAll());
        model.addAttribute("assessmentTypes", Assessment.AssessmentType.values());
        model.addAttribute("assessmentCategories", Assessment.AssessmentCategory.values());
        return "assessment/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateAssessment(@PathVariable Long id, @ModelAttribute Assessment assessment) {
        assessment.setId(id);
        assessment.setScore(assessment.getScore() != null ? assessment.getScore() : 0);
        assessmentRepository.save(assessment);
        return "redirect:/assessments";
    }

    @GetMapping("/students/{classId}")
    @ResponseBody
    public List<Student> getStudentsByClass(@PathVariable Long classId) {
        return studentRepository.findByClassRoomId(classId);
    }

    @GetMapping("/filter")
    public String filterAssessments(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String subject,
            Model model) {
        
        List<Assessment> filteredAssessments;
        if (startDate != null && endDate != null) {
            filteredAssessments = assessmentRepository.findByDateBetween(startDate, endDate);
        } else if (subject != null && !subject.isEmpty()) {
            filteredAssessments = assessmentRepository.findBySubject(subject);
        } else {
            filteredAssessments = assessmentRepository.findAll();
        }
        
        model.addAttribute("assessments", filteredAssessments);
        model.addAttribute("classrooms", classRoomRepository.findAll());
        return "assessment/list";
    }

    @GetMapping("/export/{classId}")
    public ResponseEntity<String> exportAssessments(@PathVariable Long id) {
        // TODO: Implement Excel export functionality
        return ResponseEntity.ok("Export functionality will be implemented");
    }
}
