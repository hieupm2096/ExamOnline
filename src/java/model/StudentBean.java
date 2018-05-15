/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Student;
import facade.StudentFacade;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author phamt
 */
@Named(value = "studentBean")
@RequestScoped
public class StudentBean implements Serializable {

    @EJB
    private StudentFacade studentFacade;
    
    private static final String STUDENT_LIST_PAGE_REDIRECT = "student-list?faces-redirect=true";

    private List<Student> studentList;
    private String id;
    private String name;
    private String email;
    private String newPassword;
    private String confirmPassword;
    private boolean status;

    public StudentBean() {
    }

    public List<Student> getStudentList() {
        return studentFacade.findAll();
    }

    public String removeStudent(Student s) {
        studentFacade.remove(s);
        return "student-list?faces-redirect=true";
    }

    public void setStudentFacade(StudentFacade studentFacade) {
        this.studentFacade = studentFacade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String createStudent() {
        Student s = new Student();
        s.setId(studentFacade.generateStudentId());
        s.setName(name);
        s.setEmail(email);
        s.setPassword(newPassword);
        s.setStatus(true);
        studentFacade.create(s);

        return STUDENT_LIST_PAGE_REDIRECT;
    }

    public String updateStudent() {
        Student s = new Student();
        s.setId(id);
        s.setName(name);
        s.setEmail(email);
        if (newPassword != null && newPassword.equals(confirmPassword)) {
            s.setPassword(newPassword);
        }
        s.setStatus(status);
        studentFacade.edit(s);
        return STUDENT_LIST_PAGE_REDIRECT;
    }

    public void findStudent() {
        String inputId = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("id");
        if (inputId != null) {
            Student student = studentFacade.find(inputId);
            id = student.getId();
            name = student.getName();;
            email = student.getEmail();
            status = student.getStatus();
        }
    }
}
