package try_run.gagan.shah.testapp;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.Manifest.permission.READ_CONTACTS;


public class BlankFragment2 extends Fragment {
    private static final int REQUEST_READ_CONTACTS = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_blank_fragment2, container, false);
        start();
        return v;
    }
    private void start() {
        if (!mayRequestContacts()) {
            return;
        }
        System.out.println("done 1 = ");
    }



    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (getActivity().checkSelfPermission(android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            System.out.println("1 = "+String.valueOf(new String[]{android.Manifest.permission.READ_CONTACTS}));
            System.out.println("2 = "+REQUEST_READ_CONTACTS);
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        } else {
            System.out.println("1 = "+String.valueOf(new String[]{android.Manifest.permission.READ_CONTACTS}));
            System.out.println("2 = "+REQUEST_READ_CONTACTS);
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                start();
            }
        }

    }
}
