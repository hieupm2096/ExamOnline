/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Answer;
import entity.Course;
import entity.Question;
import entity.QuestionType;
import facade.AnswerFacade;
import facade.CourseFacade;
import facade.QuestionFacade;
import facade.QuestionTypeFacade;
import java.io.Serializable;
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
 * @author oswal
 */
@Named(value = "questionBean")
@RequestScoped
public class QuestionBean implements Serializable {

    @EJB
    private AnswerFacade answerFacade;

    @EJB
    private QuestionFacade questionFacade;

    @EJB
    private QuestionTypeFacade questionTypeFacade;

    @EJB
    private CourseFacade courseFacade;

    private List<Question> questionList;
    private List<QuestionType> questionTypeList;
    private List<Course> courseList;

    private String id;
    private String content;
    private String questionTypeId;
    private String courseId;
    private boolean status;
    private List<Answer> answerList;

    private String[][] answers;

    public List<Question> getQuestionList() {
        return questionFacade.findAll();
    }

    public List<QuestionType> getQuestionTypeList() {
        return questionTypeFacade.findAll();
    }

    public List<Course> getCourseList() {
        return courseFacade.findAll();
    }

    public String[][] getAnswers() {
        return answers;
    }

    @PostConstruct
    public void init() {
        answers = new String[5][2];
    }

    public QuestionBean() {

    }

    public String createQuestion() {
        Question q = new Question();
        q.setId(questionFacade.generateQuestionId());
        q.setContent(content);
        q.setStatus(true);
        q.setQuestionTypeId(questionTypeFacade.find(questionTypeId));
        q.setCourseId(courseFacade.find(courseId));
        
        List<Answer> a = new ArrayList<>();
        String currentAnswerId = answerFacade.generateAnswerId();
        
        long number = Integer.parseInt(currentAnswerId.substring(1));

        for (int j = 0; j < 5; j++) {
            if (answers[j] != null && !answers[j][0].isEmpty()) {
                String answerId = "A" + String.format("%09d", number + j);
                a.add(createAnswer(answerId, answers[j][0], Boolean.parseBoolean(answers[j][1]), q));
            }
        }
        
        q.setAnswerList(a);
        questionFacade.create(q);
        return "question-list?faces-redirect=true";
    }
    
    private Answer createAnswer(String id, String content, boolean isCorrect, Question question) {
        Answer answer = new Answer(id, content, isCorrect);
        answer.setQuestionId(question);
        return answer;
    }

    public void findQuestion() {
        String inputId = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("id");
        if (inputId != null) {
            Question question = questionFacade.find(inputId);
            id = question.getId();
            content = question.getContent();
            questionTypeId = question.getQuestionTypeId().getId();
            courseId = question.getCourseId().getId();
            status = question.getStatus();
            answerList = question.getAnswerList();
        }
    }

    public String removeQuestion(Question q) {
        for (Answer answer : q.getAnswerList()) {
            answerFacade.remove(answer);
        }
        questionFacade.remove(q);
        return "question-list?facet-redirect=true";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQuestionTypeId() {
        return questionTypeId;
    }

    public void setQuestionTypeId(String questionTypeId) {
        this.questionTypeId = questionTypeId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }

    public int countQuestions(){
        return questionFacade.count();
    }
}
