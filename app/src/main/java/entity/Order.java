package entity;

/**
 * Created by SAI on 8/23/2017.
 */

public class Order {
    String basket_id, is_order_place, is_order_place_successfully, user_id, createdOn, placedOn, noOfItemInOrder;

    public Order(String basket_id, String is_order_place, String is_order_place_successfully, String user_id, String createdOn, String placedOn, String noOfItemInOrder) {
        this.basket_id = basket_id;
        this.is_order_place = is_order_place;
        this.is_order_place_successfully = is_order_place_successfully;
        this.user_id = user_id;
        this.createdOn = createdOn;
        this.placedOn = placedOn;
        this.noOfItemInOrder = noOfItemInOrder;
    }

    public String getBasket_id() {
        return basket_id;
    }

    public void setBasket_id(String basket_id) {
        this.basket_id = basket_id;
    }

    public String getIs_order_place() {
        return is_order_place;
    }

    public void setIs_order_place(String is_order_place) {
        this.is_order_place = is_order_place;
    }

    public String getIs_order_place_successfully() {
        return is_order_place_successfully;
    }

    public void setIs_order_place_successfully(String is_order_place_successfully) {
        this.is_order_place_successfully = is_order_place_successfully;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getPlacedOn() {
        return placedOn;
    }

    public void setPlacedOn(String placedOn) {
        this.placedOn = placedOn;
    }

    public String getNoOfItemInOrder() {
        return noOfItemInOrder;
    }

    public void setNoOfItemInOrder(String noOfItemInOrder) {
        this.noOfItemInOrder = noOfItemInOrder;
    }
}
