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

import java.io.File;

import net.gumbercules.loot.R;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.widget.Toast;

public class MemoryStatus
{
	public static final int MEMORY_ERROR	= -1;
	private static final String TAG			= "net.gumbercules.loot.backend.MemoryStatus";
	
	public static boolean externalMemoryAvailable()
	{
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	public static long getAvailableExternalMemory()
	{
		long available = MEMORY_ERROR;
		if (externalMemoryAvailable())
		{
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			available = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks();

			Log.i(TAG + ".getAvailableExternalMemory()", "External Memory available: " + available);
		}
		else
		{
			Log.e(TAG + ".getAvailableExternalMemory()", "External Memory not available");
		}
		
		return available;
	}
	
	public static long getTotalExternalMemory()
	{
		long total = MEMORY_ERROR;
		if (externalMemoryAvailable())
		{
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			total = (long)stat.getBlockSize() * (long)stat.getBlockCount();

			Log.i(TAG + ".getAvailableExternalMemory()", "External Memory total: " + total);
		}
		else
		{
			Log.e(TAG + ".getTotalExternalMemory()", "External Memory not available");
		}
		
		return total;
	}
	
	public static boolean checkMemoryStatus(Context c, boolean check_available)
	{
		boolean ret = true;
		long available = MemoryStatus.getAvailableExternalMemory();
		long total = MemoryStatus.getTotalExternalMemory();
		
		if (available == MemoryStatus.MEMORY_ERROR || total == MemoryStatus.MEMORY_ERROR)
		{
			// external storage is not available
			Toast.makeText(c, R.string.no_external_storage, Toast.LENGTH_LONG).show();
			ret = false;
		}
		
		double percent = (double)available / total;
		if (check_available && percent <= 0.01)
		{
			// less than 1% total memory is available
			Toast.makeText(c, R.string.low_external_memory, Toast.LENGTH_LONG).show();
			Log.i(TAG + ".checkMemoryStatus", "available: " + available + ", total: " + total);
		}
		
		return ret;
	}
}
