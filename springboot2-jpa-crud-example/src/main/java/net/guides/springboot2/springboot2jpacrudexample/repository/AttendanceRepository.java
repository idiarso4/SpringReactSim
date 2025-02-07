package net.guides.springboot2.springboot2jpacrudexample.repository;

import net.guides.springboot2.springboot2jpacrudexample.model.Attendance;
import net.guides.springboot2.springboot2jpacrudexample.model.ClassRoom;
import net.guides.springboot2.springboot2jpacrudexample.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudent(User student);
    List<Attendance> findByClassRoom(ClassRoom classRoom);
    List<Attendance> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Attendance> findByStudentAndDateTimeBetween(User student, LocalDateTime start, LocalDateTime end);
    List<Attendance> findByClassRoomAndDateTimeBetween(ClassRoom classRoom, LocalDateTime start, LocalDateTime end);
}
