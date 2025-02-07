package net.guides.springboot2.springboot2jpacrudexample.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import net.guides.springboot2.springboot2jpacrudexample.exception.ResourceNotFoundException;
import net.guides.springboot2.springboot2jpacrudexample.model.ClassRoom;
import net.guides.springboot2.springboot2jpacrudexample.repository.ClassRoomRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class ClassRoomController {
    @Autowired
    private ClassRoomRepository classRoomRepository;

    @GetMapping("/classrooms")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('HOMEROOM_TEACHER')")
    public List<ClassRoom> getAllClassRooms() {
        return classRoomRepository.findAll();
    }

    @GetMapping("/classrooms/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('HOMEROOM_TEACHER')")
    public ResponseEntity<ClassRoom> getClassRoomById(@PathVariable(value = "id") Long classRoomId)
            throws ResourceNotFoundException {
        ClassRoom classRoom = classRoomRepository.findById(classRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found for this id :: " + classRoomId));
        return ResponseEntity.ok().body(classRoom);
    }

    @PostMapping("/classrooms")
    @PreAuthorize("hasRole('ADMIN')")
    public ClassRoom createClassRoom(@Valid @RequestBody ClassRoom classRoom) {
        return classRoomRepository.save(classRoom);
    }

    @PutMapping("/classrooms/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClassRoom> updateClassRoom(@PathVariable(value = "id") Long classRoomId,
            @Valid @RequestBody ClassRoom classRoomDetails) throws ResourceNotFoundException {
        ClassRoom classRoom = classRoomRepository.findById(classRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found for this id :: " + classRoomId));

        classRoom.setName(classRoomDetails.getName());
        final ClassRoom updatedClassRoom = classRoomRepository.save(classRoom);
        return ResponseEntity.ok(updatedClassRoom);
    }

    @DeleteMapping("/classrooms/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Boolean> deleteClassRoom(@PathVariable(value = "id") Long classRoomId)
            throws ResourceNotFoundException {
        ClassRoom classRoom = classRoomRepository.findById(classRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found for this id :: " + classRoomId));

        classRoomRepository.delete(classRoom);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
