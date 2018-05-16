/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Class;
import entity.Student;
import entity.User;
import entity.Course;
import facade.ClassFacade;
import facade.CourseFacade;
import facade.StudentFacade;
import facade.UserFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Apple
 */
@Named(value = "classBean")
@ViewScoped
public class ClassBean implements Serializable {

    private static final String CLASS_LIST_PAGE_REDIRECT = "class-list?faces-redirect=true";
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassBean.class);

    @EJB
    private UserFacade userFacade;

    @EJB
    private StudentFacade studentFacade;

    @EJB
    private ClassFacade classFacade;

    @EJB
    private CourseFacade courseFacade;

    @Inject
    private AuthenticationBean authenticationBean;

    private String id;
    private String description;
    private boolean status;
    private User user;
    private List<Student> students;
    private List<Course> courses;

    private String studentId;
    private String courseId;

    private Student foundStudent;
    private Course foundCourse;

    /**
     * Creates a new instance of ClassBean
     *
     * @return
     */
    @PostConstruct
    public void init() {
        students = new ArrayList<>();
        courses = new ArrayList<>();
    }

    public List<entity.Class> getClassList() {
        return classFacade.findAll();
    }

    public void findClass() {
        String inputId = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("id");
        if (inputId != null) {
            Class eclass = classFacade.find(inputId);
            id = eclass.getId();
            description = eclass.getDescription();
            students = eclass.getStudentList();
            courses = eclass.getCourseList();
            status = eclass.getStatus();
        }
    }

    public String updateClass() {
        String inputId = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("id");
        entity.Class eclass = classFacade.find(inputId);
        eclass.setCourseList(courses);
        eclass.setStudentList(students);
        eclass.setDescription(description);
        classFacade.update(eclass);
        return CLASS_LIST_PAGE_REDIRECT;
    }

    public String createClass() {
        String userId = authenticationBean.getLoginUser().getId();
        user = userFacade.find(userId);
        entity.Class eclass = new entity.Class();
        eclass.setId(classFacade.generateClassId());
        eclass.setDescription(description);
        eclass.setStatus(true);
        eclass.setUserId(user);
        classFacade.create(eclass);
        return CLASS_LIST_PAGE_REDIRECT;
    }

    public void findStudent() {
        Student temp = studentFacade.find(studentId);
        if (temp != null && temp.getStatus()) {
            foundStudent = temp;
        } else {
            LOGGER.error("Student Not Found");
        }
    }

    public void addStudentToList() {
        if (foundStudent != null) {
            if (!students.contains(foundStudent)) {
                students.add(foundStudent);
                foundStudent = null;
                studentId = "";
            } else {
                LOGGER.error("Student Already Added");
            }
        } else {
            LOGGER.error("Student Not Found");
        }
    }

    public void findCourse() {
        Course temp = courseFacade.find(courseId);
        if (temp != null && temp.getStatus()) {
            foundCourse = temp;
        } else {
            LOGGER.error("Course Not Found");
        }
    }

    public void addCourseToList() {
        if (foundCourse != null) {
            if (!courses.contains(foundCourse)) {
                courses.add(foundCourse);
                foundCourse = null;
                courseId = "";
            } else {
                LOGGER.error("Course Already Added");
            }
        } else {
            LOGGER.error("Course Not Found");
        }
    }

    public int countClasses() {
        return classFacade.count();
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Student getFoundStudent() {
        return foundStudent;
    }

    public void setFoundStudent(Student foundStudent) {
        this.foundStudent = foundStudent;
    }

    public Course getFoundCourse() {
        return foundCourse;
    }

    public void setFoundCourse(Course foundCourse) {
        this.foundCourse = foundCourse;
    }

}
