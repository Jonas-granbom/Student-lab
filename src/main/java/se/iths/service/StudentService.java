package se.iths.service;

import se.iths.entity.Student;
import se.iths.entity.Subject;

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

    public List<Student> findAllStudents() {
        return entityManager.createQuery("SELECT s FROM Student s", Student.class).getResultList();
    }

    public void updateStudent(Student student) {
        entityManager.merge(student);
    }

    public Student findStudentById(Long id) {
        return entityManager.find(Student.class, id);
    }

    public void deleteStudent(Long id) {
        Student foundStudent = entityManager.find(Student.class, id);
        entityManager.remove(foundStudent);
    }

    public List<Student> getStudentByLastName(String lastName) {
        TypedQuery<Student> query = entityManager.createQuery("SELECT s FROM Student s WHERE s.lastName = ?1", Student.class);
        return query.setParameter(1, lastName).getResultList();
    }
    public List<Subject> findAllSubjects() {
        return entityManager.createQuery("SELECT s from Subject s", Subject.class).getResultList();
    }

    public boolean boolEmailAvailableCheck(String email) {
        List<String> emailList = entityManager.createQuery("SELECT s.email FROM Student s", String.class).getResultList();
        for (String emails : emailList) {
            if (emails.equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }
}
