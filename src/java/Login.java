
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author stanchev
 */
@Named(value = "login")
@SessionScoped
@ManagedBean
public class Login implements Serializable {

    private String login;
    private String password;
    private UIInput loginUI;
    private DBConnect dbConnect = new DBConnect();
    private String setDestination;

    public UIInput getLoginUI() {
        return loginUI;
    }

    public void setLoginUI(UIInput loginUI) {
        this.loginUI = loginUI;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    

    public void validate(FacesContext context, UIComponent component, Object value)
            throws ValidatorException, SQLException {
        login = loginUI.getLocalValue().toString();
        password = value.toString();
        setDestination = "";
        Connection con = dbConnect.getConnection();

        if (con == null) {
           throw new SQLException("Can't get database connection");
        }

        PreparedStatement ps
                    = con.prepareStatement(
                            "select title from Login where username=? and password=?");
        ps.setString(1, login);
        ps.setString(2, password);
        ResultSet result = ps.executeQuery();
        if (!result.next()) {
            return;
        }
        String getTitle = result.getString(1);
        result.close();
        
        con.close();
        
        switch (getTitle) {
            case "admin":
                setDestination = getTitle;
                break;
            case "employee":
                setDestination = getTitle;
                break;
            case "customer":
                setDestination = getTitle;
                break;
            default:
                FacesMessage errorMessage = new FacesMessage("Wrong login/password");
                throw new ValidatorException(errorMessage);
        }
                
    }

    public String go() {
      //  Util.invalidateUserSession();
        return setDestination;
    }

}
