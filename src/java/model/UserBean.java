/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Role;
import entity.User;
import facade.RoleFacade;
import facade.UserFacade;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Arthas
 */
@Named("userBean")
@RequestScoped
public class UserBean implements Serializable {

    @EJB
    private UserFacade userFacade;

    @EJB
    private RoleFacade roleFacade;

    @Inject
    private AuthenticationBean authenticationBean;
    
    
     private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UserBean.class);
     

    private User user;
    private String roleId;
    private String name;
    private String email;
    private String password;
    private boolean status;
    private String id;

    public List<Role> listRoles() {
        return roleFacade.findAll();
    }

    public List<User> findAll() {
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

    public Role getRoleFromId(String id) {
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


    public String createUser() {
        try {
            prepUser();
            
            user.setId(userFacade.generateUserId());
            user.setStatus(true);

            userFacade.create(user);

            LOGGER.info("Created: " + user.getId() + ": " + user.getName());

        } catch (Exception e) {
            LOGGER.error(e.getMessage());

        } finally {
            user = new User();
            return "user-list?faces-redirect=true";
        }

    }

    public String prepareUserDetails(User u) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.setAttribute("selectedUserId", u.getId());
        session.setAttribute("createOrUpdate", "update");
        return "user-details?faces-redirect=true";
    }
    

    public String updateUser() {
        prepUser();
        user.setId(id);
        try {
            this.userFacade.edit(user);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return "user-list?faces-redirect=true";
    }

    public void getSelectedUser() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        String selected = session.getAttribute("selectedUserId").toString();
        if (selected != null) {
            User selectedUser = userFacade.find(selected);
            id = selectedUser.getId();
            name = selectedUser.getName();
            email = selectedUser.getEmail();
            password = selectedUser.getPassword();
            roleId = selectedUser.getRoleId().getId();
            status = selectedUser.getStatus();
        }
    }

    /**
     * Creates a new instance of UserBean
     */
    public UserBean() {
    }

}
