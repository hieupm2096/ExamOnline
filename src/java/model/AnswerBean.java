/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Answer;
import facade.AnswerFacade;
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
@Named(value = "answerBean")
@RequestScoped
public class AnswerBean implements Serializable {

    @EJB
    private AnswerFacade answerFacade;
    
    private List<Answer> answerList;
    private String id;
    private String content;
    private String question_id;

    public List<Answer> getAnswerList(){
        return answerFacade.findAll();
    }
    
    public String removeStudent(Answer a) {
        answerFacade.remove(a);
        return "answer-list?facet-redirect=true";
    }
    
    public boolean isIs_correct() {
        return is_correct;
    }

    public void setIs_correct(boolean is_correct) {
        this.is_correct = is_correct;
    }
    private boolean is_correct;

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

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public void findAnswer() {
        String inputId = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("id");
        if (inputId != null) {
            Answer answer = answerFacade.find(inputId);
            id = answer.getId();
            content = answer.getContent();
            question_id = answer.getQuestionId().getId();
            is_correct = answer.getIsCorrect();
        }
    }

    public AnswerBean() {
    }

}
