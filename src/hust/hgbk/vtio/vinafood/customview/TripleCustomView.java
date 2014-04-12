package hust.hgbk.vtio.vinafood.customview;

import hust.hgbk.vtio.vinafood.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class TripleCustomView extends LinearLayout{
	TextView questionTextView;
	Spinner  subjectSpinner;
	Spinner  predicateSpinner;
	Spinner  objectValueSpinner;
	EditText dataValueEditText;
	Button   saveConstraintButton;
	Button   cancelConstraintDialogButton;
	
	public TripleCustomView(Context context) {
		super(context);
		LayoutInflater li = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.triple_item_layout, this, true);
		
		questionTextView = (TextView) findViewById(R.id.questionTextView);
		subjectSpinner  = (Spinner) findViewById(R.id.subjectSpinner);
		predicateSpinner = (Spinner) findViewById(R.id.predicateSpinner);
		objectValueSpinner = (Spinner) findViewById(R.id.objectValueSpinner);
		dataValueEditText = (EditText) findViewById(R.id.dataValueEditText);
		saveConstraintButton = (Button) findViewById(R.id.saveConstraintButton);
		cancelConstraintDialogButton = (Button) findViewById(R.id.cancelConstraintDialogButton);
		
		// Chon xong subject, lay ra cac property co the nhan cua doi tuong do
		subjectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view, 
					int position, long id) {
				Log.d("On Item Selected", "subject spinner selected item "+position);
				// Lay ve URI lop cua subject duoc chon
				
				// Lay ve list predicate cua lop do
				
				// Set adapter cho predicate spinner
				
				// Refress cho predicate spinner
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
				
			}
		});
		
		// Chon xong predicate, kiem tra thuoc tinh va lay ve cac range cua predicate do
		predicateSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
				Log.d("On Item Selected", "predicate spinner selected item "+position);
				// Lay ra URI cua predicate
				
				// Kiem tra xem predicate do la objec property hay data property
				
				//--- Neu la object property
				// Lay ra list Class la range cua property do
				
				// Duyet xem co resource nao thuoc cac lop tren khong, 
				// neu co thi add vao objectValuesSpinner adapter
			
				// Hien thi objectValueSpinner len
				objectValueSpinner.setVisibility(View.VISIBLE);
				// An dataValueEditText di
				dataValueEditText.setVisibility(View.GONE);
				
				//-- Neu la data property
				// Lay ve kieu gia tri  
				
				// Hien thi dataValueEditText len
				dataValueEditText.setVisibility(View.VISIBLE);
				// An objectValueSpinner di
				objectValueSpinner.setVisibility(View.GONE);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
			
		});
		
		// Save thong tin triple nay lai 
		saveConstraintButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// Luu thong tin triple nay vao bien toan cuc
			}
		});
		
		// Cancel thi huy item nay di trong list view
	}

	public TextView getQuestionTextView() {
		return questionTextView;
	}

	public void setQuestionTextView(TextView questionTextView) {
		this.questionTextView = questionTextView;
	}

	public Spinner getSubjectSpinner() {
		return subjectSpinner;
	}

	public void setSubjectSpinner(Spinner subjectSpinner) {
		this.subjectSpinner = subjectSpinner;
	}

	public Spinner getPredicateSpinner() {
		return predicateSpinner;
	}

	public void setPredicateSpinner(Spinner predicateSpinner) {
		this.predicateSpinner = predicateSpinner;
	}

	public Spinner getObjectValueSpinner() {
		return objectValueSpinner;
	}

	public void setObjectValueSpinner(Spinner objectValueSpinner) {
		this.objectValueSpinner = objectValueSpinner;
	}

	public EditText getDataValueEditText() {
		return dataValueEditText;
	}

	public void setDataValueEditText(EditText dataValueEditText) {
		this.dataValueEditText = dataValueEditText;
	}

	public Button getSaveConstraintButton() {
		return saveConstraintButton;
	}

	public void setSaveConstraintButton(Button saveConstraintButton) {
		this.saveConstraintButton = saveConstraintButton;
	}

	public Button getCancelConstraintDialogButton() {
		return cancelConstraintDialogButton;
	}

	public void setCancelConstraintDialogButton(Button cancelConstraintDialogButton) {
		this.cancelConstraintDialogButton = cancelConstraintDialogButton;
	}
	
}
