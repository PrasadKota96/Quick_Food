package uk.ac.tees.aad.w9316578.Model;

import java.io.Serializable;

public class OrderInfoForRider implements Serializable {
    String dateOrder,orderID,status,totalAmount,userID,OrderInfoID,riderID;

    public OrderInfoForRider() {
    }

    public OrderInfoForRider(String dateOrder, String orderID, String status, String totalAmount, String userID, String orderInfoID, String riderID) {
        this.dateOrder = dateOrder;
        this.orderID = orderID;
        this.status = status;
        this.totalAmount = totalAmount;
        this.userID = userID;
        OrderInfoID = orderInfoID;
        this.riderID = riderID;
    }

    public String getRiderID() {
        return riderID;
    }

    public void setRiderID(String riderID) {
        this.riderID = riderID;
    }

    public String getOrderInfoID() {
        return OrderInfoID;
    }

    public void setOrderInfoID(String orderInfoID) {
        OrderInfoID = orderInfoID;
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
