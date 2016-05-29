class SkipList
{
	//TODO: Implement all as Template
	class Node
	{
		Node next;
		Node down;
		int count;
		String value;

		Node(String str, Node nxt)
		{
			value = str;
			count = 1;
			next = nxt;
		}
		Node(String str, Node nxt, int cnt)
		{
			value = str;
			count = cnt;
			next = nxt;
		}
		Node(){};
	}
	private double chance = 0.5;
	private Node [] root;
	private int highestLevel;
	SkipList()
	{
		root = new Node[1];
		highestLevel = 0;
		root[0] = new Node();
	}
	public int count(String str)
	{
		Node node = findRec(str, root[highestLevel]);

		if(node != null)
		{
			return node.count;
		}
		return 0;
	}
	
	public Node findRec(String str, Node node)
	{
		if(node == null)
			return null;

		int cmprStr = 0;

		for(; node.next != null; node = node.next)
		{
			cmprStr = node.next.value.compareTo(str);
			if(cmprStr == 0)
			{
				return node.next;
			}	
			else if(cmprStr > 0)
				break;
		}
		return findRec(str, node.down);
	}

	public void deleteRec(String str, Node node)
	{
		if(node == null)
			return;

		Integer cmprStr = 0;

		for(; node.next != null; node = node.next)
		{
			cmprStr = node.next.value.compareTo(str);
			if(cmprStr == 0)
			{
				//point around our found node to remove it.
				node.next = node.next.next;
				break;
			}	
			else if(cmprStr > 0)
				break;
		}
		deleteRec(str, node.down);
	}

	public void remove(String str)
	{
		deleteRec(str, root[highestLevel]);
	}

	public void add(String str)
	{
		addRec(str, root[highestLevel], highestLevel);
	}
	private Node addRec(String str, Node node, int depth)
	{
		Integer cmpareString = null;


		for(; node.next != null; node = node.next)
		{
			cmpareString = node.next.value.compareTo(str);
			if(cmpareString >= 0)
				break;		
		}
		//This case will allow to keep going down until we hit lowest level.
		if(node.down != null)
		{
			Node newNode = addRec(str, node.down, depth-1);
			if(newNode != null)
			{
				node.next = new Node(str, node.next, newNode.count);
				node.next.down = newNode;
				if(Math.random() >= chance )
				{
					if(depth == highestLevel) {
						createHigherLevel(node.next);
					}
					else {
						return node.next;
					}
				}
			}
		}else if(cmpareString != null && cmpareString == 0) {
			node.count++;
		}else {	
			node.next = new Node(str, node.next);
			
			if(Math.random() >= chance)
			{
				if(depth == highestLevel)
				{
					createHigherLevel(node.next);
				}
				else {
					return node.next;
				}
			}
		}

		return null;
	}
	private void createHigherLevel(Node node)
	{	
		
		Node [] newRoot = new Node[root.length+1];
		
		int i = 0;
		for(; i < root.length; i++)
		{
			newRoot[i] = root[i];
		}

		//Gives the same child.... as the nextchild. although I need to 			//modify it slightly
		Node cloneChild = new Node(node.value, null);
		cloneChild.down = node;


		//This is assigning the newlevel with the child as the only element 		//on the new level.
		newRoot[i] = new Node(null, cloneChild);
		//This is linking the new level with a lower level.
		newRoot[i].down = newRoot[i-1];
	
		root = newRoot;
		//Increment highestLevel
		highestLevel++;
		System.out.println("new Root size is :"+ root.length);


	}

}