package se.iths.service;

import se.iths.entity.Student;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class StudentService {

    @PersistenceContext
    EntityManager entityManager;

    public void createStudent(Student student) {
        entityManager.persist(student);
    }

    public void updateStudent(Student student) {
        entityManager.merge(student);
    }

    public Student findStudentById(Long id) {
        return entityManager.find(Student.class, id);
    }

    public List<Student> getAllStudents() {
        return entityManager.createQuery("SELECT i from Student I", Student.class).getResultList();
    }

    public void deleteStudent(Long id) {
        Student foundStudent = entityManager.find(Student.class, id);
        entityManager.remove(foundStudent);
    }
    public List<Student> getStudentByLastName(String lastName){
        TypedQuery<Student> query = entityManager.createQuery("SELECT s FROM Student s WHERE s.lastName = ?1", Student.class);
        List<Student> foundStudents = query.setParameter(1, lastName).getResultList();

        return foundStudents;
    }

    public Student updateLastName(Long id, String lastName) {
        Student foundStudent = entityManager.find(Student.class, id);
        foundStudent.setLastName(lastName);
        return foundStudent;
    }
}
