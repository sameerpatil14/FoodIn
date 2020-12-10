package com.example.foodin.HelperClass;

public class UserHelperClass {
    String name, phoneno, address, email, cartTotalAmount;

    public UserHelperClass() {
    }

    public UserHelperClass(String name, String phoneno, String address, String email, String cartTotalAmount) {
        this.name = name;
        this.phoneno = phoneno;
        this.address = address;
        this.email = email;
        this.cartTotalAmount = cartTotalAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCartTotalAmount() {
        return cartTotalAmount;
    }

    public void setCartTotalAmount(String cartTotalAmount) {
        this.cartTotalAmount = cartTotalAmount;
    }

}
