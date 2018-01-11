package me.jerry.framework.android;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**服务框架（未实现）
 * @author JerryGeng
 */
public class ServiceFrame extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
