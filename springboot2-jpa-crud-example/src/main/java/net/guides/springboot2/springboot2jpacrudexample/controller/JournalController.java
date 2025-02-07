package net.guides.springboot2.springboot2jpacrudexample.controller;

import jakarta.validation.Valid;
import net.guides.springboot2.springboot2jpacrudexample.exception.ResourceNotFoundException;
import net.guides.springboot2.springboot2jpacrudexample.model.Journal;
import net.guides.springboot2.springboot2jpacrudexample.model.ClassRoom;
import net.guides.springboot2.springboot2jpacrudexample.model.User;
import net.guides.springboot2.springboot2jpacrudexample.repository.JournalRepository;
import net.guides.springboot2.springboot2jpacrudexample.repository.ClassRoomRepository;
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
public class JournalController {
    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @GetMapping("/journals")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('HOMEROOM_TEACHER')")
    public List<Journal> getAllJournals() {
        return journalRepository.findAll();
    }

    @GetMapping("/journals/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('HOMEROOM_TEACHER')")
    public ResponseEntity<Journal> getJournalById(@PathVariable(value = "id") Long journalId)
            throws ResourceNotFoundException {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new ResourceNotFoundException("Journal not found for this id :: " + journalId));
        return ResponseEntity.ok().body(journal);
    }

    @GetMapping("/journals/teacher/{teacherId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('HOMEROOM_TEACHER')")
    public List<Journal> getJournalsByTeacher(@PathVariable(value = "teacherId") Long teacherId)
            throws ResourceNotFoundException {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found for this id :: " + teacherId));
        return journalRepository.findByTeacher(teacher);
    }

    @GetMapping("/journals/class/{classId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('HOMEROOM_TEACHER')")
    public List<Journal> getJournalsByClass(@PathVariable(value = "classId") Long classId)
            throws ResourceNotFoundException {
        ClassRoom classRoom = classRoomRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found for this id :: " + classId));
        return journalRepository.findByClassRoom(classRoom);
    }

    @GetMapping("/journals/subject/{subject}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('HOMEROOM_TEACHER')")
    public List<Journal> getJournalsBySubject(@PathVariable(value = "subject") String subject) {
        return journalRepository.findBySubject(subject);
    }

    @GetMapping("/journals/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('HOMEROOM_TEACHER')")
    public List<Journal> getJournalsByDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return journalRepository.findByDateTimeBetween(start, end);
    }

    @PostMapping("/journals")
    @PreAuthorize("hasRole('TEACHER') or hasRole('HOMEROOM_TEACHER')")
    public Journal createJournal(@Valid @RequestBody Journal journal) {
        return journalRepository.save(journal);
    }

    @PutMapping("/journals/{id}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('HOMEROOM_TEACHER')")
    public ResponseEntity<Journal> updateJournal(@PathVariable(value = "id") Long journalId,
            @Valid @RequestBody Journal journalDetails) throws ResourceNotFoundException {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new ResourceNotFoundException("Journal not found for this id :: " + journalId));

        journal.setTeacher(journalDetails.getTeacher());
        journal.setClassRoom(journalDetails.getClassRoom());
        journal.setDateTime(journalDetails.getDateTime());
        journal.setSubject(journalDetails.getSubject());
        journal.setMaterialCovered(journalDetails.getMaterialCovered());
        journal.setHomework(journalDetails.getHomework());
        journal.setNotes(journalDetails.getNotes());

        final Journal updatedJournal = journalRepository.save(journal);
        return ResponseEntity.ok(updatedJournal);
    }

    @DeleteMapping("/journals/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Boolean> deleteJournal(@PathVariable(value = "id") Long journalId)
            throws ResourceNotFoundException {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new ResourceNotFoundException("Journal not found for this id :: " + journalId));

        journalRepository.delete(journal);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
