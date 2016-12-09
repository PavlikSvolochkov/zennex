package ru.zennex.fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ru.zennex.R;
import ru.zennex.common.Quote;

public class ParsingFragment extends ListFragment {

    private final String URL = "http://quotes.zennex.ru/api/v3/bash/quotes?sort=time";

    private List<Quote> quoteList;
    private ParseListAdapter adapter;

    private TextView id;
    private TextView time;
    private TextView rating;
    private TextView description;

    public ParsingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quoteList = new ArrayList<>();
        setRetainInstance(true);
        new ParseTask().execute();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_parsing, container, false);
    }

    public class ParseListAdapter extends ArrayAdapter<Quote> {

        public ParseListAdapter(Context context, int resource, List<Quote> objects) {
            super(context, resource, objects);
            quoteList.addAll(objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = inflater.inflate(R.layout.parsing_row, parent, false);

            id = (TextView) row.findViewById(R.id.id);
            time = (TextView) row.findViewById(R.id.time);
            rating = (TextView) row.findViewById(R.id.rating);
            description = (TextView) row.findViewById(R.id.description);

            id.setText("id: " + quoteList.get(position).getId());
            time.setText("time: " + quoteList.get(position).getTime());
            rating.setText("rating: " + quoteList.get(position).getRating());
            description.setText(quoteList.get(position).getDescription().replace("<br>", "\n").replace("&quot;", "\""));

            return row;
        }
    }

    private class ParseTask extends AsyncTask<Void, Void, String> {

        String resultJson = "";
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            Quote quote;
            JSONObject dataJsonObj;
            try {
                dataJsonObj = new JSONObject(strJson);
                JSONArray quotes = dataJsonObj.getJSONArray("quotes");

                for (int i = 0; i < quotes.length(); i++) {
                    JSONObject jsonObject = quotes.getJSONObject(i);

                    quote = new Quote();
                    quote.setId(jsonObject.getString("id"));
                    quote.setDescription(jsonObject.getString("description"));
                    quote.setTime(jsonObject.getString("time"));
                    quote.setRating(jsonObject.getString("rating"));

                    quoteList.add(quote);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter = new ParseListAdapter(getActivity(), R.layout.parsing_row, quoteList);
            setListAdapter(adapter);
        }
    }
}
