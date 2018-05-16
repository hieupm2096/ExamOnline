/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.Course;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author oswal
 */
@Stateless
public class CourseFacade extends AbstractFacade<Course> {

    @PersistenceContext(unitName = "ExamOnlinePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CourseFacade() {
        super(Course.class);
    }

    public Course findLast() {
        String findLast = "SELECT q FROM Course q ORDER BY q.id DESC";
        Course cse = null;
        try {
            cse = (Course) em.createQuery(findLast, Course.class)
                    .setMaxResults(1)
                    .getResultList()
                    .get(0);
        } catch (ArrayIndexOutOfBoundsException e) {

        }
        return cse;
    }

    // TO DO EqualID CSE9999
    public String generateCourseId() {
        Course q = findLast();
        if (q != null) {
            String id = q.getId();
            if (!id.equals("CSE9999")) {
                int number = Integer.parseInt(id.substring(3)) + 1;
                return "CSE" + String.format("%04d", number);
            }
        }
        return "CSE0000";
    }
    
    public List<Course> findByStatus(boolean status) {
        String query = "SELECT c FROM Course c WHERE c.status = :status";
        return em.createQuery(query)
                .setParameter("status", status)
                .getResultList();
    }

    public Course findAvailableCourseById(String id) {
        String findAvailableCourseById = "SELECT c FROM Course c WHERE c.id = :id AND c.status = 1";
        Course c = null;
        try {
            c = (Course) em.createQuery(findAvailableCourseById)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {

        }
        return c;
    }
    
    public void update(Course cl){
        em.merge(cl);
    }
}
