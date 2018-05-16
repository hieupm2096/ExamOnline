/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.Exam;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author oswal
 */
@Stateless
public class ExamFacade extends AbstractFacade<Exam> {

    @PersistenceContext(unitName = "ExamOnlinePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ExamFacade() {
        super(Exam.class);
    }

    public Exam findLast() {
        String findLast = "SELECT e FROM Exam e ORDER BY e.id DESC";
        List<Exam> exam = em.createQuery(findLast)
                .setMaxResults(1)
                .getResultList();
        return (exam != null && !exam.isEmpty()) ? exam.get(0) : null;
    }

    public String generateExamId() {
        Exam exam = findLast();
        if (exam != null) {
            String id = exam.getId();
            if (!id.equals("E999999")) {
                int number = Integer.parseInt(id.substring(1)) + 1;
                return "E" + String.format("%06d", number);
            }
        }
        return "E000001";
    }
    
    public Exam findExamById(String examId) {
        String findExamById = "SELECT e FROM Exam e WHERE e.id = :id";
        em.getEntityManagerFactory().getCache().evictAll();
        return (Exam) em.createQuery(findExamById).setParameter("id", examId).getSingleResult();
    }
    
}
