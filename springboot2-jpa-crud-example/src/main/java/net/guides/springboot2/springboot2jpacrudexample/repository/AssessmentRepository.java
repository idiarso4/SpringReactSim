package net.guides.springboot2.springboot2jpacrudexample.repository;

import net.guides.springboot2.springboot2jpacrudexample.model.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    List<Assessment> findByClassRoomId(Long classId);
    List<Assessment> findByStudentId(Long studentId);
    List<Assessment> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<Assessment> findBySubject(String subject);
}
