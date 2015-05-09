package com.waitqq.solitaire;

public class CardType
{
	public String Value;
	public static String Black = "Black";
	public static String Heart = "Heart";
	public static String Club = "Club";
	public static String Diamond = "Diamond";

	public void SetValue(int v)
	{
		switch (v)
		{
			case 0 :
				Value = Black;
				break;
			case 1 :
				Value = Heart;
				break;
			case 2 :
				Value = Club;
				break;
			case 3 :
				Value = Diamond;
				break;
			default :
				break;
		}
	}
	
	public String GetColor()
	{
		String color = "Black";
		if(Value.equals(Black) || Value.equals(Club))
		{
			color = "Black";
		}
		else {
			color = "Red";
		}
		return color;
	}

}
