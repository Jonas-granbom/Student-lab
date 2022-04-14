package se.iths.rest;

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
                    .entity("Teacher needs to have a name, error: " + e)
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build());
        }
        return Response.ok()
                .entity("Teacher created")
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }

    @Path("{id}")
    @PATCH
    public Response updateTeacher(@PathParam("id") Long id, @QueryParam("updatedname") String updatedName) {
        Teacher foundTeacher = getTeacher(id);

        try {
            foundTeacher.setName(updatedName);
        } catch (ValidationException e) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                    .entity("Teacher needs to have a name, error: " + e)
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build());
        }
        teacherService.updateTeacher(foundTeacher);
        return Response.ok()
                .entity("Teacher changed")
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }

    @Path("{id}")
    @DELETE
    public Response deleteTeacher(@PathParam("id") Long id) {
        getTeacher(id);

        for (Subject subject : teacherService.findAllSubjects()) {
            if (subject.getTeacher() != null) {
                if (subject.getTeacher().getId() == id) {
                    throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                            .entity("This teacher seem to be assigned to a subject, please remove it before trying to delete.")
                            .build());
                }
            }
        }
        teacherService.deleteTeacher(id);
        return Response.ok()
                .entity("Teacher with id: " + id + "deleted from the database")
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }

    private Teacher getTeacher(Long id) {
        Teacher foundTeacher = teacherService.findTeacherById(id);
        if (foundTeacher == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                    .entity("Teacher with id: " + id + " not found in the database")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build());
        }
        return foundTeacher;
    }
}
