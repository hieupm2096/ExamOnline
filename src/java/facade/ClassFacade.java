/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.Class;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author oswal
 */
@Stateless
public class ClassFacade extends AbstractFacade<Class> {

    @PersistenceContext(unitName = "ExamOnlinePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClassFacade() {
        super(Class.class);
    }
    
    public entity.Class findLast() {
        String findLast = "SELECT q FROM Class q ORDER BY q.id DESC";
        entity.Class cse = null;
        try {
            cse = (entity.Class) em.createQuery(findLast, entity.Class.class)
                    .setMaxResults(1)
                    .getResultList()
                    .get(0);
        } catch (ArrayIndexOutOfBoundsException e) {

        }
        return cse;
    }
    
    public void update(entity.Class cl){
        em.merge(cl);
    }

    public String generateClassId() {
        entity.Class q = findLast();
        if (q != null) {
            String id = q.getId();
            if (!id.equals("CL99999")) {
                int number = Integer.parseInt(id.substring(2)) + 1;
                return "CL" + String.format("%05d", number);
            }
        }
        return "CL00001";
    }
}
