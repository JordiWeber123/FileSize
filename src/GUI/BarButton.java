package GUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Flow;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BarButton extends JComponent {

    List<Integer> sizes;

    public BarButton() {
        sizes = new ArrayList<>();
    }

    public void addSize(int value) {
        sizes.add(value);
    }

    public void display() {
        Collections.sort(sizes);
        Collections.reverse(sizes);
        long total = 0;
        for (int size : sizes) {
            total += size;
        }
        System.out.println("total: " + total);
        double totalWidth = (double) this.getWidth();
        System.out.println("Bar width = " + totalWidth);
        int height = this.getHeight();
        int accumulatedWidth = 0;
        int debugWidth = 0;
        for (int size : sizes) {
            JButton current = new JButton();
            int width = (int) Math.round(size * totalWidth / (double) total);
            debugWidth += width;
            current.setBounds(accumulatedWidth, 0, width, height);
            current.setBackground(new Color((int) (Math.random() * 0xFFFFFF)));
            current.setBorder(null);
            accumulatedWidth += width;
            this.add(current);
        }
        System.out.println("Button widths: " + debugWidth);
    }

}