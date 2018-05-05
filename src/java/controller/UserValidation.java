/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.User;
import facade.UserFacade;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Arthas
 */
@Named("userValidation")
@ManagedBean
@RequestScoped
public class UserValidation implements Validator {

    @EJB
    private UserFacade userFacade = new UserFacade();

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String operation = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("createOrUpdate");
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        String selectedUserId = session.getAttribute("selectedUserId").toString();
                
        String emailString = (String) value;
        
        if (operation.equals("create")) {
            User check = this.userFacade.checkEmail(emailString);
            if (check != null) {
                FacesMessage message = new FacesMessage("Email already exists!");
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(message);
            }
        } else if (operation.equals("update")) {
             User checkById = this.userFacade.checkEmail(emailString, selectedUserId);
            if (checkById != null) {
            FacesMessage message = new FacesMessage("Email already exists!");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }

    }
}
}
