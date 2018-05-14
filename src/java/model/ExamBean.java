/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Class;
import entity.Course;
import entity.Exam;
import entity.ExamStudent;
import entity.ExamStudentPK;
import entity.Question;
import entity.Student;
import entity.User;
import facade.ClassFacade;
import facade.CourseFacade;
import facade.ExamFacade;
import facade.ExamStudentFacade;
import facade.QuestionFacade;
import facade.StudentFacade;
import facade.UserFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oswal
 */
@Named(value = "examBean")
@ViewScoped
public class ExamBean implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExamBean.class);
    private static final String EXAM_LIST_REDIRECT = "exam-list?faces-redirect=true";
    private static final String EXAM_DETAIL_PAGE_REDIRECT = "exam-details?faces-redirect=true";

    private final int EXTEND_DURATION = 15;

    @EJB
    private ExamStudentFacade examStudentFacade;

    @EJB
    private UserFacade userFacade;

    @EJB
    private CourseFacade courseFacade;

    @EJB
    private QuestionFacade questionFacade;

    @EJB
    private ClassFacade classFacade;

    @EJB
    private StudentFacade studentFacade;

    @EJB
    private ExamFacade examFacade;

    @Inject
    private AuthenticationBean authenticationBean;

    public List<Exam> getExamList() {
        return examFacade.findAll();
    }

    public List<Course> getCourseList() {
        return courseFacade.findByStatus(true);
    }

    private String id;
    private String description;
    private int numOfQuestion;
    private int duration;

    private List<Student> students;
    private List<Question> questions;
    private List<ExamStudent> examStudents;

    private String studentId;
    private Student foundStudent;

    private String classId;
    private entity.Class foundClass;

    private String questionId;
    private Question foundQuestion;

    private String courseId;

    private List<entity.Class> classList;

    private User user;
    private Date startTime;
    private Exam exam;

    @PostConstruct
    public void init() {
        students = new ArrayList<>();
        questions = new ArrayList<>();
    }

    public ExamBean() {

    }

    public void courseChange() {
        students = new ArrayList<Student>();
    }

    public void numberOfQuestionChange() {
        questions = new ArrayList<Question>();
    }

    public void addStudentToList() {
        if (!students.contains(foundStudent)) {
            students.add(foundStudent);
        }
    }

    public void removeStudentFromList(Student student) {
        students.remove(student);
    }

    public void addClassToList() {
        if (foundClass != null) {
            if (foundClass.getStatus()) {
                Course course = courseFacade.find(courseId);
                if (course.getClassList() != null && course.getClassList().contains(foundClass)) {
                    List<Student> classStudents = foundClass.getStudentList();
                    if (classStudents != null && !classStudents.isEmpty()) {
                        if (students != null && !students.isEmpty()) {
                            for (Student student : classStudents) {
                                if (!students.contains(student) && student.getStatus()) {
                                    students.add(student);
                                }
                            }
                        } else {
                            students = classStudents;
                        }
                        foundClass = null;
                        classId = "";
                    } else {
                        LOGGER.error("There's no student in class");
                    }
                } else {
                    LOGGER.error("Students of this class no need to take exam of this course");
                }
            } else {
                LOGGER.error("Class Unavailable");
            }
        } else {
            LOGGER.error("Class Not Found");
        }
    }

    public void addQuestionToList() {
        if (questions.size() < numOfQuestion) {
            if (foundQuestion != null) {
                if (!questions.contains(foundQuestion)) {
                    questions.add(foundQuestion);
                    foundQuestion = null;
                    questionId = "";
                } else {
                    LOGGER.error("Question Already Added");
                }
            } else {
                LOGGER.error("Question Not Found");
            }
        } else {
            LOGGER.error("Maximum number of question reached");
        }
    }

    public void addQuestionToListAuto() {
        int remain = numOfQuestion - questions.size();
        if (remain > 0) {
            Course course = courseFacade.find(courseId);
            if (course != null) {

                // get question of course id
                List<Question> courseQuestions = questionFacade.findAvailableQuestionByCourse(course);

                // shuffle course question
                Collections.shuffle(courseQuestions);

                // add course question to question list
                if (courseQuestions != null && !courseQuestions.isEmpty()) {
                    for (int i = 0; i < courseQuestions.size() && remain > 0; i++) {
                        Question tmp = courseQuestions.get(i);
                        if (!questions.contains(tmp)) {
                            questions.add(tmp);
                            remain--;
                        }
                    }
                }
            }
        } else {
            LOGGER.info("Maximum Number of Question Reached");
        }
    }

    public void removeQuestionFromList(Question q) {
        questions.remove(q);
    }

    public void findClass() {
        id = classId;
        entity.Class temp = classFacade.find(id);
        if (temp != null && temp.getStatus()) {
            foundClass = new entity.Class();
            foundClass.setId(temp.getId());
            foundClass.setDescription(temp.getDescription());
            foundClass.setUserId(temp.getUserId());
            foundClass.setStatus(temp.getStatus());
            List<Student> temps = new ArrayList<>();
            for (Student student : temp.getStudentList()) {
                if (student.getStatus()) {
                    temps.add(student);
                }
            }
            foundClass.setStudentList(temps);
        }
    }

    public void findStudent() {
        id = studentId;
        Student temp = studentFacade.find(id);
        if (temp != null && temp.getStatus()) {
            foundStudent = temp;
        }
    }

    public void findQuestion() {
        id = questionId;
        Question temp = questionFacade.find(id);
        if (temp != null && temp.getStatus()) {
            if (temp.getCourseId().getId().equals(courseId)) {
                foundQuestion = temp;
            } else {
                LOGGER.error("Question does not belong to the course");
            }
        } else {
            LOGGER.error("Question Not Found");
        }
    }

    public String createExam() {
        id = examFacade.generateExamId();

        // get user id of current login user
        String userId = authenticationBean.getLoginUser().getId();

        exam = new Exam();
        exam.setId(id);
        exam.setDescription(description);
        exam.setUserId(userFacade.find(userId));
        exam.setNumOfQuestion(numOfQuestion);
        exam.setCourseId(courseFacade.find(courseId));
        exam.setDuration(duration);
        exam.setQuestionList(questions);
        examFacade.create(exam);
        createExamStudent(exam);
        return EXAM_LIST_REDIRECT;
    }

    private void createExamStudent(Exam exam) {
        if (students != null && !students.isEmpty()) {

            // generate random string to set passcode for student
            List<String> randomStrings = randomStringArray(students.size());

            for (Student student : students) {
                ExamStudentPK espk = new ExamStudentPK(exam.getId(), student.getId());
                ExamStudent es = new ExamStudent();
                es.setExamStudentPK(espk);
                es.setStudent(student);
                es.setExam(exam);
                es.setPasscode(randomStrings.get(students.indexOf(student)));
                examStudentFacade.create(es);
            }
        }
    }

    private static List<String> randomStringArray(int n) {
        List<String> array = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            String s = null;
            do {
                s = RandomStringUtils.randomAlphanumeric(7);
            } while (array.contains(s));
            array.add(s);
        }
        return array;
    }

    public java.util.Date compareTime(int duration) {
        return new java.util.Date(new java.util.Date().getTime() - ((duration + 15) * 60000));
    }

    public void findExam() {
        String inputId = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("id");
        if (inputId != null) {
            exam = examFacade.findExamById(inputId);
            id = exam.getId();
            description = exam.getDescription();
            courseId = exam.getCourseId().getId();

            duration = exam.getDuration();

            questions = exam.getQuestionList();
            numOfQuestion = questions.size();

            examStudents = exam.getExamStudentList();

            user = exam.getUserId();
            startTime = exam.getStartTime();

        }
    }

    public boolean isExamStarted() {
        return exam.getStartTime() != null;
    }

    public boolean isExamOngoing() {
        startTime = exam.getStartTime();
        if (startTime != null) {
            Date endTime = new Date(startTime.getTime() + ((duration + EXTEND_DURATION) * 60000));
            return new Date().before(endTime);
        }
        return false;
    }

    public void startExam() {
        startTime = new Date();
        exam.setStartTime(startTime);
        examFacade.edit(exam);
    }

    public int getEXTEND_DURATION() {
        return EXTEND_DURATION;
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

    public int getNumOfQuestion() {
        return numOfQuestion;
    }

    public void setNumOfQuestion(int numOfQuestion) {
        this.numOfQuestion = numOfQuestion;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
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

    public Class getFoundClass() {
        return foundClass;
    }

    public void setFoundClass(Class foundClass) {
        this.foundClass = foundClass;
    }

    public Question getFoundQuestion() {
        return foundQuestion;
    }

    public void setFoundQuestion(Question foundQuestion) {
        this.foundQuestion = foundQuestion;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public List<ExamStudent> getExamStudents() {
        return examStudents;
    }

    public void setExamStudents(List<ExamStudent> examStudents) {
        this.examStudents = examStudents;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public long getTimeoutDuration() {
        Date endTime = new Date(startTime.getTime() + ((duration + EXTEND_DURATION) * 60000));
        long temp = (endTime.getTime() - new Date().getTime()) / 1000;
        LOGGER.info("remaining time: " + temp);
        return temp;
    }

}
