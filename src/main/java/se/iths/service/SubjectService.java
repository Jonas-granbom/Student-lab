package se.iths.service;


import se.iths.entity.Student;
import se.iths.entity.Subject;
import se.iths.entity.Teacher;
import se.iths.service.StudentService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class SubjectService {

    @PersistenceContext
    EntityManager entityManager;

    public void createSubject(Subject subject) {
        entityManager.persist(subject);
    }
    public List<Subject> findAllSubjects() {
        return entityManager.createQuery("SELECT s FROM Subject s", Subject.class).getResultList();
    }

    public Subject findSubjectById(Long id) {
        return entityManager.find(Subject.class, id);
    }

    public Student findStudentById(Long id) {
        return entityManager.find(Student.class, id);
    }

    public Teacher findTeacherById(Long id) {
        return entityManager.find(Teacher.class, id);
    }


    public void addStudentToSubject(Long subjectId, Long studentId) {
        Student foundStudent = findStudentById(studentId);
        Subject foundSubject = findSubjectById(subjectId);
        foundSubject.addStudent(foundStudent);
        entityManager.persist(foundSubject);
    }

    public void addTeacherToSubject(Long subjectId, Long teacherId) {
        Subject foundSubject = findSubjectById(subjectId);
        Teacher foundTeacher = findTeacherById(teacherId);

        foundSubject.setTeacher(foundTeacher);
        entityManager.persist(foundSubject);
    }

    public void deleteSubject(Long id) {
        Subject foundSubject = entityManager.find(Subject.class, id);
        entityManager.remove(foundSubject);
    }

    public void removeStudentFromSubject(Long subjectId, Long studentId) {
        Subject foundSubject = entityManager.find(Subject.class, subjectId);
        foundSubject.removeStudent(studentId);
        entityManager.merge(foundSubject);
    }
    public void removeTeacherFromSubject(Long teacherId, Long subjectId){
        Subject foundSubject = entityManager.find(Subject.class, subjectId);
        foundSubject.setTeacher(null);
        entityManager.merge(foundSubject);
    }
}
