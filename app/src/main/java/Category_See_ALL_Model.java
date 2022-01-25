public class Category_See_ALL_Model {
    String CategoryName,Image,Description,Price,Discount;
    Category_See_ALL_Model(){

    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryname) {
        CategoryName = categoryname;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
    public String getPrice() {
        return Price;
    }

    public void getPrice(String price) {
        Price = price;
    }
    public String getDiscount() {
        return Discount;
    }

    public void getDiscount(String discount) {
        Discount = discount;
    }
    public String getDescription() {
        return Description;
    }

    public void getdescription(String description) {
        Description = description;
    }

    public Category_See_ALL_Model(String categoryname,String price ,String description,String discount,String image) {
        CategoryName = categoryname;
        Discount=discount;
        Description=description;
        Price=price;
        Image = image;
    }
}
