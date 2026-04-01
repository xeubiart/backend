package xeubiart.com.domain.user.model;

public enum UserRole {
    USER("user"),
    ADM("adm");

    private final String type;

    UserRole(String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }

}
