package net.guides.springboot2.springboot2jpacrudexample.repository;

import net.guides.springboot2.springboot2jpacrudexample.model.Journal;
import net.guides.springboot2.springboot2jpacrudexample.model.ClassRoom;
import net.guides.springboot2.springboot2jpacrudexample.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {
    List<Journal> findByTeacher(User teacher);
    List<Journal> findByClassRoom(ClassRoom classRoom);
    List<Journal> findBySubject(String subject);
    List<Journal> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Journal> findByTeacherAndDateTimeBetween(User teacher, LocalDateTime start, LocalDateTime end);
    List<Journal> findByClassRoomAndDateTimeBetween(ClassRoom classRoom, LocalDateTime start, LocalDateTime end);
}
