package se.iths.customresponse;

public class HttpResponse {
    private String status;
    private String description;
    private int statusCode;

    public HttpResponse(String status, String description, int statusCode) {
        this.status = status;
        this.description = description;
        this.statusCode = statusCode;
    }

    public HttpResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
