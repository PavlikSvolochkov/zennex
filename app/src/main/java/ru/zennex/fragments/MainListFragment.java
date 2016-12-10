package ru.zennex.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ru.zennex.R;
import ru.zennex.common.Cat;
import ru.zennex.database.CatDAO;
import ru.zennex.database.DBHelper;

public class MainListFragment extends android.support.v4.app.ListFragment {

    private DBHelper dbHelper;
    private CatDAO catDAO;
    private SQLiteDatabase database;
    private MainListAdapter adapter;

    private Button addListItemBtn;

    @Override
    public void onStart() {
        super.onStart();
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        dbHelper = new DBHelper(getActivity());
        database = dbHelper.getWritableDatabase();
        catDAO = new CatDAO(getActivity());
        adapter = new MainListAdapter(getActivity(), R.layout.list_row, catDAO.getAllCats());
        setListAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
    }

    public void showAddCatDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_catname_dialog, null);
        builder.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.catNameEditText);

        builder.setTitle(R.string.enterCatName);
        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String catName = editText.getText().toString().trim();
                adapter.add(catDAO.createCat(catName));
                adapter.notifyDataSetInvalidated();
            }
        });
        builder.setNegativeButton(R.string.revert, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(getActivity(), "Reverted " + editText.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog b = builder.create();
        b.show();
    }



    public void showEditDialog(final Cat cat) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_catname_dialog, null);
        builder.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.catNameEditText);
        editText.setText(cat.getName());

        builder.setTitle(R.string.editName);
        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!editText.getText().toString().equals(cat.getName())) {
                    cat.setName(editText.getText().toString());
                    catDAO.updateCat(cat);
                    adapter.notifyDataSetInvalidated();
                }
                //TODO: onKeyDown() or onBackPressed()
            }
        });
        builder.setNegativeButton(R.string.revert, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(getActivity(), "Reverted " + editText.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog b = builder.create();
        b.show();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        Cat cat = (Cat) getListView().getItemAtPosition(info.position);

        switch (item.getItemId()) {
            case R.id.edit:
                showEditDialog(cat);
                return true;
            case R.id.delete:
                catDAO.deleteCat(cat.getId());
                adapter.remove(cat);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
        addListItemBtn = (Button) getActivity().findViewById(R.id.addListItem);
        addListItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCatDialog();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmeng_list, null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cat cat = (Cat) getListAdapter().getItem(position);
        showEditDialog(cat);
    }

    public class MainListAdapter extends ArrayAdapter<Cat> {

        List<Cat> allCats;

        public MainListAdapter(Context context, int resource, List<Cat> objects) {
            super(context, resource, objects);
            allCats = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.list_row, parent, false);

            final ImageView imageView1 = (ImageView) row.findViewById(R.id.image1);
            final ImageView imageView2 = (ImageView) row.findViewById(R.id.image2);

            CheckBox checkBox = (CheckBox) row.findViewById(R.id.checkbox);
            checkBox.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        imageView1.setVisibility(View.GONE);
                        imageView2.setVisibility(View.VISIBLE);
                    } else {
                        imageView2.setVisibility(View.GONE);
                        imageView1.setVisibility(View.VISIBLE);
                    }
                }
            });

            TextView rowText = (TextView) row.findViewById(R.id.catName);
            rowText.setText(allCats.get(position).getName());

            return row;
        }
    }
}
