package com.vikram.locateme.ui.main.contact;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.vikram.locateme.R;
import com.vikram.locateme.ui.main.IOnPendingUpdated;
import com.vikram.locateme.utils.DialogHelper;

import java.util.List;

/**
 * Created by M1032130 on 6/20/2017.
 */

public class ContactFragment extends Fragment implements IContactView {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText mEditTextSearch;
    private IContactPresenter contactPresenter;
    private ContactListAdapter mAdapter;
    private String authToken;
    private String clickedContact;
    private IOnPendingUpdated onPendingUpdated;

    public ContactFragment() {
        contactPresenter = new ContactPresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onPendingUpdated = (IOnPendingUpdated) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement IOnPendingUpdated");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onPendingUpdated = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        authToken = this.getArguments().getString("authToken");

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mEditTextSearch = (EditText)view.findViewById(R.id.edit_search);

        mEditTextSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mAdapter != null) {
                    mAdapter.filter(s.toString());
                }
            }
        });

        contactPresenter.getContacts(getActivity());
        return view;
    }

    @Override
    public void listContact(List<Contact> contacts) {
        mAdapter = new ContactListAdapter(contacts, getActivity(), new ContactListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final Contact item) {
                AlertDialog.Builder builder = DialogHelper.showConfirmationDialog(getActivity(), "Do you want to access "+item.getName()+" location");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clickedContact = item.getNumber();
                        clickedContact = clickedContact.replaceAll("\\s+","");
                        clickedContact = clickedContact.length() > 10 ? clickedContact.substring(clickedContact.length() - 10) : clickedContact;
                        clickedContact = getCountryDialCode()+clickedContact;
                        contactPresenter.checkContactExistence(clickedContact, authToken);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.create().show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public String getCountryDialCode(){
        String countryId;
        String countryDialCode = null;

        TelephonyManager telephonyMngr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        countryId = telephonyMngr.getSimCountryIso().toUpperCase();
        String[] arrCountryCode=this.getResources().getStringArray(R.array.DialingCountryCode);
        for(int i=0; i<arrCountryCode.length; i++){
            String[] arrDial = arrCountryCode[i].split(",");
            if(arrDial[1].trim().equals(countryId.trim())){
                countryDialCode = arrDial[0];
                break;
            }
        }
        return countryDialCode;
    }

    @Override
    public void onContactExist(String response) {
        //Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
        contactPresenter.requestLocationAccess(clickedContact, authToken);
    }

    @Override
    public void onContactNotExist(String response) {
        AlertDialog.Builder builder = DialogHelper.showErrorDialog(getActivity(), response);
        if (builder != null) {
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.create().show();
        }
    }

    @Override
    public void onLocationAccessRequestSuccess(String response) {
        AlertDialog.Builder builder = DialogHelper.showSuccessDialog(getActivity(), response);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (onPendingUpdated != null) {
                    onPendingUpdated.onPendingUpdated();
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onLocationAccessRequestError(String response) {
        AlertDialog.Builder builder = DialogHelper.showErrorDialog(getActivity(), response);
        if (builder != null) {
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.create().show();
        }
    }
}