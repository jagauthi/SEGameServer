
public class PlayerHolder {

	
	//name,  x, y, direction, location, equippedItems, status, millis
	
	public String charName;
	public int xCoord;
	public int yCoord;
	public int direction;
	public String location;
	public String status;
	public long millis;
	
	public PlayerHolder(String name, int x, int y, int d, String loc, String state )
	{
		this.charName = name;
		this.xCoord = x;
		this.yCoord = y;
		this.direction = d;
		this.location = loc;
		this.status = state;
		this.millis = System.currentTimeMillis();
	}


}
