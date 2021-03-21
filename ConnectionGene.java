import java.util.Random;

class ConnectionGene
{
  public Node fromNode;
  public Node toNode;
  public double weight;
  //boolean enabled = true;
  public ConnectionGene(Node from, Node to, double w)
  {
    fromNode = from;
    toNode = to;
    weight = w;
  }
  public void mutateWeight()
  {
    weight = (Math.random()*2)-1;
    /*if(true)
    {
      weight = (Math.random()*2)-1;
    }else
    {
      weight = weight + randGen.nextGaussian()/25.0;
      if(weight > 1)
      {
        weight = 1;
      }else if(weight < -1)
      {
        weight = -1;
      }
    }*/
  }
}
