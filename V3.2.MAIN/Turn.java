import java.util.ArrayList;


public class Turn extends Game
{
    private ArrayList<Entity> humanPieces = new ArrayList<Entity>();
    private ArrayList<Entity> masterlist = new ArrayList<Entity>();
    private int start;
    private Map map = new Map();
    private Entity e = new Entity();
    private Terrain t = new Terrain(); 

    //CHANGED
    Turn(Map map, Pieces pieceLists)
    {
        this.map = map;
        this.masterlist = pieceLists.getMasterList();
        this.humanPieces = pieceLists.getPlayerParty();

    }


    public boolean isValidMove(int end) 
    {

        boolean viable = false;
        if(e.getAP() > 0){
            if(start == end + 1|| start == end - 1|| start == end + map.getDimensions()|| start == end - map.getDimensions())
            {
                if(t.checkMountain(end,this.map) == false){
                    viable = true;       
                }
            }
        }

        return viable;

    }
    /* This method considers whether an attack is valid based on where the piece is and where the target is.
    first if-SM: considers if the target is to the left, right, above, or below the attacker(not diagonally, if necessary will implement later), respectively
    second if-SM: the two for-loops are used to obtain the <party> of the pieces. If the pieces are in the same party, the attack is invalid (no friendly-fire)
        NOTE: I have not considered an error if the spot they want to attack is unoccupied
    returns true if attack is valid.  */    
    public boolean isValidAtk(int end) 
    {

        boolean viable = true;
        if(e.getAP() > 0)
        {
            if(start == end + 1|| start == end - 1|| start == end + map.getDimensions() || start == end - map.getDimensions())
            {
                if(t.checkRiver(start,this.map) == false){
                   viable = true; 
                }
                
            }
        }

        return viable;
    }

    public boolean isValidSelection(int start) 
    {
        boolean viable = true;

        if(map.getPiece(start) > 0)
        {
            System.out.println("Which piece: " + map.getPiece(start));
            System.out.print("Masterlist: ");
            for (Entity e:masterlist) {
                System.out.print(e.getName()+", ");
            }
            if(masterlist.get(map.getPiece(start) - 1).getParty() == 1 && masterlist.get(map.getPiece(start) - 1).getState() == 1)
            {
                viable = true;
                this.start = start;
                System.out.println("Belongs to party: " + masterlist.get(map.getPiece(start) - 1).getParty());
                System.out.println("Name: " + masterlist.get(map.getPiece(start) - 1).getName());
                System.out.println("Check 2");
            }
        }
        

        return viable;
    }

    public void resetTurn()
    {
        for(Entity a:humanPieces)
        {
            a.resetap();
        }

    }

    public boolean checkPieceApAndHealth() 
    {
        boolean test = false;
        if(e.getAP() > 0 && e.getHp() > 0)
        {
            test = true;
        }
        return test;
    }


    public Entity selectpiece(int start)
    {
        boolean viable = isValidSelection(start);
        if(viable == true)
        {
            //e = masterlist.get(start - 1);
            System.out.println("testing");
        }
        return e;
    }

    public void movepiece(int end)
    {
        if(map.getPiece(end) == 0)
        {
            boolean viable = isValidMove(end); //first, checking to see if move is valid
            if (viable == true && checkPieceApAndHealth() == true)
            { 
                map.moveState(start, end);
                e.ActionTakes(1); //ap is reduced   
                start = end;       
            }
            t.pitfallDeath(end, this.map, this.e); 

        }
    }
    public void attackpiece(int end)
    {
        boolean viable  = isValidAtk(end);
        if (viable == true && checkPieceApAndHealth() == true)
        {
            e.setHp(masterlist.get(map.getPiece(end) - 1));
            e.ActionTakes(2); //ap is reduced    
        }

    }
    public void healpiece()
    {
        System.out.println("Piece has been healed by 1 hp");
        e.heal();
        e.ActionTakes(2);
    }
}