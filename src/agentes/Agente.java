package agentes;

import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author macario
 */
public class Agente extends Thread {

    private final String nombre;
    private int i;
    private int j;
    private final ImageIcon icon;
    private final ImageIcon iconSample;
    private final ImageIcon sampleIcon;

    private final int[][] matrix;
    private final JLabel tablero[][];
    private final int []left = {0,-1};
    private final int []right = {0,1};
    private final int []up = {-1,0};
    private final int []down = {1,0};
    private int lastI, lastJ;

    private JLabel casillaAnterior;
    Random aleatorio = new Random(System.currentTimeMillis());
    private MotherShip nave;
    
    private int hasSample;
    

    public Agente(String nombre, ImageIcon icon, int[][] matrix, JLabel tablero[][], MotherShip nave, ImageIcon iconSample, ImageIcon sampleIcon) {
        this.nombre = nombre;
        this.icon = icon;
        this.matrix = matrix;
        this.tablero = tablero;
        this.hasSample = 0;
        this.nave = nave;
        this.iconSample = iconSample;
        this.sampleIcon = sampleIcon;

        this.i = aleatorio.nextInt(matrix.length);
        this.j = aleatorio.nextInt(matrix.length);
        tablero[i][j].setIcon(icon);
    }

    @Override
    public void run() {

        int dirRow = 1;
        int dirCol = 1;
        int[][] moves = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        int size = matrix.length;

        while (true) {
            lastI = i; lastJ = j;
            casillaAnterior = tablero[i][j];
             int nextI = i, nextJ = j;
            if(hasSample == 0){
                int[] nextMove = moves[aleatorio.nextInt(4)];
                nextI = i + nextMove[0];
                nextJ = j + nextMove[1];

                System.out.println("(" + nextMove[0] + ", " + nextMove[1] + ")");

                while (nextI >= size || nextI < 0 || nextJ >= size || nextJ < 0 || matrix[nextI][nextJ] == 2) {
                    nextMove = moves[aleatorio.nextInt(4)];
                    nextI = i + nextMove[0];
                    nextJ = j + nextMove[1];
                    System.out.println("ciclo");
                }
                if(matrix[nextI][nextJ] == 1){
                    hasSample = 1;
                    matrix[nextI][nextJ] = 0;
                }
            }else{
                if(getDistance() == 0){
                    hasSample = 0;
                }else{
                    int dir = getDirection();
                    int [][][]possibleMoves = {
                        {down, left}, {right, down}, {right, up}, {left, up},
                        {right, right}, {left, left}, {up, up}, {down, down}
                    };

                    int myMoves[][] = possibleMoves[dir];
                    int currMove = 0;
                    nextI = i + myMoves[currMove][0];
                    nextJ = j + myMoves[currMove][1];

                    while (nextI >= size || nextI < 0 || nextJ >= size || nextJ < 0 || matrix[nextI][nextJ] == 2) {
                        currMove ^= 1;
                        nextI = i + myMoves[currMove][0];
                        nextJ = j + myMoves[currMove][1];
                    } 
                }
            }
            i = nextI;
            j = nextJ;
            
            actualizarPosicion();
            
            try {
                sleep(100 + aleatorio.nextInt(100));
            } catch (InterruptedException ex) {
                ex.printStackTrace(System.out);
            }
        }

    }

    public synchronized void actualizarPosicion() {
        casillaAnterior.setIcon(null); // Elimina su figura de la casilla anterior
        if(matrix[lastI][lastJ] == 1) casillaAnterior.setIcon(sampleIcon);
        ImageIcon currIcon;
        if(hasSample == 1) currIcon = iconSample;
        else currIcon = iconSample;
        
        tablero[i][j].setIcon(currIcon); // Pone su figura en la nueva casilla
        
        if(nave.isSet() == 1) tablero[nave.getI()][nave.getJ()].setIcon(nave.getIcon());
        System.out.println(nombre + " in -> Row: " + i + " Col:" + j);
    }

    public void setNave(MotherShip nave) {
        this.nave = nave;
    }
    
    public int getDistance(){
        int dis;
        dis = Math.abs(i - nave.getI()) + Math.abs(j - nave.getJ());
        return dis;
    }
    
    public int getDirection(){
        int x = nave.getI(), y = nave.getJ();
        int dir = 1;
        if(i <  x  && j >  y) dir = 0;
        if(i <  x  && j <  y) dir = 1;
        if(i >  x  && j <  y) dir = 2;
        if(i >  x  && j >  y) dir = 3;
        if(i == x  && j <  y) dir = 4;
        if(i == x  && j >  y) dir = 5;
        if(i >  x  && j == y) dir = 6;
        if(i <  x  && j == y) dir = 7;
        System.out.println("(x,y) = " + "("+ x + ", " + y + ")\t" + "(i,j) = (" + i +", " + j + ") => \t dir = " + dir );
        return dir;
    }
    
}
