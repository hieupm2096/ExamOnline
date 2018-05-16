/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Course;
import entity.Exam;
import entity.Question;
import entity.Student;
import entity.User;
import facade.AnswerFacade;
import facade.ClassFacade;
import facade.CourseFacade;
import facade.ExamFacade;
import facade.QuestionFacade;
import facade.StudentFacade;
import facade.UserFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Apple
 */
@Named(value = "courseBean")
@RequestScoped
public class CourseBean {
    
    @EJB
    private UserFacade userFacade;
    
    @EJB
    private StudentFacade studentFacade;
    
    @EJB
    private QuestionFacade questionFacade;
    
    @EJB
    private ExamFacade examFacade;
    
    @EJB
    private CourseFacade courseFacade;
    
    @EJB
    private ClassFacade classFacade;
    
    @EJB
    private AnswerFacade answerFacade;
    
    public List<Course> getCourseList() {
        return courseFacade.findAll();
    }
    
    private String id;
    private String name;
    private String description;
    private boolean status;
    private User user;
    
    private List<Student> studentList;
    private List<Exam> examList;
    private List<Question> questionList;
    
    @Inject
    private AuthenticationBean authenticationBean;
    
    @PostConstruct
    public void init() {
        examList = new ArrayList<>();
        questionList = new ArrayList<>();
        studentList = new ArrayList<>();
    }

    /**
     * Creates a new instance of CourseBean
     */
    public CourseBean() {
    }
    
    public List<User> getUserList() {
        return userFacade.findAll();
    }
    
    public List<Question> getQuestionList() {
        return questionFacade.findAll();
    }
    
    public List<Exam> getExamList() {
        return examFacade.findAll();
    }
    
    public void findCourse() {
        String inputId = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("id");
        if (inputId != null) {
            Course course = courseFacade.find(inputId);
            id = course.getId();
            name = course.getName();
            description = course.getDescription();
            status = course.getStatus();
            user = course.getUserId();
        }
    }
    
    public String createCourse() {
        String userId = authenticationBean.getLoginUser().getId();
        user = userFacade.find(userId);
        Course course = new Course();
        course.setId(courseFacade.generateCourseId());
        course.setName(name);
        course.setDescription(description);
        course.setStatus(true);
        course.setUserId(user);
        courseFacade.create(course);
        return "course-list?faces-redirect=true";
    }
    
    public int countCourses(){
        return courseFacade.count();
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
    
    public String updateCourse() {
        String inputId = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("id");
        Course course = courseFacade.find(inputId);
        course.setDescription(description);
        courseFacade.update(course);
        return "course-list?faces-redirect=true";
    }
}
