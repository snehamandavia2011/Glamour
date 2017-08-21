package entity;

/**
 * Created by SAI on 8/21/2017.
 */

public class BasketItem {
    String basket_id, product_id, name, desc, sizeId, size, quantity, price, image;

    public BasketItem(String basket_id, String product_id, String name, String desc, String sizeId, String size, String quantity, String price, String image) {
        this.basket_id = basket_id;
        this.product_id = product_id;
        this.name = name;
        this.desc = desc;
        this.sizeId = sizeId;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
        this.image = image;
    }

    public String getBasket_id() {
        return basket_id;
    }

    public void setBasket_id(String basket_id) {
        this.basket_id = basket_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
