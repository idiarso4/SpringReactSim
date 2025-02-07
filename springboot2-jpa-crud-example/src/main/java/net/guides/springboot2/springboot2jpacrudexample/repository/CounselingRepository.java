package net.guides.springboot2.springboot2jpacrudexample.repository;

import net.guides.springboot2.springboot2jpacrudexample.model.Counseling;
import net.guides.springboot2.springboot2jpacrudexample.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CounselingRepository extends JpaRepository<Counseling, Long> {
    List<Counseling> findByStudent(User student);
    List<Counseling> findByCounselor(User counselor);
    List<Counseling> findByType(Counseling.CounselingType type);
    List<Counseling> findByStatus(Counseling.CounselingStatus status);
    List<Counseling> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Counseling> findByStudentAndDateTimeBetween(User student, LocalDateTime start, LocalDateTime end);
    List<Counseling> findByCounselorAndDateTimeBetween(User counselor, LocalDateTime start, LocalDateTime end);
}
