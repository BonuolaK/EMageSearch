package com.myproject.v1.viewmodel;

import java.util.ArrayList;
import java.util.List;

public class DataSetImages {
    private String category;
    private List<String> images = new ArrayList<>();

    public DataSetImages(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void addImage(String image) {
        this.images.add(image);
    }
}
