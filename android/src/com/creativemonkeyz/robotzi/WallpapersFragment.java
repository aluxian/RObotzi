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

public class WallpapersFragment extends SherlockListFragment {
    private ListAdapter listAdapter;
    private HashMap<Integer, Item> items = new HashMap<Integer, Item>();

    @Override
    public void onActivityCreated(Bundle saveState) {
        super.onActivityCreated(saveState);

        getListView().setAdapter(listAdapter = new ListAdapter(getActivity(), items, R.layout.list_walls));
        registerForContextMenu(getListView());

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Items");
        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.getInBackground("Wallpapers", new GetCallback<ParseObject>() {
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
        // TODO show context menu
    }
}