package agentes;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

/**
 *
 * @author macario
 */
public class Escenario extends JFrame {

    private JLabel[][] tablero;
    private int[][] matrix;
    private final int dim;

    private ImageIcon robot1;
    private ImageIcon robot2;
    private ImageIcon obstacleIcon;
    private ImageIcon sampleIcon;
    private ImageIcon actualIcon;
    private ImageIcon motherIcon;

    private Agente wallE;
    private Agente eva;

    private final BackGroundPanel fondo = new BackGroundPanel(new ImageIcon("imagenes/surface.jpg"));

    private final JMenu settings = new JMenu("Settigs");
    private final JRadioButtonMenuItem obstacle = new JRadioButtonMenuItem("Obstacle");
    private final JRadioButtonMenuItem sample = new JRadioButtonMenuItem("Sample");
    private final JRadioButtonMenuItem motherShip = new JRadioButtonMenuItem("MotherShip");
    private final int figureSize;
    private int whichElement;
    
    MotherShip nave;

    public Escenario() {
        figureSize = 40;
        dim = 15;
        nave = new MotherShip();
        
        this.setContentPane(fondo);
        this.setTitle("Agentes");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setBounds(figureSize, figureSize, dim * figureSize + 35, dim * figureSize + 85);
        initComponents();
    }

    private void initComponents() {
        ButtonGroup settingsOptions = new ButtonGroup();
        settingsOptions.add(sample);// 1
        settingsOptions.add(obstacle);//2      
        settingsOptions.add(motherShip);//3

        JMenuBar barraMenus = new JMenuBar();
        JMenu file = new JMenu("File");

        JMenuItem run = new JMenuItem("Run");
        JMenuItem exit = new JMenuItem("Exit");

        this.setJMenuBar(barraMenus);
        barraMenus.add(file);
        barraMenus.add(settings);
        file.add(run);
        file.add(exit);
        settings.add(motherShip);
        settings.add(obstacle);
        settings.add(sample);

        robot1 = new ImageIcon("imagenes/wall-e.png");
        robot1 = new ImageIcon(robot1.getImage().getScaledInstance(figureSize, figureSize, java.awt.Image.SCALE_SMOOTH));

        robot2 = new ImageIcon("imagenes/eva.png");
        robot2 = new ImageIcon(robot2.getImage().getScaledInstance(figureSize, figureSize, java.awt.Image.SCALE_SMOOTH));

        obstacleIcon = new ImageIcon("imagenes/brick.png");
        obstacleIcon = new ImageIcon(obstacleIcon.getImage().getScaledInstance(figureSize, figureSize, java.awt.Image.SCALE_SMOOTH));

        sampleIcon = new ImageIcon("imagenes/sample.png");
        sampleIcon = new ImageIcon(sampleIcon.getImage().getScaledInstance(figureSize, figureSize, java.awt.Image.SCALE_SMOOTH));

        motherIcon = new ImageIcon("imagenes/mothership.png");
        motherIcon = new ImageIcon(motherIcon.getImage().getScaledInstance(figureSize, figureSize, java.awt.Image.SCALE_SMOOTH));

        this.setLayout(null);
        formaPlano();

        exit.addActionListener(evt -> gestionaSalir(evt));
        run.addActionListener(evt -> gestionaRun(evt));
        sample.addItemListener(evt -> gestionaSample(evt));
        obstacle.addItemListener(evt -> gestionaObstacle(evt));
        motherShip.addItemListener(evt -> gestionaMotherShip(evt));

        class MyWindowAdapter extends WindowAdapter {

            @Override
            public void windowClosing(WindowEvent eventObject) {
                goodBye();
            }
        }
        addWindowListener(new MyWindowAdapter());

        // Crea 2 agentes
        wallE = new Agente("Wall-E", robot1, matrix, tablero, nave, robot1, sampleIcon);
        eva = new Agente("Eva", robot2, matrix, tablero, nave, robot2, sampleIcon);

    }

    private void gestionaSalir(ActionEvent eventObject) {
        goodBye();
    }

    private void goodBye() {
        int respuesta = JOptionPane.showConfirmDialog(rootPane, "Desea salir?", "Aviso", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void formaPlano() {
        tablero = new JLabel[dim][dim];
        matrix = new int[dim][dim];

        int i, j;

        for (i = 0; i < dim; i++) {
            for (j = 0; j < dim; j++) {
                matrix[i][j] = 0;
                tablero[i][j] = new JLabel();
                tablero[i][j].setBounds(j * figureSize + 10, i * figureSize + 10, figureSize, figureSize);
                tablero[i][j].setBorder(BorderFactory.createDashedBorder(Color.white));
                tablero[i][j].setOpaque(false);
                this.add(tablero[i][j]);
                
                class mouseList extends MouseAdapter{
                    int i, j; 
                    int [][]mtx;
                    mouseList(int i, int j, int [][]mtx){
                        this.i = i; this.j = j;
                        this.mtx = mtx;
                    }
                    
                    @Override
                    public void mousePressed(MouseEvent e) {
                        insertMyElement(e);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        insertMyElement(e);
                    }
                    
                    private void insertMyElement(MouseEvent e){
                        if(whichElement == 3 && nave.isSet() == 1){
                            return;
                        }
                        
                        insertaObjeto(e);
                        mtx[i][j] = whichElement;
                        if(whichElement == 3){                            
                            nave = new MotherShip(i,j, motherIcon);
                            wallE.setNave(nave);
                            eva.setNave(nave);
                        }
                        
                    }
                }

                tablero[i][j].addMouseListener(new mouseList(i,j, matrix));

            }
        }
    }

   

    private void gestionaSample(ItemEvent eventObject) {
        JRadioButtonMenuItem opt = (JRadioButtonMenuItem) eventObject.getSource();
        if (opt.isSelected()) {
            actualIcon = sampleIcon;
            whichElement = 1;
        } else {
            actualIcon = null;
        }
    }
    
    private void gestionaObstacle(ItemEvent eventObject) {
        JRadioButtonMenuItem opt = (JRadioButtonMenuItem) eventObject.getSource();
        if (opt.isSelected()) {
            actualIcon = obstacleIcon;
            whichElement = 2;
        } else {
            actualIcon = null;
        }
    }

    private void gestionaMotherShip(ItemEvent eventObject) {
        JRadioButtonMenuItem opt = (JRadioButtonMenuItem) eventObject.getSource();
        if (opt.isSelected()) {
            actualIcon = motherIcon;
            whichElement = 3;
            
        } else {
            actualIcon = null;
        }
    }

    private void gestionaRun(ActionEvent eventObject) {
        if (!wallE.isAlive()) {
            wallE.start();
        }
        if (!eva.isAlive()) {
            eva.start();
        }
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix.length; j++){
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println("");
        }
        settings.setEnabled(false);
    }

    public void insertaObjeto(MouseEvent e) {
        JLabel casilla = (JLabel) e.getSource();
        if (actualIcon != null) {
            casilla.setIcon(actualIcon);
        }
    }

}
