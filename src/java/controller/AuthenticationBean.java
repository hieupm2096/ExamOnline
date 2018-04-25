/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.User;
import facade.UserFacade;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oswal
 */
@Named(value = "authenticationBean")
@SessionScoped
public class AuthenticationBean implements Serializable {

    @EJB
    private UserFacade userFacade;

    // logger
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationBean.class);

    public static final String HOME_PAGE_REDIRECT = "/app/home?faces-redirect=true";
    public static final String LOGIN_PAGE_REDIRECT = "/login?faces-redirect=true";

    private String email;
    private String password;

    private User loginUser;

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

    public User getLoginUser() {
        return loginUser;
    }

    public AuthenticationBean() {
    }

    public String login() {
        loginUser = userFacade.findUserByEmailAndPassword(email, password);
        
        if (loginUser != null) {
            LOGGER.info("login successfully for '{}'", loginUser.getName());
            return HOME_PAGE_REDIRECT;
        } else {
            LOGGER.info("login failed");
            return null;
        }
    }
    
    public String logout() {
        String identifier = loginUser.getName();
        
        // invalidate session
        LOGGER.debug("invalidating session for ", identifier);
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        
        LOGGER.info("logout successfully for ", identifier);
        return LOGIN_PAGE_REDIRECT;
    }
    
    public boolean isLoggedIn() {
        return loginUser != null;
    }
    
    public String isLoggedInForwardHome() {
        if (isLoggedIn()) {
            return HOME_PAGE_REDIRECT;
        }
        return null;
    }
}
