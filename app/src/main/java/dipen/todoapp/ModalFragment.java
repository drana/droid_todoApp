package dipen.todoapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ModalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ModalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModalFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "MODAL";
    private Button btnOk;
    private Button btnCancel;

    // TODO: Rename and change types of parameters
    private String mParam;

    private OnFragmentInteractionListener mListener;

    public ModalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param Parameter 1.
     * @return A new instance of fragment ModalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModalFragment newInstance(String param) {
        ModalFragment fragment = new ModalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param);
        fragment.setArguments(args);
        return fragment;
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM1);
        }


    }*/

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_modal, container, false);
//
//        if(mParam.equals("MSG_DELETE")){
//            TextView textView = (TextView) view.findViewById(R.id.modalText);
//            textView.setText("Are you sure you want to delete the selected item!");
//
//        }
//        else if(mParam.equals("MSG_NO_SAVE")){
//            TextView textView = (TextView) view.findViewById(R.id.modalText);
//            textView.setText("Are you sure you close without saving the changes!");
//        }
//
//
//        btnOk = (Button) view.findViewById(R.id.modalOK);
//        btnCancel = (Button) view.findViewById(R.id.modalCancel);
//
//        //set on click listner
//        setupButtonOnClickListener();
//
//        return view;
//    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title="";
        String msg = getArguments().getString(ARG_PARAM1);
        if(msg.equals("MSG_DELETE")) {
            title = "Are you sure you want to delete the selected item.";


            return new AlertDialog.Builder(getActivity())
                    .setIcon(R.drawable.ic_warning_black_24dp)
                    .setTitle(title)
                    .setPositiveButton(R.string.alert_dialog_ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((MainActivity) getActivity()).onOkSelected();
                                }
                            }
                    )
                    .setNegativeButton(R.string.alert_dialog_cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((MainActivity) getActivity()).onCancelSelected();
                                }
                            }
                    )
                    .create();
        }
        else if(msg.equals("MSG_CLOSE")) {
            title = "Are you sure you want to close without saving.";

            return new AlertDialog.Builder(getActivity())
                    .setIcon(R.drawable.ic_warning_black_24dp)
                    .setTitle(title)
                    .setPositiveButton(R.string.alert_dialog_ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((AddNewItems) getActivity()).onOkSelected();
                                }
                            }
                    )
                    .setNegativeButton(R.string.alert_dialog_cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((AddNewItems) getActivity()).onCancelSelected();
                                }
                            }
                    )
                    .create();
        }

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle(title)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((MainActivity) getActivity()).onOkSelected();
                            }
                        }
                )
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((MainActivity) getActivity()).onCancelSelected();
                            }
                        }
                )
                .create();
    }


    private void setupButtonOnClickListener() {

        View.OnClickListener btnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.modalOK:
                        OnModalOK();
                        break;
                    case R.id.modalCancel:
                        OnModalCancel();
                        break;
                    default:
                        break;
                }
            }
        };

        btnOk.setOnClickListener(btnClickListener);
        btnCancel.setOnClickListener(btnClickListener);
    }

    private void OnModalOK() {

    }

    private void OnModalCancel(){

    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get field from view

        // Fetch arguments from bundle and set title

        // Show soft keyboard automatically and request focus to field

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int Value);
    }
}
