package com.ssafy.cafe.model.dto;

public class Product {
    private Integer id;
    private String koname;
    private String name;
    private String type;
    private Integer price;
    private String img;
    private Integer salesvolume;
    
    public Product(Integer id, String koname, String name, String type, Integer price, String img, Integer salesvolume) {
        this.id = id;
        this.koname = koname;
        this.name = name;
        this.type = type;
        this.price = price;
        this.img = img;
        this.salesvolume = salesvolume;
    }
    
    public Product(String koname, String name, String type, Integer price, String img, Integer salesvolume) {
    	this.koname = koname;
        this.name = name;
        this.type = type;
        this.price = price;
        this.img = img;
        this.salesvolume = salesvolume;
    }
    public Product() {}
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	public String getKoname() {
		return koname;
	}

	public void setKoname(String koname) {
		this.koname = koname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Integer getSalesvolume() {
		return salesvolume;
	}

	public void setSalesvolume(Integer salesvolume) {
		this.salesvolume = salesvolume;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", type=" + type + ", price=" + price + ", img=" + img + "]";
	}
    
    
    
    
}
