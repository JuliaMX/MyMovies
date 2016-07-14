package photosides.juliamaksimkin;

import android.app.Activity;

public abstract class BaseLogic {
    protected DAL dal;

    public BaseLogic(Activity activity) {
        dal = new DAL(activity);
    }

    public void open() {
        dal.open();
    }

    public void close(){
        dal.close();
    }
}
