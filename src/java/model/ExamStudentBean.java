/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Exam;
import entity.ExamQuestion;
import entity.ExamQuestionAnswer;
import entity.ExamStudent;
import entity.ExamStudentAnswer;
import entity.ExamStudentPK;
import facade.ExamStudentFacade;
import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oswal
 */
@Named(value = "examStudentBean")
@RequestScoped
public class ExamStudentBean implements Serializable {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ExamStudentBean.class);
    
    private static final String SINGLE_ANSWER_QUESTION_TYPE = "T01";

    @EJB
    private ExamStudentFacade examStudentFacade;

    private ExamStudent examStudent;
    private String examId;
    private String studentId;
    private Exam exam;
    private List<ExamQuestion> questions;
    
    private String[][] questionAnswer;
    private List<ExamStudentAnswer> examStudentAnswerList;
    private List<ExamQuestionAnswer> examStudentAnswerMappers;
    
    public ExamStudentBean() {
        
    }

    public void findExamStudent() {
        examId = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("examId");
        studentId = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("studentId");
        if (examId != null && studentId != null) {
            ExamStudentPK examStudentPK = new ExamStudentPK(examId, studentId);
            examStudent = examStudentFacade.find(examStudentPK);
            if (examStudent != null) {
                initBean(examStudent);
            }
        }
    }
    
    private void initBean(ExamStudent examStudent) {
        exam = examStudent.getExam();
        questions = exam.getExamQuestionList();
        examStudentAnswerList = examStudent.getExamStudentAnswerList();
        examStudentAnswerMappers = new ArrayList<>();
        for (ExamStudentAnswer esa : examStudentAnswerList) {
            examStudentAnswerMappers.add(esa.getAnswer());
        }
    }
    
    public boolean isQuestionSingleAnswer(ExamQuestion q) {
        return q.getQuestionTypeId().getId().equals(SINGLE_ANSWER_QUESTION_TYPE);
    }
    
    public String findFromExamQuestionAnswerListSingle(ExamQuestion examQuestion) {
        for (ExamStudentAnswer esa : examStudentAnswerList) {
            if (esa.getAnswer().getExamQuestion().getExamQuestionPK().equals(examQuestion.getExamQuestionPK())) {
                return esa.getAnswer().getId();
            }
        }
        return "";
    }
    
    public List<String> findFromExamQuestionAnswerListMultiple(ExamQuestion examQuestion) {
        List<String> results = new ArrayList<>();
        for (ExamStudentAnswer esa : examStudentAnswerList) {
            if (esa.getAnswer().getExamQuestion().getExamQuestionPK().equals(examQuestion.getExamQuestionPK())) {
                results.add(esa.getAnswer().getId());
            }
        }
        return results;
    }
    
    public boolean isQuesionAnsweredCorrectly(ExamQuestion examQuestion) {
//        List<ExamQuestionAnswer> correctAnswer = new ArrayList<>();
        Set<ExamQuestionAnswer> studentAnswerForQuestion = examStudentAnswerMappers.stream()
                .filter(
                        esam -> esam.getExamQuestion()
                                .getExamQuestionPK()
                                .equals( examQuestion.getExamQuestionPK() )
                )
                .collect( Collectors.toCollection(HashSet::new) );
        Set<ExamQuestionAnswer> correctAnswer = new HashSet<>();
        for (ExamQuestionAnswer eqa : examQuestion.getExamQuestionAnswerList()) {
            if (eqa.getIsCorrect()) {
                correctAnswer.add(eqa);
            }
        }
        return studentAnswerForQuestion.equals(correctAnswer);
    }
    
    public ExamStudent getExamStudent() {
        return examStudent;
    }

    public void setExamStudent(ExamStudent examStudent) {
        this.examStudent = examStudent;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public List<ExamQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<ExamQuestion> questions) {
        this.questions = questions;
    }

    public String[][] getQuestionAnswer() {
        return questionAnswer;
    }
 
    public void setQuestionAnswer(String[][] questionAnswer) {
        this.questionAnswer = questionAnswer;
    }
    
}
