package dipen.todoapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnEditItemListener} interface
 * to handle interaction events.
 * Use the {@link EditItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditItemFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_EDIT_ITEM = "Edit_Item";
    private static final String ARG_POSITION = "Position";

    // TODO: Rename and change types of parameters
    private String mItem;
    private int mPosition;
    private EditText mEditTextItem;
    public String editedText;
    private Button btnDoneEdit;
    private ImageButton btnGoBack;

    private OnEditItemListener mEditItemListener;

    public EditItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditItemFragment newInstance(String param1, int param2) {
        EditItemFragment fragment = new EditItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EDIT_ITEM, param1);
        args.putInt(ARG_POSITION, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && getArguments() != null) {
            mItem = getArguments().getString(ARG_EDIT_ITEM);
            mPosition = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_items,
                container, false);
        btnDoneEdit = (Button) view.findViewById(R.id.btnDoneEditItem);
        btnGoBack = (ImageButton) view.findViewById(R.id.btnGoBack);

        setupButtonOnclicLsitner();

//        btnDoneEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OnCLickDoneEdit("fake");
//            }
//        });
//
//        imgBtnGoBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OnClickGoBack("fake");
//            }
//        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        // Get field from view
        mEditTextItem = (EditText) view.findViewById(R.id.editTextItem);
        // Fetch arguments from bundle and set title

        mEditTextItem.setText(mItem);
        // Show soft keyboard automatically and request focus to field
        mEditTextItem.requestFocus();

       //setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void setupButtonOnclicLsitner() {
        View.OnClickListener btnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.btnDoneEditItem:
                        OnCLickDoneEdit(v);
                        break;
                    case R.id.btnGoBack:
                        OnClickGoBack(v);
                        break;

                    default:
                        break;
                }
            }
        };

        btnDoneEdit.setOnClickListener(btnClickListener);
        btnGoBack.setOnClickListener(btnClickListener);
    }

    private void OnCLickDoneEdit(View v) {
        mEditItemListener = (OnEditItemListener) getActivity();
        mEditItemListener.OnEditItemUpdateCompleted(mEditTextItem.getText().toString());

    }

    private  void OnClickGoBack(View v){
        
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEditItemListener) {
            mEditItemListener = (OnEditItemListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEditItemListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mEditItemListener = null;
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
    public interface OnEditItemListener {
        // TODO: Update argument type and name
        void OnEditItemUpdateCompleted(String updatedItem);
    }
}
