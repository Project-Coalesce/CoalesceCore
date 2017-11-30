package com.coalesce.core.update;

import java.io.File;

public class InstallUpdateThread extends Thread {
	
	private final File update;
	private final File old;
	
	public InstallUpdateThread(File update, File old) {
		this.update = update;
		this.old = old;
	}
	
	@Override
	public void run() {
		
		if (!old.getAbsoluteFile().delete()) throw new RuntimeException("Failed to delete " + old.getName());
		if (!update.renameTo(new File(update.getParentFile().getParentFile() + File.separator + update.getName()))) throw new RuntimeException("Failed to move " + update.getName());
		
		interrupt();
		
	}
}
