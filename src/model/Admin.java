package model;

public class Admin extends Person {
    private String adminId;
    private String position;

    public Admin(String name, String id, String email, String phone, String adminId, String position) {
        super(name, id, email, phone);
        this.adminId = adminId;
        this.position = position;
    }

    // Getters
    public String getAdminId() {
        return adminId;
    }

    public String getPosition() {
        return position;
    }

    // Setters
    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String getDetails() {
        return "Admin{" +
                "name='" + getName() + '\'' +
                ", adminId='" + adminId + '\'' +
                ", position='" + position + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return adminId + "," + getName() + "," + position + "," + getEmail();
    }
}
