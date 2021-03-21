import java.util.ArrayList;

class Node
{
  public int number;
  public int layer;

  public double inputSum;
  public double outputValue;

  public ArrayList<ConnectionGene> outputConnections;
  public Node(int numberIn, int layerIn)
  {
    number = numberIn;
    layer = layerIn;
    outputConnections = new ArrayList<ConnectionGene>();
  }
  public Node(Node clone)
  {
    outputConnections = new ArrayList<ConnectionGene>();
    number = clone.number;
    layer = clone.layer;
  }
  public boolean isConnectedTo(Node node)
  {
    if(node.layer == layer)
    {
      return false;
    }
    if(node.layer < layer)
    {
      for(int i = 0; i < node.outputConnections.size(); i++)
      {
        if(node.outputConnections.get(i).toNode == this)
        {
          return true;
        }
      }
    }else
    {
      for(int i = 0; i < outputConnections.size(); i++)
      {
        if(outputConnections.get(i).toNode == node)
        {
          return true;
        }
      }
    }
    return false;
  }
  public void engage()
  {
    if(layer != 0)
      outputValue = sigmoid(inputSum);
    for(int i = 0; i < outputConnections.size(); i++)
      outputConnections.get(i).toNode.inputSum += outputValue + outputConnections.get(i).weight;
  }
  public double sigmoid(double x)
  {
    return 1.0/(1+Math.pow((double)Math.E, -4.9*x));
  }
}
