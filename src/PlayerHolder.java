
public class PlayerHolder {

	
	//name,  x, y, direction, location, equippedItems, status, millis
	
	public String charName;
	public int xCoord;
	public int yCoord;
	public int direction;
	public String location;
	public String equippedItems;
	public String sex;
	public String charClass;
	public int level;
	public String status;
	public long millis;
	public int initiative;
	
	public PlayerHolder(String name, int x, int y, int d, String loc, String equippedItems, String sex, String charClass, int level, String state, int initiative )
	{
		this.charName = name;
		this.xCoord = x;
		this.yCoord = y;
		this.direction = d;
		this.location = loc;
		this.equippedItems = equippedItems;
		this.sex = sex;
		this.charClass = charClass;
		this.level = level;
		this.status = state;
		this.millis = System.currentTimeMillis();
		this.initiative = initiative;
	}


}
