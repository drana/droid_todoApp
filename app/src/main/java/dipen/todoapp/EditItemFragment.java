package dipen.todoapp;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditItemFragment extends DialogFragment{

    private EditText mEditText;
    public String editedText;

    public EditItemFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditItemFragment newInstance(String task) {
        EditItemFragment frag = new EditItemFragment();
        Bundle args = new Bundle();
        args.putString("Item", task);
        frag.setArguments(args);
        return frag;
    }

    // 1. Defines the listener interface with a method passing back data result.
    public interface EditItemDialogListener {
        void onFinishEditDialog(String inputText);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = (LinearLayout)inflater.inflate(R.layout.fragment_edit_item, container,false);
        final EditText editedItem = (EditText) view.findViewById(R.id.editedText);

        ((Button) view.findViewById(R.id.saveEditTextItem)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
             try {
                 // When button is clicked, call up to owning activity.
                 // Return input text back to activity through the implemented listener
                 EditItemDialogListener listener = (EditItemDialogListener) getActivity();
                 listener.onFinishEditDialog(editedItem.getText().toString());
                 // Close the dialog and return back to the parent activity
                 dismiss();
             }catch (Exception e){
                 e.printStackTrace();
             }


            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.editedText);
        // Fetch arguments from bundle and set title
        String editedText = getArguments().getString("Item");
        mEditText.setText(editedText);
        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }





}
