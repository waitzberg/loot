/*
 * This file is part of the loot project for Android.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. This program is distributed in the 
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details. You should have received a copy of the GNU General 
 * Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2008, 2009, 2010, 2011 Christopher McCurdy
 */

package net.gumbercules.loot.backend;

import java.util.List;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class OptionsProvider extends ContentProvider
{
	public static final Uri CONTENT_URI = Uri.parse("content://net.gumbercules.loot.optionsprovider");
	public static final String OPTIONS_ITEM_MIME = "vnd.android.cursor.item/vnd.gumbercules.options";
	public static final String OPTIONS_DIR_MIME = "vnd.android.cursor.dir/vnd.gumbercules.options";

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		return 0;
	}

	@Override
	public String getType(Uri uri)
	{
		if (!uri.getScheme().equals("content"))
		{
			return null;
		}
		
		List<String> path = uri.getPathSegments();
		int size = path.size();
		
		String object = null;
		if (size > 0)
		{
			object = path.get(0);
		}
		else
		{
			return OPTIONS_DIR_MIME;
		}
		
		if (String.valueOf(object) != null)
		{
			return OPTIONS_ITEM_MIME;
		}
		
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		return null;
	}

	@Override
	public boolean onCreate()
	{
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder)
	{
		SQLiteDatabase lootDb = Database.getDatabase();
		String type = getType(uri);
		
		if (type == null)
		{
			return null;
		}
		
		if (selection == null)
		{	
			selection = "1=1";
		}
		
		if (sortOrder == null)
		{
			sortOrder = "value asc";
		}
			
		List<String> path = uri.getPathSegments();
		String query = "select value from options where ";
		
		if (type.equals(OPTIONS_ITEM_MIME))
		{
			query += "option = ? and " + selection +
					" order by " + sortOrder;
			selectionArgs = new String[] { path.get(0) };
		}
		else if (type.equals(OPTIONS_DIR_MIME))
		{
			query += selection + " order by " + sortOrder;
		}
		
		return lootDb.rawQuery(query, selectionArgs);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs)
	{
		return 0;
	}

}
