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

    public Student findLast() {
        String findLast = "SELECT s FROM Student s ORDER BY s.id DESC";
        Student s = null;
        try {
            s = (Student) em.createQuery(findLast, Student.class)
                    .setMaxResults(1)
                    .getResultList()
                    .get(0);
        } catch (ArrayIndexOutOfBoundsException e) {

        }
        return s;
    }

    public String generateStudentId() {
        Student s = findLast();
        if (s != null) {
            String id = s.getId();
            if (!id.equals("S999999")) {
                int number = Integer.parseInt(id.substring(1)) + 1;
                return "S" + String.format("%06d", number);
            }
        }
        return "S000001";
    }
}
