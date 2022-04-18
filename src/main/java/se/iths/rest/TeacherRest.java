package se.iths.rest;

import se.iths.customresponse.HttpResponse;
import se.iths.entity.Subject;
import se.iths.entity.Teacher;
import se.iths.service.TeacherService;

import javax.inject.Inject;
import javax.validation.ValidationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("teachers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TeacherRest {

    @Inject
    TeacherService teacherService;

    @Path("")
    @POST
    public Response createTeacher(Teacher teacher) {
        try {
            teacherService.createTeacher(teacher);
        } catch (ValidationException e) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                    .entity(new HttpResponse("Bad request", "Teacher needs to have a name", 400))
                    .build());
        }
        return Response.ok()
                .entity(new HttpResponse("Ok", "Teacher created", 200))
                .build();
    }

    @Path("{id}")
    @PATCH
    public Response updateTeacher(@PathParam("id") Long id, @QueryParam("updatedname") String updatedName) {
        Teacher foundTeacher = teacherNotFound(id);
        try {
            foundTeacher.setName(updatedName);
        } catch (ValidationException e) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                    .entity(new HttpResponse("Bad request", "Teacher needs to have a name", 400))
                    .build());
        }
        teacherService.updateTeacher(foundTeacher);
        return Response.ok()
                .entity(new HttpResponse("Ok", "Teacher updated", 200))
                .build();
    }

    @Path("{id}")
    @DELETE
    public Response deleteTeacher(@PathParam("id") Long id) {
        teacherNotFound(id);
        for (Subject subject : teacherService.findAllSubjects()) {
            if (subject.getTeacher() != null && subject.getTeacher().getId() == id) {
                throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                        .entity(new HttpResponse("Conflict", "This teacher seem to be assigned to a subject, please remove it before trying to delete.", 409))
                        .build());
            }
        }
        teacherService.deleteTeacher(id);
        return Response.ok()
                .entity(new HttpResponse("Ok", "Teacher with id: " + id + "deleted from the database", 200))
                .build();
    }

    private Teacher teacherNotFound(Long id) {
        Teacher foundTeacher = teacherService.findTeacherById(id);
        if (foundTeacher == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                    .entity(new HttpResponse("Not found", "Teacher with id: " + id + " not found in the database", 404))
                    .build());
        }
        return foundTeacher;
    }
}
