package com.waitqq.solitaire;

import java.util.ArrayList;
import java.util.List;

import android.graphics.PointF;

public class Pile
{
	public int Index;

	public PointF Position;

	public float OffsetX;

	public float OffsetY;
	
	public List<Card> CardList;
	
	public Pile()
	{
		CardList = new ArrayList<Card>();
	}

}
