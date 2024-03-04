/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agentes;

import javax.swing.ImageIcon;

/**
 *
 * @author tony
 */
public class MotherShip {
    private int i,j;
    private ImageIcon icon;
    MotherShip(){
        i = -1;
        j = -1;
        icon = null;
    }
    MotherShip(int i, int j, ImageIcon icon){
        this.i = i; this.j = j;
        this.icon = icon;
    }
    
    public int[] getCoordinates(){
        return new int[]{i,j};
    }
    public int isSet(){
        if(i == -1 || j == -1) return 0;
        return 1;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }
}
