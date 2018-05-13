/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Class;
import entity.Student;
import entity.User;
import facade.ClassFacade;
import facade.StudentFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Apple
 */
@Named(value = "classBean")
@RequestScoped
public class ClassBean {

    @EJB
    private StudentFacade studentFacade;

    @EJB
    private ClassFacade classFacade;

    private String id;
    private String description;
    private boolean status;
    private User userId;

    /**
     * Creates a new instance of ClassBean
     *
     * @return
     */

    public List<entity.Class> getClassList() {
        return classFacade.findAll();
    }

    public List<Student> getStudentList() {
        return studentFacade.findAll();
    }

    public void findClass() {
        String inputId = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("id");
        if (inputId != null) {
            Class eclass = classFacade.find(inputId);
            id = eclass.getId();
            description = eclass.getDescription();
            status = eclass.getStatus();
        }
    }

    public ClassBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

}
