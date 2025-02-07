package net.guides.springboot2.springboot2jpacrudexample.controller;

import jakarta.validation.Valid;
import net.guides.springboot2.springboot2jpacrudexample.exception.ResourceNotFoundException;
import net.guides.springboot2.springboot2jpacrudexample.model.Counseling;
import net.guides.springboot2.springboot2jpacrudexample.model.User;
import net.guides.springboot2.springboot2jpacrudexample.repository.CounselingRepository;
import net.guides.springboot2.springboot2jpacrudexample.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class CounselingController {
    @Autowired
    private CounselingRepository counselingRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/counseling")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BK')")
    public List<Counseling> getAllCounselingSessions() {
        return counselingRepository.findAll();
    }

    @GetMapping("/counseling/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BK')")
    public ResponseEntity<Counseling> getCounselingById(@PathVariable(value = "id") Long counselingId)
            throws ResourceNotFoundException {
        Counseling counseling = counselingRepository.findById(counselingId)
                .orElseThrow(() -> new ResourceNotFoundException("Counseling session not found for this id :: " + counselingId));
        return ResponseEntity.ok().body(counseling);
    }

    @GetMapping("/counseling/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BK')")
    public List<Counseling> getCounselingByStudent(@PathVariable(value = "studentId") Long studentId)
            throws ResourceNotFoundException {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for this id :: " + studentId));
        return counselingRepository.findByStudent(student);
    }

    @GetMapping("/counseling/counselor/{counselorId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BK')")
    public List<Counseling> getCounselingByCounselor(@PathVariable(value = "counselorId") Long counselorId)
            throws ResourceNotFoundException {
        User counselor = userRepository.findById(counselorId)
                .orElseThrow(() -> new ResourceNotFoundException("Counselor not found for this id :: " + counselorId));
        return counselingRepository.findByCounselor(counselor);
    }

    @GetMapping("/counseling/type/{type}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BK')")
    public List<Counseling> getCounselingByType(@PathVariable(value = "type") Counseling.CounselingType type) {
        return counselingRepository.findByType(type);
    }

    @GetMapping("/counseling/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BK')")
    public List<Counseling> getCounselingByStatus(@PathVariable(value = "status") Counseling.CounselingStatus status) {
        return counselingRepository.findByStatus(status);
    }

    @GetMapping("/counseling/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BK')")
    public List<Counseling> getCounselingByDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return counselingRepository.findByDateTimeBetween(start, end);
    }

    @PostMapping("/counseling")
    @PreAuthorize("hasRole('BK')")
    public Counseling createCounseling(@Valid @RequestBody Counseling counseling) {
        return counselingRepository.save(counseling);
    }

    @PutMapping("/counseling/{id}")
    @PreAuthorize("hasRole('BK')")
    public ResponseEntity<Counseling> updateCounseling(@PathVariable(value = "id") Long counselingId,
            @Valid @RequestBody Counseling counselingDetails) throws ResourceNotFoundException {
        Counseling counseling = counselingRepository.findById(counselingId)
                .orElseThrow(() -> new ResourceNotFoundException("Counseling session not found for this id :: " + counselingId));

        counseling.setStudent(counselingDetails.getStudent());
        counseling.setCounselor(counselingDetails.getCounselor());
        counseling.setDateTime(counselingDetails.getDateTime());
        counseling.setTitle(counselingDetails.getTitle());
        counseling.setDescription(counselingDetails.getDescription());
        counseling.setRecommendation(counselingDetails.getRecommendation());
        counseling.setFollowUpAction(counselingDetails.getFollowUpAction());
        counseling.setType(counselingDetails.getType());
        counseling.setStatus(counselingDetails.getStatus());

        final Counseling updatedCounseling = counselingRepository.save(counseling);
        return ResponseEntity.ok(updatedCounseling);
    }

    @DeleteMapping("/counseling/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Boolean> deleteCounseling(@PathVariable(value = "id") Long counselingId)
            throws ResourceNotFoundException {
        Counseling counseling = counselingRepository.findById(counselingId)
                .orElseThrow(() -> new ResourceNotFoundException("Counseling session not found for this id :: " + counselingId));

        counselingRepository.delete(counseling);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
