package com.student.studentmanagement.service;

import com.student.studentmanagement.model.Student;
import com.student.studentmanagement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository repo;

    @Override
    public Student saveStudent(Student student) {
        return repo.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return repo.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Student updateStudent(Long id, Student student) {
        student.setId(id);
        return repo.save(student);
    }

    @Override
    public String deleteStudent(Long id) {
        repo.deleteById(id);
        return "Deleted student with id: " + id;
    }
}
