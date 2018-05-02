/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Role;
import entity.User;
import facade.RoleFacade;
import facade.UserFacade;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Arthas
 */
@Named("userBean")
@SessionScoped
public class UserBean implements Serializable{
    @EJB
    private UserFacade userFacade;
    
    @EJB
    private RoleFacade roleFacade;

     private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UserBean.class);
     private static final Logger LOG = Logger.getLogger(UserBean.class.getName());

    
    private User user;
    private String roleId;
    private String name;
    private String email;
    private String password;
    private boolean status;
    private String id;

    public List<Role> listRoles(){
        return roleFacade.findAll();
    }
    
    public List<User> findAll(){
        return this.userFacade.findAll();
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
    

    public String removeUser(User user) {
        
        userFacade.remove(user);
        return "user-list?faces-redirect=true";
    }
    
    public Role getRoleFromId(String id){
        return roleFacade.find(id);
    }
    
    public void prepUser(){
            user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setStatus(status);
            user.setRoleId(getRoleFromId(roleId));
    }
    
    public String createUser(){
        try {
            prepUser();
            user.setId(userFacade.generateUserId());

        userFacade.create(user);

        LOGGER.info("Created: " + user.getId() + ": " + user.getName());
        
        user = new User();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return "user-list?faces-redirect=true";
        }
        
        return "user-list?faces-redirect=true";
    }
    
    public String updateUser(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roleId = user.getRoleId().getId();
        this.status = user.getStatus();
        return "user-details?faces-redirect=true";
    }
    
    public String updateUser(){
        prepUser();
        user.setId(id);    
        this.userFacade.edit(user);
        user = new User();
        return "user-list?faces-redirect=true";
    }
    
    
    

    /**
     * Creates a new instance of UserBean
     */
    public UserBean() {
    }
    
}
