package se.iths.rest;


import se.iths.customresponse.HttpResponse;
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
                    .entity(new HttpResponse("Bad request", "Subject name is mandatory", 400))
                    .build());
        }
        return Response.ok()
                .entity(new HttpResponse("Ok", "Subject created", 200))
                .build();
    }

    @Path("")
    @GET
    public Response getAllSubjects() {
        List<Subject> foundSubjects = subjectService.findAllSubjects();
        if (foundSubjects.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new HttpResponse("Not found", "No subjects were found in the database", 404))
                    .build();
        }
        return Response.ok(subjectService.findAllSubjects())
                .build();
    }

    @Path("query")
    @GET
    public Response getStudentsAndTeacherFromSubject(@QueryParam("subjectid") Long id) {
        Subject foundSubject = subjectNotFound(id);
        return Response.ok(foundSubject)
                .build();
    }

    @Path("addstudenttosubject")
    @GET
    public Response addStudentToSubject(@QueryParam("subjectid") Long subjectId, @QueryParam("studentid") Long studentId) {
        Subject foundSubject = subjectNotFound(subjectId);
        studentNotFound(studentId);
        for (Student student : foundSubject.getStudents()) {
            if (student.getId() == studentId) {
                throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                        .entity(new HttpResponse("Conflict", "Student with id: " + studentId + " already assigned to the subject", 409))
                        .build());
            }
        }
        subjectService.addStudentToSubject(subjectId, studentId);
        return Response.ok()
                .entity(new HttpResponse("Ok", "Student added to subject", 200))
                .build();
    }

    @Path("addteachertosubject")
    @GET
    public Response addTeacherToSubject(@QueryParam("subjectid") Long subjectId, @QueryParam("teacherid") Long teacherId) {
        subjectNotFound(subjectId);
        teacherNotFound(teacherId);
        subjectService.addTeacherToSubject(subjectId, teacherId);
        return Response.ok()
                .entity(new HttpResponse("Ok", "Teacher added to subject", 200))
                .build();
    }

    @Path("{id}")
    @DELETE
    public Response deleteSubject(@PathParam("id") Long id) {
        if (subjectService.findSubjectById(id) == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                    .entity(new HttpResponse("Not found", "Subject with id: " + id + " were found in the database", 404))
                    .build());
        }
        subjectService.deleteSubject(id);
        return Response.ok()
                .entity(new HttpResponse("Ok", "Deleted subject with id: " + id, 200))
                .build();
    }

    @Path("removestudent")
    @DELETE
    public Response removeStudentFromSubject(@QueryParam("subjectid") Long subjectId, @QueryParam("studentid") Long studentId) {
        Subject foundSubject = subjectNotFound(subjectId);
        studentNotFound(studentId);
        for (Student student : foundSubject.getStudents()) {
            if (student.getId() == studentId) {
                subjectService.removeStudentFromSubject(subjectId, studentId);
                return Response.ok()
                        .entity(new HttpResponse("Ok", "Deleted student with id: " + studentId + " from the subject", 200))
                        .build();
            }
        }
        throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                .entity(new HttpResponse("Not found", "Student with id: " + studentId + " is not assigned to subject: " + foundSubject.getSubject(), 404))
                .build());
    }

    @Path("removeteacher")
    @DELETE
    public Response removeTeacherFromSubject(@QueryParam("subjectid") Long subjectId, @QueryParam("teacherid") Long teacherId) {
        Subject foundSubject = subjectNotFound(subjectId);
        teacherNotFound(teacherId);
        if (foundSubject.getTeacher() != null && foundSubject.getTeacher().getId() == teacherId) {
            subjectService.removeTeacherFromSubject(subjectId, teacherId);
            return Response.ok()
                    .entity(new HttpResponse("Ok", "Teacher removed from subject", 200))
                    .build();
        }
        throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                .entity(new HttpResponse("Not found", "Teacher with the id: " + teacherId + "is not assigned to that subject", 404))
                .build());
    }

    private void teacherNotFound(Long teacherId) {
        Teacher foundTeacher = subjectService.findTeacherById(teacherId);
        if (foundTeacher == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                    .entity(new HttpResponse("Not found", "Teacher with id: " + teacherId + " not found in the database", 404))
                    .build());
        }
    }

    private void studentNotFound(Long studentId) {
        Student foundStudent = subjectService.findStudentById(studentId);
        if (foundStudent == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                    .entity(new HttpResponse("Not found", "Student with id: " + studentId + " not found in the database", 404))
                    .build());
        }
    }

    private Subject subjectNotFound(Long subjectId) {
        Subject foundSubject = subjectService.findSubjectById(subjectId);
        if (foundSubject == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                    .entity(new HttpResponse("Not found", "Subject with id: " + subjectId + " not found in the database", 404))
                    .build());
        }
        return foundSubject;
    }
}
