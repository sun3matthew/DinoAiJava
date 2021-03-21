import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class DinoAi
{
 JFrame frame;
 AiProgram canvas;
 public static void main (String[] args)
 {
  DinoAi kt = new DinoAi();
  kt.Run();
 } // end main

 public void Run() {
  frame = new JFrame("DinoAi");
  frame.setSize(1250, 650);
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  frame.setResizable(true);
  canvas = new AiProgram();
  frame.getContentPane().add(canvas);

  frame.setVisible(true);
 }
}

class AiProgram extends JPanel implements KeyListener{

 private Timer balltimer;

 int gameSpeed;
 int obsCounter;
 int nextCactRandom;
 int gameLen;
 final int screenWidth = 1200;
 final int screenHight = 600;
 final int playerX = 40;
 ArrayList<Cactus> cacti = new ArrayList<Cactus>();

 int ff = 10;
 boolean doFF = true;

 Population pop;

 public AiProgram() {
  nextCactRandom = ((int)(Math.random()*100));
  obsCounter = 0;
  gameSpeed = 6;
  pop = new Population(500);
  BallMover ballmover = new BallMover();
  balltimer = new Timer(5, ballmover);
  balltimer.start();
  addKeyListener(this);
 }

 class BallMover implements ActionListener {
  public void actionPerformed(ActionEvent e) {
    if(!pop.allDead())
    {
      updateObstacles();
      pop.updateAlive(cacti);
    }else
    {
      pop.naturalSelection();
      cacti = new ArrayList<Cactus>();
      gameLen = 0;
    }
    gameLen++;
    repaint();
    grabFocus();
  }
 }
 public void updateObstacles()
 {
   obsCounter++;
   //System.out.println("TEST");
   if(obsCounter > 75-gameLen/100 + nextCactRandom)
   {
     obsCounter = 0;
     //System.out.println("TEST");
     addObstacle();
     nextCactRandom = ((int)(Math.random()*200));
   }
   moveObstacle();
 }
 public void moveObstacle()
 {
   for(int i = 0; i < cacti.size(); i++)
   {
     cacti.get(i).move();
     if(cacti.get(i).cactusX < -100)
     {
       cacti.remove(i);
       i--;
     }
   }
 }

 public void addObstacle()
 {
   int cactusType = (int)(Math.random()*3)+1;
   cacti.add(new Cactus(cactusType, gameSpeed));
 }

 public void paintComponent(Graphics g) {
  super.paintComponent(g);
  setBackground(Color.gray);
  g.setColor(Color.BLACK);
  g.fillRect(0, AiPlayer.floorHight, screenWidth, 200);
  for(int i = 0; i < pop.players.size(); i++)
    if(pop.players.get(i).alive)
      g.fillRect((int)pop.players.get(i).playerX, (int)pop.players.get(i).playerY, AiPlayer.playerWidth, AiPlayer.playerHight);
  g.setColor(Color.GREEN);
  for(int i = 0; i < cacti.size(); i++)
    g.fillRect(cacti.get(i).cactusX, cacti.get(i).cactusY, cacti.get(i).cactusWidth, cacti.get(i).cactusHeight);
  g.setColor(Color.blue);
  if(pop.best != null && pop.best.alive)
      g.fillRect((int)pop.best.playerX, (int)pop.best.playerY, AiPlayer.playerWidth, AiPlayer.playerHight);
 } // end paintComponent

 public void keyTyped(KeyEvent e)
 {

   char temp = e.getKeyChar();
   if(temp == ' ')
   {
     for(int i = 0; i < 1000; i++)
     {
      gameLen++;
      if(!pop.allDead())
      {
        updateObstacles();
        pop.updateAlive(cacti);
      }else
      {
        pop.naturalSelection();
        cacti = new ArrayList<Cactus>();
        gameLen = 0;
      }
     }
   }

 }
 public void keyPressed(KeyEvent e) {}
 public void keyReleased(KeyEvent e) {}
}
