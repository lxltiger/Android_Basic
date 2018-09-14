package com.lxl.tiger.designpattern.proxy.virtual;

import java.awt.Component;
import java.awt.Graphics;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ImageProxy implements Icon {
    volatile ImageIcon imageIcon;
    final URL url;
    boolean retrieve = false;
    Thread retrieveThread;

    public ImageProxy(URL url) {
        this.url = url;
    }

    public synchronized void setImageIcon(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    @Override
    public void paintIcon(final Component component, Graphics graphics, int x, int y) {
        if (imageIcon != null) {
            imageIcon.paintIcon(component,graphics,x,y);
        }else{
            graphics.drawString("Loading CD cover, please wait...", x+300, y+190);
            if (!retrieve ) {
                retrieve=true;
                retrieveThread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        imageIcon = new ImageIcon(url, "CD Cover");
                        setImageIcon(imageIcon);
                        component.repaint();
                    }
                });
                retrieveThread.start();
            }
        }
    }

    @Override
    public int getIconWidth() {
        return imageIcon != null ? imageIcon.getIconWidth() : 800;
    }

    @Override
    public int getIconHeight() {
        return imageIcon != null ? imageIcon.getIconHeight() : 600;
    }
}
