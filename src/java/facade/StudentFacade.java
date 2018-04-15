/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.Student;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author oswal
 */
@Stateless
public class StudentFacade extends AbstractFacade<Student> {

    @PersistenceContext(unitName = "ExamOnlinePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StudentFacade() {
        super(Student.class);
    }

    public Student findAvailableStudentById(String id) {
        String findAvailableStudentById = "SELECT s FROM Student s WHERE s.id = :id AND s.status = 1";
        Student student = null;
        try {
            student = (Student) em.createQuery(findAvailableStudentById, Student.class)
                    .setParameter("id", id)
                    .getResultList()
                    .get(0);
        } catch (ArrayIndexOutOfBoundsException e) {

        }
        return student;
    }
}
