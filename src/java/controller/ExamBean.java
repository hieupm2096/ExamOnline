/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Class;
import entity.Course;
import entity.Exam;
import entity.ExamStudent;
import entity.ExamStudentPK;
import entity.Question;
import entity.Student;
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
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author oswal
 */
@Named(value = "examBean")
@ViewScoped
public class ExamBean implements Serializable {

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

    public List<Exam> getExamList() {
        return examFacade.findAll();
    }
    
    public List<Course> getCourseList() {
        return courseFacade.findAll();
    }

    private String description;
    private int numOfQuestion;
    private int duration;
    private List<Student> students;
    private List<Question> questions;

    private String studentId;
    private Student foundStudent;
    
    private String classId;
    private entity.Class foundClass;

    private String questionId;
    private String courseId;
    
    private List<entity.Class> classList;

    @PostConstruct
    public void init() {
        students = new ArrayList<>();
        questions = new ArrayList<>();
    }

    public ExamBean() {

    }

    public void addStudentToList() {
        if (!students.contains(foundStudent)) {
            students.add(foundStudent);
        }
    }
    
    public void removeStudentFromList(String id) {
        for (Student student : students) {
            if (student.getId().equals(id)) {
                students.remove(student);
                break;
            }
        }
    }

    public void addClassToList() {
        if (classId != null) {
            entity.Class c = classFacade.find(classId);
            if (c != null) {
                List<Student> classStudents = c.getStudentList();
                if (classStudents != null && !classStudents.isEmpty()) {
                    if (students != null && !students.isEmpty()) {
                        for (Student student : classStudents) {
                            if (!students.contains(student) && student.getStatus()) {
                                students.add(student);
                            }
                        }
                        foundClass = null;
                        classId = "";
                    } else {
                        students = classStudents;
                    }
                }
                // else class has no student
            }
            // else class not found
        }
        // classId is empty
    }

    public void addQuestionToList() {
        if (questions.size() < numOfQuestion) {
            if (questionId != null) {
                Question q = null;
                boolean isContained = false;
                if (!questions.isEmpty()) {
                    for (Question question : questions) {
                        if (question.getId().equals(questionId)) {
                            isContained = true;
                            break;
                        }
                    }
                    if (!isContained) {
                        q = questionFacade.findAvailableQuestionById(questionId);
                    }
                    // else question is already contained in the list
                } else {
                    q = questionFacade.findAvailableQuestionById(questionId);
                }
                if (q != null) {
                    questions.add(q);
                }
                // else questions not found
            }
            // else questionId is empty
        }
        // else max number of question reached
    }

    public void addQuestionToListAuto() {
        int remain = numOfQuestion - questions.size();
        if (remain > 0) {
            if (courseId != null) {
                Course course = courseFacade.find(courseId);
                if (course != null) {

                    // get question of course id
                    List<Question> courseQuestions = questionFacade.findQuestionByCourse(course);

                    // shuffle course question
                    long seed = System.nanoTime();
                    Collections.shuffle(courseQuestions, new Random(seed));

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
                // course not found
            }
            // else courseId is empty
        }
        // else max number of question reached
    }
    
    public void findClass() {
        String id = classId;
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
        String id = studentId;
        Student temp = studentFacade.find(id);
        if (temp != null && temp.getStatus()) {
            foundStudent = temp;
        }
    }

    public String createExam() {
        String id = examFacade.generateExamId();

        // for test purpose only
        String userId = "U000001";
        Exam exam = new Exam();
        exam.setId(id);
        exam.setDescription(description);
        exam.setUserId(userFacade.find(userId));
        exam.setNumOfQuestion(numOfQuestion);
        exam.setCourseId(courseFacade.find(courseId));
        exam.setDuration(duration);
        examFacade.create(exam);
        createExamStudent(exam);
        return "index?faces-redirect=true";
    }

    private void createExamStudent(Exam exam) {
        if (students != null && !students.isEmpty()) {
            for (Student student : students) {
                ExamStudentPK espk = new ExamStudentPK(exam.getId(), student.getId());
                ExamStudent es = new ExamStudent();
                es.setExamStudentPK(espk);
                es.setStudent(student);
                es.setExam(exam);
                examStudentFacade.create(es);
            }
        }

    }
       
    public java.util.Date compareTime(int duration) {
        return new java.util.Date(new java.util.Date().getTime() - (duration * 60000));
    }
    
    public boolean isClassFound() {
        return foundClass != null;
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
    
    
}
