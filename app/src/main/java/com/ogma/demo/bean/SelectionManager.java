package com.ogma.demo.bean;

/**
 * Created by alokdas on 28/04/16.
 */
public class SelectionManager {
    private String selectedId;
    private boolean isSelected;

    public SelectionManager(String selectedId, boolean isSelected) {
        this.selectedId = selectedId;
        this.isSelected = isSelected;
    }

    public String getSelectedId() {
        return selectedId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
