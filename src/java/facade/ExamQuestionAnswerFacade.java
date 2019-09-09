/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.ExamQuestionAnswer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author oswal
 */
@Stateless
public class ExamQuestionAnswerFacade extends AbstractFacade<ExamQuestionAnswer> {

    @PersistenceContext(unitName = "ExamOnlinePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ExamQuestionAnswerFacade() {
        super(ExamQuestionAnswer.class);
    }
    
    public String generateExamQuestionAnswerId() {
        String findLast = "SELECT eqa FROM ExamQuestionAnswer eqa ORDER BY eqa.id DESC";
        Query query = em.createQuery(findLast, ExamQuestionAnswer.class);
        try {
            ExamQuestionAnswer eqa = (ExamQuestionAnswer) query.setMaxResults(1).getResultList().get(0);
            String id = eqa.getId();
            if (!id.equals("Z999999999")) {
                long number = Integer.parseInt(id.substring(1)) + 1;
                return "Z" + String.format("%09d", number);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return "Z000000001"; 
        }
        return null;
    }
}
