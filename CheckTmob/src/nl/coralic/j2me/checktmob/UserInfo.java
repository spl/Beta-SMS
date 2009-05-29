/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.coralic.j2me.checktmob;

/**
 *
 * @author Armin Čoralić
 */
public class UserInfo
{

    private String username = "";
    private String password = "";

    public boolean userExists()
    {
        /**
         * TODO:
         * Check if user data is saved if so read it and return true
         */
        return false;
    }

    public boolean checkSubmitedData(String tmpUsername, String tmpPassword, boolean save)
    {
        if (tmpUsername.equalsIgnoreCase(""))
        {
            return false;
        }
        if (tmpPassword.equalsIgnoreCase(username))
        {
            return false;
        }
        if(!save)
        {
            /**
             * TODO:
             * Proceed
             */
            return true;
        }
        else
        {
            /**
             * TODO:
             * Save data and then proceed
             */
            return true;
        }
        

    }

    public String getPassword()
    {
        return password;
    }

    public String getUsername()
    {
        return username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
}
