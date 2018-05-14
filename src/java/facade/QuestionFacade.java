/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.Course;
import entity.Question;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author oswal
 */
@Stateless
public class QuestionFacade extends AbstractFacade<Question> {

    @PersistenceContext(unitName = "ExamOnlinePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public QuestionFacade() {
        super(Question.class);
    }

    public Question findLast() {
        String findLast = "SELECT q FROM Question q ORDER BY q.id DESC";
        Question q = null;
        try {
            q = (Question) em.createQuery(findLast, Question.class)
                    .setMaxResults(1)
                    .getResultList()
                    .get(0);
        } catch (ArrayIndexOutOfBoundsException e) {

        }
        return q;
    }

    public String generateQuestionId() {
        Question q = findLast();
        if (q != null) {
            String id = q.getId();
            if (!id.equals("Q999999")) {
                int number = Integer.parseInt(id.substring(1)) + 1;
                return "Q" + String.format("%06d", number);
            }
        }
        return "Q000001";
    }

    public Question findAvailableQuestionById(String id) {
        String findAvailableQuestionById = "SELECT q FROM Question q WHERE q.id = :id AND q.status = 1";
        Question q = null;
        try {
            q = (Question) em.createQuery(findAvailableQuestionById)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {

        }
        return q;
    }

    public List<Question> findAvailableQuestionByCourse(Course course) {
        String findQuestionByCourseId = "SELECT q FROM Question q WHERE q.courseId = :courseId AND q.status = 1";
        return em.createQuery(findQuestionByCourseId)
                .setParameter("courseId", course)
                .getResultList();
    }

    public List<Question> findQuestionByCourse(Course course) {
        String findQuestionByCourseId = "SELECT q FROM Question q WHERE q.courseId = :courseId";
        return em.createQuery(findQuestionByCourseId)
                .setParameter("courseId", course)
                .getResultList();
    }
}
