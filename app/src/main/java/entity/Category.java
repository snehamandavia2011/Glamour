package entity;

/**
 * Created by SAI on 8/11/2017.
 */

public class Category {
    int id;
    String categoryName, category_for;

    public Category(int id, String categoryName, String category_for) {
        this.id = id;
        this.categoryName = categoryName;
        this.category_for = category_for;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategory_for() {
        return category_for;
    }

    public void setCategory_for(String category_for) {
        this.category_for = category_for;
    }
}
