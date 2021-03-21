import java.util.ArrayList;
class Genome
{
  private ArrayList<ConnectionGene> genes = new ArrayList<ConnectionGene>();
  private ArrayList<Node> nodes = new ArrayList<Node>();
  private ArrayList<Node> network = new ArrayList<Node>();
  private Node biasNode;

  public static int layers = 6;
  public Genome()
  {
    int inputs = AiPlayer.genomeInputs;
    int outputs = AiPlayer.genomeOutputs;
    for(int i = 0; i < inputs; i++)
      nodes.add(new Node(i, 0));
    for(int i = 0; i < outputs; i++)
      nodes.add(new Node(inputs+i, layers-1));

    biasNode = new Node(inputs + outputs, 0);
    nodes.add(biasNode);
  }
  public Genome(Genome clone)
  {
    for(int i = 0; i < clone.nodes.size(); i++)
      nodes.add(new Node(clone.nodes.get(i)));
    for(int i = 0; i < clone.genes.size(); i++)
      genes.add(new ConnectionGene(getNode(clone.genes.get(i).fromNode.number), getNode(clone.genes.get(i).toNode.number), clone.genes.get(i).weight));
    biasNode = getNode(AiPlayer.genomeInputs + AiPlayer.genomeOutputs);
    generateNetwork();
  }

  double[] feedForward(double[] inputValues) {
    for (int i = 0; i < AiPlayer.genomeInputs; i++) {
      nodes.get(i).outputValue = inputValues[i];
    }
    biasNode.outputValue = 1.0;

    for (int i = 0; i < network.size(); i++) {
      network.get(i).engage();
    }

    //the outputs are nodes[inputs] to nodes [inputs+outputs-1]
    double[] outs = new double[AiPlayer.genomeOutputs];
    for (int i = 0; i < outs.length; i++) {
      outs[i] = nodes.get(AiPlayer.genomeInputs + i).outputValue;
    }
    for (int i = 0; i < nodes.size(); i++) {//reset all the nodes for the next feed forward
      nodes.get(i).inputSum = 0;
    }
    return outs;
  }

  public void connectNodes() //Connect the nodes together using the gene
  {
    for(int i = 0; i < nodes.size(); i++)
      nodes.get(i).outputConnections.clear();
    for(int i = 0; i < genes.size(); i++)
      genes.get(i).fromNode.outputConnections.add(genes.get(i));
  }
  public void generateNetwork()
  {
    connectNodes();
    network = new ArrayList<Node>();
    for(int l = 0; l < layers; l++)
    {
      for(int i = 0; i < nodes.size(); i++)
      {
        if(nodes.get(i).layer == l)
        {
          network.add(nodes.get(i));
          //System.out.println("TSET");
        }
      }
    }
  }
  public void mutate()
  {
    if(genes.size() == 0)
    {
      addConection();
    }
    double randomNum = Math.random();
    if(randomNum < 0.7)
    {
      for(int i = 0; i < genes.size(); i++)
      {
        genes.get(i).mutateWeight();
      }
    }
    randomNum = Math.random();
    if(randomNum < 0.07)
      addConection();
    randomNum = Math.random();
    if(randomNum < 0.02)
      nodes.add(new Node(nodes.size(), 1 + (int)(Math.random() * 6)));
  }
  private void addConection() // adding a connection between two nodes
  {
    int randomNode1 = (int)(Math.random()*nodes.size());
    int randomNode2 = (int)(Math.random()*nodes.size());
    while(newConnection(randomNode1, randomNode2))
    {
      randomNode1 = (int)(Math.random()*nodes.size());
      randomNode2 = (int)(Math.random()*nodes.size());
    }
    int temp;
    if(nodes.get(randomNode1).layer > nodes.get(randomNode2).layer)
    {
      temp = randomNode2;
      randomNode2 = randomNode1;
      randomNode1 = temp;
    }
    genes.add(new ConnectionGene(nodes.get(randomNode1), nodes.get(randomNode2), (Math.random()*2)-1));
    connectNodes();
  }
  public boolean newConnection(int r1, int r2)
  {
    if(nodes.get(r1).layer == nodes.get(r2).layer)
    {
      return true;
    }
    if(nodes.get(r1).isConnectedTo(nodes.get(r2)))
    {
      return true;
    }
    return false;
  }
  public Node getNode(int nodeNumber)
  {
    for (int i = 0; i < nodes.size(); i++) {
      if (nodes.get(i).number == nodeNumber) {
        return nodes.get(i);
      }
    }
    return null;
  }
}
