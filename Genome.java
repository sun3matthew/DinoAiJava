import java.util.ArrayList;
class Genome
{
  private ArrayList<ConnectionGene> genes = new ArrayList<ConnectionGene>();
  private ArrayList<Node> nodes = new ArrayList<Node>();
  public ArrayList<Node> network = new ArrayList<Node>();

  public static int layers = 6;
  public Genome()
  {
    int inputs = AiPlayer.genomeInputs;
    int outputs = AiPlayer.genomeOutputs;
    for(int i = 0; i < inputs; i++)
      nodes.add(new Node(i, 0));
    for(int i = 0; i < outputs; i++)
      nodes.add(new Node(inputs+i, layers-1));
  }
  public Genome(Genome clone)
  {
    for(int i = 0; i < clone.nodes.size(); i++)
      nodes.add(new Node(clone.nodes.get(i)));
    for(int i = 0; i < clone.genes.size(); i++)
      genes.add(new ConnectionGene(getNode(clone.genes.get(i).fromNode.number), getNode(clone.genes.get(i).toNode.number), clone.genes.get(i).weight));
    generateNetwork();
  }

  double[] feedForward(double[] inputValues) {
    for (int i = 0; i < AiPlayer.genomeInputs; i++) {
      nodes.get(i).outputValue = inputValues[i];
    }

    for (int i = 0; i < network.size(); i++){
      network.get(i).engage();
    }

    //the outputs are nodes[inputs] to nodes [inputs+outputs-1]
    double[] outs = new double[AiPlayer.genomeOutputs];
    for (int i = 0; i < outs.length; i++) {
      outs[i] = nodes.get(AiPlayer.genomeInputs + i).outputValue;
    }
    for (int i = 0; i < nodes.size(); i++){//reset all the nodes for the next feed forward
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
    if(randomNum < 0.9)
    {
      for(int i = 0; i < genes.size(); i++)
      {
        genes.get(i).mutateWeight();
      }
    }
    randomNum = Math.random();
    if(randomNum < 0.7)
      addConection();
    randomNum = Math.random();
    if(randomNum < 0.5){
      nodes.add(new Node(nodes.size(), 1+(int)(Math.random() * 4)));
      addConection(nodes.size()-1, true);
      addConection(nodes.size()-1, false);
    }
  }
  private void addConection() // adding a connection between two nodes
  {
    int randomNode1 = (int)(Math.random()*nodes.size()-1);
    int randomNode2 = (int)(Math.random()*nodes.size()-1);
    while(nodes.get(randomNode1).layer == nodes.get(randomNode2).layer || nodes.get(randomNode1).isConnectedTo(nodes.get(randomNode2)))
    {
      randomNode1 = (int)(Math.random()*nodes.size()-1);
      randomNode2 = (int)(Math.random()*nodes.size()-1);
    }
    addConection(randomNode1, randomNode2);
  }

  private void addConection(int newNode, boolean inputOrOutput) // adding a connection between two nodes
  {
    int randomNode2 = (int)(Math.random()*nodes.size());
    while((inputOrOutput ? nodes.get(newNode).layer <= nodes.get(randomNode2).layer : nodes.get(newNode).layer >= nodes.get(randomNode2).layer) || nodes.get(newNode).isConnectedTo(nodes.get(randomNode2)))
      randomNode2 = (int)(Math.random()*nodes.size());
    addConection(newNode, randomNode2);
  }

  private void addConection(int node1, int node2){
    int temp;
    if(nodes.get(node1).layer > nodes.get(node2).layer)
    {
      temp = node2;
      node2 = node1;
      node1 = temp;
    }
    genes.add(new ConnectionGene(nodes.get(node1), nodes.get(node2), (Math.random()*2)-1));
    connectNodes();
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
