package com.waitqq.solitaire;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Main extends Activity
{
	int marks;
	long startTime;
	CardPack cardPack;
	List<Pile> pileList;
	float cardWidth;
	float cardHeight;
	int cardOriginalWidth;
	int cardOriginalHeight;
	float pileGapX;
	float pileGapY;
	int imageViewWidth;
	int imageViewHeight;
	ImageView imageView;
	ImageView imageViewMove;
	TextView textViewScore;
	TextView textViewTimer;
	Pile oldPile;
	PointF pointDown;
	PointF pointMove;
	long timeDown;
	long timeMove;
	View menu;
	boolean isMenuShown = false;
	Button buttonReplay;
	Button buttonNewGame;
	float originalPile6OffsetX;
	float originalPile6OffsetY;
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}
	
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus)
		{
			if (cardPack != null)
			{
				return;
			}
			else
			{
				textViewScore = (TextView) this.findViewById(R.id.textViewScore);
				textViewTimer = (TextView) this.findViewById(R.id.textViewTimer);
				menu = this.findViewById(R.id.menu);
				buttonReplay = (Button) this.findViewById(R.id.buttonReplay);
				buttonNewGame = (Button) this.findViewById(R.id.buttonNewGame);
				cardPack = new CardPack();
				pileList = new ArrayList<Pile>();
				imageView = (ImageView) findViewById(R.id.imageView);
				imageViewMove = (ImageView) findViewById(R.id.imageViewMove);
				imageViewWidth = imageView.getWidth();
				imageViewHeight = imageView.getHeight();
				menu.setOnTouchListener(new OnTouchListener()
				{
					@SuppressLint("ClickableViewAccessibility")
					public boolean onTouch(View arg0, MotionEvent arg1)
					{
						PullMenu(imageViewHeight * -1, 200);
						isMenuShown = false;
						return true;
					}
				});
				PullMenu(imageViewHeight * -1, 200);
				buttonReplay.setOnClickListener(new ButtonReplayClickListener());
				buttonNewGame.setOnClickListener(new ButtonNewGameClickListener());
				LoadCardImage();
				IniPileList();
				Shuffle();
				Deal();
				SortAllPile();
				imageView.setImageBitmap(DrawBack());
				imageViewMove.setImageBitmap(DrawFont());
				imageView.setOnTouchListener(new OnTouchListener()
				{
					@SuppressLint("ClickableViewAccessibility")
					public boolean onTouch(View v, MotionEvent event)
					{
						if (isMenuShown)
						{
							return true;
						}
						float x = event.getX();
						float y = event.getY();
						//pileList.get(6).Position = new PointF(x - cardWidth / 2, y - cardHeight / 3);
						pileList.get(6).Position = new PointF(x - originalPile6OffsetX, y - originalPile6OffsetY);
						imageViewMove.setX(pileList.get(6).Position.x);
						imageViewMove.setY(pileList.get(6).Position.y);
						switch (event.getActionMasked())
						{
							case MotionEvent.ACTION_DOWN:
								ActionDown(x, y);
								break;
							case MotionEvent.ACTION_MOVE:
								ActionMove(x, y);
								break;
							case MotionEvent.ACTION_UP:
								ActionUp(x, y);
								break;
							default:
								break;
						}
						return true;
					}
				});
				
			}
		}
	}
	
	void ActionDown(float x, float y)
	{
		pointDown = new PointF(x, y);
		timeDown = System.currentTimeMillis();
		
		Pile pile0 = pileList.get(0);
		PointF point0 = pile0.Position;
		RectF rect0 = new RectF();
		rect0.left = point0.x;
		rect0.right = point0.x + cardWidth;
		rect0.top = point0.y;
		rect0.bottom = point0.y + cardHeight;
		Pile pile1 = pileList.get(1);
		Pile pile6 = pileList.get(6);
		
		if (rect0.contains(x, y))
		{
			for (Card card : pile1.CardList)
			{
				card.ShowFace = false;
			}
			if (pile0.CardList.size() > 0)
			{
				for (int i = 0; i < 3; i++)
				{
					if (pile0.CardList.size() > 0)
					{
						Card card = pile0.CardList.get(0);
						card.Pile = pile1;
						card.IndexOfpile = pile1.CardList.size() + i;
						card.ShowFace = true;
						pile0.CardList.remove(card);
					}
				}
			}
			else
			{
				for (Card card : pile1.CardList)
				{
					card.Pile = pile0;
					card.ShowFace = false;
				}
				pile0.CardList = pile1.CardList;
				pile1.CardList = new ArrayList<Card>();
			}
		}
		else
		{
			List<Card> cardList = GetSelectedCardList(x, y);
			if (cardList.size() == 0)
			{
				
			}
			else
			{
				oldPile = cardList.get(0).Pile;
				if (cardList.get(0).Pile == pile1)
				{
					Card card = cardList.get(cardList.size() - 1);
					if (card == pile1.CardList.get(pile1.CardList.size() - 1))
					{
						card.Pile = pile6;
						card.IndexOfpile = 0;
						originalPile6OffsetX = pointDown.x - card.Position.x;
						originalPile6OffsetY = pointDown.y - card.Position.y;
						pile6.Position = new PointF(x - originalPile6OffsetX, y - originalPile6OffsetY);
						imageViewMove.setX(pile6.Position.x);
						imageViewMove.setY(pile6.Position.y);
					}
				}
				else if (cardList.get(0).Pile.Index >= 2 && cardList.get(0).Pile.Index <= 5)
				{
					Card card = cardList.get(cardList.size() - 1);
					card.Pile = pile6;
					card.IndexOfpile = 0;
					originalPile6OffsetX = pointDown.x - card.Position.x;
					originalPile6OffsetY = pointDown.y - card.Position.y;
					pile6.Position = new PointF(x - originalPile6OffsetX, y - originalPile6OffsetY);
					imageViewMove.setX(pile6.Position.x);
					imageViewMove.setY(pile6.Position.y);
				}
				else
				{
					originalPile6OffsetX = pointDown.x - cardList.get(0).Position.x;
					originalPile6OffsetY = pointDown.y - cardList.get(0).Position.y;
					pile6.Position = new PointF(x - originalPile6OffsetX, y - originalPile6OffsetY);
					imageViewMove.setX(pile6.Position.x);
					imageViewMove.setY(pile6.Position.y);
					for (int i = 0; i < cardList.size(); i++)
					{
						Card card = cardList.get(i);
						card.Pile = pile6;
						card.IndexOfpile = i;
					}
				}
			}
		}
		SortAllPile();
		imageView.setImageBitmap(DrawBack());
		imageViewMove.setImageBitmap(DrawFont());
	}
	
	void ActionMove(float x, float y)
	{
		if (pileList.get(6).CardList.size() > 0)
		{
			return;
		}
		pointMove = new PointF(x, y);
		timeMove = System.currentTimeMillis();
		
		float longth = Utils.GetLongthFromPoint(pointDown, pointMove);
		if (longth >= 100)
		{
			int angle = Utils.GetAngleFromPoint(pointDown, pointMove);
			if (Math.abs(angle) <= 45)
			{
				
			}
			else if (angle > 45 && angle < 135)
			{
				
			}
			else if (Math.abs(angle) >= 135)
			{
				
			}
			else if (angle > -135 && angle < -45)
			{
				PullMenu(0, 200);
				isMenuShown = true;
			}
		}
	}
	
	void ActionUp(float x, float y)
	{
		Pile pile6 = pileList.get(6);
		if (pile6.CardList.size() == 0)
		{
			return;
		}
		if (Math.abs(x - pointDown.x) < 5 && Math.abs(y - pointDown.y) < 5)
		{
			ActionClick();
			return;
		}
		List<Card> cardList = GetSelectedCardList(x, y);
		if (cardList.size() == 0)
		{
			Pile pile = GetEmptyPile(x, y);
			if (pile != null)
			{
				if (pile.Index >= 2 && pile.Index <= 5)
				{
					Card card = pile6.CardList.get(0);
					if (pile6.CardList.size() == 1 && card.CardIndex.GetValueByInt() == 1)
					{
						card.Pile = pile;
						card.IndexOfpile = 0;
					}
					else
					{
						MovePile6Back();
					}
				}
				else if (pile.Index >= 7 && pile.Index <= 13)
				{
					Card card = pile6.CardList.get(0);
					if (card.CardIndex.GetValueByInt() == 13)
					{
						for (Card cardTemp : pile6.CardList)
						{
							cardTemp.Pile = pile;
						}
					}
					else
					{
						MovePile6Back();
					}
				}
			}
			else
			{
				MovePile6Back();
			}
		}
		else
		{
			Card cardBack = cardList.get(cardList.size() - 1);
			Card cardFont = pile6.CardList.get(0);
			if (cardBack.Pile.Index >= 2 && cardBack.Pile.Index <= 5)
			{
				if (cardList.size() == 1 && cardBack.CardType.Value == cardFont.CardType.Value && cardFont.CardIndex.GetValueByInt() - cardBack.CardIndex.GetValueByInt() == 1)
				{
					cardFont.Pile = cardBack.Pile;
					cardFont.IndexOfpile = cardBack.IndexOfpile + 1;
				}
				else
				{
					MovePile6Back();
				}
			}
			else if (cardBack.Pile.Index >= 7 && cardBack.Pile.Index <= 13)
			{
				if (cardBack.CardType.GetColor() != cardFont.CardType.GetColor() && cardBack.CardIndex.GetValueByInt() - cardFont.CardIndex.GetValueByInt() == 1)
				{
					for (int i = 0; i < pile6.CardList.size(); i++)
					{
						Card card = pile6.CardList.get(i);
						card.Pile = cardBack.Pile;
						card.IndexOfpile = cardBack.Pile.CardList.size() + i;
					}
				}
				else
				{
					MovePile6Back();
				}
			}
			else
			{
				MovePile6Back();
			}
		}
		SortAllPile();
		for (int i = 7; i < 14; i++)
		{
			Pile pile = pileList.get(i);
			if (pile.CardList.size() > 0)
			{
				Card card = pile.CardList.get(pile.CardList.size() - 1);
				if (card.ShowFace == false)
				{
					card.ShowFace = true;
				}
			}
		}
		imageView.setImageBitmap(DrawBack());
		imageViewMove.setImageBitmap(DrawFont());
	}
	
	void ActionClick()
	{
		Pile pile6 = pileList.get(6);
		Card cardLast = pile6.CardList.get(pile6.CardList.size() - 1);
		Card card0 = pile6.CardList.get(0);
		for (int i = 2; i <= 5; i++)
		{
			if (card0.Pile != pile6)
			{
				break;
			}
			if (i == oldPile.Index)
			{
				continue;
			}
			Pile pile = pileList.get(i);
			if (pile.CardList.size() == 0)
			{
				if (cardLast.IndexOfpile == 0 && cardLast.CardIndex.GetValueByInt() == 1)
				{
					cardLast.Pile = pile;
				}
			}
			else if (cardLast.IndexOfpile == 0 && cardLast.CardIndex.GetValueByInt() - pile.CardList.get(pile.CardList.size() - 1).CardIndex.GetValueByInt() == 1 && cardLast.CardType.Value == pile.CardList.get(pile.CardList.size() - 1).CardType.Value)
			{
				cardLast.Pile = pile;
				cardLast.IndexOfpile = pile.CardList.size();
			}
		}
		for (int i = 7; i <= 13; i++)
		{
			if (card0.Pile != pile6)
			{
				break;
			}
			if (i == oldPile.Index)
			{
				continue;
			}
			Pile pile = pileList.get(i);
			if (pile.CardList.size() == 0)
			{
				if (card0.CardIndex.GetValueByInt() == 13)
				{
					for (Card card : pile6.CardList)
					{
						card.Pile = pile;
					}
				}
			}
			else if (pile.CardList.get(pile.CardList.size() - 1).CardIndex.GetValueByInt() - card0.CardIndex.GetValueByInt() == 1 && card0.CardType.GetColor() != pile.CardList.get(pile.CardList.size() - 1).CardType.GetColor())
			{
				for (Card card : pile6.CardList)
				{
					card.Pile = pile;
					card.IndexOfpile = pile.CardList.size() + card.IndexOfpile;
				}
			}
		}
		if (card0.Pile == pile6)
		{
			MovePile6Back();
		}
		SortAllPile();
		for (int i = 7; i < 14; i++)
		{
			Pile pile = pileList.get(i);
			if (pile.CardList.size() > 0)
			{
				Card card = pile.CardList.get(pile.CardList.size() - 1);
				if (card.ShowFace == false)
				{
					card.ShowFace = true;
				}
			}
		}
		imageView.setImageBitmap(DrawBack());
		imageViewMove.setImageBitmap(DrawFont());
	}
	
	void MovePile6Back()
	{
		Pile pile6 = pileList.get(6);
		for (int i = 0; i < pile6.CardList.size(); i++)
		{
			Card card = pile6.CardList.get(i);
			card.Pile = oldPile;
			card.IndexOfpile = oldPile.CardList.size() + i;
		}
	}
	
	Pile GetEmptyPile(float x, float y)
	{
		for (int i = 2; i <= 5; i++)
		{
			Pile pile = pileList.get(i);
			if (pile.CardList.size() == 0)
			{
				RectF rect = new RectF(pile.Position.x, pile.Position.y, pile.Position.x + cardWidth, pile.Position.y + cardHeight);
				if (rect.contains(x, y))
				{
					return pile;
				}
			}
		}
		for (int i = 7; i <= 13; i++)
		{
			Pile pile = pileList.get(i);
			if (pile.CardList.size() == 0)
			{
				RectF rect = new RectF(pile.Position.x, pile.Position.y, pile.Position.x + cardWidth, imageViewHeight);
				if (rect.contains(x, y))
				{
					return pile;
				}
			}
		}
		return null;
	}
	
	List<Card> GetSelectedCardList(float x, float y)
	{
		List<Card> cardList = new ArrayList<Card>();
		List<Card> cardListResult = new ArrayList<Card>();
		for (Card card : cardPack.CardList)
		{
			if (card.ShowFace == false || card.Pile.Index == 6)
			{
				continue;
			}
			RectF rect = new RectF(0, 0, 0, 0);
			rect.left = card.Position.x;
			rect.right = card.Position.x + cardWidth;
			rect.top = card.Position.y;
			rect.bottom = card.Position.y + cardHeight;
			if (rect.contains(x, y))
			{
				cardList.add(card);
			}
		}
		if (cardList.size() == 0)
		{
			return cardListResult;
		}
		Collections.sort(cardList);
		Card card = cardList.get(cardList.size() - 1);
		for (int i = card.IndexOfpile; i < card.Pile.CardList.size(); i++)
		{
			cardListResult.add(card.Pile.CardList.get(i));
		}
		return cardListResult;
	}
	
	void SortAllPile()
	{
		for (int i = 0; i < 14; i++)
		{
			Pile pile = pileList.get(i);
			pile.CardList.clear();
			for (Card card : cardPack.CardList)
			{
				if (card.Pile.Index == pile.Index)
				{
					pile.CardList.add(card);
				}
			}
			Collections.sort(pile.CardList);
			for (Card card : pile.CardList)
			{
				if (pile.Index == 1)
				{
					if (card.ShowFace == false)
					{
						card.Position = new PointF(pile.Position.x, pile.Position.y);
					}
					else
					{
						int showFaceCount = 0;
						for (Card cardTemp : pile.CardList)
						{
							if (cardTemp.ShowFace == true)
							{
								showFaceCount++;
							}
						}
						card.Position = new PointF(pile.Position.x + pile.OffsetX * (showFaceCount - (pile.CardList.size() - card.IndexOfpile)), pile.Position.y);
					}
				}
				else
				{
					card.Position = new PointF(pile.Position.x + card.IndexOfpile * pile.OffsetX, pile.Position.y + card.IndexOfpile * pile.OffsetY);
				}
			}
		}
	}
	
	void SortPile(int index)
	{
		Pile pile = pileList.get(index);
		for (Card card : cardPack.CardList)
		{
			if (card.Pile.Index == pile.Index)
			{
				pile.CardList.add(card);
			}
		}
		Collections.sort(pile.CardList);
		for (Card card : pile.CardList)
		{
			card.Position = new PointF(pile.Position.x + card.IndexOfpile * pile.OffsetX, pile.Position.y + card.IndexOfpile * pile.OffsetY);
		}
	}
	
	void LoadCardImage()
	{
		try
		{
			String backBitmapFileName = "poker/back.png";
			InputStream backBitmapInputStream = getAssets().open(backBitmapFileName);
			Bitmap backBitmap = BitmapFactory.decodeStream(backBitmapInputStream);
			for (int i = 0; i < 4; i++)
			{
				for (int j = 0; j < 13; j++)
				{
					Card card = new Card();
					card.CardType.SetValue(i);
					card.CardIndex.SetValue(j);
					String faceBitmapFileName = "poker/" + card.CardType.Value + "/" + card.CardIndex.Value + ".png";
					InputStream faceBitmapInputStream = getAssets().open(faceBitmapFileName);
					card.FaceBitmap = BitmapFactory.decodeStream(faceBitmapInputStream);
					card.BackBitmap = backBitmap;
					cardPack.CardList.add(card);
				}
			}
			cardOriginalWidth = cardPack.CardList.get(0).FaceBitmap.getWidth();
			cardOriginalHeight = cardPack.CardList.get(0).FaceBitmap.getHeight();
		}
		catch (Exception e)
		{
			
		}
	}
	
	void IniPileList()
	{
		if (pileList.size() == 0)
		{
			for (int i = 0; i < 14; i++)
			{
				Pile pile = new Pile();
				pile.Index = i;
				pileList.add(pile);
			}
		}
		Bitmap bitmapTemp = cardPack.CardList.get(0).FaceBitmap;
		cardWidth = bitmapTemp.getWidth();
		cardHeight = bitmapTemp.getHeight();
		if (imageViewWidth < bitmapTemp.getWidth() * 7)
		{
			cardWidth = ((float) imageViewWidth / ((float) 7 + ((float) 7 - 1) / 10));
			pileGapX = cardWidth / 10;
		}
		else
		{
			cardWidth = bitmapTemp.getWidth();
			pileGapX = (((float) imageViewWidth - (float) cardWidth * (float) 7) / ((float) 7 - 1));
		}
		pileGapY = 20;
		cardHeight = cardWidth * bitmapTemp.getHeight() / bitmapTemp.getWidth();
		for (int i = 0; i < 7; i++)
		{
			Pile pile = pileList.get(i + 7);
			pile.Position = new PointF((cardWidth + pileGapX) * i, cardHeight + pileGapY);
			pile.OffsetY = cardHeight / 3;
		}
		
		pileList.get(0).Position = new PointF(imageViewWidth - cardWidth, 0);
		pileList.get(1).Position = new PointF(imageViewWidth - cardWidth * 2 - pileGapX, 0);
		pileList.get(1).OffsetX = -(cardWidth / 2);
		for (int i = 0; i < 4; i++)
		{
			pileList.get(i + 2).Position = new PointF(pileList.get(i + 7).Position.x, 0);
		}
		
		pileList.get(6).OffsetY = cardHeight / 3;
		for (int i = 0; i < pileList.size(); i++)
		{
			Pile pileTemp = pileList.get(i);
			if (pileTemp.Position != null)
			{
				pileTemp.Position = new PointF(pileTemp.Position.x, pileTemp.Position.y + 90);
			}
		}
	}
	
	void Shuffle()
	{
		List<Card> cardListTemp = new ArrayList<Card>();
		cardListTemp.addAll(cardPack.CardList);
		Random random = new Random(System.currentTimeMillis());
		int count = cardListTemp.size();
		cardPack.CardList.clear();
		for (int i = 0; i < count; i++)
		{
			int index = random.nextInt(cardListTemp.size());
			cardPack.CardList.add(cardListTemp.get(index));
			cardListTemp.remove(index);
		}
	}
	
	void Deal()
	{
		int indexFlag = 0;
		for (int i = 0; i < 7; i++)
		{
			for (int j = 0; j < i; j++)
			{
				if (cardPack.CardList.size() <= indexFlag)
				{
					return;
				}
				Card cardTemp = cardPack.CardList.get(indexFlag);
				cardTemp.Pile = pileList.get(7 + i);
				cardTemp.IndexOfpile = j;
				cardTemp.ShowFace = false;
				indexFlag++;
			}
			if (cardPack.CardList.size() <= indexFlag)
			{
				return;
			}
			Card cardTemp = cardPack.CardList.get(indexFlag);
			cardTemp.Pile = pileList.get(7 + i);
			cardTemp.IndexOfpile = i;
			cardTemp.ShowFace = true;
			indexFlag++;
		}
		int indexOfPile0 = 0;
		for (int i = indexFlag; i < cardPack.CardList.size(); i++)
		{
			Card cardTemp = cardPack.CardList.get(i);
			cardTemp.Pile = pileList.get(0);
			cardTemp.IndexOfpile = indexOfPile0;
			cardTemp.ShowFace = false;
			indexOfPile0++;
		}
	}
	
	Bitmap DrawBack()
	{
		Bitmap bitmap = Bitmap.createBitmap(imageViewWidth, imageViewHeight, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Pile pile0 = pileList.get(0);
		if (pile0.CardList.size() > 0)
		{
			RectF rect = new RectF(pile0.Position.x, pile0.Position.y, pile0.Position.x + cardWidth, pile0.Position.y + cardHeight);
			canvas.drawBitmap(pile0.CardList.get(0).BackBitmap, null, rect, null);
		}
		Pile pile1 = pileList.get(1);
		List<Card> cardList1Temp = new ArrayList<Card>();
		for (Card card : pile1.CardList)
		{
			if (card.ShowFace == true)
			{
				cardList1Temp.add(card);
			}
		}
		Collections.sort(cardList1Temp);
		for (int i = 0; i < cardList1Temp.size(); i++)
		{
			RectF rect = new RectF(pile1.Position.x + pile1.OffsetX * i, pile1.Position.y, pile1.Position.x + pile1.OffsetX * i + cardWidth, pile1.Position.y + cardHeight);
			canvas.drawBitmap(cardList1Temp.get(i).FaceBitmap, null, rect, null);
		}
		for (int i = 2; i < 6; i++)
		{
			Pile pile = pileList.get(i);
			if (pile.CardList.size() > 0)
			{
				RectF rect = new RectF(pile.Position.x, pile.Position.y, pile.Position.x + cardWidth, pile.Position.y + cardHeight);
				canvas.drawBitmap(pile.CardList.get(pile.CardList.size() - 1).FaceBitmap, null, rect, null);
			}
		}
		for (int i = 0; i < 7; i++)
		{
			Pile pile = pileList.get(7 + i);
			for (int j = 0; j < pile.CardList.size(); j++)
			{
				Card card = pile.CardList.get(j);
				Bitmap bitmapTemp = card.ShowFace ? card.FaceBitmap : card.BackBitmap;
				RectF rect = new RectF(card.Position.x, card.Position.y, card.Position.x + cardWidth, card.Position.y + cardHeight);
				canvas.drawBitmap(bitmapTemp, null, rect, null);
			}
		}
		return bitmap;
	}
	
	Bitmap DrawFont()
	{
		Pile pile6 = pileList.get(6);
		List<Card> pile6CardList = pile6.CardList;
		float heightTemp = (pile6CardList.size() - 1) * pile6.OffsetY + cardHeight;
		Bitmap bitmap = Bitmap.createBitmap((int) cardWidth, (int) heightTemp, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		for (int i = 0; i < pile6CardList.size(); i++)
		{
			Card cardTemp = pile6CardList.get(i);
			Bitmap bitmapTemp = cardTemp.ShowFace ? cardTemp.FaceBitmap : cardTemp.BackBitmap;
			RectF rect = new RectF(pile6.OffsetX * i, pile6.OffsetY * i, pile6.OffsetX * i + cardWidth, pile6.OffsetY * i + cardHeight);
			canvas.drawBitmap(bitmapTemp, null, rect, null);
		}
		return bitmap;
	}
	
	void PullMenu(float y, long timeSpan)
	{
		if (timeSpan == 0)
		{
			menu.setTranslationY(y);
		}
		else
		{
			ObjectAnimator objectAnimator = new ObjectAnimator();
			objectAnimator.setTarget(menu);
			objectAnimator.setPropertyName("translationY");
			objectAnimator.setFloatValues(y);
			objectAnimator.setDuration(timeSpan);
			objectAnimator.start();
		}
	}
	
	private final class ButtonReplayClickListener implements View.OnClickListener
	{
		public void onClick(View v)
		{
			Deal();
			SortAllPile();
			imageView.setImageBitmap(DrawBack());
			imageViewMove.setImageBitmap(DrawFont());
			PullMenu(imageViewHeight * -1, 200);
			isMenuShown = false;
		}
	}
	
	private final class ButtonNewGameClickListener implements View.OnClickListener
	{
		public void onClick(View v)
		{
			Shuffle();
			Deal();
			SortAllPile();
			imageView.setImageBitmap(DrawBack());
			imageViewMove.setImageBitmap(DrawFont());
			PullMenu(imageViewHeight * -1, 200);
			isMenuShown = false;
		}
	}
	
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
	}
}
