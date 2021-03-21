class Cactus
{
  int cactusX, cactusY;
  int cactusWidth, cactusHeight;
  int gameSpeed;
  int floorHight;

  final int screenWidth = 1200;
  final int screenHight = 600;

  public Cactus(int type, int gameSpeedIn)
  {
    floorHight = screenHight-100;
    gameSpeed = gameSpeedIn;
    cactusX = screenWidth+20;
    if(type == 0)
    {
      cactusWidth = 40;
      cactusHeight = 80;
    }else if(type == 1)
    {
      cactusWidth = 60;
      cactusHeight = 120;;
    }else
    {
      cactusWidth = 120;
      cactusHeight = 80;
    }
    cactusY = floorHight-cactusHeight;
  }
  public void move()
  {
    cactusX = cactusX - gameSpeed;
    //System.out.println(cactusX);
  }
  public boolean ouchy(int x, int y, int w, int h)
  {
    return x + w > cactusX && y + h > cactusY && cactusX + cactusWidth > x && cactusY + cactusHeight > y;
  }
}
