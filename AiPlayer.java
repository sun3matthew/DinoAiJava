import java.util.ArrayList;
class AiPlayer
{
  //double fitness;
  double playerX, playerY;
  double playerVel;

  boolean jump;
  boolean alive;

  public static int screenHight = 600;
  public static int floorHight = screenHight-100;
  public static int playerHight = 120;
  public static int playerWidth = 80;
  public static int genomeInputs = 5;
  public static int genomeOutputs = 2;
  public static double gravity = 0.2;

  int fitness;

  double[] vision = new double[genomeInputs];
  double[] decision = new double[genomeOutputs];

  Genome brain;

  public AiPlayer()
  {
    playerX = 40;
    playerY = floorHight-playerHight;
    playerVel = 0.0;
    jump = false;
    alive = true;
    brain = new Genome();
  }
  public AiPlayer(AiPlayer clone)
  {
    this();
    brain = new Genome(clone.brain);
  }
  public void reset()
  {
    playerY = floorHight-playerHight;
    playerVel = 0.0;
    fitness = 0;
    jump = false;
    alive = true;
  }

  public void updatePlayer()
  {
    playerY = playerY - playerVel;
    playerVel = playerVel - gravity;
    if(playerY+playerHight > floorHight)
    {
      jump = false;
      playerVel = 0;
      playerY = floorHight-playerHight;
    }
  }
  public void look(ArrayList <Cactus> cacti)
  {
    double temp = 0;
    double min = 10000;
    int minIndex = -1;
    for(int i = 0; i < cacti.size(); i++)
    {
      if(cacti.get(i).cactusX < min && cacti.get(i).cactusX + cacti.get(i).cactusWidth > 0)
      {
        min = cacti.get(i).cactusX - playerX + playerWidth;
        minIndex = i;
      }
    }
    vision[3] = playerY-floorHight+playerHight;//playerY
    if(minIndex == -1)
    {
      vision[0] = 0;//dist to obs
      vision[1] = 0;//hight of obs
      vision[2] = 0;//width or obs
      vision[4] = 0;//dist btw
    }else
    {
      vision[0] = 1.0/(min);
      vision[1] = 1.0/cacti.get(minIndex).cactusHeight;
      vision[2] = 1.0/cacti.get(minIndex).cactusWidth;
      int bestIndex = minIndex;
      double closestDist = min;
      min = 10000;
      minIndex = -1;
      for(int i = 0; i < cacti.size(); i++)
      {
        if(i !=  bestIndex && cacti.get(i).cactusX < min && cacti.get(i).cactusX + cacti.get(i).cactusWidth > 0)
        {
          min = cacti.get(i).cactusX - playerX + playerWidth;
          minIndex = i;
        }
      }
      //System.out.println((min-closestDist));
      if(minIndex == -1)
      {
        vision[4] = 0;
      }else
      {
        vision[4] = 1.0/(min-closestDist);
      }
    }
  }
  public void think()
  {
    double max = 0;
    int maxIndex = 0;
    //System.out.println(vision[0] + "\t" + vision[1]+ "\t" + vision[2]+ "\t" + vision[3]+ "\t" + vision[4]);
    decision = brain.feedForward(vision);
    for(int i = 0; i < decision.length; i++)
    {
      if(decision[i] > max)
      {
        max = decision[i];
        maxIndex = i;
      }
    }
    //System.out.println(decision[0] + "\t" + decision[1]);
    if(max < 0.7)
    {
      return;
    }
    switch(maxIndex)
    {
      case 0:
        jump(false);
        break;
      case 1:
        jump(true);
        break;
    }
  }
  public void jump(boolean jumpType)
  {
    if(!jump)
    {
      jump = true;
      if(jumpType)
      {
        playerVel = 12;
        //System.out.println("BIGGIE");
      }else
      {
        playerVel = 9;
        //System.out.println("Small dick");
      }
    }
  }
  public void move(ArrayList <Cactus> cacti)
  {
    fitness++;
    updatePlayer();
    for(int i = 0; i < cacti.size(); i++)
    {
      if(cacti.get(i).ouchy((int)(playerX), (int)(playerY), playerWidth, playerHight))
      {
        alive = false;
      }
    }
  }
  public void calculateFitness()
  {
    //fitness = fitness/10;
    //fitness = fitness*fitness;
  }
  
}
