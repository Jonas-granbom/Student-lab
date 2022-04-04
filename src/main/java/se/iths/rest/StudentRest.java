package se.iths.rest;


import se.iths.entity.Student;
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
        if(studentService.boolEmailAvailableCheck(student.getEmail())){
            return Response.status(Response.Status.CONFLICT)
                    .entity("Email is already in use")
                    .type(MediaType.TEXT_PLAIN_TYPE).build();
        }
        studentService.createStudent(student);
        return Response.ok(student).entity("Student created").type(MediaType.TEXT_PLAIN_TYPE).build();
    }

    @Path("")
    @GET
    public Response getAllStudents() {
        return Response.ok(studentService.findAllStudents()).build();
    }

    @Path("{id}")
    @PATCH
    public Response updateStudent(@PathParam("id") Long id, Student student) {
        Student foundStudent = studentService.findStudentById(id);

        if (foundStudent == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Student with ID: " + id + " is not currently in the database").type(MediaType.TEXT_PLAIN_TYPE).build();
        }
        if (student.getFirstName() != null) foundStudent.setFirstName(student.getFirstName());

        if (student.getLastName() != null) foundStudent.setLastName(student.getLastName());

        if (student.getEmail() != null) foundStudent.setEmail(student.getEmail());

        if (student.getPhoneNumber() != null) foundStudent.setPhoneNumber(student.getPhoneNumber());

        studentService.updateStudent(foundStudent);

        return Response.ok(foundStudent).build();
    }


    @Path("{id}")
    @GET
    public Response findStudentById(@PathParam("id") Long id) {
        Student foundStudent = studentService.findStudentById(id);
        if (foundStudent == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("Student with ID " + id + " not found in DB").type(MediaType.TEXT_PLAIN_TYPE).build());
        }
        return Response.ok(foundStudent).build();
    }

    @Path("query")
    @GET
    public Response getStudentByLastName(@QueryParam("lastname") String lastName) {
        List<Student> foundStudents = studentService.getStudentByLastName(lastName);
        if (foundStudents.size() == 0) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No students with lastname " + lastName + " were found in the DB").type(MediaType.TEXT_PLAIN_TYPE).build();
        }
        return Response.ok(foundStudents).build();
    }



    @Path("{id}")
    @DELETE
    public Response deleteStudent(@PathParam("id") Long id) {
        Student foundStudent = studentService.findStudentById(id);
        if (foundStudent == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No student with ID:  " + id + " were found in the DB").type(MediaType.TEXT_PLAIN_TYPE).build();
        }
        studentService.deleteStudent(id);
        return Response.ok().entity("Deleted student with ID: " + id).type(MediaType.TEXT_PLAIN_TYPE).build();
    }


}
