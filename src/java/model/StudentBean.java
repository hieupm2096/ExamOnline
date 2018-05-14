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

    private List<Student> studentList;
    private String id;
    private String name;
    private String email;
    private String password;
    private boolean status;

    public StudentBean() {
    }

    public List<Student> getStudentList() {
        return studentFacade.findAll();
    }

    public String removeStudent(Student s) {
        studentFacade.remove(s);
        return "student?facet-redirect=true";
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        s.setPassword(password);
        s.setStatus(true);
        studentFacade.create(s);

        return "student?faces-redirect=true";
    }

    public void findStudent() {
        String inputId = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("id");
        if (inputId != null) {
            Student student = studentFacade.find(inputId);
            id = student.getId();
            name = student.getName();;
            email = student.getEmail();
            password = student.getPassword();
            status = student.getStatus();
        }
    }
}
