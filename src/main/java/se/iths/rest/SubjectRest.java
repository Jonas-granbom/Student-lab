package se.iths.rest;


import se.iths.entity.Student;
import se.iths.entity.Subject;
import se.iths.entity.Teacher;
import se.iths.service.SubjectService;

import javax.inject.Inject;
import javax.validation.ValidationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("subjects")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SubjectRest {
    @Inject
    SubjectService subjectService;

    @Path("")
    @POST
    public Response createSubject(Subject subject) {
        try {
            subjectService.createSubject(subject);
        } catch (ValidationException e) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                    .entity("Subject needs to have a name, error: " + e)
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build());
        }
        return Response.ok()
                .entity("Subject created")
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }

    @Path("")
    @GET
    public Response getAllSubjects() {
        List<Subject> foundSubjects = subjectService.findAllSubjects();
        if (foundSubjects.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No subjects in database")
                    .type(MediaType.TEXT_PLAIN_TYPE).build();
        }
        return Response.ok(subjectService.findAllSubjects())
                .build();
    }

    @Path("query")
    @GET
    public Response getStudentsAndTeacherFromSubject(@QueryParam("subjectid") Long id) {
        Subject foundSubject = getSubject(id);
        return Response.ok(foundSubject)
                .build();
    }

    @Path("addstudenttosubject")
    @GET
    public Response addStudentToSubject(@QueryParam("subjectid") Long subjectId, @QueryParam("studentid") Long studentId) {
        Subject foundSubject = getSubject(subjectId);
        getStudent(studentId);

        for (Student student : foundSubject.getStudents()) {
            if (student.getId() == studentId) {
                throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                        .entity("Student with id " + studentId + "is already assigned to subject " + foundSubject.getSubject())
                        .type(MediaType.TEXT_PLAIN_TYPE)
                        .build());
            }
        }
        subjectService.addStudentToSubject(subjectId, studentId);
        return Response.ok()
                .entity("Student has been added to database successfully")
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }

    @Path("addteachertosubject")
    @GET
    public Response addTeacherToSubject(@QueryParam("subjectid") Long subjectId, @QueryParam("teacherid") Long teacherId) {
        getSubject(subjectId);
        getTeacher(teacherId);

        subjectService.addTeacherToSubject(subjectId, teacherId);
        return Response.ok()
                .entity("Teacher added to subject")
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }

    @Path("{id}")
    @DELETE
    public Response deleteSubject(@PathParam("id") Long id) {
        if (subjectService.findSubjectById(id) == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                    .entity("No subject with id: " + id + "found.")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build());
        }
        subjectService.deleteSubject(id);
        return Response.ok()
                .entity("Subject with id: " + id + "deleted.")
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }

    @Path("removestudent")
    @DELETE
    public Response removeStudentFromSubject(@QueryParam("subjectid") Long subjectId, @QueryParam("studentid") Long studentId) {
        Subject foundSubject = getSubject(subjectId);
        getStudent(studentId);

        for (Student student : foundSubject.getStudents()) {
            if (student.getId() == studentId) {
                subjectService.removeStudentFromSubject(subjectId, studentId);
                return Response.ok()
                        .entity("Student with id: " + studentId + " removed from subject.")
                        .type(MediaType.TEXT_PLAIN_TYPE)
                        .build();
            }
        }
        throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                .entity("Student with id " + studentId + " is not assigned to subject " + foundSubject.getSubject())
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build());
    }

    @Path("removeteacher")
    @DELETE
    public Response removeTeacherFromSubject(@QueryParam("subjectid") Long subjectId, @QueryParam("teacherid") Long teacherId) {
        Subject foundSubject = getSubject(subjectId);
        getTeacher(teacherId);

        if (foundSubject.getTeacher() != null) {
            if (foundSubject.getTeacher().getId() == teacherId) {
                subjectService.removeTeacherFromSubject(subjectId, teacherId);
                return Response.ok()
                        .entity("Teacher removed from the subject.")
                        .type(MediaType.TEXT_PLAIN_TYPE)
                        .build();
            }
        }
        throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                .entity("Teacher with the id: " + teacherId + "is not assigned to that subject")
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build());
    }

    private void getTeacher(Long teacherId) {
        Teacher foundTeacher = subjectService.findTeacherById(teacherId);
        if (foundTeacher == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                    .entity("Teacher with id: " + teacherId + " not found in the database")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build());
        }
    }

    private void getStudent(Long studentId) {
        Student foundStudent = subjectService.findStudentById(studentId);
        if (foundStudent == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                    .entity("Student with id: " + studentId + " not found in the database")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build());
        }
    }

    private Subject getSubject(Long subjectId) {
        Subject foundSubject = subjectService.findSubjectById(subjectId);
        if (foundSubject == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                    .entity("Subject with id: " + subjectId + " not found in the database")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build());
        }
        return foundSubject;
    }
}
