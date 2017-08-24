package SimDistributedComputing;
public class Link {
	
	public int label=0;
	public Entity neighbor;
	
	public Link(Entity n, int l){
		neighbor=n;
		label=l;
	}
	
	public Link(Entity n){
		neighbor=n;
	}
	
	public Link(){
		neighbor=null;
	}
}
