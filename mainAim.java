//KEVIN CHEN AIM TRAINER DEC.15.2023

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class mainAim implements ActionListener, KeyListener {

    JFrame frame; // main application window
    static int panW = 900, panH = 600;
    JPanel mainpanel; // JPanel for managing the main layout
    HomePanel homepanel; // home screen
    TargetPanel targetpanel;// target game screen
    CardLayout cardlayout; // card layout for managing card screen

    JComboBox ballsize; // selecting the size of the ball
    JButton startgame; // Jbutton to start the game
    Timer timer; // timer to update targer's position

    // images for application
    Image backgroundimage;
    Image gameimg;
    Image targetimg;
    Image hoverimg;

    target t; // instance of target class
    int changecount = 0;// counter for tracking changes
    int changefrequent = (int) (Math.random() * 150) + 50; // updates randomly between 50 and 100.
    static boolean isMouseOver = false; // if mouse is move target
    static double timeonball; // the total time mouse is on the ball

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new mainAim();
            }
        });
    }

    mainAim() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        mainpanel = new JPanel();
        cardlayout = new CardLayout(); // setup cardlayout
        mainpanel.setLayout(cardlayout);

        homepanel = new HomePanel(); // Setup of home panel
        homepanel.setLayout(new BoxLayout(homepanel, BoxLayout.PAGE_AXIS)); // manually setting the layout
        homepanel.setBorder(BorderFactory.createEmptyBorder(275, 100, 100, 100));

        JLabel subtitle = new JLabel("BALL TRACKER"); // Jlabel element as subtitle
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font("Arial", Font.ITALIC, 20));
        subtitle.setAlignmentX(0.5f);
        homepanel.add(subtitle);

        homepanel.addVerticalSpace(20);

        JButton startGame = new JButton("START GAME"); // Start game button
        startGame.setBackground(Color.WHITE);
        startGame.setAlignmentX(0.5f);
        homepanel.add(startGame);

        homepanel.addVerticalSpace(15);

        // JCombobox for setting ball size
        String[] sizes = { "SELECT BALL SIZE", "25", "35", "45" };
        ballsize = new JComboBox(sizes);
        Dimension size = new Dimension(140, 20);
        ballsize.setPreferredSize(size);
        ballsize.setMaximumSize(size);
        ballsize.setMinimumSize(size);
        ballsize.setAlignmentX(0.5f);
        homepanel.add(ballsize);

        homepanel.addVerticalSpace(15);
        mainpanel.add(homepanel, "homepanel"); // add hompanel to main painel

        //

        targetpanel = new TargetPanel(); // setup target panel
        targetpanel.addKeyListener(this); // setup keylistener
        targetpanel.setFocusable(true);
        targetpanel.requestFocusInWindow(); // focus needed for Keylistener

        mainpanel.add(targetpanel, "targetpanel");

        t = new target(35, "target.png", "yellowtarget.png"); // setup targer element
        t.Centertarget();

        // adding everything and displaying
        frame.add(mainpanel);
        frame.pack();
        frame.setVisible(true);

        /**
         * action listener for startgame button, switches panels, sets focus, centers
         * target for when it gets called again
         */
        startGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardlayout.show(mainpanel, "targetpanel");
                targetpanel.requestFocusInWindow();
                t.Centertarget();
            }
        });

        /**
         * actionlistener for ballsize
         * uses the setsize method within the target class to reset size of target
         */
        ballsize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                String size = (String) comboBox.getSelectedItem();

                if (size.equals("25")) {
                    t.setsize(25);
                    targetpanel.repaint();
                } else if (size.equals("45")) {
                    t.setsize(45);
                    targetpanel.repaint();
                } else if (size.equals("35")) {
                    t.setsize(35);
                    targetpanel.repaint();
                }
            }
        });

        // timer setup
        timer = new Timer(15, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                t.RandomTrack();
                targetpanel.repaint();
            }
        });
        timer.start();

        // mousemotion setup
        targetpanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                isMouseOver = targetpanel.isMouseOverBall(e.getX(), e.getY());
                targetpanel.repaint();
                if (!timer.isRunning()) {
                    timer.restart();
                }
            }
        });

    }

    /**
     * unused action performed
     */
    public void actionPerformed(ActionEvent e) {

    }

    // Homepanel class - contains all the attributes and methods of Homepanel
    class HomePanel extends JPanel {
        int panW = 900, panH = 600;

        HomePanel() {
            this.setPreferredSize(new Dimension(panW, panH));
            loadImage();
        }

        /**
         * to add spacing between elements when in boxlayout
         * 
         * @param height int value that is specified to create the vertical height of
         *               the spacing
         */
        public void addVerticalSpace(int height) {
            add(Box.createVerticalStrut(height));
        }

        /**
         * loading in new image
         * loading in first background image
         */
        void loadImage() {
            try {
                ImageIcon imageIcon = new ImageIcon(
                        "blurredbackground.jpg");
                backgroundimage = imageIcon.getImage();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }

        /**
         * normal paint component
         */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (backgroundimage != null) {
                g2.drawImage(backgroundimage, 0, 0, getWidth(), getHeight(), this);
            }

            g2.setFont(new Font("Arial", Font.ITALIC, 65));
            g2.setColor(Color.WHITE);

            String title = "AIM TRAINER";
            int x = 250;
            int y = panH / 2 - 30;

            g2.drawString(title, x, y);
        }
    }

    // Target panel class, contains all targetpanel attributes and targetpanel
    // methods
    class TargetPanel extends JPanel {
        int panW = 900, panH = 400;
        double mousein;
        double mouseout;

        /**
         * loading in new image
         * loading in second background image
         */
        void loadImage() {
            try {
                ImageIcon imageIcon = new ImageIcon(
                        "background.jpeg");
                gameimg = imageIcon.getImage();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }

        }

        // main target panel
        TargetPanel() {
            this.setPreferredSize(new Dimension(panW, panH));
            loadImage();
        }

        /**
         * isMouseOverBall - detects if mose is hovering over the ball
         * mousein and mouseout are timers that help calculate amount of time cursor is
         * on the ball
         * 
         * @param mouseX mouse's x position
         * @param mouseY mouse's y position
         * @return isMouseOver the state of the mouse - if over the ball
         */
        boolean isMouseOverBall(int mouseX, int mouseY) {
            boolean mouseOn = mouseX >= t.x && mouseX <= t.x + t.r && mouseY >= t.y && mouseY <= t.y + t.r;
            if (mouseOn) { // if cursor on mouse
                if (!isMouseOver) { // if ismouseover is false, means that its a new time the cursor enter target
                    mousein = System.currentTimeMillis(); // get time
                }
            } else { // if mouse not on target
                if (isMouseOver) { // check if mouse used to be on target, if so, means mouse just left target
                    mouseout = System.currentTimeMillis();
                    timeonball += mouseout - mousein; // calculate time mouse was on target
                }
            }

            isMouseOver = mouseOn; // update the global boolean
            return isMouseOver;
        }

        /**
         * resets the value for when the program runs again
         */
        void reset() {
            timeonball = 0;
            targetpanel.mousein = 0;
            targetpanel.mouseout = 0;
        }

        // pain component for TargetPanel
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (backgroundimage != null) {
                g2.drawImage(gameimg, 0, 0, getWidth(), getHeight(), this);
            }

            if (isMouseOver) {
                g2.drawImage(hoverimg, t.x, t.y, t.r, t.r, this);
            } else {
                g2.drawImage(targetimg, t.x, t.y, t.r, t.r, this);
            }

        }

    }

    /**
     * detects if H or h is clicked, then calculates the percentage of time that the
     * cursor was on ball
     * Also returns user back to home screen if need to readjust settings.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'H' || e.getKeyChar() == 'h') {
            timer.stop();
            double elapsedtime = (System.currentTimeMillis() - targetpanel.mousein) *
                    1000;
            double percentage = (timeonball / elapsedtime) * 100;
            String percentageFormat = String.format("%.2f%%", percentage);
            JOptionPane.showMessageDialog(frame, "YOU ARE: " + percentageFormat + " ON TARGET");
            cardlayout.show(mainpanel, "homepanel");
            targetpanel.requestFocusInWindow();

            timeonball = 0;
            targetpanel.mousein = 0;
            targetpanel.mouseout = 0;

            targetpanel.reset();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    // target class - the methods and attributes for the target
    class target extends Rectangle {
        int x, y;
        double xx, yy;

        int r;
        Image targets;
        int vx;
        int vy;

        /**
         * initializes a target with attributes it needs
         * 
         * @param size size of target
         * @param img  default image
         * @param img2 image when mouse hover
         */
        target(int size, String img, String img2) {
            ImageIcon imageIcon = new ImageIcon(img);
            targetimg = imageIcon.getImage();
            ImageIcon imageIcon2 = new ImageIcon(img2);
            hoverimg = imageIcon2.getImage();
            r = size;
            x = panW / 2;
            y = panH / 2;
        }

        /**
         * Resets the size of the target
         * 
         * @param size size in pixels of the target
         */
        void setsize(int size) {
            r = size;
        }

        /**
         * resets the location of the target
         * for when start game is pressed a second time
         */
        public void Centertarget() {
            xx = panW / 2 - 17;
            yy = panH / 2;

            x = (int) xx;
            y = (int) yy;
        }

        /**
         * Target's motion
         * -sets collision
         * -moves the ball
         * -uses RandomSpeed to change direction/speed
         */
        public void RandomTrack() {
            xx += vx;
            yy += vy;

            // check collisions on sides
            if ((int) xx + r > 900) {
                xx = (double) (panW - r);
                vx *= -1;
            } else if (xx < 0.0) {
                xx = 0.0;
                vx = Math.abs(vx);
                RandomSpeed();
            }
            // check collision with floor and ceiling
            if ((int) yy + r > 600) {
                yy = (double) (panH - r);
                vy *= -1;
            } else if (yy < 0.0) {
                yy = 0.0;
                vy = Math.abs(vy);
                RandomSpeed();
            }

            // set when to use RandomSpeed
            changecount++;
            if (changecount > changefrequent) {
                RandomSpeed();
                changecount = 0;
            }

            x = (int) (xx);
            y = (int) (yy);
        }

        /**
         * sets vx and vy to be random ints from -4 to 4 to vary speed and direction
         */
        public void RandomSpeed() {
            vx = (int) (Math.random() * 8) - 3;
            vy = (int) (Math.random() * 8) - 3;
        }
    }

}
