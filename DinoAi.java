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
  pop = new Population(800);
  BallMover ballmover = new BallMover();
  balltimer = new Timer(4, ballmover);
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
   if(obsCounter > 75-gameLen/100 + nextCactRandom)
   {
     obsCounter = 0;
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
  setBackground(new Color(255, 230, 230));
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

  if(pop.best != null && pop.best.alive)
  {
    int[] xy = null;
    int[] x2y2 = null;
    g.setColor(Color.red);
    //System.out.println(pop.best.brain.network.size());
    for(int i = 0; i < pop.best.brain.network.size(); i++)
    {
      xy = getXYofNode(pop.best.brain.network.get(i));
      //System.out.println("Size: " + pop.best.brain.network.size() + " : " + pop.best.brain.network.get(i).layer);
      for(int j = 0; j < pop.best.brain.network.get(i).outputConnections.size(); j++)
      {
        x2y2 = getXYofNode(pop.best.brain.network.get(i).outputConnections.get(j).toNode);
        //System.out.println(pop.best.brain.network.get(i).outputConnections.get(j).weight);
        int shade = (int)(pop.best.brain.network.get(i).outputConnections.get(j).weight*127.0)+127;
        shade = 255 - shade;
        g.setColor(new Color(shade, shade, shade));
        if(x2y2 != null)
          g.drawLine(xy[0] * 30 + 225, xy[1] * 20 + 25, x2y2[0] * 30 + 225, x2y2[1] * 20 + 25);
      }
    }
    for(int i = 0; i < pop.best.brain.network.size(); i++)
    {
      int shade =(int)(((pop.best.brain.network.get(i).outputValue))*255);
      if(shade > 255)
        shade = 255;
      if(shade < 0)
        shade = 0;
      g.setColor(new Color(shade, shade, shade));
      xy = getXYofNode(pop.best.brain.network.get(i));
      g.fillOval((int)(xy[0] * 30 + 220),(int)(xy[1] * 20 + 20), 10, 10);
    }
  }
 } // end paintComponent

 private int[] getXYofNode(Node node){
  int currLayer = 0;
  int numInLayer = 0;
  for(int i = 0; i < pop.best.brain.network.size(); i++)
  {
    int layer = (pop.best.brain.network.get(i).layer);
    if(layer == currLayer)
    {
      numInLayer++;
    }else{
      numInLayer = 1;
      currLayer = layer;
    }
    if(pop.best.brain.network.get(i) == node)
      return new int[]{currLayer, numInLayer};
  }
  return null;
 }

 public void keyTyped(KeyEvent e)
 {

   char temp = e.getKeyChar();
   if(temp == ' ')
   {
     for(int i = 0; i < 10000; i++)
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
