package se.iths.rest;


import se.iths.customresponse.HttpResponse;
import se.iths.entity.Student;
import se.iths.entity.Subject;
import se.iths.service.StudentService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("students")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StudentRest {
    @Inject
    StudentService studentService;

    @Path("")
    @POST
    public Response createStudent(Student student) {
        if (studentService.boolEmailAvailableCheck(student.getEmail())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new HttpResponse("Conflict", "Email already taken", 409))
                    .build();
        }
        try {
            studentService.createStudent(student);
        } catch (Exception e) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(new HttpResponse("Not Acceptable", "All fields are required", 406))
                    .build());
        }

        return Response.ok(student)
                .entity(new HttpResponse("Ok", "Student created", 200))
                .build();
    }

    @Path("")
    @GET
    public Response getAllStudents() {
        List<Student> foundStudents = studentService.findAllStudents();
        if (foundStudents.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new HttpResponse("Not found", "No students found", 404))
                    .build();
        }
        return Response.ok(studentService.findAllStudents())
                .build();
    }


    @Path("{id}")
    @GET
    public Response findStudentById(@PathParam("id") Long id) {
        Student foundStudent = studentService.findStudentById(id);
        if (foundStudent == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                    .entity(new HttpResponse("Not found", "Student with id: " + id + " not found in database", 404))
                    .build());
        }
        return Response.ok(foundStudent)
                .build();
    }

    @Path("query")
    @GET
    public Response getStudentByLastName(@QueryParam("lastname") String lastName) {
        List<Student> foundStudents = studentService.getStudentByLastName(lastName);
        if (foundStudents.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new HttpResponse("Not found", "No students with lastname: " + lastName + " were found in the in the database", 404))
                    .build();
        }
        return Response.ok(foundStudents)
                .build();
    }

    @Path("{id}")
    @PATCH
    public Response updateStudent(@PathParam("id") Long id, Student student) {
        Student foundStudent = studentService.findStudentById(id);
        if (foundStudent == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new HttpResponse("Not found", "Student with id: " + id + " is not currently in the database", 404))
                    .build();
        }
        if (student.getFirstName() != null) foundStudent.setFirstName(student.getFirstName());
        if (student.getLastName() != null) foundStudent.setLastName(student.getLastName());
        if (student.getEmail() != null) foundStudent.setEmail(student.getEmail());
        if (student.getPhoneNumber() != null) foundStudent.setPhoneNumber(student.getPhoneNumber());

        studentService.updateStudent(foundStudent);

        return Response.ok(foundStudent)
                .build();
    }

    @Path("{id}")
    @DELETE
    public Response deleteStudent(@PathParam("id") Long id) {
        Student foundStudent = studentService.findStudentById(id);
        if (foundStudent == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new HttpResponse("Not found", "Student with id: " + id + " not found in database", 404))
                    .build();
        }
        for (Subject subject : studentService.findAllSubjects()) {
            for (Student student : subject.getStudents()) {
                if (student.getId() == foundStudent.getId()) {
                    throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                            .entity(new HttpResponse("Conflict", "To delete a student assigned to a subject, first remove it from the subject", 409))
                            .build());
                }
            }
        }

        studentService.deleteStudent(id);
        return Response.ok()
                .entity(new HttpResponse("Ok", "Deleted student with id: " + id, 200))
                .build();
    }
}
