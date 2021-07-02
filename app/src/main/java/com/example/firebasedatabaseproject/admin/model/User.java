package com.example.firebasedatabaseproject.admin.model;

public class User {

    String Email;
    String Password;
    String UserName;
    String MobileNumber;
    String UserUID;
    String Department;

    public User() {
        //Empty Constructor For Firebase
    }

    public User(String email, String password, String userName, String mobileNumber, String userUID, String department) {
        Email = email;
        Password = password;
        UserName = userName;
        MobileNumber = mobileNumber;
        UserUID = userUID;
        Department = department;
    }

    //Getters and Setters
    public String getEmail() { return Email; }

    public void setEmail(String email) { Email = email; }

    public String getPassword() { return Password; }

    public void setPassword(String password) { Password = password; }

    public String getUserName() { return UserName; }

    public void setUserName(String userName) { UserName = userName; }

    public String getMobileNumber() { return MobileNumber; }

    public void setMobileNumber(String mobileNumber) { MobileNumber = mobileNumber; }

    public String getUserUID() { return UserUID; }

    public void setUserUID(String userUID) { UserUID = userUID; }

    public String getDepartment() { return Department; }

    public void setDepartment(String department) { Department = department; }


    /*@Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return ((User) obj).getDepartment() == getDepartment();
        }
        return false;
    }
    @Override
    public int hashCode() {
        return (this.getDepartment().hashCode());
    }*/

   /* @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if(obj instanceof User)
        {
            User temp = (User) obj;
            if(this.getUserUID().equals(temp.getUserUID()) && this.getMobileNumber().equals(temp.getMobileNumber()) && this.getDepartment().equals(temp.getDepartment()) && this.getPassword().equals(temp.getPassword()) && this.getUserName().equals(temp.getUserName()) && this.getEmail().equals(temp.getEmail()))
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub

        return (this.getUserUID().hashCode() + this.getMobileNumber().hashCode() + this.getDepartment().hashCode() + this.getPassword().hashCode() + this.getUserName().hashCode() + this.getEmail().hashCode());
    }*/
}
