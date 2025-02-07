package net.guides.springboot2.springboot2jpacrudexample.controller;

import jakarta.validation.Valid;
import net.guides.springboot2.springboot2jpacrudexample.exception.ResourceNotFoundException;
import net.guides.springboot2.springboot2jpacrudexample.model.Attendance;
import net.guides.springboot2.springboot2jpacrudexample.model.ClassRoom;
import net.guides.springboot2.springboot2jpacrudexample.model.User;
import net.guides.springboot2.springboot2jpacrudexample.repository.AttendanceRepository;
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
public class AttendanceController {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @GetMapping("/attendance")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('HOMEROOM_TEACHER')")
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    @GetMapping("/attendance/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('HOMEROOM_TEACHER')")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable(value = "id") Long attendanceId)
            throws ResourceNotFoundException {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found for this id :: " + attendanceId));
        return ResponseEntity.ok().body(attendance);
    }

    @GetMapping("/attendance/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('HOMEROOM_TEACHER')")
    public List<Attendance> getAttendanceByStudent(@PathVariable(value = "studentId") Long studentId)
            throws ResourceNotFoundException {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for this id :: " + studentId));
        return attendanceRepository.findByStudent(student);
    }

    @GetMapping("/attendance/class/{classId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('HOMEROOM_TEACHER')")
    public List<Attendance> getAttendanceByClass(@PathVariable(value = "classId") Long classId)
            throws ResourceNotFoundException {
        ClassRoom classRoom = classRoomRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found for this id :: " + classId));
        return attendanceRepository.findByClassRoom(classRoom);
    }

    @GetMapping("/attendance/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('HOMEROOM_TEACHER')")
    public List<Attendance> getAttendanceByDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return attendanceRepository.findByDateTimeBetween(start, end);
    }

    @PostMapping("/attendance")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('HOMEROOM_TEACHER')")
    public Attendance createAttendance(@Valid @RequestBody Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    @PutMapping("/attendance/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('HOMEROOM_TEACHER')")
    public ResponseEntity<Attendance> updateAttendance(@PathVariable(value = "id") Long attendanceId,
            @Valid @RequestBody Attendance attendanceDetails) throws ResourceNotFoundException {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found for this id :: " + attendanceId));

        attendance.setStudent(attendanceDetails.getStudent());
        attendance.setClassRoom(attendanceDetails.getClassRoom());
        attendance.setDateTime(attendanceDetails.getDateTime());
        attendance.setStatus(attendanceDetails.getStatus());
        attendance.setNotes(attendanceDetails.getNotes());

        final Attendance updatedAttendance = attendanceRepository.save(attendance);
        return ResponseEntity.ok(updatedAttendance);
    }

    @DeleteMapping("/attendance/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Boolean> deleteAttendance(@PathVariable(value = "id") Long attendanceId)
            throws ResourceNotFoundException {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found for this id :: " + attendanceId));

        attendanceRepository.delete(attendance);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
