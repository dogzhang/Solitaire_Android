package com.waitqq.solitaire;

public class CardIndex
{
	public String Value;
	public static String _A = "_A";
	public static String _2 = "_2";
	public static String _3 = "_3";
	public static String _4 = "_4";
	public static String _5 = "_5";
	public static String _6 = "_6";
	public static String _7 = "_7";
	public static String _8 = "_8";
	public static String _9 = "_9";
	public static String _10 = "_10";
	public static String _J = "_J";
	public static String _Q = "_Q";
	public static String _K = "_K";

	public void SetValue(int v)
	{
		switch (v)
		{
			case 0 :
				Value = _A;
				break;
			case 1 :
				Value = _2;
				break;
			case 2 :
				Value = _3;
				break;
			case 3 :
				Value = _4;
				break;
			case 4 :
				Value = _5;
				break;
			case 5 :
				Value = _6;
				break;
			case 6 :
				Value = _7;
				break;
			case 7 :
				Value = _8;
				break;
			case 8 :
				Value = _9;
				break;
			case 9 :
				Value = _10;
				break;
			case 10 :
				Value = _J;
				break;
			case 11 :
				Value = _Q;
				break;
			case 12 :
				Value = _K;
				break;
			default :
				Value = _A;
				break;
		}
	}

	public int GetValueByInt()
	{
		int value = 1;
		if(Value.equals(_A))
		{
			value = 1;
		}
		else if (Value.equals(_2)) {
			value = 2;
		}
		else if (Value.equals(_3)) {
			value = 3;
		}
		else if (Value.equals(_4)) {
			value = 4;
		}
		else if (Value.equals(_5)) {
			value = 5;
		}
		else if (Value.equals(_6)) {
			value = 6;
		}
		else if (Value.equals(_7)) {
			value = 7;
		}
		else if (Value.equals(_8)) {
			value = 8;
		}
		else if (Value.equals(_9)) {
			value = 9;
		}
		else if (Value.equals(_10)) {
			value = 10;
		}
		else if (Value.equals(_J)) {
			value = 11;
		}
		else if (Value.equals(_Q)) {
			value = 12;
		}
		else if (Value.equals(_K)) {
			value = 13;
		}
		return value;
	}
}
