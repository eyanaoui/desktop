package utils;



import models.User;

public class SessionManager {
    private  User user;
    private static SessionManager instance;
    private SessionManager(User user){
        this.user=user;
    }
    public static SessionManager startSession(User user){

        if(instance==null){
            instance=new SessionManager(user);
        }
        return instance;
    }
    public static SessionManager getSession(){
        return instance;
    }
    public static  void clearSession(){
        instance =null;
    }

    public User getUser() {
        return user;
    }

    public  void setUser(User user) {
        this.user = user;
    }
}

