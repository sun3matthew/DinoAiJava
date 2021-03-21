import java.util.ArrayList;
class Population
{
  ArrayList<AiPlayer> players = new ArrayList<AiPlayer>();
  int populationLife = 0;
  int numPlayers;
  public AiPlayer best;
  public double bestFit;
  public Population(int numPlayer)
  {
    numPlayers = numPlayer;
    for(int i = 0; i < numPlayer; i++)
    {
      players.add(new AiPlayer());
      players.get(i).brain.mutate();
      players.get(i).brain.generateNetwork();
    }
  }
  public void updateAlive(ArrayList <Cactus> cacti)
  {
    populationLife++;
    for(int i = 0; i < players.size(); i++)
    {
      if(players.get(i).alive)
      {
        players.get(i).look(cacti);
        players.get(i).think();
        players.get(i).move(cacti);
      }
    }
  }
  public boolean allDead()
  {
    for(int i = 0; i < players.size(); i++)
    {
      if(players.get(i).alive)
      {
        return false;
      }
    }
    return true;
  }
  public void naturalSelection()
  {
    calculateFitnessPop();
    //ArrayList <AiPlayer> temp = new ArrayList <AiPlayer>();
    double max = 0.0;
    int maxIndex = 0;
    for(int i = 0; i < players.size(); i++)
    {
      if(players.get(i).fitness > max)
      {
        max = players.get(i).fitness;
        maxIndex = i;
      }
    }
    if(max > bestFit)
    {
      best = players.get(maxIndex);
      bestFit = max;
      System.out.println(bestFit);
    }
    players = new ArrayList <AiPlayer>();
    players.add(best);
    best.reset();
    for(int i = 1; i < numPlayers; i++)
    {
      players.add(new AiPlayer(best));
      players.get(i).brain.mutate();
    }
    for(int i = numPlayers - 200; i < numPlayers; i++)
    {
      players.set(i, new AiPlayer());
      players.get(i).brain.mutate();
    }
  }
  public void calculateFitnessPop()
  {
    for(int i = 0; i < players.size(); i++)
    {
      players.get(i).calculateFitness();
    }
  }
}
