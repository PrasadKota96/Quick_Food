package uk.ac.tees.aad.w9316578.Model;

import java.io.Serializable;

public class OrderInfo implements Serializable {
    String dateOrder,orderID,status,totalAmount,userID;

    public OrderInfo() {
    }

    public OrderInfo(String dateOrder, String orderID, String status, String totalAmount, String userID) {
        this.dateOrder = dateOrder;
        this.orderID = orderID;
        this.status = status;
        this.totalAmount = totalAmount;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
