/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oswal
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {
    
    private Logger LOGGER = LoggerFactory.getLogger(UserFacade.class);

    @PersistenceContext(unitName = "ExamOnlinePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }
    
    public User findUserByEmailAndPassword(String email, String password) {
        User user = null;
        if (email != null && password != null) {
            String findUserByEmailAndPassword = "SELECT u FROM User u WHERE u.email = :email AND u.password = :password";
            try {
                user = (User) em.createQuery(findUserByEmailAndPassword)
                        .setParameter("email", email)
                        .setParameter("password", password)
                        .getSingleResult();
            } catch (NoResultException e) {
                LOGGER.error(e.getMessage());
            }
        }
        // else  email or/and password is empty
        return user;
    }
    
    public String generateUserId() {
        User u = findLast();
        if (u != null) {
            String id = u.getId();
            if (!id.equals("U999999")) {
                int number = Integer.parseInt(id.substring(1)) + 1;
                return "U" + String.format("%06d", number);
            }
        }
        return "U000001";
    }
    
    public User findLast() {
        String findLast = "SELECT u FROM User u ORDER BY u.id DESC";
        User u = null;
        try {
            u = (User) em.createQuery(findLast, User.class)
                    .setMaxResults(1)
                    .getResultList()
                    .get(0);
        } catch (ArrayIndexOutOfBoundsException e) {

        }
        return u;
    }
    
    public User checkEmail(String email){
        String findByEmail ="SELECT u from User u WHERE u.email = :email";
        User u = null;
        try {
            u = (User) em.createQuery(findByEmail)
                .setParameter("email", email)
                .getSingleResult();
        } catch (NoResultException e) {
            LOGGER.error(e.getMessage());
        }
        
        return u;
    }
    
    public User checkEmail(String email, String id){
        String findByEmailAndId ="SELECT u from User u WHERE u.email = :email AND u.id != :id";
        User u = null;
        try {
            u = (User) em.createQuery(findByEmailAndId)
                .setParameter("email", email)
                .setParameter("id", id)
                .getSingleResult();
        } catch (NoResultException e) {
            LOGGER.error(e.getMessage());
        }
        return u;
    }
}
