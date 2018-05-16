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
    private String userId;
    private User user;

    private List<Student> studentList;
    private List<Exam> examList;
    private List<Question> questionList;

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

//     public String createQuestion() {
//        Question q = new Question();
//        q.setId(questionFacade.generateQuestionId());
//        q.setContent(content);
//        q.setStatus(true);
//        q.setQuestionTypeId(questionTypeFacade.find(questionTypeId));
//        q.setCourseId(courseFacade.find(courseId));
//        
//        List<Answer> a = new ArrayList<>();
//        String currentAnswerId = answerFacade.generateAnswerId();
//        
//        long number = Integer.parseInt(currentAnswerId.substring(1));
//        int i = 0;
//        
//        for (String[] answer : answers) {
//            if (answer != null && !answer[0].isEmpty()) {
//                a.add(createAnswer("A" + String.format("%09d", number + i),answer[0], Boolean.parseBoolean(answer[1]), q));
//                i++;
//            }
//        }
//        
//        q.setAnswerList(a);
//        questionFacade.create(q);
//        return "question-list?faces-redirect=true";
//    }
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
        user = userFacade.find(userId);
        Course course = new Course();
        course.setId(id);
        course.setName(name);
        course.setDescription(description);
        course.setStatus(status);
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
