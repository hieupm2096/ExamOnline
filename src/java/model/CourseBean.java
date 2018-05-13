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
    private User userId;

    private List<Student> studentList;
    private List<Exam> examList;
    private List<Question> questionList;

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
    
        public void findCourse() {
        String inputId = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("id");
        if (inputId != null) {
            Course course = new Course(inputId);
            id = course.getId();
            name = course.getName();
            description = course.getDescription();
            status = course.getStatus();
            userId = course.getUserId();
        }
    }

    
    public String createCourse(){
        Course course = new Course(id);
        
        return "course-list?faces-redirect=true";
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

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
    
    
}