/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.ExamStudent;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author oswal
 */
@Stateless
public class ExamStudentFacade extends AbstractFacade<ExamStudent> {

    @PersistenceContext(unitName = "ExamOnlinePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ExamStudentFacade() {
        super(ExamStudent.class);
    }
    
}
