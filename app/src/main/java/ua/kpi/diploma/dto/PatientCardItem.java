package ua.kpi.diploma.dto;

/**
 * @author Mykola Yashchenko
 */
public class PatientCardItem {
    private String id;
    private String date;
    private String workplace;
    private String complaint;
    private String diagnosis;
    private String relatedDiseases;
    private String visualInspection;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getRelatedDiseases() {
        return relatedDiseases;
    }

    public void setRelatedDiseases(String relatedDiseases) {
        this.relatedDiseases = relatedDiseases;
    }

    public String getVisualInspection() {
        return visualInspection;
    }

    public void setVisualInspection(String visualInspection) {
        this.visualInspection = visualInspection;
    }
}
