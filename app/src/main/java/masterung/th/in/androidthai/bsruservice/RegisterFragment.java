package masterung.th.in.androidthai.bsruservice;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private boolean aBoolean = true;
    private String genderString;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Create Toolbar
        Toolbar toolbar = getView().findViewById(R.id.toolbarRegister);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.register));
        ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("Please Fill All Blank");
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        setHasOptionsMenu(true);

//        Gender Controller
        RadioGroup radioGroup = getView().findViewById(R.id.ragGender);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                aBoolean = false;
                switch (checkedId) {
                    case R.id.radMale:
                        genderString = "Male";
                        break;
                    case R.id.radFeMale:
                        genderString = "Female";
                        break;
                }


            }
        });


    }   // Main Method

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.itemUpload) {

            checkValueAndUpload();

        }   // if


        return super.onOptionsItemSelected(item);
    }

    private void checkValueAndUpload() {

//        Get Value From EditText
        EditText nameEditText = getView().findViewById(R.id.edtName);
        EditText userEditText = getView().findViewById(R.id.edtUser);
        EditText passwordEditText = getView().findViewById(R.id.edtPassword);

        String name = nameEditText.getText().toString().trim();
        String user = userEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        MyAlert myAlert = new MyAlert(getActivity());

        if (name.isEmpty() || user.isEmpty() || password.isEmpty()) {
//            Have Space
            myAlert.normalDialog("Have Space", "Please Fill All Blank");
        } else if (aBoolean) {
            myAlert.normalDialog("No Gender", "Please Choose Gender");
        } else {

            try {

                MyConstant myConstant = new MyConstant();
                AddUserThread addUserThread = new AddUserThread(getActivity());
                addUserThread.execute(name, user, password, genderString, myConstant.getUrlAddUser());

                String result = addUserThread.get();

                if (Boolean.parseBoolean(result)) {
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    myAlert.normalDialog("Cannot Register", "Please Try Again");
                }



            } catch (Exception e) {
                e.printStackTrace();
            }

        }   // if


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_register, menu);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

}
