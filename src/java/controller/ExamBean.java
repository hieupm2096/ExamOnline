/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

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

    private String name;
    private int numOfQuestion;
    private int duration;
    private List<Student> students;
    private List<Question> questions;

    private String studentId;
    private String classId;

    private String questionId;
    private String courseId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @PostConstruct
    public void init() {
        students = new ArrayList<>();
        questions = new ArrayList<>();
    }

    public ExamBean() {

    }

    public void addStudentToList() {
        Student student = null;
        boolean isContained = false;
        if (studentId != null) {
            if (students != null && !students.isEmpty()) {
                for (Student s : students) {
                    if (s.getId().equals(studentId)) {
                        isContained = true;
                        break;
                    }
                }
                if (!isContained) {
                    student = studentFacade.findAvailableStudentById(studentId);
                }
            } else {
                student = studentFacade.findAvailableStudentById(studentId);
            }
            if (student != null) {
                students.add(student);
            }
            // else student is not found
        }
        // else studentId is empty
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

    public String createExam() {
        String id = examFacade.generateExamId();
        
        // for test purpose only
        String userId = "U000001";
        Exam exam = new Exam();
        exam.setId(id);
        exam.setName(name);
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
}
