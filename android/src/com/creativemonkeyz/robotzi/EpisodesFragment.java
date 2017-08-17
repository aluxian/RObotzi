package com.creativemonkeyz.robotzi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockListFragment;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import de.keyboardsurfer.android.widget.crouton.Style;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EpisodesFragment extends SherlockListFragment {
    private ListAdapter listAdapter;
    private HashMap<Integer, Item> items = new HashMap<Integer, Item>();

    @Override
    public void onActivityCreated(Bundle saveState) {
        super.onActivityCreated(saveState);

        listAdapter = new ListAdapter(getActivity(), items, R.layout.list_ep_pics);

        getListView().setAdapter(listAdapter);
        registerForContextMenu(getListView());

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Items");
        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.getInBackground("Episodes", new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e != null) {
                    Utils.showToast(getString(R.string.error_general), getActivity(), Style.ALERT);
                    return;
                }

                JSONArray data = object.getJSONArray("items");
                items.clear();

                for (int i = 0; i < data.length(); i++) {
                    try {
                        JSONObject jsonObject = data.getJSONObject(i);
                        items.put(Integer.parseInt(jsonObject.getString("objectId")), new Item(jsonObject));
                    } catch (JSONException ignored) {}
                }

                listAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment, null);
    }

    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {
        if (position == items.size()) {
            Utils.launchPlayStorePubPage(getActivity());
            return;
        }

        RateMeMaybe rateDialog = new RateMeMaybe(getActivity());
        rateDialog.setAdditionalListener(new RateMeMaybe.OnRMMUserChoiceListener() {
            @Override
            public void handlePositive() {
                Utils.launchPlayStoreAppPage(getActivity());
            }

            @Override
            public void handleNeutral() {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(listAdapter.getItem(position).youtubeLink)));
            }

            @Override
            public void handleNegative() {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(listAdapter.getItem(position).youtubeLink)));
            }
        });

        if (!rateDialog.run())
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(listAdapter.getItem(position).youtubeLink)));
    }
}