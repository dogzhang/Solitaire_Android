package com.waitqq.solitaire;

import android.graphics.PointF;

public class Utils
{
	public static int GetAngleFromPoint(PointF pointStart, PointF pointEnd)
	{
		return (int)(Math.atan2(pointEnd.y - pointStart.y, pointEnd.x - pointStart.x) * -180 / Math.PI);
	}
	
	public static float GetLongthFromPoint(PointF pointStart, PointF pointEnd)
	{
		return (float) Math.sqrt((pointStart.x - pointEnd.x) * (pointStart.x - pointEnd.x) + (pointStart.y - pointEnd.y) * (pointStart.y - pointEnd.y));
	}
}
