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
	
	
	//If size == 2, then destroy the group object.  Otherwise, check if the leavingMember is the current leader, and reassign leaderIndex to 0 (fastest member).
	//If leaderIndex > the index of the leavingMember then leaderIndex--;
	public void removeMember( PlayerHolder leavingMember ){
//		if( members.size() < 3 )
//			GroupHolder.finalize();
		if( this.leaderIndex == members.indexOf(leavingMember) )
			leaderIndex = 0;
		else if (this.leaderIndex > members.indexOf(leavingMember) )
			leaderIndex--;
		
		
	}
	
	//We need a method to destroy a group.
	
	
	
	
}
