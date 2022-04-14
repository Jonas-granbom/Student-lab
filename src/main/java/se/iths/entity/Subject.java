package se.iths.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull @NotBlank
    private String subject;

    @ManyToMany
    private List<Student> students = new ArrayList<>();

    @ManyToOne
    private Teacher teacher;

    public Subject() {
    }

    public Subject(String subject, List<Student> students, Teacher teacher) {
        this.subject = subject;
        this.students = students;
        this.teacher = teacher;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
    public void addStudent(Student student){
        students.add(student);
    }
    public void removeStudent(Long id){
        students.removeIf(s -> s.getId().equals(id));
    }
}
