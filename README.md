# JONAS-Student-Management

## Java-EE (Jakarta-EE) CRUD-application for student management.



### Create a new student, subject or teacher with a POST-request via endpoints:

Students: http://localhost:8080/student-management-system/api/v1/students

Subjects: http://localhost:8080/student-management-system/api/v1/subjects

Teachers: http://localhost:8080/student-management-system/api/v1/teachers


#### JSON-format to create a student:
```yaml
{
  "firstName": "Lars",
  "lastName": "Kongo",
  "email": "lasse.kongo@tallinn.ee",
  "phoneNumber": "0736483434"
}
```
All fields but phonenumber is required, and email has to be unique.

#### JSON-format to create a subject
```yaml
{
  "subject" : "History 101",
}
```
#### JSON-format to create a teacher
```yaml
{
  "name" : "Bengt-åke",
}
```
### Enlist a student or teacher in a subject with a GET-request and query-parameters via endpoints:

Student: http://localhost:8080/student-management-system/api/v1/subjects/addstudenttosubject?subjectid={subjectid}&studentid={studentid}

Teacher: http://localhost:8080/student-management-system/api/v1/subjects/addteachertosubject?subjectid={subjectid}&teacherid={teacherid}

Where **{subjectid}** is the id of the subject you want to enlist someone to, and **{studentid}** or **{teacherid}** is the id of the student or teacher you want to enlist.
### Show all students, teachers or subjects with a GET-request via endpoints:

Students: http://localhost:8080/student-management-system/api/v1/students

Subjects: http://localhost:8080/student-management-system/api/v1/subjects

Teachers: http://localhost:8080/student-management-system/api/v1/teachers

The show all request for subjects will show all enlisted students and teachers.

### Show all the enlisted students and teacher of a specific subject with GET-request and query-parameter via endpoint:

http://localhost:8080/student-management-system/api/v1/subjects/query?subjectid={id}

Where **{id}** is the subject you want to see.

### Show student with specific ID with a GET-request via endpoint:

http://localhost:8080/student-management-system/api/v1/students/{id}

Where **{id}** is the id of the student you want to see.



### Show all students with a specific lastname with a GET-request and query-parameter via endpoint:

http://localhost:8080/student-management-system/api/v1/students/query?lastname=Kongo

Where you replace "Kongo" with the lastname you are looking for.



### Update a student or teacher with a PATCH-request via endpoints:

http://localhost:8080/student-management-system/api/v1/students/{id}

http://localhost:8080/student-management-system/api/v1/teachers/{id}

Where **{id}** is the id you want to update, and with the field you want to update in this JSON-format:
```yaml
{
  "email": "lassesnya@tallinn.ee"
}
```

### Delete a student, subject or teacher with a DELETE-request via endpoints:

http://localhost:8080/student-management-system/api/v1/students/{id}

http://localhost:8080/student-management-system/api/v1/subjects/{id}

http://localhost:8080/student-management-system/api/v1/teachers/{id}

Where **{id}** is the id of the student, subject or teacher you want to delete.
A student or teacher needs to be removed from a subject before you can delete it.

### Remove a student or teacher from a subject with a DELETE-request and query-parameters via endpoints:

Student: http://localhost:8080/student-management-system/api/v1/subjects/removestudent?subjectid={subjectid}&studentid={studentid}

Teacher: http://localhost:8080/student-management-system/api/v1/subjects/removeteacher?subjectid={subjectid}&teacherid={teacherid}

Where **{subjectid}** is the id of the subject you want to remove someone from, and **{studentid}** or **{teacherid}** is the id of the student or teacher you want to remove.


