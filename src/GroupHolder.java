import java.util.ArrayList;
import java.util.List;

public class GroupHolder {

	ArrayList<PlayerHolder> members = new ArrayList<PlayerHolder>();
	int leaderIndex;
	
	//Assuming exactly two characters will be required to create a group, we will need the following parameters:
	//leaderCharObject, otherCharObject, leaderInitiative, otherCharInitiative
	
	GroupHolder(PlayerHolder leader, PlayerHolder member, int leaderInit, int memberInit){
		
		if( leaderInit > memberInit ){
			members.add(leader);
			leaderIndex = 0;
			members.add(member);
		}
		else{
			members.add(member);
			members.add(leader);
			leaderIndex = 1;
		}	
	}
	
	public void addMember(PlayerHolder otherMember, int otherMemberInit){
		int i = 0;
		while( otherMemberInit < members.get(i).initiative ){
			i++;
		}
		members.add(i, otherMember);
		System.out.println("Added to the " + i + " position in the list out of " + members.size() );
	}
	
	
	
}
