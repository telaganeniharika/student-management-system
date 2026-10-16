package com.student.studentmanagement.service;

import com.student.studentmanagement.model.Student;
import com.student.studentmanagement.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository repo;

    @InjectMocks
    private StudentServiceImpl service;

    private Student createStudent(Long id, String name, String email, String course) {
        Student s = new Student();
        s.setId(id);
        s.setName(name);
        s.setEmail(email);
        s.setCourse(course);
        return s;
    }

    @Test
    void saveStudent_returnsSavedStudent() {
        Student input = createStudent(null, "Harika", "harika@gmail.com", "CSE");
        Student saved = createStudent(1L, "Harika", "harika@gmail.com", "CSE");
        when(repo.save(input)).thenReturn(saved);

        Student result = service.saveStudent(input);

        assertEquals(1L, result.getId());
        assertEquals("Harika", result.getName());
        verify(repo).save(input);
    }

    @Test
    void getAllStudents_returnsAllStudents() {
        List<Student> students = Arrays.asList(
                createStudent(1L, "Harika", "harika@gmail.com", "CSE"),
                createStudent(2L, "Anusha", "anusha@gmail.com", "ECE"));
        when(repo.findAll()).thenReturn(students);

        List<Student> result = service.getAllStudents();

        assertEquals(2, result.size());
        assertEquals("Anusha", result.get(1).getName());
    }

    @Test
    void getAllStudents_whenNoStudents_returnsEmptyList() {
        when(repo.findAll()).thenReturn(Collections.emptyList());

        List<Student> result = service.getAllStudents();

        assertTrue(result.isEmpty());
    }

    @Test
    void getStudentById_whenFound_returnsStudent() {
        Student student = createStudent(1L, "Harika", "harika@gmail.com", "CSE");
        when(repo.findById(1L)).thenReturn(Optional.of(student));

        Student result = service.getStudentById(1L);

        assertSame(student, result);
    }

    @Test
    void getStudentById_whenNotFound_returnsNull() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        Student result = service.getStudentById(99L);

        assertNull(result);
    }

    @Test
    void updateStudent_setsGivenIdAndSaves() {
        Student input = createStudent(null, "Harika T", "harika@gmail.com", "CSE");
        when(repo.save(input)).thenReturn(input);

        Student result = service.updateStudent(5L, input);

        assertEquals(5L, result.getId());
        assertEquals("Harika T", result.getName());
        verify(repo).save(input);
    }

    @Test
    void deleteStudent_callsRepositoryAndReturnsMessage() {
        String message = service.deleteStudent(3L);

        verify(repo).deleteById(3L);
        assertEquals("Deleted student with id: 3", message);
    }
}
