package ro.atm.corden.viewmodel;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import ro.atm.corden.model.user.LoginUser;
import ro.atm.corden.util.constant.AppConstants;
import ro.atm.corden.view.activity.LoginActivity;

public class LoginViewModel extends AndroidViewModel {
    public MutableLiveData<String> username = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();

    private MutableLiveData<LoginUser> user;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<LoginUser> getUser() {

        if (user == null) {
            user = new MutableLiveData<>();
        }
        return user;

    }

    public void onClick(View view) {
        LoginUser loginUser = new LoginUser(username.getValue(), password.getValue());
        user.setValue(loginUser);

    }
}
