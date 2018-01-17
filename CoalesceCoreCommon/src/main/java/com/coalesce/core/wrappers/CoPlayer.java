package com.coalesce.core.wrappers;

import com.coalesce.core.text.ActionBar;
import com.coalesce.core.text.BossBar;
import com.coalesce.core.text.Text;
import com.coalesce.core.text.Title;
import com.coalesce.core.text.Toast;

public interface CoPlayer<T> extends CoSender<T> {

    void sendActionBar(ActionBar actionBar);
    
    void sendTitle(Title title);
    
    void sendToast(Toast toast);
    
    void sendBossBar(BossBar bossBar);
    
    void sendMessage(Text text);

}
