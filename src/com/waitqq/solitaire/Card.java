package com.waitqq.solitaire;

import android.graphics.Bitmap;
import android.graphics.PointF;

public class Card implements Comparable<Object>
{
	public CardIndex CardIndex;
	public CardType CardType;
	public boolean ShowFace;
	public Pile Pile;
	public int IndexOfpile;
	public Bitmap FaceBitmap;
	public Bitmap BackBitmap;
	public PointF Position;
	
	public Card()
	{
		CardIndex = new CardIndex();
		CardType = new CardType();
	}
	
	public int compareTo(Object o)
	{
		Card s = (Card) o;
		return IndexOfpile > s.IndexOfpile ? 1 : (IndexOfpile == s.IndexOfpile ? 0 : -1);
	}
	
}
