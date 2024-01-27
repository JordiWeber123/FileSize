package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainBar {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(750, 750);
        window.setVisible(true);
        window.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();

        mainPanel.setBackground(new Color(0x808080));

        mainPanel.setPreferredSize(new Dimension(250, 250));
        window.add(mainPanel, BorderLayout.CENTER);

        BarButton bar = new BarButton();
        bar.setBounds(10, window.getHeight() / 2, 470, 30);

        for (int i = 0; i < (int) (Math.random() * 10 + 2); i++) {
            bar.addSize((int) (Math.random() * 90 + 10));
        }

        bar.display();
        bar.setBorder(BorderFactory.createLineBorder(new Color(0)));

        mainPanel.add(bar);

    }
}
